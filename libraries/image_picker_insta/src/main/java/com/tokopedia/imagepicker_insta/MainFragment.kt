package com.tokopedia.imagepicker_insta

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker_insta.di.DaggerImagePickerComponent
import com.tokopedia.imagepicker_insta.di.module.AppModule
import com.tokopedia.imagepicker_insta.item_decoration.GridItemDecoration
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.Camera
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.views.FolderChooserView
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.lang.ref.WeakReference
import javax.inject.Inject

const val ALL = "All"
class MainFragment: Fragment() {

    lateinit var viewModel: PickerViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var rv:RecyclerView
    lateinit var selectedImage:AssetImageView
    lateinit var recentSection:LinearLayout
    lateinit var tvSelectedFolder:AppCompatTextView

    lateinit var imageAdapter:ImageAdapter
    val imageDataList = ArrayList<Asset>()
    val folders = arrayListOf<String>()

    var cameraCaptureFilePath:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDagger()
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
        setupRv()
    }

    fun setClicks(){
        recentSection.setOnClickListener {
            if(folders.isEmpty()) return@setOnClickListener

            val bottomSheet = BottomSheetUnify()
            val folderView = FolderChooserView(it.context,)
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

    fun handleOnCameraIconTap(){
        cameraCaptureFilePath = null
        cameraCaptureFilePath = CameraUtil.openCamera(WeakReference(this))
    }

    fun setObservers(){
        viewModel.photosLiveData.observe(viewLifecycleOwner,{
            when(it.status){
                LiveDataResult.STATUS.LOADING->{
                    Toast.makeText(context,"Loading",Toast.LENGTH_SHORT).show()
                }
                LiveDataResult.STATUS.SUCCESS->{
                    if(!it.data?.assets.isNullOrEmpty()){
                        imageDataList.clear()
                        imageDataList.add(Camera())
                        imageDataList.addAll(it.data!!.assets)
                        imageAdapter.notifyDataSetChanged()

                        //update folders
                        folders.clear()
                        if(!it.data.folders.isNullOrEmpty()){
                            folders.addAll(it.data.folders)
                            tvSelectedFolder.text = it.data.selectedFolder ?: ALL
                        }

                        Toast.makeText(context,"List updated",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"No data",Toast.LENGTH_SHORT).show()
                    }
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
        if(requestCode == CameraUtil.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            handleCameraSuccessResponse()
        }
    }

    private fun handleCameraSuccessResponse(){
        cameraCaptureFilePath = null
    }
}