package com.tokopedia.sellerhome.settings.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhome.settings.domain.entity.OthersBalance
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class BalanceInfoUseCase @Inject constructor(private val graphQlRepository: GraphqlRepository) : UseCase<OthersBalance>() {

    companion object {
        const val QUERY = "query BalanceInfo {\n" +
                "  balance {\n" +
                "    seller_usable\n" +
                "    buyer_usable\n" +
                "  }\n" +
                "}"
    }

    private val params = HashMap<String, Any>()

    override suspend fun executeOnBackground(): OthersBalance {
        val gqlRequest = GraphqlRequest(QUERY, OthersBalance::class.java, params)
        val gqlResponse = graphQlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(OthersBalance::class.java)
        if (errors.isNullOrEmpty()) {
            return gqlResponse.getData(OthersBalance::class.java)
        } else throw MessageErrorException(errors.firstOrNull()?.message)
    }
}