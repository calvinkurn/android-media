package com.tokopedia.sellerhome.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerhome.domain.mapper.OtherMenuShopShareMapper
import com.tokopedia.sellerhome.domain.model.ShopShareOtherResponse
import com.tokopedia.sellerhome.settings.view.adapter.uimodel.OtherMenuShopShareData
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("ShareInfoOtherGqlQuery", ShareInfoOtherUseCase.QUERY)
class ShareInfoOtherUseCase @Inject constructor(
    val graphqlRepository: GraphqlRepository,
    private val mapper: OtherMenuShopShareMapper,
) : GraphqlUseCase<ShopShareOtherResponse>(graphqlRepository) {

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(ShareInfoOtherGqlQuery())
        setTypeClass(ShopShareOtherResponse::class.java)
    }

    suspend fun execute(shopId: String): OtherMenuShopShareData? {
        val requestParams = RequestParams.create().apply {
            putLong(SHOP_ID_KEY, shopId.toLongOrZero())
        }

        setRequestParams(requestParams.parameters)

        return mapper.mapToOtherMenuShopShareData(executeOnBackground())
    }

    companion object {
        const val QUERY = """
            query GetUserShop(${'$'}shopId: Int!) {
              shopInfoByID(
                input: {
                  shopIDs: [${'$'}shopId]
                  fields: ["shop-snippet", "location", "core", "branch-link"]
                }
              ) {
                result {
                  shopSnippetURL
                  location
                  branchLinkDomain
                  shopCore {
                    description
                    tagLine
                    url
                  }
                }
              }
            }
        """
        private const val SHOP_ID_KEY = "shopId"
    }
}