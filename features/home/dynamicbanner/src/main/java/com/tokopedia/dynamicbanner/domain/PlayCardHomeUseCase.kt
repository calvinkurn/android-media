package com.tokopedia.dynamicbanner.domain

import com.tokopedia.dynamicbanner.QUERY_PLAY_CARD
import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class PlayCardHomeUseCase @Inject constructor(
        @Named(QUERY_PLAY_CARD) val query: String,
        private val graphqlUseCase: MultiRequestGraphqlUseCase
): UseCase<PlayCardHome>() {

    override suspend fun executeOnBackground(): PlayCardHome {
        val request = GraphqlRequest(query, PlayCardHome::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(request)

        val response = graphqlUseCase.executeOnBackground()
        return response.getData<PlayCardHome>(PlayCardHome::class.java)
    }

}