package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.data.mapper.ProductHighlightMapper
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.domain.ProductHighlightUseCase
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase
import com.tokopedia.notifcenter.util.SingleLiveEvent
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import javax.inject.Inject
import com.tokopedia.notifcenter.domain.ProductHighlightUseCase.Companion.params as productHighlightParams
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase.Companion.params as stockReminderParams

interface ProductStockHandlerContract {
    fun setProductReminder(productId: String, notificationId: String)
    fun getHighlightProduct(shopId: String)
}

class ProductStockHandlerViewModel @Inject constructor(
        private val stockReminderUseCase: ProductStockReminderUseCase,
        private val productHighlightUseCase: ProductHighlightUseCase,
        dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()), ProductStockHandlerContract {

    private val _productStockReminder = SingleLiveEvent<ProductStockReminder>()
    val productStockReminder: LiveData<ProductStockReminder> get() = _productStockReminder

    private val _productHighlight = MutableLiveData<List<ProductHighlightViewBean>>()
    val productHighlight: LiveData<List<ProductHighlightViewBean>> get() = _productHighlight

    override fun setProductReminder(productId: String, notificationId: String) {
        val params = stockReminderParams(notificationId, productId)
        stockReminderUseCase.get(params, {
            _productStockReminder.setValue(it)
        }, {})
    }

    override fun getHighlightProduct(shopId: String) {
        val params = productHighlightParams(shopId)
        productHighlightUseCase.get(params, {
            _productHighlight.value = ProductHighlightMapper.map(it)
        }, {})
    }

    fun cleared() {
        onCleared()
    }

}