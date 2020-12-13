package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sessioncommon.data.admin.AdminData
import com.tokopedia.sessioncommon.data.admin.AdminInfoResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AccountAdminInfoUseCase @Inject constructor(private val userSession: UserSessionInterface,
                                                  private val graphqlRepository: GraphqlRepository): GraphqlUseCase<Pair<Boolean, String?>>(graphqlRepository) {

    override suspend fun executeOnBackground(): Pair<Boolean, String?> {
        if (userSession.hasShop()) {
            try {
                val requestParams = RequestParams().apply {
                    putInt(GetAdminInfoUseCase.shopID, userSession.shopId.toIntOrNull() ?: 0)
                }.parameters
                val request = GraphqlRequest(GetAdminInfoUseCase.QUERY, AdminInfoResponse::class.java, requestParams)
                graphqlRepository.getReseponse(listOf(request)).let { response ->
                    response.getError(AdminInfoResponse::class.java).let { errors ->
                        if (errors.isNullOrEmpty()) {
                            response.getData<AdminInfoResponse>(AdminInfoResponse::class.java).response.data.firstOrNull()?.let { adminData ->
                                return updateUserSessionAndReturnDataData(adminData)
                            }
                        }
                    }
                }
            } catch (ex: Exception) {
                // NO OP. if fails, will return pair of true and null
            }
        }
        return Pair(true, null)
    }

    private fun updateUserSessionAndReturnDataData(adminData: AdminData): Pair<Boolean, String?> {
        adminData.detail.roleType.let {
            userSession.run {
                setIsShopOwner(it.isShopOwner)
                setIsLocationAdmin(it.isLocationAdmin)
                setIsShopAdmin(it.isShopAdmin)
                setIsMultiLocationShop((adminData.locations.count() > 1))
            }
            return if (it.isLocationAdmin) {
                removeUnnecessaryShopData()
                Pair(false, null)
            } else {
                Pair(true, adminData.adminTypeText)
            }
        }
    }

    private fun removeUnnecessaryShopData() {
        userSession.run {
            shopAvatar = null
            shopId = null
            shopName = null
        }
    }
}