package com.tokopedia.home.account.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.account.data.exception.RefreshShopDataException
import com.tokopedia.home.account.data.pojo.RefreshShopDataResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class RefreshShopBasicDataUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                                      private val userSession: UserSessionInterface): GraphqlUseCase<Boolean>(gqlRepository) {

    companion object {
        private const val QUERY = "query RefreshShopBasicData {\n" +
                "  shopBasicData {\n" +
                "    result {\n" +
                "      shopID\n" +
                "      name\n" +
                "      logo\n" +
                "      level\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val LEVEL_GOLD = 1
        private const val LEVEL_OFFICIAL_STORE = 2
    }

    override suspend fun executeOnBackground(): Boolean {
        try {
            GraphqlRequest(QUERY, RefreshShopDataResponse::class.java, RequestParams.EMPTY.parameters).let { request ->
                gqlRepository.getReseponse(listOf(request)).let { response ->
                    response.getError(RefreshShopDataResponse::class.java).let { errors ->
                        if (errors.isNullOrEmpty()) {
                            response.getData<RefreshShopDataResponse>(RefreshShopDataResponse::class.java).shopInfo.shopData.let {
                                refreshShopData(it)
                                return true
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            throw RefreshShopDataException(ex.message.orEmpty())
        }
        return false
    }

    private fun refreshShopData(shopData: ShopData) {
        with(userSession) {
            shopId = shopData.shopId
            shopName = shopData.shopName
            shopAvatar = shopData.shopAvatar
            setIsGoldMerchant(shopData.shopLevel == LEVEL_GOLD  ||  shopData.shopLevel == LEVEL_OFFICIAL_STORE)
            setIsShopOfficialStore(shopData.shopLevel == LEVEL_OFFICIAL_STORE)
        }
    }
}