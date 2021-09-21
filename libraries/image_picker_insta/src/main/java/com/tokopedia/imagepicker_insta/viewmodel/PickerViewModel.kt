package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.os.FileObserver
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.usecase.CropUseCase
import com.tokopedia.imagepicker_insta.usecase.PhotosUseCase
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PickerViewModel(val app: Application) : BaseAndroidViewModel(app) {
    val TAG = "CameraInsta"
    override val coroutineContext: CoroutineContext
        get() = super.coroutineContext + workerDispatcher + ceh

    val ceh = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }
    @Inject
    lateinit var photosUseCase: PhotosUseCase

    @Inject
    lateinit var cropUseCase: CropUseCase

    @Inject
    lateinit var workerDispatcher: CoroutineDispatcher

    val photosLiveData: MutableLiveData<LiveDataResult<MediaUseCaseData>> = MutableLiveData()
    val selectedMediaUriLiveData: MutableLiveData<LiveDataResult<List<Uri>>> = MutableLiveData()
    var mediaUseCaseData: MediaUseCaseData? = null
    var fileObserver: FileObserver? = null

    fun handleFileAddedEvent(fileUriList: ArrayList<Uri>) {
        launchCatchError(block = {
            fileUriList.forEach { fileUri ->
                if (!fileUri.path.isNullOrEmpty()) {
                    val file = File(fileUri.path!!)
                    if (file.exists() && file.length() > 0) {
                        mediaUseCaseData?.uriSet?.let { uriSet ->
                            if (!uriSet.contains(fileUri)) {
                                val asset = photosUseCase.createAssetsFromFile(file, app.applicationContext)
                                if (asset != null) {

                                    /**
                                     * 1. add item in imageadapterlist
                                     * 2. update folder item
                                     */

                                    // 1
                                    mediaUseCaseData?.mediaImporterData?.imageAdapterDataList?.add(0, ImageAdapterData(asset))
                                    val internalFolderData = mediaUseCaseData?.folderDataList?.first {
                                        it.folderTitle == StorageUtil.INTERNAL_FOLDER_NAME
                                    }
                                    if (internalFolderData == null) {
                                        val finalInternalFolderData = FolderData(
                                            StorageUtil.INTERNAL_FOLDER_NAME,
                                            photosUseCase.getSubtitle(1),
                                            fileUri, 1
                                        )
                                        //2
                                        mediaUseCaseData?.folderDataList?.add(finalInternalFolderData)
                                    } else {
                                        mediaUseCaseData?.folderDataList?.remove(internalFolderData)

                                        val finalInternalFolderData = FolderData(
                                            StorageUtil.INTERNAL_FOLDER_NAME,
                                            photosUseCase.getSubtitle(internalFolderData.itemCount + 1),
                                            fileUri,
                                            internalFolderData.itemCount + 1
                                        )
                                        //2
                                        mediaUseCaseData?.folderDataList?.add(finalInternalFolderData)
                                    }
                                }
                            }
                            photosLiveData.postValue(LiveDataResult.success(mediaUseCaseData!!))
                        }
                    }
                }

            }
        }, onError = {})
    }

    fun getImagesByFolderName(folderName: String?) {

        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())

            if (folderName == null && mediaUseCaseData != null) {
                photosLiveData.postValue(LiveDataResult.success(mediaUseCaseData!!))
            } else if (mediaUseCaseData == null) {
                photosLiveData.postValue(LiveDataResult.error(Exception("No Media found")))
            } else {
                val imageAdapterList = ArrayList(mediaUseCaseData!!.mediaImporterData.imageAdapterDataList.filter { it.asset.folder == folderName })
                val data = mediaUseCaseData!!.createMediaUseCaseData(imageAdapterList, mediaUseCaseData!!.mediaImporterData.folderSet)
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

    fun getUriOfSelectedMedia(imageSize: Int, map: Map<ImageAdapterData, ZoomInfo>) {
        launchCatchError(block = {
            selectedMediaUriLiveData.postValue(LiveDataResult.loading())
            val uriList = cropUseCase.cropPhotos(app, imageSize, map)
            selectedMediaUriLiveData.postValue(LiveDataResult.success(uriList))
        }, onError = {
            selectedMediaUriLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun insertIntoGallery(asset: Asset) {}

    override fun onCleared() {
        super.onCleared()
        fileObserver?.startWatching()
    }
}