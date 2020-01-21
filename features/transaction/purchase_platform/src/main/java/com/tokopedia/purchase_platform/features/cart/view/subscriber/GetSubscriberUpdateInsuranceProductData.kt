package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.data.model.response.insurance.entity.response.UpdateInsuranceDataGqlResponse
import com.tokopedia.purchase_platform.features.cart.view.ICartListPresenter
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

class GetSubscriberUpdateInsuranceProductData(val view: ICartListView,
                                              val presenter: ICartListPresenter,
                                              val productId: Long) : Subscriber<GraphqlResponse>() {

    val SUCCESS_RESPONSE_STATUS_VALUE = "ok"

    override fun onNext(graphqlResponse: GraphqlResponse?) {

        val updateInsuranceDataGqlResponse: UpdateInsuranceDataGqlResponse?
        if (graphqlResponse?.getData<UpdateInsuranceDataGqlResponse>(UpdateInsuranceDataGqlResponse::class.java) != null) {
            updateInsuranceDataGqlResponse = graphqlResponse.getData(UpdateInsuranceDataGqlResponse::class.java)
            if (updateInsuranceDataGqlResponse?.data?.updateCart?.status.equals(SUCCESS_RESPONSE_STATUS_VALUE, ignoreCase = true)) {
                view.showToastMessageGreen(view.getActivityObject()?.resources?.getString(R.string.update_insurance_data_success) ?: "")
                val productIdList = ArrayList<Long>()
                productIdList.add(productId)
                view.removeInsuranceProductItem(productIdList)
                presenter.getInsuranceTechCart()
            } else {
                view.showToastMessageRed(updateInsuranceDataGqlResponse?.data?.updateTransactional?.errorMessage ?: "")
            }
        }
        view.hideProgressLoading()

    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        view.hideProgressLoading()
        view.showToastMessageRed(ErrorHandler.getErrorMessage(view.getActivityObject(), e))
    }

}