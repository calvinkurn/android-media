package com.tokopedia.instantloan.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.view.contractor.InstantLoanLendingDataContractor
import rx.Subscriber

class GetLendingDataSubscriber(var view: InstantLoanLendingDataContractor.View):
        Subscriber<GraphqlResponse>(){

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (view.isViewAttached()) {
            val gqlLendingDataResponse = graphqlResponse?.getData(GqlLendingDataResponse::class.java)
                    as GqlLendingDataResponse
            view.renderLendingData(gqlLendingDataResponse)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }
}
