package com.tokopedia.notifcenter.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.notifcenter.data.entity.NotificationUpdateTotalUnread
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.viewbean.NotificationUpdateFilterViewBean
import com.tokopedia.notifcenter.domain.*
import com.tokopedia.notifcenter.presentation.contract.NotificationUpdateContract
import com.tokopedia.notifcenter.presentation.subscriber.GetNotificationTotalUnreadSubscriber
import com.tokopedia.notifcenter.presentation.subscriber.GetNotificationUpdateFilterSubscriber
import com.tokopedia.notifcenter.presentation.subscriber.GetNotificationUpdateSubscriber
import com.tokopedia.notifcenter.presentation.subscriber.NotificationUpdateActionSubscriber
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
        private var getNotificationUpdateMapper: GetNotificationUpdateMapper,
        private var getNotificationUpdateFilterMapper: GetNotificationUpdateFilterMapper,
        private var addToCartUseCase: AddToCartUseCase
) : BaseDaggerPresenter<NotificationUpdateContract.View>(), NotificationUpdateContract.Presenter {

    var variables: HashMap<String, Any> = HashMap()

    override fun updateFilter(filter: HashMap<String, Int>) {
        resetFilter()
        variables.putAll(filter)
    }

    override fun resetFilter() {
        variables.clear()
    }

    override fun loadData(lastNotifId: String, onSuccessInitiateData: (NotificationViewData) -> Unit, onErrorInitiateData: (Throwable) -> Unit) {
        getNotificationUpdateUseCase.execute(
                GetNotificationUpdateUseCase.getRequestParams(1, variables, lastNotifId),
                GetNotificationUpdateSubscriber(getNotificationUpdateMapper, onSuccessInitiateData, onErrorInitiateData)
        )
    }

    override fun getFilter(onSuccessGetFilter: (ArrayList<NotificationUpdateFilterViewBean>) -> Unit) {
        getNotificationUpdateFilterUseCase.execute(GetNotificationUpdateFilterSubscriber(getNotificationUpdateFilterMapper, onSuccessGetFilter))
    }

    override fun clearNotifCounter() {
        clearCounterNotificationUpdateUseCase.params = ClearCounterNotificationUpdateUseCase.getRequestParams()
        clearCounterNotificationUpdateUseCase.execute(NotificationUpdateActionSubscriber())
    }

    override fun markReadNotif(notifId: String) {
        markReadNotificationUpdateItemUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(
                        notifId,
                        TYPE_OF_NOTIF_UPDATE),
                NotificationUpdateActionSubscriber())
    }

    override fun markAllReadNotificationUpdate(onSuccessMarkAllReadNotificationUpdate: () -> Unit) {
        markAllReadNotificationUpdateUseCase.execute(
                MarkAllReadNotificationUpdateUseCase.params(),
                NotificationUpdateActionSubscriber(onSuccessMarkAllReadNotificationUpdate))
    }

    override fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit) {
        getNotificationTotalUnreadUseCase.execute(
                GetNotificationTotalUnreadUseCase.getRequestParams(),
                GetNotificationTotalUnreadSubscriber(onSuccessGetTotalUnreadCounter)
        )
    }

    override fun addProductToCart(
            userId: String,
            product: ProductData,
            onSuccessAddToCart: (data: DataModel) -> Unit
    ) {
        val requestParams = getCartRequestParams(userId, product)
        val atcSubscriber = getAtcSubscriber(onSuccessAddToCart)
        addToCartUseCase.createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(atcSubscriber)
    }

    private fun getAtcSubscriber(
            onSuccessAddToCart: (data: DataModel) -> Unit
    ): Subscriber<AddToCartDataModel> {
        return object : Subscriber<AddToCartDataModel>() {
            override fun onNext(data: AddToCartDataModel) {
                if (data.status.equals(STATUS_OK, true) && data.data.success == 1) {
                    onSuccessAddToCart(data.data)
                } else {
                    val errorException = MessageErrorException(data.errorMessage[0])
                    onError(errorException)
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                view.showToastMessageError(e)
            }
        }
    }

    private fun getCartRequestParams(userId: String, product: ProductData): RequestParams {
        val addToCartRequestParams = AddToCartRequestParams()
        addToCartRequestParams.productId = product.productId.toLongOrZero()
        addToCartRequestParams.shopId = product.shop?.id ?: -1
        addToCartRequestParams.quantity = 1
        addToCartRequestParams.notes = ""
        addToCartRequestParams.productName = product.name
        addToCartRequestParams.price = product.price
        addToCartRequestParams.userId = userId

       return RequestParams.create().apply {
           putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
       }
    }

    companion object {
        private const val TYPE_OF_NOTIF_UPDATE = 1
    }
}
