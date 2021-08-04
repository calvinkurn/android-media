package com.tokopedia.imagepicker_insta

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.models.PhotosImporterData
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PickerViewModel @Inject constructor(
    workerDispatcher: CoroutineDispatcher,
    val photosUseCase: PhotosUseCase,
    val app: Application
) : BaseAndroidViewModel(workerDispatcher,app) {

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
                photosLiveData.postValue(LiveDataResult.success(PhotosImporterData(photosImporterData!!.folders,tempList,folderName)))
            }

        },onError = {
            photosLiveData.postValue(LiveDataResult.error(Exception("Unknown error")))
        })
    }

    fun getPhotos() {
        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())
            val result = photosUseCase.getPhotos(app)
            photosImporterData = result
            photosLiveData.postValue(LiveDataResult.success(result))
        },onError = {
            photosLiveData.postValue(LiveDataResult.error(it))
        })
    }
}