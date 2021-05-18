package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.exception.RefreshShopDataException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AccountAdminInfoUseCase @Inject constructor(private val refreshShopBasicDataUseCase: RefreshShopBasicDataUseCase,
                                                  private val graphqlRepository: GraphqlRepository): GraphqlUseCase<Pair<AdminDataResponse?, ShopData?>>(graphqlRepository) {

    companion object {
        private const val DEFAULT_SOURCE = "android"
        private const val PARAM_SOURCE = "source"
        private const val SOURCE = "\$source"

        private val QUERY = """
            query getAdminType(${SOURCE}: String!) {
              getAdminType(source: ${SOURCE}) {
                shopID
                isMultiLocation
                admin_data {
                  admin_type_string
                  detail_information {
                    admin_role_type {
                      is_shop_admin
                      is_location_admin
                      is_shop_owner            
                    }
                  }
                  status
                }
              }
            }
        """.trimIndent()

        @JvmStatic
        fun createRequestParams(source: String = DEFAULT_SOURCE): RequestParams =
                RequestParams.create().apply {
                    putString(PARAM_SOURCE, source)
                }
    }

    var isLocationAdmin: Boolean = false
    var requestParams: RequestParams = GetAdminTypeUseCase.createRequestParams()

    override suspend fun executeOnBackground(): Pair<AdminDataResponse?, ShopData?> {
        try {
            val request = GraphqlRequest(QUERY, AdminTypeResponse::class.java, requestParams.parameters)
            graphqlRepository.getReseponse(listOf(request)).let { response ->
                response.getError(AdminTypeResponse::class.java).let { errors ->
                    if (errors.isNullOrEmpty()) {
                        response.getData<AdminTypeResponse>(AdminTypeResponse::class.java).response.let { adminResponse ->
                            return getAdminDataAndShopInfo(adminResponse)
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // Only throw error if failing get shop basic data because it can break flow.
            // But we dont need to throw error if we only fail to get admin info
            if (ex is RefreshShopDataException) {
                throw ex
            }
        }
        return Pair(null, null)
    }

    private suspend fun getAdminDataAndShopInfo(adminDataResponse: AdminDataResponse?): Pair<AdminDataResponse?, ShopData?> {
        val shopData =
                adminDataResponse?.data?.detail?.roleType?.let {
                    // If role changed from location admin to non location admin, refresh the shop data
                    if (isLocationAdmin && !it.isLocationAdmin) {
                        refreshShopBasicDataUseCase.executeOnBackground()
                    } else {
                        null
                    }
                }
        return Pair(adminDataResponse, shopData)
    }

    fun setStrategyCloudThenCache() {
        setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(5 * GraphqlConstant.ExpiryTimes.HOUR.`val`())
                        .setSessionIncluded(true)
                        .build())
    }
}