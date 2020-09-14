package com.tokopedia.promocheckout.list.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.deals.PromoCheckoutListDealsUseCase
import com.tokopedia.promocheckout.common.domain.deals.Type
import com.tokopedia.promocheckout.common.domain.mapper.DealsCheckoutMapper
import com.tokopedia.promocheckout.common.domain.model.TravelCollectiveBanner
import rx.Subscriber

class PromoCheckoutListDealsPresenter (private val promoCheckoutListDealsUseCase: PromoCheckoutListDealsUseCase,  val dealsCheckoutMapper: DealsCheckoutMapper) : BaseDaggerPresenter<PromoCheckoutListContract.View>(), DealsCheckoutListContract.Presenter {

    override fun getType(product: Type) {
        view.showProgressLoading()
        promoCheckoutListDealsUseCase.execute(promoCheckoutListDealsUseCase.createRequestParams(product), object : Subscriber<GraphqlResponse>() {
            override fun onNext(objects: GraphqlResponse?) {
                view.hideProgressLoading()
                val errors = objects?.getError(TravelCollectiveBanner.Response::class.java)
                if (!errors.isNullOrEmpty()) {
                    val rawErrorMessage = errors[0].message
                } else {
                    val dealsData = objects?.getData<TravelCollectiveBanner.Response>(TravelCollectiveBanner.Response::class.java)?.response
                }
            }

            override fun onCompleted() {
                //do nothing
            }

            override fun onError(e: Throwable?) {
                if (isViewAttached) {
                    view.hideProgressLoading()
                    view.onErrorCheckPromo(e!!)
                }
            }

        })
    }


}