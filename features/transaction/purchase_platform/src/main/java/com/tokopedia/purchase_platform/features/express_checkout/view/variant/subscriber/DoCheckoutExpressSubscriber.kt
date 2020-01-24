package com.tokopedia.purchase_platform.features.express_checkout.view.variant.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.view.error_bottomsheet.ErrorBottomsheets.Companion.RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT
import com.tokopedia.purchase_platform.features.express_checkout.data.entity.response.checkout.CheckoutExpressGqlResponse
import com.tokopedia.purchase_platform.features.express_checkout.domain.mapper.checkout.CheckoutDomainModelMapper
import com.tokopedia.purchase_platform.features.express_checkout.view.variant.CheckoutVariantContract
import rx.Subscriber
import timber.log.Timber

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

class DoCheckoutExpressSubscriber(val view: CheckoutVariantContract.View?,
                                  val presenter: CheckoutVariantContract.Presenter,
                                  val domainModelMapper: CheckoutDomainModelMapper) :
        Subscriber<GraphqlResponse>() {

    companion object {
        val STATE_SUCCESS = 200
        val STATE_ERROR = 400
        val STATE_HEADER_ERROR_CHANGE_COURIER = "shop_shipment_empty_object"
        val STATE_HEADER_ERROR_CHANGE_PAYMENT = "Apicalls success but got error"
        val STATE_HEADER_ERROR_PRODUCT_NOT_ACTIVE = "Product is not active"
        val STATE_HEADER_ERROR_SHOP_NOT_ACTIVE = "Shop is not active"

        val STATE_CHECKOUT_SUCCESS = 0
        val STATE_CHECKOUT_ERROR_CHANGE_COURIER = 1
        val STATE_CHECKOUT_ERROR_CHANGE_PAYMENT = 2
        val STATE_CHECKOUT_ERROR_FAILED_PAYMENT = 3
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoadingDialog()
        view?.showErrorAPI(RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT)
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoadingDialog()
        val checkoutResponse = response.getData<CheckoutExpressGqlResponse>(CheckoutExpressGqlResponse::class.java)
        val checkoutResponseModel = domainModelMapper.convertToDomainModel(checkoutResponse.checkoutResponse)
        val headerErrorCode = if (checkoutResponseModel.headerModel?.errorCode?.isEmpty() == true) 0 else checkoutResponseModel.headerModel?.errorCode?.toInt()
                ?: 0
        var dataErrorCode = 0
        if (checkoutResponseModel.checkoutDataModel?.error?.isNotEmpty() == true) {
            try {
                dataErrorCode = checkoutResponseModel.checkoutDataModel?.error?.toInt() ?: 0
            } catch (exception: NumberFormatException) {
                Timber.d(exception)
            }
        }

        val headerMessage = if (checkoutResponseModel.headerModel?.messages?.isNotEmpty() == true) checkoutResponseModel.headerModel?.messages?.get(0)
                ?: "" else ""
        val dataMessage = checkoutResponseModel.checkoutDataModel?.message ?: ""

        when {
            headerErrorCode == STATE_SUCCESS && dataErrorCode == STATE_CHECKOUT_SUCCESS -> {
                view?.navigateCheckoutToThankYouPage(checkoutResponseModel.checkoutDataModel?.dataModel?.applink
                        ?: "")
            }
            headerMessage.equals(STATE_HEADER_ERROR_CHANGE_COURIER) -> {
                view?.showErrorCourier(checkoutResponseModel.checkoutDataModel?.error ?: "")
            }
            (headerErrorCode >= STATE_ERROR && headerMessage.equals(STATE_HEADER_ERROR_CHANGE_PAYMENT)) ||
                    headerErrorCode == STATE_SUCCESS && dataErrorCode >= STATE_ERROR -> {
                view?.showErrorPayment(dataMessage)
            }
            headerMessage == STATE_HEADER_ERROR_PRODUCT_NOT_ACTIVE -> {
                view?.showErrorNotAvailable(view.getActivityContext()?.getString(R.string.label_error_stock_not_available)
                        ?: "")
            }
            headerMessage == STATE_HEADER_ERROR_SHOP_NOT_ACTIVE -> {
                view?.showErrorNotAvailable(view.getActivityContext()?.getString(R.string.label_error_shop_closed)
                        ?: "")
            }
            else -> view?.showErrorAPI(RETRY_ACTION_RELOAD_EXPRESS_CHECKOUT)
        }
    }

}