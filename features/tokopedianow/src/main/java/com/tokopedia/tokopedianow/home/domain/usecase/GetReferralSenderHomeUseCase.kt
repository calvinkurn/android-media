package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetReferralSenderHomeResponse
import com.tokopedia.tokopedianow.home.domain.query.GetReferralSenderHome
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetReferralSenderHomeUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetReferralSenderHomeResponse>(graphqlRepository) {

    companion object {
        const val SLUG = "slug"
    }

    init {
        setTypeClass(GetReferralSenderHomeResponse::class.java)
        setGraphqlQuery(GetReferralSenderHome.QUERY)
    }

    suspend fun execute(slug: String): GetReferralSenderHomeResponse {
        setRequestParams(RequestParams.create().apply {
            putString(SLUG, slug)
        }.parameters)
        return executeOnBackground()
    }

}