package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.AdminConfirmationRegResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.AdminConfirmationRegQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.AdminConfirmationRegUiModel
import javax.inject.Inject

class AdminConfirmationRegUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<AdminConfirmationRegResponse>,
    private val mapper: AdminInvitationConfirmationMapper
) {

    init {
        useCase.setGraphqlQuery(AdminConfirmationRegQuery)
        useCase.setTypeClass(AdminConfirmationRegResponse::class.java)
    }

    suspend fun execute(
        shopID: String,
        userId: String,
        acceptBecomeAdmin: Boolean,
        manageID: String
    ): AdminConfirmationRegUiModel {
        useCase.setRequestParams(
            AdminConfirmationRegQuery.createRequestParams(
                shopID,
                userId,
                acceptBecomeAdmin,
                manageID
            )
        )
        return mapper.mapToAdminConfirmationRegUiModel(useCase.executeOnBackground())
    }
}