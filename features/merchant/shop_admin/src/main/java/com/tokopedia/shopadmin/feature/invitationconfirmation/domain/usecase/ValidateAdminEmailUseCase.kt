package com.tokopedia.shopadmin.feature.invitationconfirmation.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.mapper.AdminInvitationConfirmationMapper
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.model.ValidateAdminEmailResponse
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.query.VALIDATE_ADMIN_EMAIL_QUERY
import com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.uimodel.ValidateAdminEmailUiModel
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject


@GqlQuery("ValidateAdminEmailQuery", VALIDATE_ADMIN_EMAIL_QUERY)
class ValidateAdminEmailUseCase @Inject constructor(
    private val useCase: GraphqlUseCase<ValidateAdminEmailResponse>,
    private val mapper: AdminInvitationConfirmationMapper
) {

    init {
        useCase.setGraphqlQuery(ValidateAdminEmailQuery())
        useCase.setTypeClass(ValidateAdminEmailResponse::class.java)
    }

    suspend fun execute(
        shopID: String,
        email: String,
        manageID: String
    ): ValidateAdminEmailUiModel {
        useCase.setRequestParams(
            createRequestParams(
                shopID,
                email,
                manageID
            )
        )
        return mapper.mapToValidateAdminUiModel(useCase.executeOnBackground())
    }

    private fun createRequestParams(shopID: String, email: String, manageID: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(SHOP_ID_KEY, shopID)
            putString(EMAIL_KEY, email)
            putString(MANAGE_ID_KEY, manageID)
        }.parameters
    }

    companion object {
        private const val SHOP_ID_KEY = "shopID"
        private const val MANAGE_ID_KEY = "manageID"
        private const val EMAIL_KEY = "email"
    }
}