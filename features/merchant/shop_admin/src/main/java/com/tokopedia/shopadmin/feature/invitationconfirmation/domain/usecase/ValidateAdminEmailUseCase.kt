package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.ValidateAdminEmailResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.ValidateAdminEmailQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import javax.inject.Inject

class ValidateAdminEmailUseCase @Inject constructor(
     val useCase: GraphqlUseCase<ValidateAdminEmailResponse>,
    private val mapper: AdminInvitationConfirmationMapper
) {

    init {
        useCase.setGraphqlQuery(ValidateAdminEmailQuery)
        useCase.setTypeClass(ValidateAdminEmailResponse::class.java)
    }

    suspend fun execute(shopID: String, email: String, manageID: String): ValidateAdminEmailUiModel {
        useCase.setRequestParams(ValidateAdminEmailQuery.createRequestParams(shopID, email, manageID))
        return mapper.mapToValidateAdminUiModel(useCase.executeOnBackground())
    }
}