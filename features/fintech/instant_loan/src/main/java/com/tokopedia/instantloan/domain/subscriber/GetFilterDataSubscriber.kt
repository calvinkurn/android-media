package com.tokopedia.instantloan.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlFilterDataResponse
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import rx.Subscriber

class GetFilterDataSubscriber(var presenter: OnlineLoanContractor.Presenter):
        Subscriber<GraphqlResponse>() {

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (!presenter.isViewAttached()) {
            return
        }
        val gqlFilterDataResponse =
                graphqlResponse?.getData(GqlFilterDataResponse::class.java) as GqlFilterDataResponse
        presenter.getView().setFilterDataForOnlineLoan(gqlFilterDataResponse.gqlFilterData)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }

}