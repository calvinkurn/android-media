package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoUiModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetUserShopInfoUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository): UseCase<UserShopInfoUiModel>() {

    companion object {
        val USER_SHOP_INFO_QUERY = """
            query GetUserShop(${'$'}shopID: Int!) {
              userShopInfo {
                info {
                  date_shop_created
                }
                owner {
                  pm_status
                }
                stats {
                  shop_total_transaction
                }
              }
              shopInfoByID(
                input: {
                  shopIDs: [${'$'}shopID]
                  fields: ["other-goldos"]
                }
              ) {
                result {
                  goldOS {
                    isGold
                    isGoldBadge
                    isOfficial
                    title
                    badge
                    shopTier  
                    shopTierWording
                    shopGrade
                    shopGradeWording
                  }
                }
              }
            }
        """.trimIndent()

        private const val SHOP_ID_KEY = "shopId"
    }


    fun createRequestParams(shopId: Int) = HashMap<String, Any>().apply {
        put(SHOP_ID_KEY, shopId)
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): UserShopInfoUiModel {
        val gqlRequest = GraphqlRequest(USER_SHOP_INFO_QUERY, UserShopInfoResponse::class.java, params)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(UserShopInfoResponse::class.java)
        if (gqlError.isNullOrEmpty()) {
            val userShopInfoResponse: UserShopInfoResponse = gqlResponse.getData(UserShopInfoResponse::class.java)


        }
        throw MessageErrorException(gqlError.joinToString { it.message })
    }
}