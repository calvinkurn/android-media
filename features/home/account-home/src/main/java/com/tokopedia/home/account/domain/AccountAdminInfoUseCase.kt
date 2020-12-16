package com.tokopedia.home.account.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.account.data.exception.RefreshShopDataException
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminInfoResponse
import com.tokopedia.sessioncommon.data.profile.ShopData
import com.tokopedia.sessioncommon.domain.usecase.GetAdminInfoUseCase
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class AccountAdminInfoUseCase @Inject constructor(private val refreshShopBasicDataUseCase: RefreshShopBasicDataUseCase,
                                                  private val graphqlRepository: GraphqlRepository): GraphqlUseCase<Pair<AdminData?, ShopData?>>(graphqlRepository) {

    companion object {
        @JvmStatic
        fun createRequestParams(shopId: Int): RequestParams =
                RequestParams.create().apply {
                    putInt(GetAdminInfoUseCase.shopID, shopId)
                }
    }

    var requestParams: RequestParams = RequestParams.EMPTY
    var isLocationAdmin: Boolean = false

    override suspend fun executeOnBackground(): Pair<AdminData?, ShopData?> {
        try {
            val request = GraphqlRequest(GetAdminInfoUseCase.QUERY, AdminInfoResponse::class.java, requestParams.parameters)
            graphqlRepository.getReseponse(listOf(request)).let { response ->
                response.getError(AdminInfoResponse::class.java).let { errors ->
                    if (errors.isNullOrEmpty()) {
                        response.getData<AdminInfoResponse>(AdminInfoResponse::class.java).response.data.firstOrNull().let { adminData ->
                            return updateUserSessionAndReturnDataData(adminData)
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            // Only throw error if failing get shop basic data because it can break flow
            if (ex is RefreshShopDataException) {
                throw ex
            }
        }
        return Pair(null, null)
    }

    private suspend fun updateUserSessionAndReturnDataData(adminData: AdminData?): Pair<AdminData?, ShopData?> {
        val shopData =
                adminData?.detail?.roleType?.let {
                    // If role changed from location admin to non location admin, refresh the shop data
                    if (isLocationAdmin && !it.isLocationAdmin) {
                        refreshShopBasicDataUseCase.executeOnBackground()
                    } else {
                        null
                    }
                }
        return Pair(adminData, shopData)
    }
}