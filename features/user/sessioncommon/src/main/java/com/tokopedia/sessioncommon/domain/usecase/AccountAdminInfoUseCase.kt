package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sessioncommon.data.admin.AdminDataResponse
import com.tokopedia.sessioncommon.data.admin.AdminTypeResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.exception.RefreshShopDataException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AccountAdminInfoUseCase @Inject constructor(private val refreshShopBasicDataUseCase: RefreshShopBasicDataUseCase,
                                                  private val graphqlRepository: GraphqlRepository): GraphqlUseCase<Pair<AdminDataResponse?, ShopData?>>(graphqlRepository) {


    var isLocationAdmin: Boolean = false

    override suspend fun executeOnBackground(): Pair<AdminDataResponse?, ShopData?> {
        try {
            val request = GraphqlRequest(GetAdminTypeUseCase.QUERY, AdminTypeResponse::class.java, RequestParams.EMPTY.parameters)
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
}