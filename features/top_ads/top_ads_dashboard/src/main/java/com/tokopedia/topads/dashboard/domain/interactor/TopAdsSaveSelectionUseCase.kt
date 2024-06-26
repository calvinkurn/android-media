package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DASH_SELECTION_ITEM
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_TOGGLE_OFF
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_TOGGLE_ON
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.TopAdsCreditTopUpConstant.FREQUENCY
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val POST_AUTO_TOPUP_QUERY = """
    query topAdsPostAutoTopupV2(${'$'}shopId: String!,${'$'}action: String!,${'$'}selectionId: String!, ${'$'}frequency: String) {
    topAdsPostAutoTopupV2(shop_id:${'$'}shopId, action: ${'$'}action, tkpd_product_id: ${'$'}selectionId, frequency: ${'$'}frequency){
        data{
            status
            status_desc
            tkpd_product_id
        }
        errors {
            Code
            Detail
            Title
        }
    }
}
"""

@GqlQuery("SaveSelection", POST_AUTO_TOPUP_QUERY)
class TopAdsSaveSelectionUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                                     private val userSessionInterface: UserSessionInterface) : GraphqlUseCase<AutoTopUpData.Response>(graphqlRepository) {
    init {
        setTypeClass(AutoTopUpData.Response::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(SaveSelection())
    }

    fun setParam(isActive: Boolean, selectedItem: AutoTopUpItem, frequency: String) {
        val params = mutableMapOf(
            ParamObject.SHOP_Id to userSessionInterface.shopId,
            ParamObject.ACTION to (if (isActive) PARAM_TOGGLE_ON else PARAM_TOGGLE_OFF),
            PARAM_DASH_SELECTION_ITEM to selectedItem.id.toString(),
            FREQUENCY to frequency
        )

        setRequestParams(params)
    }

    fun setParam(isActive: Boolean, selectedItemId: String, frequency: String) {
        val params = mutableMapOf(
            ParamObject.SHOP_Id to userSessionInterface.shopId,
            ParamObject.ACTION to (if (isActive) PARAM_TOGGLE_ON else PARAM_TOGGLE_OFF),
            PARAM_DASH_SELECTION_ITEM to selectedItemId,
            FREQUENCY to frequency
        )

        setRequestParams(params)
    }
}
