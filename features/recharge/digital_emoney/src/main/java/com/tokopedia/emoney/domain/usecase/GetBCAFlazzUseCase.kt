package com.tokopedia.emoney.domain.usecase

import com.tokopedia.emoney.domain.query.UpdateBalanceBCAFlazz
import com.tokopedia.emoney.domain.request.BCAFlazzRequest
import com.tokopedia.emoney.domain.request.CommonBodyEnc
import com.tokopedia.emoney.domain.response.BCAFlazzResponse
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import javax.inject.Inject

class GetBCAFlazzUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<BCAFlazzResponse>(graphqlRepository) {

    init {
        setGraphqlQuery(UpdateBalanceBCAFlazz())
        setTypeClass(BCAFlazzResponse::class.java)
    }

    suspend fun execute(params: BCAFlazzRequest): BCAFlazzResponse {
        setRequestParams(mapParams(params))
        return executeOnBackground()
    }

    private fun mapBody(params: CommonBodyEnc): HashMap<String, Any> {
        val mapBody = HashMap<String, Any>()
        mapBody[ENC_KEY] = params.encKey
        mapBody[ENC_PAYLOAD] = params.encPayload
        return mapBody
    }

    private fun mapParams(params: BCAFlazzRequest): HashMap<String, Any> {
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
