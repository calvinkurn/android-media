package com.tokopedia.tokopoints.view.catalogdetail

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.NetworkDetector
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class CouponCatalogPresenter @Inject constructor(private val mGetHomePageData: GraphqlUseCase,
                                                 private val mSaveCouponUseCase: GraphqlUseCase,
                                                 private val mValidateCouponUseCase: GraphqlUseCase,
                                                 private val mRedeemCouponUseCase: GraphqlUseCase,
        //new apis
                                                 private val mGetCouponDetail: GraphqlUseCase,
                                                 private val mStartSendGift: GraphqlUseCase,
                                                 private val mRefreshCatalogStatus: GraphqlUseCase) : BaseDaggerPresenter<CouponCatalogContract.View?>(), CouponCatalogContract.Presenter, CatalogPurchaseRedemptionPresenter {

    override fun destroyView() {
        mGetHomePageData?.unsubscribe()
        mSaveCouponUseCase?.unsubscribe()
        mValidateCouponUseCase?.unsubscribe()
        mRedeemCouponUseCase?.unsubscribe()
        mGetCouponDetail?.unsubscribe()
    }

    override fun startValidateCoupon(item: CatalogsValueEntity) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = item.id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0 //Never be a gift
        val graphqlRequest = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources, R.raw.tp_gql_tokopoint_validate_redeem),
                ValidateCouponBaseEntity::class.java, variables, false)
        mValidateCouponUseCase!!.clearRequest()
        mValidateCouponUseCase.addRequest(graphqlRequest)
        mValidateCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
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
        mSaveCouponUseCase!!.clearRequest()
        mSaveCouponUseCase.addRequest(request)
        mSaveCouponUseCase.execute(object : Subscriber<GraphqlResponse>() {
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
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0 //Never be a gift
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
                        var desc: String? = null
                        var title: String? = errorsMessage[0]
                        var validateResponseCode = 0
                        if (errorsMessage.size == 1) {
                            val rawString = errorsMessage[0]
                            val rawTitle = rawString.split("\\.").toTypedArray()[0]
                            val rawDesc = rawString.split("\\.").toTypedArray()[1]
                            if (rawTitle != null && rawTitle.length > 0) {
                                title = rawTitle
                            }
                            if (rawDesc != null && rawDesc.length > 0) {
                                desc = rawDesc
                            }
                        }
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

    override fun getCatalogDetail(uniqueCatalogCode: String) {
        view!!.showLoader()
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.SLUG] = uniqueCatalogCode
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_catalog_detail),
                CatalogDetailOuter::class.java,
                variables, false)
        mGetCouponDetail!!.clearRequest()
        mGetCouponDetail.addRequest(request)
        val graphqlRequestPoints = GraphqlRequest(GraphqlHelper.loadRawString(view!!.resources, R.raw.tp_gql_current_points),
                TokoPointDetailEntity::class.java, false)
        mGetCouponDetail.addRequest(graphqlRequestPoints)
        mGetCouponDetail.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA
                if (view != null) {
                    view!!.hideLoader()
                    view!!.onFinishRendering()
                    view!!.showError(NetworkDetector.isConnectedToInternet(view!!.activityContext))
                }
            }

            override fun onNext(response: GraphqlResponse) {
                if (view != null) {
                    view!!.hideLoader()
                    val data = response.getData<CatalogDetailOuter>(CatalogDetailOuter::class.java)
                    view!!.populateDetail(data.detail)
                    handlePointQuery(response.getData<TokoPointDetailEntity>(TokoPointDetailEntity::class.java))
                    view!!.onFinishRendering()
                }
            }
        })
    }

    override fun fetchLatestStatus(catalogsIds: List<Int>) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_IDS] = catalogsIds
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_catalog_status),
                CatalogStatusOuter::class.java,
                variables, false)
        mRefreshCatalogStatus.clearRequest()
        mRefreshCatalogStatus.addRequest(request)
        mRefreshCatalogStatus.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA No handling require.
            }

            override fun onNext(response: GraphqlResponse) {
                val data = response.getData<CatalogStatusOuter>(CatalogStatusOuter::class.java)
                if (data != null && data.catalogStatus != null) { //For detail page we only interested in one item
                    view!!.refreshCatalog(data.catalogStatus.catalogs[0])
                }
            }
        })
    }

    override fun showRedeemCouponDialog(cta: String, code: String, title: String) {
        view!!.showRedeemCouponDialog(cta, code, title)
    }

    private fun handlePointQuery(pointDetailEntity: TokoPointDetailEntity?) { //Handling the point
        if (pointDetailEntity == null || pointDetailEntity.tokoPoints == null || pointDetailEntity.tokoPoints.resultStatus == null || pointDetailEntity.tokoPoints.status == null || pointDetailEntity.tokoPoints.status.points == null) {
            view!!.onErrorPoint(null)
        } else {
            if (pointDetailEntity.tokoPoints.resultStatus.code == CommonConstant.CouponRedemptionCode.SUCCESS) {
                view!!.onSuccessPoints(pointDetailEntity.tokoPoints.status.points.rewardStr)
            }
        }
    }

    override fun startSendGift(id: Int, title: String, pointStr: String, banner: String) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 1
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_pre_validate_redeem),
                PreValidateRedeemBase::class.java,
                variables, false)
        mStartSendGift.clearRequest()
        mStartSendGift.addRequest(request)
        mStartSendGift.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA
            }

            override fun onNext(response: GraphqlResponse) {
                val data = response.getData<PreValidateRedeemBase>(PreValidateRedeemBase::class.java)
                if (data != null && data.preValidateRedeem != null && data.preValidateRedeem.isValid == 1) {
                    view!!.gotoSendGiftPage(id, title, pointStr, banner)
                } else { //show error
                    val errors = response.getError(PreValidateRedeemBase::class.java)
                    var errorTitle = view!!.appContext.getString(R.string.tp_send_gift_failed_title)
                    var errorMessage = view!!.appContext.getString(R.string.tp_send_gift_failed_message)
                    if (errors != null && errors.size > 0) {
                        val mesList = errors[0].message.split("|").toTypedArray()
                        if (mesList.size == 3) {
                            errorTitle = mesList[0]
                            errorMessage = mesList[1]
                        } else if (mesList.size == 2) {
                            errorMessage = mesList[0]
                        }
                    }
                    view!!.onPreValidateError(errorTitle, errorMessage)
                }
            }
        })
    }

}