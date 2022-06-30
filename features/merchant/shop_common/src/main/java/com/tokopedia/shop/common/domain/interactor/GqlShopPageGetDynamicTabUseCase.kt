package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GqlShopPageGetDynamicTabUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<ShopPageGetDynamicTabResponse>(gqlRepository) {

    var isFromCacheFirst: Boolean = true

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(ShopPageGetDynamicTabQuery())
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ShopPageGetDynamicTabResponse::class.java)
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_EXT_PARAM = "extParam"
        private const val QUERY_NAME = "ShopPageGetDynamicTabQuery"

        @JvmStatic
        fun createParams(shopId: Int, extParam: String): RequestParams =
            RequestParams.create().apply {
                putObject(PARAM_SHOP_ID, shopId)
                putObject(PARAM_EXT_PARAM, extParam)
            }

        const val QUERY = """
            query shopPageGetDynamicTab(${'$'}shopID: Int!, ${'$'}extParam: String!){
              shopPageGetDynamicTab(
                shopID: ${'$'}shopID,
                extParam: ${'$'}extParam
              ){
                tabData {
                   name
                  isActive
                  isFocus
                  isDefault
                  errorMessage
                  text
                  icon
                  iconFocus
                  type
                  shopLayoutFeatures {
                    name
                    isActive
                  }
                  data {
                    ... on HomeTabData {
                      homeLayoutData {
                        layoutID
                        masterLayoutID
                        widgetIDList {
                          widgetID
                          widgetMasterID
                          widgetType
                          widgetName
                          header {
                            title
                            ctaText
                            ctaLink
                            cover
                            ratio
                            sizeOption
                            isATC
                            isActive
                            isBrokenLink
                            errMsgBrokenLink
                            etalaseID
                            isShowEtalaseName
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
        """
    }
}