package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetReferralReceiverHomeResponse
import com.tokopedia.tokopedianow.home.domain.query.GetReferralReceiverHome
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetReferralReceiverHomeUseCase @Inject constructor(graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<GetReferralReceiverHomeResponse>(graphqlRepository) }

    suspend fun execute(slug: String): GetReferralReceiverHomeResponse {
        graphql.apply {
            setTypeClass(GetReferralReceiverHomeResponse::class.java)
            setGraphqlQuery(GetReferralReceiverHome)

            setRequestParams(RequestParams.create().apply {
                putString(GetReferralReceiverHome.PARAM_SLUG, slug)
            }.parameters)
        }

        return graphql.executeOnBackground()
    }
}