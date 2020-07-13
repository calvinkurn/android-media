package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.domain.model.SomGetUserRoleDataModel
import com.tokopedia.sellerorder.common.domain.model.SomGetUserRoleResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomGetUserRoleUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomGetUserRoleResponse>) {

    suspend fun execute(): Result<SomGetUserRoleDataModel> {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomGetUserRoleResponse::class.java)

        return try {
            val result = useCase.executeOnBackground().goldGetUserShopInfo?.data ?: SomGetUserRoleDataModel()
            Success(result)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun setUserId(userId: Int) {
        useCase.setRequestParams(mapOf(USER_ID to userId))
    }

    companion object {
        private const val QUERY = "query UserRole(\$userID: Int!){\n" +
                "  GoldGetUserShopInfo(userID:\$userID) {\n" +
                "    Data {\n" +
                "      Roles\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val USER_ID = "userID"
    }
}