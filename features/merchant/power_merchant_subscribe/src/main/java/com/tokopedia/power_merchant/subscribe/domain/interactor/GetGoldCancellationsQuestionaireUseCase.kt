package com.tokopedia.power_merchant.subscribe.domain.interactor

import com.tokopedia.gm.common.constant.GMParamConstant.RAW_GM_QUESTIONNAIRE_QUESTION
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Named

class GetGoldCancellationsQuestionaireUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase,
        @Named(RAW_GM_QUESTIONNAIRE_QUESTION) private val rawQuery: String
) : UseCase<GoldCancellationsQuestionaire>() {

    override fun createObservable(requestParams: RequestParams): Observable<GoldCancellationsQuestionaire> {
        val graphqlRequest = GraphqlRequest(rawQuery, GoldCancellationsQuestionaire::class.java)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        return graphqlUseCase.createObservable(requestParams).map {
            val data: GoldCancellationsQuestionaire? = it.getData(GoldCancellationsQuestionaire::class.java)
            val error: List<GraphqlError> = it.getError(GraphqlError::class.java) ?: listOf()
            if (data == null) {
                throw RuntimeException()
            } else if (error.isNotEmpty() && error.first().message.isNotEmpty()) {
                throw MessageErrorException(error.first().message)
            } else if (data.result.header.errorCode != "" && data.result.header.messages.isNotEmpty()) {
                throw MessageErrorException(data.result.header.messages.first())
            }

            data
        }
    }

}