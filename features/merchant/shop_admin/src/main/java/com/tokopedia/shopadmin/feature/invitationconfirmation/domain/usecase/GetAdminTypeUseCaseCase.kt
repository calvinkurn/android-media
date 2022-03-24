package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.GetAdminInfoResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.GetAdminTypeQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.model.AdminTypeUiModel
import javax.inject.Inject

class GetAdminTypeUseCaseCase @Inject constructor(
    private val useCase: GraphqlUseCase<GetAdminInfoResponse>,
    private val adminInvitationConfirmationMapper: AdminInvitationConfirmationMapper
) {
    init {
        useCase.setGraphqlQuery(GetAdminTypeQuery)
        useCase.setTypeClass(GetAdminInfoResponse::class.java)
    }

    suspend fun execute(): AdminTypeUiModel {
        useCase.setRequestParams(GetAdminTypeQuery.createRequestParams())
        return adminInvitationConfirmationMapper.mapToAdminTypeUiModel(useCase.executeOnBackground())
    }
}