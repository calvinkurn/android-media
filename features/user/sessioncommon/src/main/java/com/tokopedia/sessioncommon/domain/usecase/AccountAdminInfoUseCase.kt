package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.admin.AccountAdminInfoGqlParam
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.exception.RefreshShopDataException
import javax.inject.Inject

class AccountAdminInfoUseCase @Inject constructor(
    private val refreshShopBasicDataUseCase: RefreshShopBasicDataUseCase,
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<AccountAdminInfoGqlParam, Pair<AdminDataResponse?, ShopData?>>(dispatcher.io) {

    companion object {
        private const val DEFAULT_SOURCE = "android"
        private const val PARAM_SOURCE = "source"
        private const val SOURCE = "\$source"

        private val QUERY = """
            query getAdminType($SOURCE: String!) {
              getAdminType(source: $SOURCE) {
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
    }

    private var isLocationAdmin: Boolean = false

    private suspend fun getAdminDataAndShopInfo(adminDataResponse: AdminDataResponse?): Pair<AdminDataResponse?, ShopData?> {
        val shopData =
            adminDataResponse?.data?.detail?.roleType?.let {
                // If role changed from location admin to non location admin, refresh the shop data
                if (isLocationAdmin && !it.isLocationAdmin) {
                    refreshShopBasicDataUseCase(Unit)
                } else {
                    null
                }
            }
        return Pair(adminDataResponse, shopData)
    }

    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: AccountAdminInfoGqlParam): Pair<AdminDataResponse?, ShopData?> {
        return try {
            val adminData: AdminTypeResponse = graphqlRepository.request(graphqlQuery(), mapOf(PARAM_SOURCE to params.source))
            isLocationAdmin = params.isLocationAdmin
            getAdminDataAndShopInfo(adminData.response)
        } catch (e: RefreshShopDataException) {
            throw e
        } catch (_: Exception) {
            Pair(null, null)
        }
    }
}
