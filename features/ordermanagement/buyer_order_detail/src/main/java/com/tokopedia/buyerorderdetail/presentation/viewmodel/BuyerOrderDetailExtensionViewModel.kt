package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorderdetail.domain.usecases.GetOrderExtensionRespondInfoUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.InsertOrderExtensionRespondUseCase
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderExtensionRespondUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BuyerOrderDetailExtensionViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val insertOrderExtensionRespondUseCase: Lazy<InsertOrderExtensionRespondUseCase>,
    private val getOrderExtensionRespondInfoUseCase: Lazy<GetOrderExtensionRespondInfoUseCase>
) : BaseViewModel(dispatcher.main) {

    private val _orderExtensionRespondInfo: MutableLiveData<Result<OrderExtensionRespondInfoUiModel>> =
        MutableLiveData()
    val orderExtensionRespondInfo: LiveData<Result<OrderExtensionRespondInfoUiModel>>
        get() = _orderExtensionRespondInfo

    private val _orderExtensionRespond: MutableLiveData<Result<OrderExtensionRespondUiModel>> =
        MutableLiveData()
    val orderExtensionRespond: LiveData<Result<OrderExtensionRespondUiModel>>
        get() = _orderExtensionRespond

    fun requestRespondInfo(orderId: String) {
        launchCatchError(block = {
            val respondInfo = withContext(dispatcher.io) {
                getOrderExtensionRespondInfoUseCase.get().setParams(orderId.toLongOrZero())
                getOrderExtensionRespondInfoUseCase.get().executeOnBackground()
            }
            _orderExtensionRespondInfo.value = Success(respondInfo)
        }, onError = {
            _orderExtensionRespondInfo.value = Fail(it)
        })
    }

    fun requestRespond(orderId: String, action: Int) {
        launchCatchError(block = {
            val respondInfo = withContext(dispatcher.io) {
                insertOrderExtensionRespondUseCase.get()
                    .setParams(orderId.toLongOrZero(), action)
                insertOrderExtensionRespondUseCase.get().executeOnBackground()
            }
            _orderExtensionRespond.value = Success(respondInfo)
        }, onError = {
            _orderExtensionRespond.value = Fail(it)
        })
    }
}