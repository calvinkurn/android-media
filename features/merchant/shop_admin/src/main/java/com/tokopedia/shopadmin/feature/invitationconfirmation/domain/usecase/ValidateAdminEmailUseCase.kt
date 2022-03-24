package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.ValidateAdminEmailResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.ValidateAdminEmailQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.ValidateEmailUiModel
import javax.inject.Inject

class ValidateAdminEmailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<ValidateAdminEmailResponse>,
    private val adminInvitationConfirmationMapper: AdminInvitationConfirmationMapper
) {
    init {
        useCase.setGraphqlQuery(ValidateAdminEmailQuery)
        useCase.setTypeClass(ValidateAdminEmailResponse::class.java)
    }

    suspend fun execute(shopID: String, email: String, manageID: String): ValidateEmailUiModel {
        useCase.setRequestParams(ValidateAdminEmailQuery.createRequestParams(shopID, email, manageID))
        return adminInvitationConfirmationMapper.mapToValidateAdminUiModel(useCase.executeOnBackground().validateAdminEmail)
    }
}