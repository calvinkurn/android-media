package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetQuestWidgetListResponse
import com.tokopedia.tokopedianow.home.domain.query.GetQuestWidgetList
import javax.inject.Inject

class GetQuestWidgetListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetQuestWidgetListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetQuestWidgetList.QUERY)
        setTypeClass(GetQuestWidgetListResponse::class.java)
    }

    suspend fun execute(): GetQuestWidgetListResponse {
        return executeOnBackground()
    }
}