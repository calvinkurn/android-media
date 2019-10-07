package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.transactiondata.insurance.entity.response.UpdateInsuranceDataGqlResponse
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
                view.showToastMessageGreen(view.activity.resources.getString(R.string.update_insurance_data_success))
                val productIdList = ArrayList<Long>()
                productIdList.add(productId)
                view.removeInsuranceProductItem(productIdList)
                presenter.getInsuranceTechCart()
            } else {
                view.showToastMessageRed(updateInsuranceDataGqlResponse?.data?.updateTransactional?.errorMessage)
            }
        }
        view.hideProgressLoading()

    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        view.hideProgressLoading()
        view.showToastMessageRed(ErrorHandler.getErrorMessage(view.getActivity(), e))
    }

}