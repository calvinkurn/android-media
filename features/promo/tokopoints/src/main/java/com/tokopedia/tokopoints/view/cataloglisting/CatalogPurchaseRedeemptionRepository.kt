package com.tokopedia.tokopoints.view.cataloglisting

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@TokoPointScope
open class CatalogPurchaseRedeemptionRepository @Inject constructor(private val map: Map<String, String>) {

    @Inject
    lateinit var mSaveCouponUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mValidateCouponUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mRedeemCouponUseCase: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mRefreshCatalogStatus: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mGetCatalogUsecase: MultiRequestGraphqlUseCase

    suspend fun startValidateCoupon(id: Int) = withContext(Dispatchers.IO) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0 //Never be a gift
        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_VALIDATE_REDEEM],
                ValidateCouponBaseEntity::class.java,
                variables, false)
        mValidateCouponUseCase.clearRequest()
        mValidateCouponUseCase.addRequest(request)
        mValidateCouponUseCase.executeOnBackground().getSuccessData<ValidateCouponBaseEntity>()
    }

    suspend fun fetchLatestStatus(catalogsIds: List<Int>) = withContext(Dispatchers.IO) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_IDS] = catalogsIds
        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_CATLOG_STATUS],
                CatalogStatusOuter::class.java,
                variables, false)
        mRefreshCatalogStatus.clearRequest()
        mRefreshCatalogStatus.addRequest(request)
        mRefreshCatalogStatus.executeOnBackground().getSuccessData<CatalogStatusOuter>()
    }

    suspend fun startSaveCoupon(id: Int) = withContext(Dispatchers.IO) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 0 //Never be a gift
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = CommonConstant.APIVERSION
        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_REDEEM_COUPON],
                RedeemCouponBaseEntity::class.java,
                variables, false)
        mRedeemCouponUseCase.clearRequest()
        mRedeemCouponUseCase.addRequest(request)
        mRedeemCouponUseCase.executeOnBackground().getSuccessData<RedeemCouponBaseEntity>()
    }

    suspend fun redeemCoupon(promoCode: String) = withContext(Dispatchers.IO) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.PROMO_CODE] = promoCode
        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_TOKOPOINT_APPLY_COUPON],
                ApplyCouponBaseEntity::class.java,
                variables, false)
        mSaveCouponUseCase.clearRequest()
        mSaveCouponUseCase.addRequest(request)
        mSaveCouponUseCase.executeOnBackground().getSuccessData<ApplyCouponBaseEntity>()
    }

    suspend fun getListOfCatalog(categoryId: Int, subCategoryId: Int, pointsRange: Int) = withContext(Dispatchers.IO) {
        val variablesMain: MutableMap<String, Any> = HashMap()
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE] = 1
        variablesMain[CommonConstant.GraphqlVariableKeys.PAGE_SIZE] = CommonConstant.PAGE_SIZE
        //Default page sort id
        variablesMain[CommonConstant.GraphqlVariableKeys.SORT_ID] = 1
        variablesMain[CommonConstant.GraphqlVariableKeys.CATEGORY_ID] = categoryId
        variablesMain[CommonConstant.GraphqlVariableKeys.SUB_CATEGORY_ID] = subCategoryId
        //Point range will be zero for all catalog
        variablesMain[CommonConstant.GraphqlVariableKeys.POINTS_RANGE] = pointsRange
        val graphqlRequestMain = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_CATALOG_LIST],
                CatalogListingOuter::class.java,
                variablesMain, false)
        mGetCatalogUsecase.clearRequest()
        mGetCatalogUsecase.addRequest(graphqlRequestMain)
        mGetCatalogUsecase.executeOnBackground().getSuccessData<CatalogListingOuter>()
    }
}