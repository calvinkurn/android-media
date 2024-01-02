package com.tokopedia.shareexperience.ui

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.data.dto.request.ShareExProductRequest
import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.ui.uistate.ShareExBottomSheetUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExFetchUiState
import com.tokopedia.shareexperience.ui.uistate.ShareExNavigationUiState
import com.tokopedia.shareexperience.ui.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ShareExViewModel @Inject constructor(
    private val getSharePropertiesUseCase: ShareExGetSharePropertiesUseCase,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _actionFlow =
        MutableSharedFlow<ShareExAction>(extraBufferCapacity = 16)

    // Cache the model from activity and body switching when chip clicked
    private var _bottomSheetModel: ShareExBottomSheetModel? = null

    private val _fetchedDataState = MutableStateFlow<ShareExFetchUiState?>(null)
    val fetchedDataState = _fetchedDataState.asStateFlow()

    private val _bottomSheetUiState = MutableStateFlow(ShareExBottomSheetUiState())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    private val _navigationUiState = MutableSharedFlow<ShareExNavigationUiState>(
        extraBufferCapacity = 16
    )
    val navigationUiState = _navigationUiState.asSharedFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    fun processAction(action: ShareExAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    private fun Flow<ShareExAction>.process() {
        onEach {
            when (it) {
                is ShareExAction.FetchShareData -> {
                    getShareData()
                }
                is ShareExAction.InitializePage -> {
                    getShareBottomSheetData()
                }
                is ShareExAction.UpdateShareBody -> {
                    updateShareBottomSheetBody(it.position)
                }
                is ShareExAction.UpdateShareImage -> {
                    updateShareImage(it.imageUrl)
                }
                is ShareExAction.NavigateToPage -> {
                    navigateToPage(it.appLink)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getShareData() {
        viewModelScope.launch {
            try {
                getSharePropertiesUseCase.getData(
                    ShareExProductRequest(
                        pageType = ShareExPageTypeEnum.PDP.value,
                        id = 2150412049
                    )
                ).catch {
                    _fetchedDataState.value = ShareExFetchUiState(false, it)
                }.collectLatest {
                    _bottomSheetModel = it
                    _fetchedDataState.value = ShareExFetchUiState(true, null)
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _fetchedDataState.value = ShareExFetchUiState(false, throwable)
            }
        }
    }

    private fun getShareBottomSheetData() {
        viewModelScope.launch {
            try {
                val uiResult = _bottomSheetModel?.map()
                _bottomSheetUiState.update { uiState ->
                    uiState.copy(
                        title = _bottomSheetModel?.title ?: "",
                        uiModelList = uiResult
                    )
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun updateShareBottomSheetBody(position: Int) {
        viewModelScope.launch {
            try {
                _bottomSheetModel?.let { bottomSheetModel ->
                    val updatedUiResult = bottomSheetModel.map(position = position)
                    _bottomSheetUiState.update { uiState ->
                        uiState.copy(
                            uiModelList = updatedUiResult
                        )
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun updateShareImage(imageUrl: String) {
        viewModelScope.launch {
            try {
                // TODO: Update Image Generator value
                Timber.d("New Image Url: $imageUrl")
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun navigateToPage(appLink: String) {
        _navigationUiState.tryEmit(ShareExNavigationUiState(appLink))
    }
}
