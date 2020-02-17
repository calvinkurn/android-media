package com.tokopedia.tokopoints.view.cataloglisting

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.contract.CatalogListItemContract
import com.tokopedia.tokopoints.view.contract.CatalogPurchaseRedemptionPresenter
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class CatalogListItemPresenter @Inject constructor(private val mGetHomePageData: GraphqlUseCase?,
                                                   private val mSaveCouponUseCase: GraphqlUseCase?,
                                                   private val mValidateCouponUseCase: GraphqlUseCase?,
                                                   private val mFetchCatalogStatusUseCase: GraphqlUseCase?,
                                                   private val mRedeemCouponUseCase: GraphqlUseCase?) : BaseDaggerPresenter<CatalogListItemContract.View?>(), CatalogListItemContract.Presenter, CatalogPurchaseRedemptionPresenter {
    private var pointRange = 0
    override fun destroyView() {
        mGetHomePageData?.unsubscribe()
        mSaveCouponUseCase?.unsubscribe()
        mValidateCouponUseCase?.unsubscribe()
        mRedeemCouponUseCase?.unsubscribe()
        mFetchCatalogStatusUseCase?.unsubscribe()
    }

    override fun getCatalog(categoryId: Int, subCategoryId: Int, showLoader: Boolean) {
        view!!.populateCatalog(categoryId, subCategoryId, pointRange, showLoader)
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

    override fun fetchLatestStatus(catalogsIds: List<Int>) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_IDS] = catalogsIds
        val request = GraphqlRequest(GraphqlHelper.loadRawString(view!!.appContext.resources,
                R.raw.tp_gql_catalog_status),
                CatalogStatusOuter::class.java,
                variables, false)
        mFetchCatalogStatusUseCase!!.clearRequest()
        mFetchCatalogStatusUseCase.addRequest(request)
        mFetchCatalogStatusUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {}
            override fun onError(e: Throwable) { //NA
            }

            override fun onNext(response: GraphqlResponse) {
                val data = response.getData<CatalogStatusOuter>(CatalogStatusOuter::class.java)
                if (data != null && data.catalogStatus != null) {
                    view!!.refreshCatalog(data.catalogStatus.catalogs)
                }
            }
        })
    }

    override fun showRedeemCouponDialog(cta: String, code: String, title: String) {
        view!!.showRedeemCouponDialog(cta, code, title)
    }

    fun setPointRange(pointRange: Int) {
        this.pointRange = pointRange
    }

}