package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.GetAdminInfoQuery
import javax.inject.Inject

class ValidateAdminEmailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminInfoResponse>,
    private val adminInvitationConfirmationMapper: AdminInvitationConfirmationMapper
) {
    init {
        useCase.setGraphqlQuery(GetAdminInfoQuery)
        useCase.setTypeClass(GetAdminInfoResponse::class.java)
    }

    suspend fun execute() {

    }
}