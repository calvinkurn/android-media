package com.tokopedia.instantloan.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import rx.Subscriber

class GetLendingDataSubscriber(var presenter: InstantLoanLendingDataContractor.Presenter):
        Subscriber<GraphqlResponse>(){

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (presenter.isViewAttached()) {
            val gqlLendingDataResponse = graphqlResponse?.getData(GqlLendingDataResponse::class.java)
                    as GqlLendingDataResponse
            presenter.getView().renderLendingData(gqlLendingDataResponse)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }
}
