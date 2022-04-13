package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.imagepicker_insta.common.ui.model.FeedAccountUiModel
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

    val photosFlow: MutableStateFlow<LiveDataResult<MediaVmMData>> = MutableStateFlow(LiveDataResult.loading())
    val selectedMediaUriLiveData: MutableLiveData<LiveDataResult<List<Uri>>> = MutableLiveData()
    val folderFlow :MutableStateFlow<LiveDataResult<List<FolderData>>> = MutableStateFlow(LiveDataResult.loading())
    private val folderDataList = arrayListOf<FolderData>()
    private val uriSet = HashSet<Uri>()

    private val _selectedFeedAccount = MutableStateFlow(FeedAccountUiModel.Empty)
    val selectedFeedAccount: Flow<FeedAccountUiModel>
        get() = _selectedFeedAccount

    fun getFolderData() {
        launchCatchError(block = {
            val list = photosUseCase.getFolderData(app)
            folderDataList.clear()
            folderDataList.addAll(list)
            folderFlow.emit(LiveDataResult.success(folderDataList))
        }, onError = {
            folderFlow.emit(LiveDataResult.error(it))
        })
    }

    fun handleFileAddedEvent(fileUriList: ArrayList<Uri>, queryConfiguration: QueryConfiguration) {
        launchCatchError(block = {
            val imageAdapterList = ArrayList<ImageAdapterData>()

            fileUriList.reversed().forEach { fileUri ->
                if (!fileUri.path.isNullOrEmpty()) {
                    val file = File(fileUri.path!!)
                    if (file.exists() && file.length() > 0) {
                        if (!uriSet.contains(fileUri)) {
                            val asset =
                                photosUseCase.createAssetsFromFile(file, app.applicationContext,queryConfiguration)
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
                folderFlow.emit(LiveDataResult.success(folderDataList))
                withContext(Dispatchers.Main) {
                    photosFlow.emit (
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

    private fun updateMediaCountInFolders(fileUri: Uri, mediaCount:Int, folderName: String) {
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

    fun getMediaByFolderName(folderName: String,queryConfiguration: QueryConfiguration) {

        launchCatchError(block = {
            withContext(Dispatchers.Main) {
                photosFlow.emit(LiveDataResult.loading())
            }
            photosUseCase.getMediaByFolderNameFlow(folderName, app, queryConfiguration)
                .collect {
                    withContext(Dispatchers.Main){
                        photosFlow.emit(LiveDataResult.success(MediaVmMData(it, folderName)))
                    }

                }
        }, onError = {
            withContext(Dispatchers.Main){
                photosFlow.emit(LiveDataResult.error(Exception("Unknown error")))
            }
        })
    }

    fun getPhotos(queryConfiguration: QueryConfiguration) {

        launchCatchError(block = {
            withContext(Dispatchers.Main) {
                photosFlow.emit(LiveDataResult.loading())
            }
            photosUseCase.getMediaByFolderNameFlow(AlbumUtil.RECENTS, app, queryConfiguration)
                .collect {
                    uriSet.addAll(photosUseCase.getUriSetFromImageAdapterData(it.mediaImporterData.imageAdapterDataList))
                    withContext(Dispatchers.Main){
                        photosFlow.emit(LiveDataResult.success(MediaVmMData(it)))
                    }
                }
        }, onError = {
            withContext(Dispatchers.Main) {
                photosFlow.emit(LiveDataResult.error(it))
            }
        })
    }

    fun getUriOfSelectedMedia(width: Int,height:Int, pairList: List<Pair<ImageAdapterData, ZoomInfo>>) {
        launchCatchError(block = {
            selectedMediaUriLiveData.postValue(LiveDataResult.loading())
            val uriList = cropUseCase.cropPhotos(app, width, height, pairList)
            selectedMediaUriLiveData.postValue(LiveDataResult.success(uriList))
        }, onError = {
            selectedMediaUriLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun setSelectedFeedAccount(feedAccount: FeedAccountUiModel) {
        launchCatchError(block = {
            val current = _selectedFeedAccount.value
            if(current.type != feedAccount.type) {
                _selectedFeedAccount.value = feedAccount
            }
        }, onError = { })
    }

    override fun onCleared() {
        super.onCleared()
        flush()
    }
}