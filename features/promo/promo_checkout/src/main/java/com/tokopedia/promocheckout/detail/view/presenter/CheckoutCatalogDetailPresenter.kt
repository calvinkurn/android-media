package com.tokopedia.promocheckout.detail.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.model.couponprevalidate.PromoCouponPreValidateResponse
import com.tokopedia.promocheckout.detail.model.couponredeem.PromoRedeemCouponResponse
import com.tokopedia.promocheckout.detail.model.detailmodel.CouponDetailsResponse
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail
import com.tokopedia.promocheckout.detail.model.userpoints.UserPointsResponse
import com.tokopedia.promocheckout.util.PromoCheckoutQuery

import java.util.HashMap

import javax.inject.Inject

import rx.Subscriber

class CheckoutCatalogDetailPresenter @Inject constructor(private val mGetCouponDetail: GraphqlUseCase?, private val mValidateCouponUseCase: GraphqlUseCase, private val mRedeemCouponUseCase: GraphqlUseCase) : BaseDaggerPresenter<CheckoutCatalogDetailContract.View>(), CheckoutCatalogDetailContract.Presenter {
    lateinit var data: CouponDetailsResponse

    override fun destroyView() {
        mGetCouponDetail?.unsubscribe()
    }

    fun startValidateCoupon(item: HachikoCatalogDetail) {
        val variables = HashMap<String, Any>()
        variables["catalog_id"] = item.id ?: 0
        variables["is_gift"] = 0
        variables["gift_user_id"] = 0
        variables["gift_email"] = ""
        val graphqlRequest = GraphqlRequest(PromoCheckoutQuery.promoCheckoutPrevalidateCoupon(),
                PromoCouponPreValidateResponse::class.java, variables, false)
        mValidateCouponUseCase.clearRequest()
        mValidateCouponUseCase.addRequest(graphqlRequest)
        mValidateCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(response: GraphqlResponse) {
                val validateResponseCode: Int
                val message: String?
                val title: String?
                val validateCoupon = response.getData<PromoCouponPreValidateResponse>(PromoCouponPreValidateResponse::class.java)

                if (validateCoupon != null && validateCoupon.validateRedeem != null) {
                    validateResponseCode = 200
                    message = validateCoupon.validateRedeem.messageSuccess
                    title = validateCoupon.validateRedeem.messageTitle
                } else {
                    val errorsMessage = response.getError(PromoCouponPreValidateResponse::class.java)[0].message.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    title = errorsMessage[0]
                    message = view.getAppContext()?.resources?.getString(R.string.promo_checkout_error_desc_redeem)
                    validateResponseCode = Integer.parseInt(errorsMessage[1])
                }
                view.showValidationMessageDialog(item, title!!, message!!, validateResponseCode)
            }
        })
    }

    fun startSaveCoupon(item: HachikoCatalogDetail) {
        val variables = HashMap<String, Any>()
        variables["catalog_id"] = item.id ?: 0
        variables["is_gift"] = 0

        val request = GraphqlRequest(PromoCheckoutQuery.promoCheckoutRedeemCoupon(),
                PromoRedeemCouponResponse::class.java,
                variables, false)
        mRedeemCouponUseCase.clearRequest()
        mRedeemCouponUseCase.addRequest(request)
        mRedeemCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(response: GraphqlResponse) {
                val redeemCouponBaseEntity = response.getData<PromoRedeemCouponResponse>(PromoRedeemCouponResponse::class.java)
                if (redeemCouponBaseEntity?.hachikoRedeem?.coupons != null) {
                    view.showCouponDetail(redeemCouponBaseEntity.hachikoRedeem.coupons[0]?.cta,
                            redeemCouponBaseEntity.hachikoRedeem.coupons[0]?.code,
                            redeemCouponBaseEntity.hachikoRedeem.coupons[0]?.title)
                } else {
                    val errorsMessage = response.getError(PromoRedeemCouponResponse::class.java)[0].message.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (errorsMessage.isNotEmpty()) {
                        val title = errorsMessage[0]
                        var desc: String? = null
                        var validateResponseCode = 0
                        desc = view.getAppContext()?.resources?.getString(R.string.promo_checkout_error_desc_redeem)
                        if (errorsMessage.size >= 2) {
                            desc = errorsMessage[1]
                        }
                        if (errorsMessage.size >= 3)
                            validateResponseCode = Integer.parseInt(errorsMessage[2])
                        view.showValidationMessageDialog(item, title, desc!!, validateResponseCode)

                    }
                }
            }
        })
    }

    override fun getCatalogDetail(uniqueCatalogCode: String, catalogId: Int) {
        view.showLoader()
        val variables = HashMap<String, Any>()
        variables["slug"] = uniqueCatalogCode
        variables["catalog_id"] = catalogId
        variables["apiVersion"] = "2.0.0"

        val request = GraphqlRequest(PromoCheckoutQuery.promoCheckoutCatalogDetail(),
                CouponDetailsResponse::class.java,
                variables, false)
        mGetCouponDetail!!.clearRequest()
        mGetCouponDetail.addRequest(request)

        val graphqlRequestPoints = GraphqlRequest(PromoCheckoutQuery.promoCurrentPoints(), UserPointsResponse::class.java, false)
        mGetCouponDetail.addRequest(graphqlRequestPoints)
        mGetCouponDetail.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

                if (view != null) {
                    view.hideLoader()
                }
            }

            override fun onNext(response: GraphqlResponse) {
                if (view != null) {
                    view.hideLoader()
                    data = response.getData(CouponDetailsResponse::class.java)
                    view.populateDetail(data.hachikoCatalogDetail!!)
                    handlePointQuery(response.getData<UserPointsResponse>(UserPointsResponse::class.java))

                }
            }
        })
    }

    private fun handlePointQuery(pointDetailEntity: UserPointsResponse?) {
        if (pointDetailEntity?.tokopoints?.resultStatus == null
                || pointDetailEntity.tokopoints.status == null
                || pointDetailEntity.tokopoints.status.points == null) {
            //Need to handle error
        } else if (pointDetailEntity.tokopoints.resultStatus.code == "200") {
            pointDetailEntity.tokopoints.status.points.rewardStr?.let { view.onSuccessPoints(it) }
        }
    }
}
