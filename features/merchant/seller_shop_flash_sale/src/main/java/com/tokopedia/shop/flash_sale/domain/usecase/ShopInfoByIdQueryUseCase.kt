package com.tokopedia.shop.flash_sale.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.flash_sale.data.mapper.ShopInfoByIdMapper
import com.tokopedia.shop.flash_sale.data.request.ShopInfoByIdRequest
import com.tokopedia.shop.flash_sale.data.response.ShopInfoByIdResponse
import com.tokopedia.shop.flash_sale.domain.entity.ShopInfo
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopInfoByIdQueryUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    private val mapper: ShopInfoByIdMapper
) : GraphqlUseCase<ShopInfo>(repository) {

    companion object {
        private const val REQUEST_PARAM = "input"
        const val QUERY_NAME = "ShopInfoByID"
        const val QUERY = """
            query ShopInfoByID(${'$'}input: ParamShopInfoByID!)  {
              shopInfoByID(input: ${'$'}input) {
                result {
                  goldOS {
                    isGold
                    isOfficial
                    badge
                    shopTier
                    shopTierWording
                    shopGrade
                    shopGradeWording
                  }
                  isOwner
                  shopCore {
                    name
                    shopID
                    domain
                    description
                    tagLine
                  }
                  shopHomeType
                  closedInfo {
                    closedNote
                    until
                    detail {
                      startDate
                      endDate
                      openDate
                      status
                    }
                  }
                  statusInfo {
                    shopStatus
                    statusName
                  }
                  os {
                    isOfficial
                    expired
                  }
                }
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    suspend fun execute(): ShopInfo {
        val request = buildRequest()
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<ShopInfoByIdResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(): GraphqlRequest {
        val payload = ShopInfoByIdRequest(listOf(userSession.shopId.toLong()), getDefaultShopFields())
        val params = mapOf(REQUEST_PARAM to payload)
        return GraphqlRequest(
            ShopInfoByID(),
            ShopInfoByIdResponse::class.java,
            params
        )
    }

    private fun getDefaultShopFields(): List<String> {
        return listOf(
            "assets",
            "core",
            "favorite",
            "location",
            "other-goldos",
            "other-shiploc",
            "status",
            "allow_manage",
            "is_owner",
            "closed_info",
            "status",
            "assets"
        )
    }
}