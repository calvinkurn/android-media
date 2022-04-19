package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_DASH_SELECTION_ITEM
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_TOGGLE_OFF
import com.tokopedia.topads.common.data.internal.ParamObject.PARAM_TOGGLE_ON
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpData
import com.tokopedia.topads.debit.autotopup.data.model.AutoTopUpItem
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

const val POST_AUTO_TOPUP_QUERY = """
    query topAdsPostAutoTopup(${'$'}shopId: Int!,${'$'}action: String!,${'$'}selectionId: Int) {
    topAdsPostAutoTopup(shop_id:${'$'}shopId, action: ${'$'}action, tkpd_product_id: ${'$'}selectionId){
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
        setGraphqlQuery(SaveSelection.GQL_QUERY)
    }

    fun setParam(isActive: Boolean, selectedItem: AutoTopUpItem) {
        val params = mutableMapOf(
                ParamObject.SHOP_Id to userSessionInterface.shopId.toIntOrZero(),
                ParamObject.ACTION to (if (isActive) PARAM_TOGGLE_ON else PARAM_TOGGLE_OFF),
                PARAM_DASH_SELECTION_ITEM to selectedItem.id)

        setRequestParams(params)
    }
}