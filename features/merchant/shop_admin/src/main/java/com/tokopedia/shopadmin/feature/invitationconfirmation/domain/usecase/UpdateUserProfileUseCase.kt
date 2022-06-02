package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.UserProfileUpdateResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.UpdateUserProfileQuery
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<UserProfileUpdateResponse>,
    private val mapper: AdminInvitationConfirmationMapper
) {

    init {
        useCase.setGraphqlQuery(UpdateUserProfileQuery)
        useCase.setTypeClass(UserProfileUpdateResponse::class.java)
    }

    suspend fun execute(
        email: String
    ): UserProfileUpdateUiModel {
        useCase.setRequestParams(UpdateUserProfileQuery.createRequestParams(email))
        return mapper.mapToUserProfileUpdateUiModel(useCase.executeOnBackground().data)
    }
}