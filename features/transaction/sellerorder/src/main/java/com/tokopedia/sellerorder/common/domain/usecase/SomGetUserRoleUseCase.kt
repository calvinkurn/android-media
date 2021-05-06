package com.tokopedia.sellerorder.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.domain.mapper.SomUserRoleMapper
import com.tokopedia.sellerorder.common.domain.model.SomGetUserRoleResponse
import com.tokopedia.sellerorder.common.presenter.model.SomGetUserRoleUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class SomGetUserRoleUseCase @Inject constructor(
        private val useCase: GraphqlUseCase<SomGetUserRoleResponse>,
        private val mapper: SomUserRoleMapper) {

    init {
        useCase.setGraphqlQuery(QUERY)
        useCase.setTypeClass(SomGetUserRoleResponse::class.java)
    }

    suspend fun execute(): SomGetUserRoleUiModel = mapper.mapDomainToUiModel(useCase.executeOnBackground().goldGetUserShopInfo?.data)

    fun setUserId(userId: Int) {
        useCase.setRequestParams(mapOf(USER_ID to userId))
    }

    companion object {
        private const val QUERY = "query GetUserRole(\$userID: Int!){\n" +
                "  GoldGetUserShopInfo(userID:\$userID) {\n" +
                "    Data {\n" +
                "      Roles\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val USER_ID = "userID"
    }
}