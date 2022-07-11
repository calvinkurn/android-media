package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.UserProfileUpdateResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.USER_PROFILE_UPDATE_QUERY
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.UserProfileUpdateUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

@GqlQuery("UpdateUserProfileQuery", USER_PROFILE_UPDATE_QUERY)
class UpdateUserProfileUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<UserProfileUpdateResponse>,
    private val mapper: AdminInvitationConfirmationMapper
) {

    init {
        useCase.setGraphqlQuery(UpdateUserProfileQuery())
        useCase.setTypeClass(UserProfileUpdateResponse::class.java)
    }

    suspend fun execute(
        email: String
    ): UserProfileUpdateUiModel {
        useCase.setRequestParams(createRequestParams(email))
        return mapper.mapToUserProfileUpdateUiModel(useCase.executeOnBackground().data)
    }

    private fun createRequestParams(email: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(EMAIL_KEY, email)
        }.parameters
    }

    companion object {
        private const val EMAIL_KEY = "email"
    }
}