package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.models.*
import com.tokopedia.imagepicker_insta.usecase.CropUseCase
import com.tokopedia.imagepicker_insta.usecase.PhotosUseCase
import com.tokopedia.imagepicker_insta.util.AlbumUtil
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
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
            val imageAdapterList = ArrayList<ImageAdapterData>()

            fileUriList.reversed().forEach { fileUri ->
                if (!fileUri.path.isNullOrEmpty()) {
                    val file = File(fileUri.path!!)
                    if (file.exists() && file.length() > 0) {
                        if (!uriSet.contains(fileUri)) {
                            val asset =
                                photosUseCase.createAssetsFromFile(file, app.applicationContext)
                            if (asset != null) {

                                /**
                                 * 1. add item in imageadapterlist
                                 * 2. update folder item
                                 */

                                // 1
                                uriSet.add(asset.contentUri)
                                imageAdapterList.add(ImageAdapterData(asset))
                            }
                        }
                    }
                }
            }
            if (imageAdapterList.isNotEmpty()) {
                updateMediaCountInFolders(imageAdapterList.first().asset.contentUri,imageAdapterList.size, AlbumUtil.RECENTS)
                updateMediaCountInFolders(imageAdapterList.first().asset.contentUri,imageAdapterList.size, StorageUtil.INTERNAL_FOLDER_NAME)
                folderLiveData.postValue(LiveDataResult.success(folderDataList))
                withContext(Dispatchers.Main) {
                    photosLiveData.value = (
                            LiveDataResult.success(
                                MediaVmMData(
                                    MediaUseCaseData(
                                        MediaImporterData(
                                            imageAdapterList
                                        )
                                    ),
                                    StorageUtil.INTERNAL_FOLDER_NAME,
                                    isNewItem = true
                                )
                            )
                            )
                }
            }
        }, onError = {})
    }

    fun updateMediaCountInFolders(fileUri: Uri, mediaCount:Int, folderName: String) {
        val internalFolderData: FolderData? = folderDataList.firstOrNull {
            it.folderTitle == folderName
        }
        if (internalFolderData == null) {
            val finalInternalFolderData = FolderData(
                folderName,
                CameraUtil.getMediaCountText(mediaCount),
                fileUri, mediaCount
            )
            //2
            folderDataList.add(finalInternalFolderData)
        } else {
            folderDataList.remove(internalFolderData)

            val finalInternalFolderData = FolderData(
                folderName,
                CameraUtil.getMediaCountText(internalFolderData.itemCount + mediaCount),
                fileUri,
                internalFolderData.itemCount + mediaCount
            )
            //2
            folderDataList.add(finalInternalFolderData)
        }
    }

    fun getMediaByFolderName(folderName: String) {

        launchCatchError(block = {
            withContext(Dispatchers.Main) {
                photosLiveData.value = (LiveDataResult.loading())
            }
            photosUseCase.getMediaByFolderNameFlow(folderName, app)
                .collect {
                    withContext(Dispatchers.Main) {
                        photosLiveData.value =
                            (LiveDataResult.success(MediaVmMData(it, folderName)))
                    }
                }
        }, onError = {
            withContext(Dispatchers.Main) {
                photosLiveData.value = (LiveDataResult.error(Exception("Unknown error")))
            }
            Timber.e(it)
        })
    }

    fun getPhotos() {

        launchCatchError(block = {
            withContext(Dispatchers.Main) {
                photosLiveData.value = (LiveDataResult.loading())
            }
            photosUseCase.getMediaByFolderNameFlow(AlbumUtil.RECENTS, app)
                .collect {
                    uriSet.addAll(photosUseCase.getUriSetFromImageAdapterData(it.mediaImporterData.imageAdapterDataList))
                    withContext(Dispatchers.Main) {
                        photosLiveData.value = (LiveDataResult.success(MediaVmMData(it)))
                    }

                }
        }, onError = {
            withContext(Dispatchers.Main) {
                photosLiveData.value = (LiveDataResult.error(it))
            }
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
        flush()
    }
}