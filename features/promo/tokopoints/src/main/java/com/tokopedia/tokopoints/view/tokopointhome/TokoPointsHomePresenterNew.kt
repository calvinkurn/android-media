package com.tokopedia.tokopoints.view.tokopointhome

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter
import com.tokopedia.tokopoints.view.contract.TokoPointsHomeContract
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.model.section.TokopointsSectionOuter
import com.tokopedia.tokopoints.view.subscriber.Tokopoint2020Subscriber
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.NetworkDetector
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class TokoPointsHomePresenterNew @Inject constructor(private val mGetTokoPointDetailUseCase: GraphqlUseCase?, private val mRedeemCouponUseCase: GraphqlUseCase?, getCouponCountUseCase: GraphqlUseCase?) : BaseDaggerPresenter<TokoPointsHomeContract.View?>(), TokoPointsHomeContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private val mGetCouponCountUseCase: GraphqlUseCase?
    var pagerSelectedItem = 0
    override fun destroyView() {
        mGetTokoPointDetailUseCase?.unsubscribe()
        mRedeemCouponUseCase?.unsubscribe()
        mGetCouponCountUseCase?.unsubscribe()
    }

    override fun getTokoPointDetail() {
        view!!.showLoading()
        mGetTokoPointDetailUseCase!!.clearRequest()
        //Main details
        val request1 = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_tokopoint_detail_new),
                TokoPointDetailEntity::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request1)
        //Lucky egg
        val request2 = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_lucky_egg_details),
                TokenDetailOuter::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request2)
        //Section
        val request4 = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_homepage_section),
                TokopointsSectionOuter::class.java, false)
        mGetTokoPointDetailUseCase.addRequest(request4)
        mGetTokoPointDetailUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                view!!.onError(e.localizedMessage, NetworkDetector.isConnectedToInternet(view!!.activityContext))
                if (view != null) view!!.onFinishRendering()
            }

            override fun onNext(graphqlResponse: GraphqlResponse) { //Handling for main data
                if (view == null) {
                    return
                }
                view!!.hideLoading()
                val data = graphqlResponse.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java)
                val dataSection = graphqlResponse.getData<TokopointsSectionOuter>(TokopointsSectionOuter::class.java)
                if (data != null && dataSection != null && dataSection.sectionContent != null) {
                    view!!.onSuccessResponse(data.tokoPoints, dataSection.sectionContent.sectionContent)
                }
                //handling for lucky egg data
                val tokenDetail = graphqlResponse.getData<TokenDetailOuter>(TokenDetailOuter::class.java)
                if (tokenDetail != null && tokenDetail.tokenDetail != null && tokenDetail.tokenDetail.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                    view!!.onSuccessTokenDetail(tokenDetail.tokenDetail)
                }
                if (view != null) view!!.onFinishRendering()
            }
        })
    }

    override fun tokopointOnboarding2020() {
        TokoPointsNotificationManager.fetchNotification(view!!.activityContext, "onboarding", Tokopoint2020Subscriber(view!!))
    }

    override fun startValidateCoupon(item: CatalogsValueEntity) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = item.id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_tokopoint_validate_redeem),
                ValidateCouponBaseEntity::class.java, variables, false)
        mRedeemCouponUseCase!!.clearRequest()
        mRedeemCouponUseCase.addRequest(graphqlRequest)
        mRedeemCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA
            }

            override fun onNext(response: GraphqlResponse) {
                val validateResponseCode: Int
                val message: String
                val title: String
                val validateCoupon = response.getData<ValidateCouponBaseEntity>(ValidateCouponBaseEntity::class.java)
                if (validateCoupon != null && validateCoupon.validateCoupon != null) {
                    validateResponseCode = CommonConstant.CouponRedemptionCode.SUCCESS
                    message = validateCoupon.validateCoupon.messageSuccess
                    title = validateCoupon.validateCoupon.messageTitle
                } else {
                    val errorsMessage = response.getError(ValidateCouponBaseEntity::class.java)[0].message.split("\\|").toTypedArray()
                    title = errorsMessage[0]
                    message = errorsMessage[1]
                    validateResponseCode = errorsMessage[2].toInt()
                }
                view!!.showValidationMessageDialog(item, title, message, validateResponseCode)
            }
        })
    }

    override fun redeemCoupon(promoCode: String, cta: String) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.PROMO_CODE] = promoCode
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_tokopoint_apply_coupon),
                ApplyCouponBaseEntity::class.java,
                variables, false)
        mRedeemCouponUseCase!!.clearRequest()
        mRedeemCouponUseCase.addRequest(request)
        mRedeemCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) {
                RouteManager.route(view!!.activityContext, cta)
            }

            override fun onNext(saveCoupon: GraphqlResponse) {
                RouteManager.route(view!!.activityContext, cta)
            }
        })
    }

    override fun startSaveCoupon(item: CatalogsValueEntity) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = item.id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_tokopoint_redeem_coupon),
                RedeemCouponBaseEntity::class.java,
                variables, false)
        mRedeemCouponUseCase!!.clearRequest()
        mRedeemCouponUseCase.addRequest(request)
        mRedeemCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA
            }

            override fun onNext(response: GraphqlResponse) {
                val redeemCouponBaseEntity = response.getData<RedeemCouponBaseEntity>(RedeemCouponBaseEntity::class.java)
                if (redeemCouponBaseEntity != null && redeemCouponBaseEntity.hachikoRedeem != null) {
                    view!!.showConfirmRedeemDialog(redeemCouponBaseEntity.hachikoRedeem.coupons[0].cta,
                            redeemCouponBaseEntity.hachikoRedeem.coupons[0].code,
                            redeemCouponBaseEntity.hachikoRedeem.coupons[0].title)
                } else {
                    val errorsMessage = response.getError(RedeemCouponBaseEntity::class.java)[0].message.split("\\|").toTypedArray()
                    if (errorsMessage != null && errorsMessage.size > 0) {
                        val title = errorsMessage[0]
                        var desc: String? = null
                        var validateResponseCode = 0
                        if (errorsMessage.size >= 2) {
                            desc = errorsMessage[1]
                        }
                        if (errorsMessage.size >= 3) validateResponseCode = errorsMessage[2].toInt()
                        view!!.showValidationMessageDialog(item, title, desc, validateResponseCode)
                    }
                }
            }
        })
    }

    override fun showRedeemCouponDialog(cta: String, code: String, title: String) {
        view!!.showRedeemCouponDialog(cta, code, title)
    }

    //Handling sum token
    val couponCount: Unit
        get() {
            mGetCouponCountUseCase!!.clearCache()
            val request5 = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_sum_coupon),
                    TokoPointSumCouponOuter::class.java, false)
            mGetCouponCountUseCase.addRequest(request5)
            mGetCouponCountUseCase.execute(object : Subscriber<GraphqlResponse>() {
                override fun onCompleted() {}
                override fun onError(e: Throwable) {}
                override fun onNext(graphqlResponse: GraphqlResponse) { //Handling sum token
                    val couponOuter = graphqlResponse.getData<TokoPointSumCouponOuter>(TokoPointSumCouponOuter::class.java)
                    if (couponOuter != null && couponOuter.tokopointsSumCoupon != null) {
                        view!!.showTokoPointCoupon(couponOuter.tokopointsSumCoupon)
                    }
                }
            })
        }

    init {
        mGetCouponCountUseCase = mRedeemCouponUseCase
    }
}