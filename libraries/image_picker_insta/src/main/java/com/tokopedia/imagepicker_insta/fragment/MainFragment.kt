package com.tokopedia.imagepicker_insta.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker.common.ImageEditorBuilder
import com.tokopedia.imagepicker.common.ImagePickerResultExtractor
import com.tokopedia.imagepicker.common.ImageRatioType
import com.tokopedia.imagepicker.editor.main.view.ImageEditorActivity
import com.tokopedia.imagepicker_insta.*
import com.tokopedia.imagepicker_insta.activity.MainActivity
import com.tokopedia.imagepicker_insta.di.DaggerImagePickerComponent
import com.tokopedia.imagepicker_insta.di.module.AppModule
import com.tokopedia.imagepicker_insta.item_decoration.GridItemDecoration
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.viewmodel.PickerViewModel
import com.tokopedia.imagepicker_insta.views.AssetImageView
import com.tokopedia.imagepicker_insta.views.FolderChooserView
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.lang.ref.WeakReference
import javax.inject.Inject


class MainFragment: Fragment() {

    val EDITOR_REQUEST_CODE = 221

    lateinit var viewModel: PickerViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var rv:RecyclerView
    lateinit var selectedImage: AssetImageView
    lateinit var recentSection:LinearLayout
    lateinit var tvSelectedFolder:AppCompatTextView

    lateinit var imageAdapter: ImageAdapter
    val imageDataList = ArrayList<Asset>()
    val folders = arrayListOf<String>()

    var cameraCaptureFilePath:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
        handleCameraPermissionCallback()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.imagepicker_insta_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.imagepicker_insta_lanjut-> proceedNextStep()
        }
        return true
    }

    private fun proceedNextStep(){
        if(imageAdapter.selectedPositions.isNotEmpty()){
            val assets = imageAdapter.selectedPositions.map {
                imageAdapter.dataList[it].assetPath
            }
            val assetList = ArrayList(assets)
            val intent = ImageEditorActivity.getIntent(context,
                ImageEditorBuilder(assetList,
                    defaultRatio = ImageRatioType.RATIO_1_1))
            startActivityForResult(intent,EDITOR_REQUEST_CODE)
        }else{
            Toast.makeText(context,"Select any image first",Toast.LENGTH_SHORT).show()
        }
    }

    fun handleCameraPermissionCallback(){
        if(activity is MainActivity){
            (activity as MainActivity).cameraPermissionCallback = { hasAllPermission->
                if(hasAllPermission){
                    openCamera()
                }else{
                    Toast.makeText(context,"Please allow Permissions",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun initDagger(){
        val component = DaggerImagePickerComponent.builder()
            .appModule(AppModule((context as AppCompatActivity).application))
            .build()
        component.inject(this)
        if (context is AppCompatActivity) {
            val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
            viewModel = viewModelProvider[PickerViewModel::class.java]
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

    fun initViews(v:View){
        rv = v.findViewById(R.id.rv)
        selectedImage = v.findViewById(R.id.selected_image_view)
        recentSection = v.findViewById(R.id.recent_section)
        tvSelectedFolder = v.findViewById(R.id.tv_selected_folder)
        val toolbar:Toolbar = v.findViewById(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setupRv()
    }

    fun setClicks(){
        recentSection.setOnClickListener {
            if(folders.isEmpty()) return@setOnClickListener

            val bottomSheet = BottomSheetUnify()
            val folderView = FolderChooserView(it.context)
            bottomSheet.setChild(folderView)
            bottomSheet.show(childFragmentManager,"BottomSheet Tag")
            folderView.setData(folders)
            folderView.itemOnClick {
                refreshImages(it)
                bottomSheet.dismiss()
            }
        }
    }

    fun refreshImages(folderName:String?){
        viewModel.getImagesByFolderName(folderName)
    }

    fun setupRv(){

        val columnCount = 3
        rv.layoutManager = GridLayoutManager(context,columnCount)
        val width = context?.resources?.displayMetrics?.widthPixels?:0
        val contentHeight = width/columnCount
        imageAdapter = ImageAdapter(imageDataList, contentHeight,this::handleOnCameraIconTap)
        rv.adapter = imageAdapter
        val itemPadding = 4.toPx().toInt()
        rv.addItemDecoration(GridItemDecoration(itemPadding,true))
    }


    fun openCamera(){
        cameraCaptureFilePath = null
        cameraCaptureFilePath = CameraUtil.openCamera(WeakReference(this))
    }

    fun handleOnCameraIconTap(){
        if(activity is MainActivity){
                PermissionUtil.requestCameraAndWritePermission(activity as MainActivity)
        }
    }

    fun setObservers(){
        viewModel.photosLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                LiveDataResult.STATUS.LOADING->{
                    Toast.makeText(context,"Loading",Toast.LENGTH_SHORT).show()
                }
                LiveDataResult.STATUS.SUCCESS->{

                    imageDataList.clear()
                    imageDataList.add(Camera())
                    folders.clear()

                    if(!it.data?.assets.isNullOrEmpty()){
                        imageDataList.addAll(it.data!!.assets)


                        //update folders
                        if(!it.data.folders.isNullOrEmpty()){
                            folders.addAll(it.data.folders)
                            tvSelectedFolder.text = it.data.selectedFolder ?: PhotoImporter.ALL
                        }

                        Toast.makeText(context,"List updated",Toast.LENGTH_SHORT).show()
                    }else{
                        tvSelectedFolder.text = "No Photos are available"
                        Toast.makeText(context,"No data",Toast.LENGTH_SHORT).show()
                    }
                    imageAdapter.notifyDataSetChanged()
                }
                LiveDataResult.STATUS.ERROR->{
                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
                }
            }
        })

        imageAdapter.itemSelectCallback = {asset: Asset, isSelected: Boolean ->
            if(isSelected){
                selectedImage.loadAsset(asset)
            }else{
                selectedImage.removeAsset()
            }
        }
    }

    fun getPhotos(){
        viewModel.getPhotos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CameraUtil.REQUEST_IMAGE_CAPTURE->{
                if(resultCode == Activity.RESULT_OK){
                    handleCameraSuccessResponse()
                }
            }
            EDITOR_REQUEST_CODE->{
                handleEditorCallback(data)
            }
        }
    }

    private fun handleEditorCallback(data:Intent?){
        val imageOrPathList = ImagePickerResultExtractor.extract(data).imageUrlOrPathList
        imageOrPathList.size
    }

    private fun handleCameraSuccessResponse(){
        /*
        * 1. Add image to viewModel's list
        * 2. add image to selected image
        * 3. Add image to gallery
        * 4. Clear current Image file path
        * */
        if(!cameraCaptureFilePath.isNullOrEmpty()){
            val asset = viewModel.photosImporterData?.addCameraImage(cameraCaptureFilePath!!)
            if(asset!=null) {
                selectedImage.loadAsset(asset)
                addAssetToGallery(asset)
                addToCurrnetDisplayedList(asset)
            }

            cameraCaptureFilePath = null
        }
    }
    private fun addAssetToGallery(asset:Asset){
        viewModel.insertIntoGallery(asset)
    }

    private fun addToCurrnetDisplayedList(asset: Asset){

        if(tvSelectedFolder.text == Environment.DIRECTORY_PICTURES ||
            tvSelectedFolder.text == PhotoImporter.ALL ||
            tvSelectedFolder.text == StorageUtil.INTERNAL_FOLDER_NAME
                ){
            imageDataList.add(1,asset)
            imageAdapter.selectedPositions.add(1)
            imageAdapter.notifyItemInserted(1)
        }
    }
}