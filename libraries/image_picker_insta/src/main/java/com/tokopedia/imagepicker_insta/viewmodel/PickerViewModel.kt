package com.tokopedia.imagepicker_insta.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.tokopedia.imagepicker_insta.LiveDataResult
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.imagepicker_insta.models.ImageAdapterData
import com.tokopedia.imagepicker_insta.models.MediaVmMData
import com.tokopedia.imagepicker_insta.models.QueryConfiguration
import com.tokopedia.imagepicker_insta.models.FolderData
import com.tokopedia.imagepicker_insta.models.ZoomInfo
import com.tokopedia.imagepicker_insta.models.MediaUseCaseData
import com.tokopedia.imagepicker_insta.models.MediaImporterData
import com.tokopedia.imagepicker_insta.usecase.CropUseCase
import com.tokopedia.content.common.usecase.GetContentFormUseCase
import com.tokopedia.imagepicker_insta.usecase.PhotosUseCase
import com.tokopedia.imagepicker_insta.util.AlbumUtil
import com.tokopedia.imagepicker_insta.util.CameraUtil
import com.tokopedia.imagepicker_insta.util.StorageUtil
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PickerViewModel(
    val app: Application,
) : BaseAndroidViewModel(app) {

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

    @Inject
    lateinit var getContentFormUseCase: GetContentFormUseCase

    val photosFlow: MutableStateFlow<LiveDataResult<MediaVmMData>> = MutableStateFlow(LiveDataResult.loading())
    val selectedMediaUriLiveData: MutableLiveData<LiveDataResult<List<Uri>>> = MutableLiveData()
    val folderFlow :MutableStateFlow<LiveDataResult<List<FolderData>>> = MutableStateFlow(LiveDataResult.loading())
    private val folderDataList = arrayListOf<FolderData>()
    private val uriSet = HashSet<Uri>()

    val selectedFeedAccountId: String
        get() = _selectedFeedAccount.value.id

    private val _selectedFeedAccount = MutableStateFlow(ContentAccountUiModel.Empty)
    val selectedContentAccount: Flow<ContentAccountUiModel>
        get() = _selectedFeedAccount

    private val _feedAccountListState = MutableStateFlow<List<ContentAccountUiModel>>(emptyList())
    val contentAccountListState: Flow<List<ContentAccountUiModel>>
        get() = _feedAccountListState

    val contentAccountList: List<ContentAccountUiModel>
        get() = _feedAccountListState.value

    val isAllowChangeAccount: Boolean
        get() = contentAccountList.size > 1 && contentAccountList.find { it.isUserPostEligible } != null

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

    fun getFeedAccountList(isCreatePostAsBuyer: Boolean) {
        launchCatchError(block = {
            val response = getContentFormUseCase.apply {
                setRequestParams(GetContentFormUseCase.createParams(mutableListOf(), "entrypoint", ""))
            }.executeOnBackground()

            val feedAccountList = response.feedContentForm.authors.map {
                ContentAccountUiModel(
                    id = it.id,
                    name = it.name,
                    iconUrl = it.thumbnail,
                    badge = it.badge,
                    type = it.type,
                    hasUsername = response.feedContentForm.hasUsername,
                    hasAcceptTnc = response.feedContentForm.hasAcceptTnc,
                    enable = response.feedContentForm.hasAcceptTnc,
                )
            }

            _feedAccountListState.value = feedAccountList

            if(feedAccountList.isNotEmpty()) {
                _selectedFeedAccount.value = if(isCreatePostAsBuyer) feedAccountList.firstOrNull { it.isUser } ?: feedAccountList.first()
                else feedAccountList.first()
            }
        }, onError = {})
    }

    fun setSelectedFeedAccount(contentAccount: ContentAccountUiModel) {
        launchCatchError(block = {
            val current = _selectedFeedAccount.value
            if(current.id != contentAccount.id) {
                _selectedFeedAccount.value = contentAccount
            }
        }, onError = { })
    }

    fun setSelectedFeedAccountId(feedAccountId: String) {
        launchCatchError(block = {
            val current = _selectedFeedAccount.value
            if(current.id != feedAccountId) {
                _selectedFeedAccount.value = _feedAccountListState.value.firstOrNull {
                    it.id == feedAccountId
                } ?: ContentAccountUiModel.Empty
            }
        }, onError = { })
    }

    override fun onCleared() {
        super.onCleared()
        flush()
    }
}
