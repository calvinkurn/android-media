package com.tokopedia.navigation.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.*
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.navigation.domain.pojo.ProductData
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateContract
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationTotalUnreadSubscriber
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationUpdateFilterSubscriber
import com.tokopedia.navigation.presentation.view.subscriber.GetNotificationUpdateSubscriber
import com.tokopedia.navigation.presentation.view.subscriber.NotificationUpdateActionSubscriber
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateFilterItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateViewModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class NotificationUpdatePresenter @Inject constructor(
        private var getNotificationUpdateUseCase: GetNotificationUpdateUseCase,
        private var getNotificationTotalUnreadUseCase: GetNotificationTotalUnreadUseCase,
        private var getNotificationUpdateFilterUseCase: GetNotificationUpdateFilterUseCase,
        private var clearCounterNotificationUpdateUseCase: ClearCounterNotificationUpdateUseCase,
        private var markReadNotificationUpdateItemUseCase: MarkReadNotificationUpdateItemUseCase,
        private var markAllReadNotificationUpdateUseCase: MarkAllReadNotificationUpdateUseCase,
        private var getNotificationUpdateMapper : GetNotificationUpdateMapper,
        private var getNotificationUpdateFilterMapper : GetNotificationUpdateFilterMapper,
        private var addToCartUseCase: AddToCartUseCase
)
    : BaseDaggerPresenter<NotificationUpdateContract.View>(),
        NotificationUpdateContract.Presenter {

    var variables: HashMap<String, Any> = HashMap()

    override fun updateFilter(filter: HashMap<String, Int>) {
        resetFilter()
        variables.putAll(filter)
    }

    override fun resetFilter() {
        variables.clear()
    }

    override fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationUpdateViewModel) -> Unit, onErrorInitiateData: (Throwable) -> Unit) {
        getNotificationUpdateUseCase.execute(
                GetNotificationUpdateUseCase.getRequestParams(1, variables, lastNotifId),
                GetNotificationUpdateSubscriber(getNotificationUpdateMapper, onSuccessInitiateData, onErrorInitiateData)
        )
    }

    override fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterItemViewModel>) -> Unit) {
        getNotificationUpdateFilterUseCase.execute(GetNotificationUpdateFilterSubscriber(view, getNotificationUpdateFilterMapper, onSuccessGetFilter))
    }

    override fun clearNotifCounter() {
        clearCounterNotificationUpdateUseCase.execute(NotificationUpdateActionSubscriber())
    }

    override fun markReadNotif(notifId: String) {
        markReadNotificationUpdateItemUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(notifId),
                NotificationUpdateActionSubscriber())
    }

    override fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit) {
        markAllReadNotificationUpdateUseCase.execute(
                NotificationUpdateActionSubscriber(onSuccessMarkAllReadNotificationUpdate))
    }

    override fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit) {
        getNotificationTotalUnreadUseCase.execute(GetNotificationTotalUnreadSubscriber(onSuccessGetTotalUnreadCounter))
    }

    override fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit) {
        val requestParams = getCartRequestParams(product)
        val atcSubscriber = getAtcSubscriber(onSuccessAddToCart)
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(atcSubscriber)
    }

    private fun getAtcSubscriber(onSuccessAddToCart: () -> Unit): Subscriber<AddToCartDataModel> {
        return object : Subscriber<AddToCartDataModel>() {
            override fun onNext(data: AddToCartDataModel) {
                val isAtcSuccess = data.status.equals(AddToCartDataModel.STATUS_OK, true)
                        && data.data.success == 1
                if (isAtcSuccess) {
                    val message = data.data.message[0]
                    view.showMessageAtcSuccess(message)
                    onSuccessAddToCart()
                } else {
                    val errorException = MessageErrorException(data.errorMessage[0])
                    onError(errorException)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view.showMessageAtcError(e)
            }
        }
    }

    private fun getCartRequestParams(product: ProductData): RequestParams {
        val addToCartRequestParams = AddToCartRequestParams()
        addToCartRequestParams.productId = product.productId.toLongOrZero()
        addToCartRequestParams.shopId = product.shop?.id ?: -1
        addToCartRequestParams.quantity = 1
        addToCartRequestParams.notes = ""

       return RequestParams.create().apply {
           putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
       }
    }
}
