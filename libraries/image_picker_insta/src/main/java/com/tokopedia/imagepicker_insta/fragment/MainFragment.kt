package com.tokopedia.imagepicker_insta.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker.common.ImageEditorBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.ImageRatioType
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker_insta.*
import com.tokopedia.imagepicker_insta.activity.MainActivity
import com.tokopedia.imagepicker_insta.activity.MainActivity.Companion.MAX_MULTI_SELECT_LIMIT
import com.tokopedia.imagepicker_insta.di.DaggerImagePickerComponent
import com.tokopedia.imagepicker_insta.item_decoration.GridItemDecoration
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.menu.MenuManager
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.models.FolderData
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import com.tokopedia.imagepicker_insta.views.FolderChooserView
import com.tokopedia.imagepicker_insta.views.MediaView
import com.tokopedia.imagepicker_insta.views.ToggleImageView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference


class MainFragment : Fragment(), MainFragmentContract {

    val EDITOR_REQUEST_CODE = 221

    lateinit var viewModel: PickerViewModel

    lateinit var rv: RecyclerView
    lateinit var selectedMediaView: MediaView
    lateinit var recentSection: LinearLayout
    lateinit var tvSelectedFolder: AppCompatTextView
    lateinit var imageFitCenter: AppCompatImageView
    lateinit var imageMultiSelect: ToggleImageView

    lateinit var imageAdapter: ImageAdapter
    val imageDataList = ArrayList<ImageAdapterData>()
    val folders = arrayListOf<FolderData>()

    var cameraCaptureFilePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        handleCameraPermissionCallback()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        MenuManager.addCustomMenu(activity, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            MenuManager.MENU_ITEM_ID -> {
                proceedNextStep()
                return true
            }
        }

