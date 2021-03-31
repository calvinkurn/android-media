package com.tokopedia.play.broadcaster.view.viewmodel

import androidx.lifecycle.*
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.data.model.ProductData
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play_common.util.event.Event
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 23/06/20
 */
class PlayEditProductViewModel @Inject constructor(
        private val channelConfigStore: ChannelConfigStore,
        private val dispatcher: CoroutineDispatcherProvider,
        private val setupDataStore: PlayBroadcastSetupDataStore
) : ViewModel() {

    private val channelId: String
        get() = channelConfigStore.getChannelId()

    private val job: Job = SupervisorJob()
    private val scope = CoroutineScope(job + dispatcher.main)

    val observableSelectedProducts: LiveData<List<ProductData>> = setupDataStore.getObservableSelectedProducts()
            .asLiveData(viewModelScope.coroutineContext + dispatcher.computation)

    private val _selectedProductData: List<ProductData> = setupDataStore.getSelectedProducts()
    val selectedProducts: List<ProductContentUiModel> = _selectedProductData.map {
        ProductContentUiModel.createFromData(it, isSelectable = { Selectable }, isSelectedHandler = setupDataStore::isProductSelected)
    }

    private val selectedProductMap = _selectedProductData.associateBy { it.id }

    val observableUploadProductEvent: LiveData<NetworkResult<Event<Unit>>>
        get() = _observableUploadProductEvent
    private val _observableUploadProductEvent = MutableLiveData<NetworkResult<Event<Unit>>>()

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }

    fun selectProduct(productId: Long, isSelected: Boolean) {
        val product = selectedProductMap[productId] ?: throw IllegalStateException("Product not found")
        setupDataStore.selectProduct(
                product,
                isSelected
        )
    }

    fun uploadProduct() {
        _observableUploadProductEvent.value = NetworkResult.Loading
        scope.launch {
            val result = setupDataStore.uploadSelectedProducts(channelId)
            if (result is NetworkResult.Success) _observableUploadProductEvent.value = NetworkResult.Success(Event(Unit))
            else if (result is NetworkResult.Fail) _observableUploadProductEvent.value = result
        }
    }
}