package com.tokopedia.content.common.producttag.domain.usecase

import com.tokopedia.content.common.producttag.model.FeedAceSearchShopResponse
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
@GqlQuery(FeedAceSearchShopUseCase.QUERY_NAME, FeedAceSearchShopUseCase.QUERY)
class FeedAceSearchShopUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<FeedAceSearchShopResponse>(gqlRepository) {

    init {
        setGraphqlQuery(FeedAceSearchShopUseCaseQuery())
        setCacheStrategy(
            GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(FeedAceSearchShopResponse::class.java)
    }

    companion object {
        private const val PARAMS = "params"

        const val QUERY_NAME = "FeedAceSearchShopUseCaseQuery"
        const val QUERY = """
            query FeedAceSearchShop(${"$$PARAMS"}: String!) {
              aceSearchShop($PARAMS: ${"$$PARAMS"}) {
                total_shop
                header {
                  total_data
                  total_data_text
                  response_code
                  keyword_process
                }
                shops {
                  shop_id
                  shop_name
                  shop_image
                  shop_location
                  shop_gold_shop
                  shop_status
                  is_official
                  is_pm_pro
                }
              }
            }
        """

        fun createParams(
            param: SearchParamUiModel,
        ): Map<String, Any> {
            return mapOf<String, Any>(
                PARAMS to param.joinToString()
            )
        }
    }
}