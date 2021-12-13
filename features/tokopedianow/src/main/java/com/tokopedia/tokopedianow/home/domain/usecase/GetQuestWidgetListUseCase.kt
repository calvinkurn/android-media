package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetQuestListResponse
import com.tokopedia.tokopedianow.home.domain.query.GetQuestList
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetQuestWidgetListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetQuestListResponse>(graphqlRepository) {

    companion object {
        const val CHANNEL_SLUG = "channelSlug"
    }

    init {
        setGraphqlQuery(GetQuestList.QUERY)
        setTypeClass(GetQuestListResponse::class.java)
    }

    suspend fun execute(channelSlug: String = "tokonow-main-quest"): GetQuestListResponse {
        setRequestParams(RequestParams.create().apply {
            putString(CHANNEL_SLUG, channelSlug)
        }.parameters)
        return executeOnBackground()
    }
}