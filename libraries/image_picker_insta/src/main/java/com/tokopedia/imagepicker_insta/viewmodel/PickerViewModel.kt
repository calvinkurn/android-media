package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import android.content.ContentValues
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.models.Asset
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.models.MediaUseCaseData
import com.tokopedia.imagepicker_insta.usecase.PhotosUseCase
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.imagepicker_insta.util.WriteStorageLocation
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PickerViewModel(val app: Application) : BaseAndroidViewModel(app) {
    val TAG = "CameraInsta"
    override val coroutineContext: CoroutineContext
        get() = super.coroutineContext + workerDispatcher

    @Inject
    lateinit var photosUseCase: PhotosUseCase

    @Inject
    lateinit var workerDispatcher: CoroutineDispatcher

    val photosLiveData: MutableLiveData<LiveDataResult<MediaUseCaseData>> = MutableLiveData()
    var mediaUseCaseData: MediaUseCaseData? = null

    fun getImagesByFolderName(folderName: String?) {

        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())

            if (folderName == null && mediaUseCaseData != null) {
                photosLiveData.postValue(LiveDataResult.success(mediaUseCaseData!!))
            } else if (mediaUseCaseData == null) {
                photosLiveData.postValue(LiveDataResult.error(Exception("No Media found")))
            } else {
                val imageAdapterList = ArrayList(mediaUseCaseData!!.mediaImporterData.imageAdapterDataList.filter { it.asset.folder == folderName })
                val data = MediaUseCaseData(
                    MediaImporterData(imageAdapterList, mediaUseCaseData!!.mediaImporterData.folderSet),
                    mediaUseCaseData!!.folderDataList,
                    folderName
                )
                photosLiveData.postValue(LiveDataResult.success(data))
            }

        }, onError = {
            photosLiveData.postValue(LiveDataResult.error(Exception("Unknown error")))
            Timber.e(it)
        })
    }

    fun getPhotos() {
        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())
            val result = photosUseCase.getAssetsFromMediaStorage(app)
            mediaUseCaseData = result
            photosLiveData.postValue(LiveDataResult.success(result))
        }, onError = {
            photosLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun insertIntoGallery(asset: Asset) {
        when (StorageUtil.WRITE_LOCATION) {
            WriteStorageLocation.EXTERNAL -> {
                launchCatchError(block = {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    values.put(MediaStore.Images.Media.DATA, asset.assetPath)
                    val uri = app.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                }, onError = {
                    it.printStackTrace()
                })
            }
        }
    }
}