package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokopedianow.home.domain.model.GetQuestListResponse
import com.tokopedia.tokopedianow.home.domain.query.GetQuestList
import javax.inject.Inject

class GetQuestListUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<GetQuestListResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(GetQuestList.QUERY)
        setTypeClass(GetQuestListResponse::class.java)
    }

    suspend fun execute(): GetQuestListResponse {
        return executeOnBackground()
    }
}