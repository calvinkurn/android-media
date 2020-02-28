package com.tokopedia.tokopoints.view.validatePin

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdateOuter
import com.tokopedia.tokopoints.view.util.CommonConstant
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class ValidateMerchantPinPresenter @Inject constructor(private val mUseCase: GraphqlUseCase?) : BaseDaggerPresenter<ValidateMerchantPinContract.View?>(), ValidateMerchantPinContract.Presenter {
    override fun destroyView() {
        mUseCase?.unsubscribe()
    }

    override fun swipeMyCoupon(code: String, pin: String) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CODE] = code
        variables[CommonConstant.GraphqlVariableKeys.PIN] = pin
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_swipe_coupon),
                CouponSwipeUpdateOuter::class.java,
                variables, false)
        mUseCase!!.clearRequest()
        mUseCase.addRequest(request)
        mUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA
            }

            override fun onNext(response: GraphqlResponse) {
                val data = response.getData<CouponSwipeUpdateOuter>(CouponSwipeUpdateOuter::class.java)
                if (data != null && data.swipeCoupon != null) {
                    if (data.swipeCoupon.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                        view!!.onSuccess(data.swipeCoupon)
                    } else {
                        if (data.swipeCoupon.resultStatus.messages.size > 0) {
                            view!!.onError(data.swipeCoupon.resultStatus.messages[0])
                        }
                    }
                }
            }
        })
    }

}