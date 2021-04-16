package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.entity.ProductStockReminder
import com.tokopedia.notifcenter.data.entity.deletereminder.DeleteReminderResponse
import com.tokopedia.notifcenter.data.mapper.ProductHighlightMapper
import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean
import com.tokopedia.notifcenter.domain.ProductHighlightUseCase
import com.tokopedia.notifcenter.domain.ProductStockReminderDeleteUseCase
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase
import com.tokopedia.notifcenter.util.SingleLiveEvent
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.notifcenter.domain.ProductHighlightUseCase.Companion.params as productHighlightParams
import com.tokopedia.notifcenter.domain.ProductStockReminderUseCase.Companion.params as stockReminderParams

interface ProductStockHandlerContract {
    fun setProductReminder(productId: String, notificationId: String)
    fun deleteReminder(productId: String, notificationId: String)
    fun getHighlightProduct(shopId: String)
    fun addProductToCart(userId: String, product: ProductData?)
    fun onErrorMessage(throwable: Throwable)
}

class ProductStockHandlerViewModel @Inject constructor(
        private val stockReminderUseCase: ProductStockReminderUseCase,
        private val productHighlightUseCase: ProductHighlightUseCase,
        private var addToCartUseCase: AddToCartUseCase,
        private val deleteReminderUseCase: ProductStockReminderDeleteUseCase,
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io), ProductStockHandlerContract {

    private val _productStockReminder = SingleLiveEvent<ProductStockReminder>()
    val productStockReminder: LiveData<ProductStockReminder> get() = _productStockReminder

    private val _deleteReminder = SingleLiveEvent<Result<DeleteReminderResponse>>()
    val deleteReminder: LiveData<Result<DeleteReminderResponse>>
        get() = _deleteReminder

    private val _productHighlight = MutableLiveData<List<ProductHighlightViewBean>>()
    val productHighlight: LiveData<List<ProductHighlightViewBean>> get() = _productHighlight

    private val _addToCart = SingleLiveEvent<Pair<ProductData, AddToCartDataModel>>()
    val addToCart: LiveData<Pair<ProductData, AddToCartDataModel>> get() = _addToCart

    private val _errorMessage = SingleLiveEvent<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

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

    override fun addProductToCart(userId: String, product: ProductData?) {
        if (product == null) return
        addToCartUseCase.createObservable(atcParams(userId, product))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.status.equals(STATUS_OK, true)
                        && it.data.success == 1) {
                        _addToCart.setValue(Pair(product, it))
                    } else {
                        _errorMessage.setValue(it.errorMessage.first())
                    }
                }, ::onErrorMessage)
    }

    override fun onErrorMessage(throwable: Throwable) {
        when(throwable) {
            is UnknownHostException ->
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL)
            is SocketTimeoutException ->
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
            is ConnectException ->
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
            is ResponseErrorException ->
                _errorMessage.postValue(throwable.message)
            is ResponseDataNullException ->
                _errorMessage.postValue(throwable.message)
            is HttpErrorException ->
                _errorMessage.postValue(throwable.message)
            else -> {
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            }
        }
    }

    override fun deleteReminder(productId: String, notificationId: String) {
        deleteReminderUseCase.get(productId, notificationId, {
            _deleteReminder.setValue(Success(it))
        }, {
            _deleteReminder.setValue(Fail(it))
        })
    }

    fun cleared() {
        onCleared()
    }

    companion object {
        private fun atcParams(userId: String, product: ProductData?): RequestParams {
            val addToCartRequestParams = AddToCartRequestParams()
            addToCartRequestParams.productId = product?.productId.toLongOrZero()
            addToCartRequestParams.shopId = product?.shop?.id ?: -1
            addToCartRequestParams.quantity = 1
            addToCartRequestParams.notes = ""
            addToCartRequestParams.productName = product?.name?: ""
            addToCartRequestParams.price = product?.price?: ""
            addToCartRequestParams.userId = userId

            return RequestParams.create().apply {
                putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
            }
        }
    }

}