package com.tokopedia.tokopoints.view.catalogdetail

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.cataloglisting.CatalogPurchaseRedeemptionRepository
import com.tokopedia.tokopoints.view.model.*
import com.tokopedia.tokopoints.view.util.CommonConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@TokoPointScope
class CouponCatalogRepository @Inject constructor(private val map: Map<String, String>, @Named(CommonConstant.GQLQuery.TP_GQL_CURRENT_POINTS) val tp_gql_current_Point: String) : CatalogPurchaseRedeemptionRepository(map) {

    @Inject
    lateinit var mGetCouponDetail: MultiRequestGraphqlUseCase

    @Inject
    lateinit var mStartSendGift: MultiRequestGraphqlUseCase

    suspend fun getcatalogDetail(uniqueCatalogCode: String): GraphqlResponse = withContext(Dispatchers.IO) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.SLUG] = uniqueCatalogCode
        variables[CommonConstant.GraphqlVariableKeys.APIVERSION] = "3.0.0"
        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_CATALOG_DETAIL],
                CatalogDetailOuter::class.java,
                variables, false)
        mGetCouponDetail.clearRequest()
        mGetCouponDetail.addRequest(request)
        val graphqlRequestPoints = GraphqlRequest(tp_gql_current_Point,
                TokoPointDetailEntity::class.java, false)
        mGetCouponDetail.addRequest(graphqlRequestPoints)
        mGetCouponDetail.executeOnBackground()
    }

    suspend fun startSendGift(id: Int) = withContext(Dispatchers.IO) {
        val variables: MutableMap<String, Any> = HashMap()
        variables[CommonConstant.GraphqlVariableKeys.CATALOG_ID] = id
        variables[CommonConstant.GraphqlVariableKeys.IS_GIFT] = 1
        val request = GraphqlRequest(map[CommonConstant.GQLQuery.TP_GQL_PRE_VALIDATE_REDEEM],
                PreValidateRedeemBase::class.java,
                variables, false)
        mStartSendGift.clearRequest()
        mStartSendGift.addRequest(request)
        mStartSendGift.executeOnBackground().getSuccessData<PreValidateRedeemBase>()
    }


}
