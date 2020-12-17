package com.tokopedia.home.account.revamp.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler
import com.tokopedia.home.account.revamp.Utils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShortcutDataUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): UseCase<ShortcutResponse>() {

    override suspend fun executeOnBackground(): ShortcutResponse {
        val rawQuery = rawQueries[AccountConstants.Query.QUERY_USER_REWARDSHORCUT]
        val gqlRequest = GraphqlRequest(rawQuery,
                ShortcutResponse::class.java, mapOf<String, Any>())
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(ShortcutResponse::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            var data: ShortcutResponse? = gqlResponse.getData(ShortcutResponse::class.java)
            if(data == null) {
                val mapResponse = Utils.convertResponseToJson(gqlResponse)
                data = ShortcutResponse()
                AccountHomeErrorHandler.logDataNull("GetShortcutDataUseCase",
                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
            }
            return data
        }
    }

}