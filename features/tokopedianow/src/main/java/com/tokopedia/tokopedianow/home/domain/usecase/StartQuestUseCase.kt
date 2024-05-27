package com.tokopedia.tokopedianow.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokopedianow.home.domain.model.StartQuestResponse
import com.tokopedia.tokopedianow.home.domain.query.StartQuest
import com.tokopedia.tokopedianow.home.domain.query.StartQuest.QUEST_ID_PARAM
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class StartQuestUseCase @Inject constructor(gqlRepository: GraphqlRepository) {

    companion object {
        private const val SUCCESS_CODE = "200"
    }

    private val graphql by lazy { GraphqlUseCase<StartQuestResponse>(gqlRepository) }

    init {
        graphql.apply {
            setTypeClass(StartQuestResponse::class.java)
            setGraphqlQuery(StartQuest)
        }
    }

    suspend fun execute(questId: Int): StartQuestResponse {
        graphql.setRequestParams(RequestParams().apply {
            putInt(QUEST_ID_PARAM, questId)
        }.parameters)

        val startQuest = graphql.executeOnBackground()

        return if(startQuest.response.resultStatus.code == SUCCESS_CODE) {
            startQuest
        } else {
            throw MessageErrorException()
        }
    }
}
