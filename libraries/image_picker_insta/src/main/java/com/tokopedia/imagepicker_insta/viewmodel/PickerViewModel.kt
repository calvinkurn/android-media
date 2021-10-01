package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import android.net.Uri
import android.os.FileObserver
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.mediaImporter.PhotoImporter
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.usecase.CropUseCase
import com.tokopedia.imagepicker_insta.usecase.PhotosUseCase
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collect
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

    val photosLiveData: MutableLiveData<LiveDataResult<MediaVmMData>> = MutableLiveData()
    val selectedMediaUriLiveData: MutableLiveData<LiveDataResult<List<Uri>>> = MutableLiveData()

    var fileObserver: FileObserver? = null
    val folderDataList = arrayListOf<FolderData>()
    val uriSet = HashSet<Uri>()
    val folderLiveData = MutableLiveData<LiveDataResult<List<FolderData>>>()

    fun getFolderData() {
        launchCatchError(block = {
            val list = photosUseCase.getFolderData(app)
            folderDataList.addAll(list)
            folderLiveData.postValue(LiveDataResult.success(folderDataList))
        }, onError = {
            folderLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun handleFileAddedEvent(fileUriList: ArrayList<Uri>) {
        launchCatchError(block = {
            fileUriList.forEach { fileUri ->
                if (!fileUri.path.isNullOrEmpty()) {
                    val file = File(fileUri.path!!)
                    if (file.exists() && file.length() > 0) {
                        if (!uriSet.contains(fileUri)) {
                            val asset = photosUseCase.createAssetsFromFile(file, app.applicationContext)
                            if (asset != null) {

                                /**
                                 * 1. add item in imageadapterlist
                                 * 2. update folder item
                                 */

                                // 1
                                uriSet.add(asset.contentUri)
                                val imageAdapterList = ArrayList<ImageAdapterData>()
                                imageAdapterList.add(ImageAdapterData(asset))

                                val internalFolderData: FolderData? = folderDataList.firstOrNull {
                                    it.folderTitle == StorageUtil.INTERNAL_FOLDER_NAME
                                }
                                if (internalFolderData == null) {
                                    val finalInternalFolderData = FolderData(
                                        StorageUtil.INTERNAL_FOLDER_NAME,
                                        CameraUtil.getMediaCountText(1),
                                        fileUri, 1
                                    )
                                    //2
                                    folderDataList.add(finalInternalFolderData)
                                } else {
                                    folderDataList.remove(internalFolderData)

                                    val finalInternalFolderData = FolderData(
                                        StorageUtil.INTERNAL_FOLDER_NAME,
                                        CameraUtil.getMediaCountText(internalFolderData.itemCount + 1),
                                        fileUri,
                                        internalFolderData.itemCount + 1
                                    )
                                    //2
                                    folderDataList.add(finalInternalFolderData)
                                }
                                folderLiveData.postValue(LiveDataResult.success(folderDataList))
                                photosLiveData.postValue(
                                    LiveDataResult.success(
                                        MediaVmMData(
                                            MediaUseCaseData(MediaImporterData(imageAdapterList)),
                                            StorageUtil.INTERNAL_FOLDER_NAME,
                                            isNewItem = true
                                        )
                                    )
                                )
                            }
                        }
                    }
                }

            }
        }, onError = {})
    }

    fun getMediaByFolderName(folderName: String, mediaCount: Int?) {

        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())
            photosUseCase.getMediaByFolderNameFlow(folderName, app)
                .collect {
                    photosLiveData.postValue(LiveDataResult.success(MediaVmMData(it, folderName)))
                }
        }, onError = {
            photosLiveData.postValue(LiveDataResult.error(Exception("Unknown error")))
            Timber.e(it)
        })
    }

    fun getPhotos() {

        launchCatchError(block = {
            photosLiveData.postValue(LiveDataResult.loading())
            photosUseCase.getMediaByFolderNameFlow(PhotoImporter.ALL, app)
                .collect {
                    uriSet.addAll(photosUseCase.getUriSetFromImageAdapterData(it.mediaImporterData.imageAdapterDataList))
                    photosLiveData.postValue(LiveDataResult.success(MediaVmMData(it)))
                }
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

    override fun onCleared() {
        super.onCleared()
        fileObserver?.startWatching()
        flush()
    }
}