package com.tokopedia.imagepicker_insta.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker_insta.*
import com.tokopedia.imagepicker_insta.activity.CameraActivity
import com.tokopedia.imagepicker_insta.activity.ImagePickerInstaActivity
import com.tokopedia.imagepicker_insta.activity.ImagePickerInstaActivity.Companion.MAX_MULTI_SELECT_LIMIT
import com.tokopedia.imagepicker_insta.di.DaggerImagePickerComponent
import com.tokopedia.imagepicker_insta.item_decoration.GridItemDecoration
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.menu.MenuManager
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.trackers.TrackerProvider
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import com.tokopedia.imagepicker_insta.views.FolderChooserView
import com.tokopedia.imagepicker_insta.views.MediaView
import com.tokopedia.imagepicker_insta.views.NoPermissionsView
import com.tokopedia.imagepicker_insta.views.ToggleImageView
import com.tokopedia.imagepicker_insta.views.adapters.ImageAdapter
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference


class ImagePickerInstaMainFragment : PermissionFragment(), ImagePickerFragmentContract {

    val EDITOR_REQUEST_CODE = 221

    lateinit var viewModel: PickerViewModel

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
    val folders = arrayListOf<FolderData>()
    val zoomImageAdapterDataMap = mutableMapOf<ImageAdapterData, ZoomInfo>()

