package com.tokopedia.notifications.data

import android.util.Log
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.notifications.analytics.NotificationAnalytics
import com.tokopedia.notifications.domain.AmplificationUseCase
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
        private val atcProductUseCase: AddToCartUseCase,
        private val amplificationUseCase: AmplificationUseCase
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
                blastId = notification?.blastId
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
                    atc.shopId
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

    /*
    * Amplification of Push Notification
    * fetch all of notification haven't rendered yet
    * and store it on local CM_Push_Notification database
    * */
    fun amplification(deviceToken: String) {
        val params = amplificationUseCase.params(deviceToken)
        amplificationUseCase.execute(params) {
            it.webhookAttributionNotifier.pushData.forEach { data ->
                //TODO add converter to baseNotificationModel
                //TODO store into local CM database
                Log.d("TAG", data)
            }
        }
    }

    companion object {
        
        fun atcParams(productId: String, shopId: Int?): RequestParams {
            val addToCartRequestParams = AddToCartRequestParams()
            addToCartRequestParams.productId = productId.toLongOrNull()?: 0
            addToCartRequestParams.shopId = shopId?: -1
            addToCartRequestParams.quantity = 1
            addToCartRequestParams.notes = ""

            return RequestParams.create().apply {
                putObject(REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, addToCartRequestParams)
            }
        }
        
    }

}