package com.tokopedia.universal_sharing.view

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.universal_sharing.data.model.UniversalSharingPostPurchaseProductResponse
import com.tokopedia.universal_sharing.domain.mapper.UniversalSharingPostPurchaseMapper
import com.tokopedia.universal_sharing.domain.usecase.UniversalSharingPostPurchaseGetDetailProductUseCase
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.util.Result
import com.tokopedia.universal_sharing.view.bottomsheet.typefactory.UniversalSharingTypeFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UniversalSharingPostPurchaseViewModel @Inject constructor(
    private val getDetailProductUseCase: UniversalSharingPostPurchaseGetDetailProductUseCase,
    private val mapper: UniversalSharingPostPurchaseMapper,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _actionFlow =
        MutableSharedFlow<UniversalSharingPostPurchaseAction>(extraBufferCapacity = 16)

    private val _uiState = MutableStateFlow<
        Result<List<Visitable<in UniversalSharingTypeFactory>>>>(Result.Loading)
    val uiState = _uiState.asStateFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    private fun Flow<UniversalSharingPostPurchaseAction>.process() {
        onEach {
            when (it) {
                is UniversalSharingPostPurchaseAction.RefreshData -> {
                    loadData(it.data)
                }
                is UniversalSharingPostPurchaseAction.ClickShare -> {
                    getDetailData(it.productId)
                }
            }
        }
            .launchIn(viewModelScope)
    }

    fun observeDetailProductFlow(): StateFlow<Result<UniversalSharingPostPurchaseProductResponse>?> =
        getDetailProductUseCase.observe()

    fun processAction(action: UniversalSharingPostPurchaseAction) {
        viewModelScope.launch {
            _actionFlow.tryEmit(action)
        }
    }

    private fun loadData(data: UniversalSharingPostPurchaseModel) {
        viewModelScope.launch {
            try {
                if (data.shopList.isEmpty()) {
                    _uiState.value = Result.Error(MessageErrorException("Product is empty"))
                } else {
                    val result = mapper.mapToUiModel(data)
                    _uiState.value = Result.Success(result)
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _uiState.value = Result.Error(throwable)
            }
        }
    }

    private fun getDetailData(productId: String) {
        viewModelScope.launch {
            try {
                getDetailProductUseCase.getDetailProduct(productId)
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }
}
