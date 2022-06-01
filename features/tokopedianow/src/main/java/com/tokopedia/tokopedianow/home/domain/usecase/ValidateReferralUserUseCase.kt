package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.ValidateReferralUserResponse
import com.tokopedia.tokopedianow.home.domain.query.ValidateReferralUser
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class ValidateReferralUserUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ValidateReferralUserResponse>(graphqlRepository) {

    companion object {
        const val SLUG = "slug"
    }

    init {
        setTypeClass(ValidateReferralUserResponse::class.java)
        setGraphqlQuery(ValidateReferralUser.QUERY)
    }

    suspend fun execute(slug: String): ValidateReferralUserResponse {
        setRequestParams(RequestParams.create().apply {
            putString(SLUG, slug)
        }.parameters)
        return executeOnBackground()
    }

}