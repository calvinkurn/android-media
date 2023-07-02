package com.tokopedia.emoney.domain.usecase

import com.tokopedia.emoney.domain.query.UpdateBalanceEmoneyDKIJakcard
import com.tokopedia.emoney.domain.request.JakCardBodyEnc
import com.tokopedia.emoney.domain.request.JakCardRequest
import com.tokopedia.emoney.domain.response.JakCardResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetJakCardUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
): GraphqlUseCase<JakCardResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(UpdateBalanceEmoneyDKIJakcard())
        setTypeClass(JakCardResponse::class.java)
    }

    suspend fun execute(params: JakCardRequest): JakCardResponse {
        setRequestParams(mapParams(params))
        return executeOnBackground()
    }

    private fun mapBody(params: JakCardBodyEnc): HashMap<String, Any> {
        val mapBody = HashMap<String, Any>()
        mapBody[ENC_KEY] = params.encKey
        mapBody[ENC_PAYLOAD] = params.encPayload
        return mapBody
    }

    private fun mapParams(params: JakCardRequest): HashMap<String, Any> {
        val mapParams = HashMap<String, Any>()
        mapParams[ENC_BODY] = mapBody(params.body)
        return mapParams
    }

    companion object {
        private const val ENC_KEY = "encKey"
        private const val ENC_PAYLOAD = "encPayload"
        private const val ENC_BODY = "body"
    }
}
