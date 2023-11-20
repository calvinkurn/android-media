package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.ResponseProductList
import com.tokopedia.topads.common.domain.query.GetTopadsListProductV2
import javax.inject.Inject

class TopAdsGetListProductUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<ResponseProductList.Result>(graphqlRepository) {

    init {
        setTypeClass(ResponseProductList.Result::class.java)
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build())
        setGraphqlQuery(GetTopadsListProductV2)
    }

    fun setParams(keyword: String, etalaseId: String, sortBy: String, promoted: String, rows: Int, start: Int, shopId:String, source: String) {
        val queryMap = mutableMapOf(
                ParamObject.KEYWORD to keyword,
                ParamObject.ETALASE to etalaseId,
                ParamObject.SORT_BY to sortBy,
                ParamObject.ROWS to rows,
                ParamObject.START to start,
                ParamObject.STATUS to promoted,
                ParamObject.SHOP_ID to shopId,
                ParamObject.SOURCE to source
        )
        setRequestParams(queryMap)
    }
}
