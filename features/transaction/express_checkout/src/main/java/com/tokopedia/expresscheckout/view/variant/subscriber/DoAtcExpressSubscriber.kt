package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 08/01/19.
 */

class DoAtcExpressSubscriber(val view: CheckoutVariantContract.View?,
                             val presenter: CheckoutVariantContract.Presenter,
                             val domainModelMapper: AtcDomainModelMapper) :
        Subscriber<GraphqlResponse>() {

    companion object {
        val INACTIVE_EXPREES_AND_REDIRECT_TO_OCS = 1
        val INACTIVE_EXPREES_AND_REDIRECT_TO_NORMAL_ATC = 2
        val NO_PROFILE_AND_REDIRECT_TO_OCS = 3
        val NO_PROFILE_AND_REDIRECT_TO_NORMAL_ATC = 4
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.finishWithError("")
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoading()
        val expressCheckoutResponse = response.getData<AtcExpressGqlResponse>(AtcExpressGqlResponse::class.java)
        if (expressCheckoutResponse.atcExpress.status.equals("OK")) {
            if (expressCheckoutResponse.atcExpress.data.errors.isNotEmpty()) {
                when (expressCheckoutResponse.atcExpress.data.errorCode) {
                    INACTIVE_EXPREES_AND_REDIRECT_TO_OCS, NO_PROFILE_AND_REDIRECT_TO_OCS -> view?.navigateAtcToOcs()
                    INACTIVE_EXPREES_AND_REDIRECT_TO_NORMAL_ATC, NO_PROFILE_AND_REDIRECT_TO_NORMAL_ATC -> view?.navigateAtcToNcf()
                    else -> {
                        view?.finishWithError(expressCheckoutResponse.atcExpress.data.errors.joinToString(" "))
                    }
                }
            } else {
                val atcResponseModel = domainModelMapper.convertToDomainModel(expressCheckoutResponse.atcExpress)
                presenter.setAtcResponseModel(atcResponseModel)
                val productModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)
                val serviceId = atcResponseModel.atcDataModel?.userProfileModelDefaultModel?.shipmentModel?.serviceId
                presenter.loadShippingRates(productModel?.productPrice?.toLong()
                        ?: 0, productModel?.productMinOrder
                        ?: 0, serviceId
                        ?: 0, 0)
            }
        } else {
            view?.finishWithError(expressCheckoutResponse.atcExpress.errorMessage.joinToString(" "))
        }
    }

}
