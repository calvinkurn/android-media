package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.SingleAdInFo
import com.tokopedia.topads.common.domain.query.GetTopadsPromo
import javax.inject.Inject

class TopAdsGetPromoUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<SingleAdInFo>(graphqlRepository) {

    init {
        setTypeClass(SingleAdInFo::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetTopadsPromo)
    }

    fun setParams(adId:String, shopId:String) {
        val queryMap = mutableMapOf(
                ParamObject.AD_ID to adId,
                ParamObject.SHOP_ID to shopId
        )
        setRequestParams(queryMap)
    }
}
