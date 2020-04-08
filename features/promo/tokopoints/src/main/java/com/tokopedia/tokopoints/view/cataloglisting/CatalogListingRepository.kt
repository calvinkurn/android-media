package com.tokopedia.tokopoints.view.cataloglisting

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.tokopoints.di.TokoPointScope
import com.tokopedia.tokopoints.view.model.CatalogBannerOuter
import com.tokopedia.tokopoints.view.model.CatalogFilterOuter
import com.tokopedia.tokopoints.view.model.TokenDetailOuter
import com.tokopedia.tokopoints.view.model.TokoPointDetailEntity
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CommonConstant.GQLQuery.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@TokoPointScope
class CatalogListingRepository @Inject constructor(@Named(TP_GQL_CATALOG_BANNER) private val tp_gql_catalog_banner: String,
                                                   @Named(TP_GQL_TOKOPOINT_DETAIL) private val tp_gql_tokopoint_detail: String,
                                                   @Named(TP_GQL_CATALOG_FILTER) private val tp_gql_catalog_filter: String,
                                                   @Named(TP_GQL_LUCKY_EGG_DETAILS) private val tp_lucky_egg_detail: String,
                                                   @Named(TP_GQL_CURRENT_POINTS) private val  tp_gql_current_Point: String, map : Map<String,String>) : CatalogPurchaseRedeemptionRepository(map){






    @Inject
    lateinit var mGetHomePageData: MultiRequestGraphqlUseCase



    @Inject
    lateinit var mGetPointData: MultiRequestGraphqlUseCase

    suspend fun getHomePageData(slugCategory: String?, slugSubCategory: String?, isBannerRequire: Boolean) = withContext(Dispatchers.IO) {
        mGetHomePageData.clearRequest()
        if (isBannerRequire) {
            val variablesBanner: MutableMap<String, Any> = HashMap()
            variablesBanner[CommonConstant.GraphqlVariableKeys.DEVICE] = CommonConstant.DEVICE_ID_BANNER
            val graphqlRequestBanners = GraphqlRequest(tp_gql_catalog_banner,
                    CatalogBannerOuter::class.java,
                    variablesBanner, false)
            mGetHomePageData.addRequest(graphqlRequestBanners)
        }
        val graphqlRequestTokenDetail = GraphqlRequest(tp_gql_tokopoint_detail,
                TokoPointDetailEntity::class.java, false)
        mGetHomePageData.addRequest(graphqlRequestTokenDetail)
        val variableFilter: MutableMap<String, Any?> = HashMap()
        variableFilter[CommonConstant.GraphqlVariableKeys.SLUG_CATEGORY] = slugCategory
        variableFilter[CommonConstant.GraphqlVariableKeys.SLUG_SUB_CATEGORY] = slugSubCategory
        val graphqlRequestFilter = GraphqlRequest(tp_gql_catalog_filter,
                CatalogFilterOuter::class.java,
                variableFilter, false)
        mGetHomePageData.addRequest(graphqlRequestFilter)
        val graphqlRequestEgg = GraphqlRequest(tp_lucky_egg_detail,
                TokenDetailOuter::class.java, false)
        mGetHomePageData.addRequest(graphqlRequestEgg)
        mGetHomePageData.executeOnBackground()
    }

    suspend fun getPointData() = withContext(Dispatchers.IO) {
        mGetPointData.clearRequest()
        val graphqlRequestPoints = GraphqlRequest(tp_gql_current_Point,
                TokoPointDetailEntity::class.java, false)
        mGetPointData.addRequest(graphqlRequestPoints)
        mGetPointData.executeOnBackground().getSuccessData<TokoPointDetailEntity>()
    }

}