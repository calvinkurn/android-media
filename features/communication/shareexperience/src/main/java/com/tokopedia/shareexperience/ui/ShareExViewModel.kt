package com.tokopedia.shareexperience.ui

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shareexperience.data.dto.request.ShareExProductRequest
import com.tokopedia.shareexperience.data.util.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.usecase.ShareExGetSharePropertiesUseCase
import com.tokopedia.shareexperience.ui.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
        MutableSharedFlow<ShareExBottomSheetAction>(extraBufferCapacity = 16)

    private var _bottomSheetModel: ShareExBottomSheetModel? = null // Cache the model for body switching
    private val _bottomSheetUiState = MutableStateFlow(ShareExBottomSheetUiState())
    val bottomSheetUiState = _bottomSheetUiState.asStateFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    fun processAction(action: ShareExBottomSheetAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    private fun Flow<ShareExBottomSheetAction>.process() {
        onEach {
            when (it) {
                is ShareExBottomSheetAction.InitializePage -> {
                    getShareBottomSheetData()
                }
                is ShareExBottomSheetAction.UpdateShareBody -> {
                    updateShareBottomSheetBody(it.position)
                }
                is ShareExBottomSheetAction.UpdateShareImage -> {
                    updateShareImage(it.imageUrl)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getShareBottomSheetData() {
        viewModelScope.launch {
            try {
                getSharePropertiesUseCase.getData(
                    ShareExProductRequest(
                        pageType = ShareExPageTypeEnum.PDP.value,
                        id = 2150412049
                    )
                ).collectLatest {
                    _bottomSheetModel = it
                    val uiResult = it.map()
                    _bottomSheetUiState.update { uiState ->
                        uiState.copy(
                            uiModelList = uiResult
                        )
                    }
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
}
