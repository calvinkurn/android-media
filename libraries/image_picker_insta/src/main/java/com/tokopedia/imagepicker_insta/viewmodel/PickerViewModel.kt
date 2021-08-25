package com.tokopedia.imagepicker_insta.viewmodel

import android.Manifest
import android.app.Application
import android.content.ContentValues
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.usecase.PhotosUseCase
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.PhotosImporterData
import com.tokopedia.imagepicker_insta.util.PermissionUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.util.WriteStorageLocation
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    workerDispatcher: CoroutineDispatcher,
    val photosUseCase: PhotosUseCase,
    val app: Application
) : BaseAndroidViewModel(workerDispatcher,app) {
    val TAG = "CameraInsta"

    val photosLiveData:MutableLiveData<LiveDataResult<PhotosImporterData>> = MutableLiveData()
    var photosImporterData:PhotosImporterData?=null

    fun getImagesByFolderName(folderName:String?){

        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())

            if(folderName == null && photosImporterData!=null){
                photosLiveData.postValue(LiveDataResult.success(photosImporterData!!))
            }
            else if(photosImporterData == null) {
                photosLiveData.postValue(LiveDataResult.error(Exception("No Images found")))
            }else{
                val tempList = photosImporterData!!.assets.filter { it.folder == folderName }
                photosLiveData.postValue(LiveDataResult.success(PhotosImporterData(photosImporterData!!.folders, ArrayList(tempList), folderName)))
            }

        },onError = {
            photosLiveData.postValue(LiveDataResult.error(Exception("Unknown error")))
        })
    }

    fun getPhotos() {
        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())
            val result = photosUseCase.getPhotosFromMediaStorage(app)
            photosImporterData = result
            photosLiveData.postValue(LiveDataResult.success(result))
        },onError = {
            photosLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun insertIntoGallery(asset: Asset){
        when(StorageUtil.WRITE_LOCATION){
            WriteStorageLocation.EXTERNAL->{
                launchCatchError(block = {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    values.put(MediaStore.Images.Media.DATA, asset.assetPath)
                    val uri = app.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                },onError = {
                    it.printStackTrace()
                })
            }
        }
    }
}