    var cameraCaptureFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuManager.addCustomMenu(activity, hasReadPermission(), menu) {
            proceedNextStep()
            TrackerProvider.tracker?.onNextButtonClick()
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
        val selectedUris = arrayListOf<Uri>()
        if (!imageAdapter.isSelectedPositionsEmpty()) {
            imageAdapter.selectedPositionMap.keys.forEach {
                selectedUris.add(it.asset.contentUri)
            }
        } else if (selectedMediaView.imageAdapterData != null) {
            selectedUris.add(selectedMediaView.imageAdapterData!!.asset.contentUri)
        }


        if (!selectedUris.isNullOrEmpty()) {
            viewModel.getUriOfSelectedMedia(selectedMediaView.width, zoomImageAdapterDataMap)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(context).inflate(R.layout.imagepicker_insta_fragment_main, container, false)
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
        setupRv()
    }

    fun setupToolbar(v: View) {
        val toolbar: Toolbar = v.findViewById(R.id.toolbar)
        val toolbarIcon: AppCompatImageView = v.findViewById(R.id.toolbar_icon)
        val toolbarTitle: Typography = v.findViewById(R.id.toolbar_title)
        val toolbarSubtitle: Typography = v.findViewById(R.id.toolbar_subtitle)
        val toolbarNavIcon: AppCompatImageView = v.findViewById(R.id.toolbar_nav_icon)
        toolbarNavIcon.setOnClickListener {
            TrackerProvider.tracker?.onBackButtonFromPicker()
            activity?.finish()
        }

        (activity as ImagePickerInstaActivity).run {
            setSupportActionBar(toolbar)
            setToolbarIcon(this, toolbarIcon)
            toolbarTitle.text = this.toolbarTitle
            toolbarSubtitle.text = this.toolbarSubTitle
        }
    }

    private fun setToolbarIcon(activity: ImagePickerInstaActivity, imageView: AppCompatImageView) {
        if (activity.toolbarIconRes != null && activity.toolbarIconRes != 0) {
            imageView.setImageResource(activity.toolbarIconRes)
        } else if (!activity.toolbarIconUrl.isNullOrEmpty()) {
            imageView.loadImageCircle(activity.toolbarIconUrl)
        } else {
            imageView.visibility = View.GONE
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
                refreshImages(folderData?.folderTitle)
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

                imageAdapter.addSelectedItem(selectedMediaView.imageAdapterData!!)
                imageAdapter.getListOfIndexWhichAreSelected().forEach {
                    imageAdapter.notifyItemChanged(it)
                }

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

    private fun refreshImages(folderName: String?) {
        viewModel.getImagesByFolderName(folderName)
    }

    fun setupRv() {

        val columnCount = 3
        val lm = GridLayoutManager(context, columnCount)
        rv.layoutManager = lm
        val width = context?.resources?.displayMetrics?.widthPixels ?: 0
        val contentHeight = width / columnCount

        var maxMultiSelect: Int = MAX_MULTI_SELECT_LIMIT
        if ((activity as? ImagePickerInstaActivity)?.maxMultiSelectAllowed != null) {
            maxMultiSelect = (activity as ImagePickerInstaActivity).maxMultiSelectAllowed
        }

        imageAdapter = ImageAdapter(imageDataList, contentHeight, this, maxMultiSelect, lm)
        rv.adapter = imageAdapter
        val itemPadding = 4.toPx().toInt()
        rv.addItemDecoration(GridItemDecoration(itemPadding, true))
        rv.itemAnimator = null
    }

    private fun openCamera() {
        cameraCaptureFilePath = null
        cameraCaptureFilePath = CameraUtil.openCamera(WeakReference(this), (activity as? ImagePickerInstaActivity)?.applinkToNavigateAfterMediaCapture)
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

    private fun setObservers() {
        viewLifecycleOwner.lifecycle.addObserver(selectedMediaView)

        viewModel.photosLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                }
                LiveDataResult.STATUS.SUCCESS -> {

                    imageDataList.clear()

                    context?.let { c ->
                        if (c.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
                            imageDataList.add(ImageAdapterData(Camera()))
                        }
                    }

                    folders.clear()

                    if (!it.data?.mediaImporterData?.imageAdapterDataList.isNullOrEmpty()) {
                        imageDataList.addAll(it.data!!.mediaImporterData.imageAdapterDataList)

                        if (!isMultiSelectEnable()) {
                            imageAdapter.clearSelectedItems()

                            if (imageAdapter.addSelectedItem(1)) {

                                zoomImageAdapterDataMap.clear()

                                val itemData = it.data.mediaImporterData.imageAdapterDataList.first()
                                selectedMediaView.loadAsset(itemData, prepareZoomInfo(itemData))
                            }
                        }

                        //update folders
                        if (!it.data.folderDataList.isNullOrEmpty()) {
                            folders.addAll(it.data.folderDataList)
                            tvSelectedFolder.text = it.data.selectedFolder ?: PhotoImporter.ALL
                        }

                    } else {
                        tvSelectedFolder.text = getString(R.string.imagepicker_insta_no_media_available)
                        showToast(getString(R.string.imagepicker_insta_no_media_available))
                    }
                    imageAdapter.notifyDataSetChanged()
                    rv.post { rv.scrollTo(0, 0) }
                }
                LiveDataResult.STATUS.ERROR -> {
                    tvSelectedFolder.text = getString(R.string.imagepicker_insta_utlam)
                    showToast(getString(R.string.imagepicker_insta_utlam), Toaster.TYPE_ERROR)
                }
            }
        })

        viewModel.selectedMediaUriLiveData.observe(viewLifecycleOwner, {
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
        })

        imageAdapter.itemSelectCallback = { imageAdapterData: ImageAdapterData, isSelected: Boolean ->
            if (isSelected) {

                if (!isMultiSelectEnable()) {
                    zoomImageAdapterDataMap.clear()
                }

                selectedMediaView.loadAsset(imageAdapterData, prepareZoomInfo(imageAdapterData))
            } else {
                //DO nothing
            }
        }

        imageAdapter.onItemLongClick = { imageAdapterData: ImageAdapterData ->
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

    private fun handleSuccessSelectedUri(uris: List<Uri>) {
        if (!uris.isNullOrEmpty()) {

            val applink = (activity as? ImagePickerInstaActivity)?.applinkForGalleryProceed
            if (!applink.isNullOrEmpty()) {

                val finalApplink = CameraUtil.createApplinkToSendFileUris(applink, uris)
                RouteManager.route(activity, finalApplink)
            } else {
                activity?.setResult(Activity.RESULT_OK, CameraUtil.getIntentfromFileUris(ArrayList(uris)))
                activity?.finish()
            }
        }
    }

    private fun prepareZoomInfo(imageAdapterData: ImageAdapterData): ZoomInfo {
        var zoomInfo = zoomImageAdapterDataMap[imageAdapterData]
        if (zoomInfo == null) {
            zoomInfo = ZoomInfo()
            zoomImageAdapterDataMap[imageAdapterData] = zoomInfo
        }
        return zoomInfo
    }

    private fun getPhotos() {
        viewModel.getPhotos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CameraActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleCameraSuccessResponse(data)
                } else {
                    handleCameraErrorResponse(data)
                }
            }
            EDITOR_REQUEST_CODE -> {
                handleEditorCallback(data)
            }
        }
    }

    private fun handleEditorCallback(data: Intent?) {
        val imageOrPathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
        imageOrPathList.size
    }

    private fun handleCameraSuccessResponse(data: Intent?) {
        val dstLink = (activity as? ImagePickerInstaActivity)?.applinkToNavigateAfterMediaCapture
        if (dstLink.isNullOrEmpty()) {
            //Update current UI
            val uriList = data?.extras?.getParcelableArrayList<Uri>(BundleData.URIS)
            if (!uriList.isNullOrEmpty()) {
                viewModel.handleFileAddedEvent(uriList)
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

    private fun addAssetToGallery(asset: Asset) {
        //Do nothing
    }

    private fun addToCurrnetDisplayedList(imageAdapterData: ImageAdapterData) {

        if (tvSelectedFolder.text == Environment.DIRECTORY_PICTURES ||
            tvSelectedFolder.text == PhotoImporter.ALL ||
            tvSelectedFolder.text == StorageUtil.INTERNAL_FOLDER_NAME
        ) {
            imageDataList.add(1, imageAdapterData)
            imageAdapter.addSelectedItem(1)
            imageAdapter.notifyItemInserted(1)
        }
    }

    private fun stopMedia() {
        selectedMediaView.stopVideo()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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

    override fun onStop() {
        super.onStop()
        stopMedia()
    }
}