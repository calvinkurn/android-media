package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.data.entity.response.atc.AtcExpressGqlResponse
import com.tokopedia.expresscheckout.domain.mapper.atc.AtcDomainModelMapper
import com.tokopedia.expresscheckout.domain.model.atc.AtcResponseModel
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 08/01/19.
 */

class AtcExpressSubscriber(val view: CheckoutVariantContract.View?, val presenter: CheckoutVariantContract.Presenter) :
        Subscriber<GraphqlResponse>() {

    private lateinit var domainModelMapper: AtcDomainModelMapper
    private lateinit var atcResponseModel: AtcResponseModel

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }

    override fun onNext(objects: GraphqlResponse) {
        view?.hideLoading()
        val expressCheckoutResponse = objects.getData<AtcExpressGqlResponse>(AtcExpressGqlResponse::class.java)
        if (expressCheckoutResponse.atcExpress.status.equals("OK")) {
            if (expressCheckoutResponse.atcExpress.data.errors.isNotEmpty()) {
                when (expressCheckoutResponse.atcExpress.data.errorCode) {
                    1, 3 -> view?.navigateToOcs()
                    2, 4 -> view?.navigateToNcf()
                    else -> {
                        view?.finishWithError(expressCheckoutResponse.atcExpress.data.errors.joinToString(". "))
                    }
                }
            } else {
                domainModelMapper = AtcDomainModelMapper()
                atcResponseModel = domainModelMapper.convertToDomainModel(expressCheckoutResponse.atcExpress)
                presenter.setAtcResponseModel(atcResponseModel)
                presenter.prepareViewModel()
                val productModel = atcResponseModel.atcDataModel?.cartModel?.groupShopModels?.get(0)?.productModels?.get(0)
                presenter.loadShippingRates(atcResponseModel, productModel?.productPrice
                        ?: 0, productModel?.productMinOrder ?: 0)
            }
        } else {
            view?.finishWithError(expressCheckoutResponse.atcExpress.errorMessage.joinToString(". "))
        }
    }

}
