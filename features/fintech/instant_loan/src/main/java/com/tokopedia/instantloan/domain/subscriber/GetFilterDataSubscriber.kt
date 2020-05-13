package com.tokopedia.instantloan.domain.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlFilterData
import com.tokopedia.instantloan.data.model.response.GqlFilterDataResponse
import com.tokopedia.instantloan.view.contractor.OnlineLoanContractor
import rx.Subscriber

class GetFilterDataSubscriber(var view: OnlineLoanContractor.View) :
        Subscriber<GraphqlResponse>() {

    private lateinit var filterData: GqlFilterData
    private lateinit var gqlFilterDataResponse: GqlFilterDataResponse

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (!view.isViewAttached()) {
            return
        }
        gqlFilterDataResponse =
                graphqlResponse?.getData(GqlFilterDataResponse::class.java) as GqlFilterDataResponse
        filterData = gqlFilterDataResponse.gqlFilterData
        view.setFilterDataForOnlineLoan(filterData)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }

}
