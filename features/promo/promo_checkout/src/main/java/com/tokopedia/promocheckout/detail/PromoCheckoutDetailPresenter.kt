package com.tokopedia.promocheckout.detail

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.model.DataPromoCheckoutDetail
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import java.util.*

class PromoCheckoutDetailPresenter(val getDetailCouponUseCase: GraphqlUseCase) : BaseDaggerPresenter<PromoCheckoutDetailContract.View>(), PromoCheckoutDetailContract.Presenter {

    override fun getDetailPromo(codeCoupon: String, resources : Resources) {
        val variables = HashMap<String, Any>()
        variables.put(INPUT_CODE, codeCoupon)
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(resources,
                R.raw.promo_checkout_detail), DataPromoCheckoutDetail::class.java, variables)
        getDetailCouponUseCase.clearRequest()
        getDetailCouponUseCase.addRequest(graphqlRequest)
        getDetailCouponUseCase.execute(RequestParams.create(), object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.onErroGetDetail(e)
                }
            }

            override fun onNext(objects: GraphqlResponse) {
                val dataDetailCheckoutPromo = objects.getData<DataPromoCheckoutDetail>(DataPromoCheckoutDetail::class.java)
                view.onSuccessGetDetailPromo(dataDetailCheckoutPromo?.promoCheckoutDetailModel)
            }
        })
    }

    override fun detachView() {
        getDetailCouponUseCase.unsubscribe()
        super.detachView()
    }

    companion object {
        private val INPUT_CODE = "code"
    }
}