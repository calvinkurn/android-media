package com.tokopedia.seller.menu.common.domain.usecase

import com.tokopedia.gm.common.domain.interactor.GetPMShopInfoUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.menu.common.domain.entity.UserShopInfoResponse
import com.tokopedia.seller.menu.common.domain.mapper.UserShopInfoMapper
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetUserShopInfoUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val userShopInfoMapper: UserShopInfoMapper
): UseCase<UserShopInfoWrapper>() {

    companion object {
        val USER_SHOP_INFO_QUERY = """
            query GetUserShop(${'$'}shopId: Int!, ${'$'}filter: GetPMShopInfoFilter) {
              userShopInfo {
                info {
                  date_shop_created
                }
              }
              shopInfoByID(
                input: {
                  shopIDs: [${'$'}shopId]
                  fields: ["other-goldos", "shopstats-limited", "shop-snippet", "location", "core", "branch-link"]
                }
              ) {
                result {
                  goldOS {
                    title
                    badge
                    shopTier  
                    shopTierWording
                    shopGrade
                    shopGradeWording
                  }
                  statsByDate {
                    identifier
                    value
                    startTime
                  }
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
              goldGetPMSettingInfo(shopID: ${'$'}shopId, source: "goldmerchant") {
                period_type_pm_pro
              }
              goldGetPMShopInfo(shop_id: ${'$'}shopId, source: "android-goldmerchant", filter: ${'$'}filter) {
                kyc_status_id
                is_eligible_pm
                is_eligible_pm_pro
              }
              goldGetPMOSStatus(
                 shopID: ${'$'}shopId,
                 includeOS: true){
                 data {
                   power_merchant {
                     status
                     pm_tier              
                   }
                   official_store {
                     status
                     error
                   }
                 }
              }
            }
        """.trimIndent()

        private const val SHOP_ID_KEY = "shopId"
        private const val KEY_FILTER = "filter"
        private const val KEY_INCLUDING_PM_PRO_ELIGIBILITY = "including_pm_pro_eligibility"

        fun createRequestParams(shopId: Long) = HashMap<String, Any>().apply {
            val filter: Map<String, Boolean> = mapOf(
                KEY_INCLUDING_PM_PRO_ELIGIBILITY to true
            )
            put(SHOP_ID_KEY, shopId)
            put(KEY_FILTER, filter)
        }
    }

    var params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): UserShopInfoWrapper {
        val gqlRequest = GraphqlRequest(USER_SHOP_INFO_QUERY, UserShopInfoResponse::class.java, params)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val gqlError = gqlResponse.getError(UserShopInfoResponse::class.java)
        if (gqlError.isNullOrEmpty()) {
            val userShopInfoResponse: UserShopInfoResponse = gqlResponse.getData(UserShopInfoResponse::class.java)
            return userShopInfoMapper.mapToUserShopInfoUiModel(userShopInfoResponse)
        }
        throw MessageErrorException(gqlError.joinToString { it.message })
    }
}