package com.tokopedia.imagepicker_insta.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.cameraview.size.AspectRatio
import com.tokopedia.applink.RouteManager
import com.tokopedia.coachmark.*
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.R
import com.tokopedia.imagepicker_insta.activity.ImagePickerInstaActivity
import com.tokopedia.imagepicker_insta.bottomsheets.CreatorTypesBottomSheet
import com.tokopedia.imagepicker_insta.common.BundleData
import com.tokopedia.imagepicker_insta.common.ImagePickerRouter.DEFAULT_MULTI_SELECT_LIMIT
import com.tokopedia.imagepicker_insta.common.trackers.TrackerProvider
import com.tokopedia.imagepicker_insta.common.ui.bottomsheet.FeedAccountTypeBottomSheet
import com.tokopedia.imagepicker_insta.common.ui.menu.MenuManager
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel
import com.tokopedia.imagepicker_insta.common.ui.toolbar.ImagePickerCommonToolbar
import com.tokopedia.imagepicker_insta.di.DaggerImagePickerComponent
import com.tokopedia.imagepicker_insta.item_decoration.GridItemDecoration
import com.tokopedia.imagepicker_insta.mediacapture.MediaRepository
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.toPx
import com.tokopedia.imagepicker_insta.util.*
import com.tokopedia.imagepicker_insta.util.VideoUtil.getImageDimensions
import com.tokopedia.imagepicker_insta.util.VideoUtil.getVideoDimensions
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import com.tokopedia.imagepicker_insta.views.FolderChooserView
import com.tokopedia.imagepicker_insta.views.MediaView
import com.tokopedia.imagepicker_insta.views.NoPermissionsView
import com.tokopedia.imagepicker_insta.views.ToggleImageView
import com.tokopedia.imagepicker_insta.views.adapters.ImageAdapter
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.imagepicker_insta_camera_activity.*
import kotlinx.android.synthetic.main.imagepicker_insta_fragment_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ImagePickerInstaMainFragment : PermissionFragment(), ImagePickerFragmentContract {

    lateinit var viewModel: PickerViewModel

    private lateinit var toolbarCommon: ImagePickerCommonToolbar
    lateinit var rv: RecyclerView
    lateinit var selectedMediaView: MediaView
    lateinit var recentSection: LinearLayout
    lateinit var tvSelectedFolder: AppCompatTextView
    lateinit var imageFitCenter: AppCompatImageView
    lateinit var imageMultiSelect: ToggleImageView
    lateinit var viewDataContainer: ConstraintLayout
    lateinit var noPermissionView: NoPermissionsView

    lateinit var imageAdapter: ImageAdapter
    val imageDataList = ArrayList<ImageAdapterData>()
    val allImageDataList = ArrayList<ImageAdapterData>()
    val folders = arrayListOf<FolderData>()
    val zoomImageAdapterDataMap = mutableMapOf<ImageAdapterData, ZoomInfo>()

    var selectedFolderText: String? = null
    var mediaLoadingFailed = false
    lateinit var noMediaAvailableText: String
    lateinit var loadingMediaText: String
    lateinit var queryConfiguration: QueryConfiguration
    var maxMultiSelect: Int = DEFAULT_MULTI_SELECT_LIMIT
    val coachMarkItem = ArrayList<CoachMark2Item>()
    private  lateinit var coachMark: CoachMark2

    private val userSession: UserSessionInterface by lazy(mode = LazyThreadSafetyMode.NONE) {
        UserSession(requireContext())
    }

    private val feedAccountBottomSheet: FeedAccountTypeBottomSheet by lazy(mode = LazyThreadSafetyMode.NONE) {
        val fragment = FeedAccountTypeBottomSheet.getFragment(childFragmentManager, requireActivity().classLoader)
        fragment.setOnAccountClickListener(object : FeedAccountTypeBottomSheet.Listener {
            override fun onAccountClick(feedAccount: FeedAccountUiModel) {
                viewModel.setSelectedFeedAccount(feedAccount)
            }
        })
        fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuTitle = (activity as? ImagePickerInstaActivity)?.menuTitle
            ?: activity?.getString(R.string.imagepicker_insta_lanjut)
        if (!menuTitle.isNullOrEmpty()) {
            MenuManager.addCustomMenu(activity, menuTitle, hasReadPermission(), menu) {
                proceedNextStep()
                TrackerProvider.tracker?.onNextButtonClick()
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onVolumeDown() {
        //DO nothing
    }

    fun onVolumeUp() {
        selectedMediaView.onVolumeUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MenuManager.MENU_ITEM_ID -> {
                proceedNextStep()
                TrackerProvider.tracker?.onNextButtonClick()
                return true
            }
        }
        return false
    }

    private fun proceedNextStep() {
        val selectedImageAdapterData = arrayListOf<ImageAdapterData>()
        if (!imageAdapter.isSelectedPositionsEmpty()) {
            imageAdapter.selectedPositionMap.keys.forEach {
                selectedImageAdapterData.add(it)
            }
        } else if (selectedMediaView.imageAdapterData != null) {
            selectedImageAdapterData.add(selectedMediaView.imageAdapterData!!)
        }

        if (!selectedImageAdapterData.isNullOrEmpty()) {
            val filteredResults = arrayListOf<Pair<ImageAdapterData, ZoomInfo>>()
            selectedImageAdapterData.forEach {
                if (zoomImageAdapterDataMap[it] != null) {
                    filteredResults.add(Pair(it, zoomImageAdapterDataMap[it]!!))
                }
            }
            viewModel.getUriOfSelectedMedia(
                selectedMediaView.assetView.width,
                selectedMediaView.assetView.height,
                filteredResults
            )
        } else {
            showToast(getString(R.string.imagepicker_insta_samf), Toaster.TYPE_NORMAL)
        }
    }

    private fun initDagger() {
        if (context is AppCompatActivity) {
            viewModel = ViewModelProviders.of(this)[PickerViewModel::class.java]
            val component = DaggerImagePickerComponent.builder().build()
            component.inject(viewModel)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = LayoutInflater.from(context)
            .inflate(R.layout.imagepicker_insta_fragment_main, container, false)
        initViews(v)
        setObservers()
        setClicks()
        return v
    }

    private fun hasReadPermission(): Boolean {
        if (context != null) {
            return PermissionUtil.isReadPermissionGranted(requireContext())
        }
        return false
    }

    fun showPermissionUi() {
        activity?.invalidateOptionsMenu()

        noPermissionView.visibility = View.VISIBLE
        viewDataContainer.visibility = View.GONE
    }

    fun isPermissionUiVisible(): Boolean {
        return noPermissionView.visibility == View.VISIBLE
    }


    fun isUiInitialized(): Boolean {
        return ::noPermissionView.isInitialized
    }

    /*
    * Weird crash is happening.
    * showDataUi() is getting called but fragment's onCreateView is not called
    * So ::noPermissionView.isInitialized is added to prevent crash
    * */
    fun showDataUi() {
        if (::noPermissionView.isInitialized) {
            activity?.invalidateOptionsMenu()

            noPermissionView.visibility = View.GONE
            viewDataContainer.visibility = View.VISIBLE

            imageAdapter.clearSelectedItems()
            zoomImageAdapterDataMap.clear()
            selectedMediaView.removeAsset()
            getPhotos()
        }
    }

    fun showEmptyUi() {
        activity?.invalidateOptionsMenu()

        noPermissionView.visibility = View.GONE
        viewDataContainer.visibility = View.GONE
    }

    private fun initViews(v: View) {
        rv = v.findViewById(R.id.rv)
        selectedMediaView = v.findViewById(R.id.selected_image_view)
        recentSection = v.findViewById(R.id.recent_section)
        tvSelectedFolder = v.findViewById(R.id.tv_selected_folder)
        imageFitCenter = v.findViewById(R.id.image_fit_center)
        imageMultiSelect = v.findViewById(R.id.multi_select_toggle)
        viewDataContainer = v.findViewById(R.id.data_container)
        noPermissionView = v.findViewById(R.id.no_permission_view)
        showEmptyUi()

        setupToolbar(v)

        imageMultiSelect.onDrawableId = R.drawable.imagepicker_insta_ic_select_multiple_on
        imageMultiSelect.offDrawableId = R.drawable.imagepicker_insta_ic_select_multiple_off

        imageMultiSelect.toggle(false)
        prepareDataFromActivity()
        setupRv()

        noMediaAvailableText = getString(R.string.imagepicker_insta_no_media_available)
        loadingMediaText = getString(R.string.imagepicker_insta_loading)

    }

    fun prepareDataFromActivity() {
        var maxDuration = (activity as? ImagePickerInstaActivity)?.videoMaxDurationInSeconds
            ?: VideoUtil.DEFAULT_DURATION_MAX_LIMIT
        if (maxDuration == 0L) {
            maxDuration = VideoUtil.DEFAULT_DURATION_MAX_LIMIT
        }

        if ((activity as? ImagePickerInstaActivity)?.maxMultiSelectAllowed != null) {
            maxMultiSelect = (activity as ImagePickerInstaActivity).maxMultiSelectAllowed
        }

        queryConfiguration = QueryConfiguration(maxDuration)
    }

    private fun setupToolbar(v: View) {
        toolbarCommon = v.findViewById(R.id.toolbar_common)
        toolbarCommon.setOnBackClickListener {
            TrackerProvider.tracker?.onBackButtonFromPicker()
            activity?.finish()
        }
        toolbarCommon.setOnAccountClickListener {
            openFeedAccountBottomSheet()
        }

        toolbarCommon.getToolbarParentView().addOneTimeGlobalLayoutListener {
            coachMark = CoachMark2(activity as Context)

            coachMarkItem.add(
                CoachMark2Item(
                    toolbarCommon.getToolbarParentView(),
                    getString(R.string.imagepicker_coachmark_header),
                    getString(R.string.imagepicker_coachmark_text),
                    CoachMark2.POSITION_BOTTOM
                )
            )
            if (Prefs.getShouldShowCoachMarkValue(activity as Context))
            showFabCoachMark()
        }

        (activity as ImagePickerInstaActivity).run {
            setSupportActionBar(toolbarCommon)
            toolbarCommon.title = toolbarTitle
            viewModel.setSelectedFeedAccount(
                FeedAccountUiModel(
                    name = userSession.name,
                    iconUrl = userSession.profilePicture,
                    type = FeedAccountUiModel.Type.BUYER,
                )
            )
        }
    }

    private fun openFeedAccountBottomSheet(){
        feedAccountBottomSheet.show(childFragmentManager)
    }

    private fun showFabCoachMark() {
        Prefs.saveShouldShowCoachMarkValue(activity as Context)
        if (::coachMark.isInitialized) {
            coachMark.showCoachMark(coachMarkItem)
        }
    }

    private fun setClicks() {
        recentSection.setOnClickListener {
            if (folders.isEmpty()) return@setOnClickListener

            val bottomSheet = BottomSheetUnify()
            val folderView = FolderChooserView(it.context)
            bottomSheet.setChild(folderView)
            bottomSheet.show(childFragmentManager, "BottomSheet Tag")
            folderView.setData(folders)
            bottomSheet.setTitle(it.context?.getString(R.string.imagepicker_insta_pilih) ?: "")
            folderView.itemOnClick { folderData ->

                //When user selects same folder
                if (folderData?.folderTitle == selectedFolderText) {
                    bottomSheet.dismiss()
                    return@itemOnClick
                }

                //When user selects same folder and folder name is AlbumUtil.RECENTS
                if (folderData?.folderTitle.isNullOrEmpty() && selectedFolderText == AlbumUtil.RECENTS) {
                    bottomSheet.dismiss()
                    return@itemOnClick
                }

                if (!folderData?.folderTitle.isNullOrEmpty()) {
                    val size = imageDataList.size
                    imageDataList.clear()
                    imageAdapter.notifyItemRangeRemoved(0, size)

                    updateSelectedFolderText(folderData?.folderTitle!!)
                    refreshImages(folderData.folderTitle)
                } else {
                    updateSelectedFolderText(AlbumUtil.RECENTS)

                    imageDataList.clear()
                    addCameraItemInEmptyList()
                    imageDataList.addAll(allImageDataList)
                    imageAdapter.notifyItemRangeInserted(0, imageDataList.size)
                }

                bottomSheet.dismiss()
            }
        }

        imageFitCenter.setOnClickListener {
            if (selectedMediaView.imageAdapterData != null) {
                selectedMediaView.toggleScaleType()
            }
        }

        imageMultiSelect.setOnClickListener {
            imageMultiSelect.toggle()

            if (selectedMediaView.imageAdapterData != null) {

                //Clear previous selected
                val selectedItemIndexList = imageAdapter.getListOfIndexWhichAreSelected()
                imageAdapter.clearSelectedItems()

                if (!selectedItemIndexList.isNullOrEmpty()) {
                    selectedItemIndexList.forEach {
                        imageAdapter.notifyItemChanged(it)
                    }
                }

                //Add selected item
                imageAdapter.addSelectedItem(selectedMediaView.imageAdapterData!!)
                imageAdapter.getListOfIndexWhichAreSelected().forEach {
                    imageAdapter.notifyItemChanged(it)
                }

                //update zoomMap
                val zoomInfo = zoomImageAdapterDataMap[selectedMediaView.imageAdapterData!!]
                zoomImageAdapterDataMap.clear()
                if (zoomInfo != null) {
                    zoomImageAdapterDataMap[selectedMediaView.imageAdapterData!!] = zoomInfo
                }
            }
        }

        noPermissionView.btnPermission.setOnClickListener {
            PermissionUtil.requestReadPermission(this)
        }
    }

    private fun refreshImages(folderName: String) {
        viewModel.getMediaByFolderName(folderName, queryConfiguration)
    }

    fun setupRv() {

        val columnCount = 3
        val lm = GridLayoutManager(context, columnCount)
        rv.layoutManager = lm
        val width = context?.resources?.displayMetrics?.widthPixels ?: 0
        val contentHeight = width / columnCount

        imageAdapter = ImageAdapter(imageDataList, contentHeight, this, maxMultiSelect, lm)
        rv.adapter = imageAdapter
        val itemPadding = 4.toPx().toInt()
        rv.addItemDecoration(GridItemDecoration(itemPadding, true))
        rv.itemAnimator = null
    }

    private fun updateSelectedFolderText(text: String, clearList: Boolean = true) {
        if (selectedFolderText == text) return

        selectedFolderText = text
        tvSelectedFolder.text = selectedFolderText

        if (clearList) {
            imageDataList.clear()
            imageAdapter.notifyDataSetChanged()
        }
    }

    private fun openCamera() {
        CameraUtil.openCamera(
            this,
            (activity as? ImagePickerInstaActivity)?.applinkToNavigateAfterMediaCapture,
            queryConfiguration.videoMaxDuration
        )
    }

    override fun handleOnCameraIconTap() {
        openCamera()
        TrackerProvider.tracker?.onCameraButtonFromPickerClick()
    }

    override fun showToast(message: String, toasterType: Int) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, toasterType).show()
        }
    }

    override fun isMultiSelectEnable(): Boolean {
        return imageMultiSelect.isChecked
    }

    override fun getAssetInPreview(): Asset? {
        return selectedMediaView.imageAdapterData?.asset
    }

    override fun showErrorToast(@AdapterErrorType type: Int) {
        when (type) {
            AdapterErrorType.MULTISELECT -> showToast(
                getString(
                    R.string.imagepicker_max_limit_reached,
                    maxMultiSelect
                ), Toaster.TYPE_ERROR
            )
            AdapterErrorType.VIDEO_DURATION -> showToast(
                getString(
                    R.string.imagepicker_max_vid_dur,
                    queryConfiguration.videoMaxDuration
                ), Toaster.TYPE_ERROR
            )
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.selectedFeedAccount.collectLatest {
                toolbarCommon.subtitle = it.name
                toolbarCommon.icon = it.iconUrl
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(selectedMediaView)

        viewLifecycleOwner.lifecycleScope.launch {
            MediaRepository.getMediaChangeFlow().collect {
                viewModel.handleFileAddedEvent(arrayListOf(it), queryConfiguration)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.photosFlow.collect {
                when (it.status) {
                    LiveDataResult.STATUS.LOADING -> {
                    }
                    LiveDataResult.STATUS.SUCCESS -> {
                        updateMediaToUi(it.data)
                    }
                    LiveDataResult.STATUS.ERROR -> {
                        showToast(getString(R.string.imagepicker_insta_utlam), Toaster.TYPE_ERROR)
                    }
                }
            }
        }

        viewModel.folderFlow.onEach {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                }

                LiveDataResult.STATUS.SUCCESS -> {
                    updateFolders(it.data)
                }
                LiveDataResult.STATUS.ERROR -> {
                    updateFolders(null)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)


        imageMultiSelect.toggleCallback = { isMultiSelect ->
            if (isMultiSelect) {
                imageFitCenter.visibility = View.GONE
                selectedMediaView.lockAspectRatio()
            } else {
                imageFitCenter.visibility = View.VISIBLE
                selectedMediaView.unLockAspectRatio()
            }
        }

        viewModel.selectedMediaUriLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    //Do nothing
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null) {
                        handleSuccessSelectedUri(it.data)
                    } else {
                        showToast(getString(R.string.imagepicker_insta_smwr), Toaster.TYPE_ERROR)
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    showToast(getString(R.string.imagepicker_insta_smwr), Toaster.TYPE_ERROR)
                }
            }
        }

        imageAdapter.itemSelectCallback =
            { imageAdapterData: ImageAdapterData, isSelected: Boolean ->
                if (isSelected) {

                    var zoomInfo: ZoomInfo? = null
                    imageAdapter.getListOfIndexWhichAreSelected()

                    if (!isMultiSelectEnable()) {
                        zoomImageAdapterDataMap.clear()
                    } else {
                        val firstSelectedMedia = imageAdapter.selectionOrder.getOrderList().last()
                        zoomInfo = if (imageAdapterData.asset is PhotosData) {
                            getZoomInfoForImage(firstSelectedMedia, imageAdapterData)
                        } else {
                            getZoomInfoForVideo(firstSelectedMedia, imageAdapterData)
                        }
                        zoomImageAdapterDataMap[imageAdapterData] = zoomInfo
                    }
                    if (zoomInfo == null) {
                        zoomInfo = prepareZoomInfo(imageAdapterData)
                    }
                    selectedMediaView.loadAsset(imageAdapterData, zoomInfo)
                } else {
                    //DO nothing
                }
            }

        imageAdapter.onItemLongClick = { _: ImageAdapterData ->
            if (!isMultiSelectEnable()) {
                /**
                 * 1. tell adapter that multiselect is active by doing step 2
                 * 2. toggle multi-select icon
                 * 2. update all items except selected one to show empty circle
                 * */
                imageMultiSelect.toggle(true)
            }
        }
    }

    private fun getZoomInfoForVideo(
        firstSelectedMedia: ImageAdapterData,
        originalImageAdapterData: ImageAdapterData
    ): ZoomInfo {
        val originalWidth =
            originalImageAdapterData.asset.contentUri.getVideoDimensions(requireContext()).width
        val originalHeight =
            originalImageAdapterData.asset.contentUri.getVideoDimensions(requireContext()).height

        val width = zoomImageAdapterDataMap[firstSelectedMedia]?.bmpWidth
        val height = zoomImageAdapterDataMap[firstSelectedMedia]?.bmpHeight

        var ratio = 0.0f

        try {
            if (width != null && height != null) {
                val ar = AspectRatio.of(width, height)
                ratio = (ar.x.toFloat() / ar.y)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*
        * As we are supposed to maintain the orientation PORTRAIT
        * So will keep the width same
        * And change height according to aspect ratio
        */

        val zoomInfo = ZoomInfo()
        zoomInfo.bmpWidth = originalWidth
        if (ratio != 0.0F) {
            zoomInfo.bmpHeight = originalWidth.div(ratio).toInt()
        } else {
            zoomInfo.bmpHeight = originalHeight
        }
        return zoomInfo
    }

    private fun getZoomInfoForImage(
        firstSelectedMedia: ImageAdapterData,
        originalImageAdapterData: ImageAdapterData
    ): ZoomInfo {

        // getting original width of the new selected video
        val originalWidth =
            originalImageAdapterData.asset.contentUri.getImageDimensions(requireContext()).width
        val originalHeight =
            originalImageAdapterData.asset.contentUri.getVideoDimensions(requireContext()).height

        //TODO get selected media ki list ka first index
        val width = zoomImageAdapterDataMap[firstSelectedMedia]?.bmpWidth
        val height = zoomImageAdapterDataMap[firstSelectedMedia]?.bmpHeight

        var ratio = 0.0f

        try {
            if (width != null && height != null) {
                val ar = AspectRatio.of(width, height)
                ratio = (ar.x.toFloat() / ar.y)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        /*
        * As we are supposed to maintain the orientation PORTRAIT
        * So will keep the width same
        * And change height according to aspect ratio
        */


        val zoomInfo = ZoomInfo()
        zoomInfo.bmpWidth = originalWidth
        if (ratio != 0.0F) {
            zoomInfo.bmpHeight = originalWidth.div(ratio).toInt()
        } else {
            zoomInfo.bmpHeight = originalHeight
        }
        return zoomInfo

    }

    private fun updateMediaToUi(mediaVmMData: MediaVmMData?) {
        val tempImageAdapterList =
            mediaVmMData?.mediaUseCaseData?.mediaImporterData?.imageAdapterDataList ?: emptyList()

        val wasListInitiallyEmpty = imageDataList.size == 0
        val oldSize = imageDataList.size
        val dataForAllFolders = mediaVmMData?.folderName == null
        var itemsToBeAdded = tempImageAdapterList.size

        if (dataForAllFolders) {
            allImageDataList.addAll(tempImageAdapterList)

            if (selectedFolderText.isNullOrEmpty() ||
                selectedFolderText == noMediaAvailableText ||
                selectedFolderText == loadingMediaText ||
                selectedFolderText == AlbumUtil.RECENTS
            ) {
                imageDataList.clear()

                itemsToBeAdded += addCameraItemInEmptyList()
                imageDataList.addAll(allImageDataList)
            }

        } else {

            if (mediaVmMData?.isNewItem == true) {
                allImageDataList.addAll(0, tempImageAdapterList)
            }

            if (tvSelectedFolder.text == mediaVmMData?.folderName || tvSelectedFolder.text == AlbumUtil.RECENTS) {

                itemsToBeAdded += addCameraItemInEmptyList()

                if (mediaVmMData?.isNewItem == true) {
                    if (imageDataList.isNotEmpty()) {
                        imageDataList.addAll(1, tempImageAdapterList)

                        autoSelectFirstItemWhenFolderIsChanged(tempImageAdapterList)
                        imageAdapter.notifyItemRangeInserted(1, tempImageAdapterList.size)
                        return
                    }
                } else {
                    imageDataList.addAll(tempImageAdapterList)
                }
            }
        }

        if ((wasListInitiallyEmpty || mediaVmMData?.isNewItem == true) && imageDataList.isNotEmpty()) {
            autoSelectFirstItemWhenFolderIsChanged(tempImageAdapterList)
        }

        if (oldSize != imageDataList.size) {
            imageAdapter.notifyItemRangeInserted(oldSize, itemsToBeAdded)
        }
    }

    private fun autoSelectFirstItemWhenFolderIsChanged(list: List<ImageAdapterData>) {
        if (!isMultiSelectEnable()) {
            imageAdapter.clearSelectedItems()

            if (imageAdapter.itemCount > 1 && imageAdapter.addSelectedItem(1)) {

                zoomImageAdapterDataMap.clear()

                val itemData = list.first()
                selectedMediaView.loadAsset(itemData, prepareZoomInfo(itemData))

                imageAdapter.notifyItems()
            }
        }
    }

    private fun updateFolders(folderList: List<FolderData>?) {
        if (!folderList.isNullOrEmpty()) {
            folders.clear()
            folders.addAll(folderList)
            updateSelectedFolderText(AlbumUtil.RECENTS, clearList = false)
        } else {
            mediaLoadingFailed = true
            updateSelectedFolderText(
                getString(R.string.imagepicker_insta_no_media_available),
                clearList = false
            )
            showToast(getString(R.string.imagepicker_insta_no_media_available))
        }
    }

    private fun addCameraItemInEmptyList(): Int {
        val itemsAdded = 0
        if (imageDataList.isEmpty()) {
            context?.let { c ->
                if (c.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                    imageDataList.add(ImageAdapterData(Camera()))
                    return imageDataList.size
                }
            }
        }
        return itemsAdded
    }

    private fun handleSuccessSelectedUri(uris: List<Uri>) {

        if (!uris.isNullOrEmpty()) {

            val applink = (activity as? ImagePickerInstaActivity)?.applinkForGalleryProceed
            if (!applink.isNullOrEmpty()) {

                val finalApplink = CameraUtil.createApplinkToSendFileUris(applink, uris)
                val intent = RouteManager.getIntent(activity, finalApplink)
                intent.putExtra(EXTRA_SELECTED_FEED_ACCOUNT, viewModel.selectedFeedAccountType.value)
                startActivityForResult(intent, CREATE_POST_REQUEST_CODE)
            } else {
                activity?.setResult(
                    Activity.RESULT_OK,
                    CameraUtil.getIntentfromFileUris(ArrayList(uris))
                )
                activity?.finish()
            }
        }
    }

    private fun prepareZoomInfo(imageAdapterData: ImageAdapterData): ZoomInfo {
        var zoomInfo = zoomImageAdapterDataMap[imageAdapterData]
        if (zoomInfo == null) {
            zoomInfo = ZoomInfo()

            if (imageAdapterData.asset is PhotosData) {
                val size = imageAdapterData.asset.contentUri.getImageDimensions(requireContext())
                zoomInfo.bmpWidth = size.width
                zoomInfo.bmpHeight = size.height
            } else {
                val size = imageAdapterData.asset.contentUri.getVideoDimensions(requireContext())
                zoomInfo.bmpWidth = size.width
                zoomInfo.bmpHeight = size.height
            }
            zoomImageAdapterDataMap[imageAdapterData] = zoomInfo
        }
        return zoomInfo
    }

    private fun getPhotos() {
        viewModel.getFolderData()
        viewModel.getPhotos(queryConfiguration)
    }

    private fun handleCameraSuccessResponse(data: Intent?) {
        val dstLink = (activity as? ImagePickerInstaActivity)?.applinkToNavigateAfterMediaCapture
        if (dstLink.isNullOrEmpty()) {
            //Update current UI
            val uriList = data?.extras?.getParcelableArrayList<Uri>(BundleData.URIS)
            if (!uriList.isNullOrEmpty()) {
                viewModel.handleFileAddedEvent(uriList, queryConfiguration)
            }
        } else {
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        }
    }

    private fun handleCameraErrorResponse(data: Intent?) {
        val dstLink = (activity as? ImagePickerInstaActivity)?.applinkToNavigateAfterMediaCapture
        if (dstLink.isNullOrEmpty()) {
            //DO nothing
        } else {
            activity?.setResult(Activity.RESULT_CANCELED, data)
            activity?.finish()
        }
    }

    private fun stopMedia() {
        selectedMediaView.stopVideo()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtil.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    showDataUi()
                } else {
                    showPermissionUi()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_POST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedFeedAccountTypeValue = data?.getIntExtra(
                EXTRA_SELECTED_FEED_ACCOUNT,
                FeedAccountUiModel.Type.BUYER.value
            ) ?: FeedAccountUiModel.Type.BUYER.value

            val selectedFeedAccountType = FeedAccountUiModel.getTypeByValue(selectedFeedAccountTypeValue)
            val selectedFeedAccount = when(selectedFeedAccountType) {
                FeedAccountUiModel.Type.SELLER -> FeedAccountUiModel(
                    name = userSession.shopName,
                    iconUrl = userSession.shopAvatar,
                    type = selectedFeedAccountType
                )
                FeedAccountUiModel.Type.BUYER -> FeedAccountUiModel(
                    name = userSession.name,
                    iconUrl = userSession.profilePicture,
                    type = selectedFeedAccountType
                )
                else -> FeedAccountUiModel(
                    name = "",
                    iconUrl = "",
                    type = selectedFeedAccountType
                )
            }
            viewModel.setSelectedFeedAccount(selectedFeedAccount)
        }
    }

    override fun onStop() {
        super.onStop()
        stopMedia()
    }

    companion object {
        private const val EXTRA_SELECTED_FEED_ACCOUNT = "EXTRA_SELECTED_FEED_ACCOUNT"
        private const val CREATE_POST_REQUEST_CODE = 101
    }
}