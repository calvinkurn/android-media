package com.tokopedia.instantloan.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse
import com.tokopedia.instantloan.domain.interactor.GetLendingDataUseCase
import com.tokopedia.instantloan.view.contractor.BannerContractor
import rx.Subscriber
import javax.inject.Inject

class BannerListPresenter @Inject
constructor(private val mGetLendingDataUseCase: GetLendingDataUseCase)
    : BaseDaggerPresenter<BannerContractor.View>(), BannerContractor.Presenter {

    override fun deAttachView() {
        super.detachView()
        if (mGetLendingDataUseCase != null)
            mGetLendingDataUseCase.unsubscribe()
    }

    override fun getLendingData() {

        mGetLendingDataUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onNext(graphqlResponse: GraphqlResponse?) {
                if (isViewNotAttached) {
                    return
                }
                val gqlLendingDataResponse = graphqlResponse?.getData(GqlLendingDataResponse::class.java) as GqlLendingDataResponse
                view.renderLendingData(gqlLendingDataResponse)

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e?.printStackTrace()
            }

        })
    }
}
