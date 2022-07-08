package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.AdminConfirmationRegResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.ADMIN_CONFIRMATION_REG_QUERY
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("AdminConfirmationRegQuery", ADMIN_CONFIRMATION_REG_QUERY)
class AdminConfirmationRegUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<AdminConfirmationRegResponse>,
    private val mapper: AdminInvitationConfirmationMapper
) {

    init {
        useCase.setGraphqlQuery(AdminConfirmationRegQuery())
        useCase.setTypeClass(AdminConfirmationRegResponse::class.java)
    }

    suspend fun execute(
        shopID: String,
        userId: String,
        acceptBecomeAdmin: Boolean,
        manageID: String
    ): AdminConfirmationRegUiModel {
        useCase.setRequestParams(
            createRequestParams(
                shopID,
                userId,
                acceptBecomeAdmin,
                manageID
            )
        )
        return mapper.mapToAdminConfirmationRegUiModel(useCase.executeOnBackground().adminConfirmationReg)
    }

    private fun createRequestParams(shopID: String, userId: String, acceptBecomeAdmin: Boolean, shopManageId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SOURCE_KEY, SOURCE)
            putString(SHOP_ID_KEY, shopID)
            putString(USER_ID_KEY, userId)
            putString(SHOP_MANAGE_ID, shopManageId)
            putBoolean(ACCEPT_BECOME_ADMIN_KEY, acceptBecomeAdmin)
        }.parameters
    }

    companion object {
        private const val SOURCE = "adminConfirmationReg-android"
        private const val SOURCE_KEY = "source"
        private const val SHOP_ID_KEY = "shopID"
        private const val USER_ID_KEY = "userId"
        private const val ACCEPT_BECOME_ADMIN_KEY = "acceptBecomeAdmin"
        private const val SHOP_MANAGE_ID = "shopManageId"
    }
}