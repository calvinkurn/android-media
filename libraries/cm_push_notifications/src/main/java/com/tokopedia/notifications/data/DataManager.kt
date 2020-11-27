package com.tokopedia.notifications.data

import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.notifications.analytics.NotificationAnalytics
import com.tokopedia.notifications.domain.AttributionUseCase
import com.tokopedia.notifications.model.AddToCart
import com.tokopedia.notifications.model.BaseNotificationModel
import com.tokopedia.notifications.subscriber.DataSubscriber.atcSubscriber
import com.tokopedia.usecase.RequestParams
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class DataManager @Inject constructor(
        private val attributionUseCase: AttributionUseCase,
        private val atcProductUseCase: AddToCartUseCase
) {

    /*
    * Attribution tracker,
    * Measure push notification order attribution which clicked by user
    * */
    fun attribution(notification: BaseNotificationModel?) {
        val params = attributionUseCase.params(
                transactionId = notification?.transactionId,
                userTransId = notification?.userTransactionId,
                recipientId = notification?.userId,
                shopId = notification?.shopId,
                blastId = notification?.blastId,
                data = notification?.webHookParamData()
        )
        attributionUseCase.execute(params)
    }

    /*
    * Add to cart of product,
    * To make a seamless experience for user in
    * push notification that contains product related
    * */
    fun atcProduct(campaignId: String,
                   userId: String,
                   addToCart: AddToCart?
    ) {
        addToCart?.let { atc ->
            val params = atcParams(
                    atc.productId.toString(),
                    atc.shopId,
                    atc.productName ?: "",
                    atc.productPrice?.toString() ?: "",
                    userId
            )

            fun tracker(data: AddToCartDataModel) {
                NotificationAnalytics.addToCartClicked(
                        campaignId = campaignId,
                        userId = userId,
                        cartId = data.data.cartId,
                        addToCart = atc
                )
            }

            atcProductUseCase.createObservable(params)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(atcSubscriber(::tracker))
        }
    }

    companion object {
        
        fun atcParams(productId: String, shopId: Int?, productName: String, price: String, userId: String): RequestParams {
            val addToCartRequestParams = AddToCartRequestParams()
            addToCartRequestParams.productId = productId.toLongOrNull()?: 0
            addToCartRequestParams.shopId = shopId?: -1
            addToCartRequestParams.quantity = 1
            addToCartRequestParams.notes = ""
            addToCartRequestParams.productName = productName
            addToCartRequestParams.price = price
            addToCartRequestParams.userId = userId

            return RequestParams.create().apply {
                putObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
            }
        }

    }

}