        return false
    }

    private fun proceedNextStep() {
        if (imageAdapter.isSelectedPositionsEmpty()) {

            val assets = imageAdapter.selectedPositionMap.keys.map {
                imageAdapter.dataList[it].asset.assetPath
            }
            val assetList = ArrayList<String>(assets)
            val intent = ImageEditorActivity.getIntent(
                context,
                ImageEditorBuilder(
                    assetList,
                    defaultRatio = ImageRatioType.RATIO_1_1
                )
            )
            startActivityForResult(intent, EDITOR_REQUEST_CODE)
        } else {
            Toast.makeText(context, "Select any image first", Toast.LENGTH_SHORT).show()
        }
    }

    fun handleCameraPermissionCallback() {
        if (activity is MainActivity) {
            (activity as MainActivity).cameraPermissionCallback = { hasAllPermission ->
                if (hasAllPermission) {
                    openCamera()
                } else {
                    Toast.makeText(context, "Please allow Permissions", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun initDagger() {
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
        getPhotos()
        return v
    }

    fun initViews(v: View) {
        rv = v.findViewById(R.id.rv)
        selectedMediaView = v.findViewById(R.id.selected_image_view)
        recentSection = v.findViewById(R.id.recent_section)
        tvSelectedFolder = v.findViewById(R.id.tv_selected_folder)
        imageFitCenter = v.findViewById(R.id.image_fit_center)
        imageMultiSelect = v.findViewById(R.id.multi_select_toggle)

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
            activity?.finish()
        }

        (activity as MainActivity).run {
            setSupportActionBar(toolbar)
            toolbarIcon.setImageResource(this.toolbarIconRes)
            toolbarTitle.text = this.toolbarTitle
            toolbarSubtitle.text = this.toolbarSubTitle
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
            folderView.itemOnClick { folderData ->
                refreshImages(folderData?.folderTitle)
                bottomSheet.dismiss()
            }
        }

        imageFitCenter.setOnClickListener {
            if (selectedMediaView.asset != null) {
                if (selectedMediaView.scaleType == ImageView.ScaleType.CENTER_CROP) {
                    selectedMediaView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                } else {
                    selectedMediaView.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

        imageMultiSelect.setOnClickListener {
            imageMultiSelect.toggle()
            imageAdapter.canMultiSelect = imageMultiSelect.isChecked
            imageAdapter.selectedPositionMap.keys.map {
                imageAdapter.dataList[it].isSelected = false
            }
            imageAdapter.dataList.forEach {
                it.isInMultiSelectMode = imageAdapter.canMultiSelect
            }
            val hasSelectedItems = imageAdapter.selectedPositionMap.isNotEmpty()
            imageAdapter.clearSelectedItems()
            if (hasSelectedItems) {
                imageAdapter.notifyDataSetChanged()
            }
            selectedMediaView.removeAsset()
        }
    }

    private fun refreshImages(folderName: String?) {
        viewModel.getImagesByFolderName(folderName)
    }

    fun setupRv() {

        val columnCount = 3
        rv.layoutManager = GridLayoutManager(context, columnCount)
        val width = context?.resources?.displayMetrics?.widthPixels ?: 0
        val contentHeight = width / columnCount

        var maxMultiSelect: Int = MAX_MULTI_SELECT_LIMIT
        if ((activity as? MainActivity)?.maxMultiSelectAllowed != null) {
            maxMultiSelect = (activity as MainActivity).maxMultiSelectAllowed
        }

        imageAdapter = ImageAdapter(imageDataList, contentHeight, this, maxMultiSelect)
        rv.adapter = imageAdapter
        val itemPadding = 4.toPx().toInt()
        rv.addItemDecoration(GridItemDecoration(itemPadding, true))
        rv.itemAnimator = null
    }

    private fun openCamera() {
        cameraCaptureFilePath = null
        cameraCaptureFilePath = CameraUtil.openCamera(WeakReference(this))
    }

    override fun handleOnCameraIconTap() {
        if (activity is MainActivity) {
            PermissionUtil.requestCameraAndWritePermission(activity as MainActivity)
        }
    }

    override fun showToast(message: String, toasterType: Int) {
        view?.let {
            Toaster.build(it, message, toasterType).show()
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycle.addObserver(selectedMediaView)

        viewModel.photosLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
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
                        imageAdapter.clearSelectedItems()
                        imageAdapter.addSelectedItem(1)
                        selectedMediaView.loadAsset(it.data.mediaImporterData.imageAdapterDataList.first().asset)

                        //update folders
                        if (!it.data.folderDataList.isNullOrEmpty()) {
                            folders.addAll(it.data.folderDataList)
                            tvSelectedFolder.text = it.data.selectedFolder ?: PhotoImporter.ALL
                        }

                        Toast.makeText(context, "List updated", Toast.LENGTH_SHORT).show()
                    } else {
                        tvSelectedFolder.text = "No Media available"
                        Toast.makeText(context, "No data", Toast.LENGTH_SHORT).show()
                    }
                    imageAdapter.notifyDataSetChanged()
                }
                LiveDataResult.STATUS.ERROR -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })

        imageAdapter.itemSelectCallback = { imageAdapterData: ImageAdapterData, isSelected: Boolean ->
            if (isSelected) {
                selectedMediaView.loadAsset(imageAdapterData.asset)
            } else {
                selectedMediaView.removeAsset()
            }
        }
    }

    private fun getPhotos() {
        viewModel.getPhotos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CameraUtil.REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    handleCameraSuccessResponse()
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

    private fun handleCameraSuccessResponse() {
        /*
        * 1. Add image to viewModel's list
        * 2. add image to selected image
        * 3. Add image to gallery
        * 4. Clear current Image file path
        * */
        if (!cameraCaptureFilePath.isNullOrEmpty()) {
            val imageAdapterData = viewModel.mediaUseCaseData?.mediaImporterData?.addCameraImage(cameraCaptureFilePath!!)
            if (imageAdapterData != null) {
                selectedMediaView.loadAsset(imageAdapterData.asset)
                addAssetToGallery(imageAdapterData.asset)
                addToCurrnetDisplayedList(imageAdapterData)
            }

            cameraCaptureFilePath = null
        }
    }

    private fun addAssetToGallery(asset: Asset) {
        viewModel.insertIntoGallery(asset)
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

    private fun reset(){
        if (selectedMediaView.asset != null) {
            selectedMediaView.removeAsset()
        }
        imageAdapter.clearSelectedItems()
        //Todo Rahul can avoid this - Later
        imageAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        reset()
    }
}