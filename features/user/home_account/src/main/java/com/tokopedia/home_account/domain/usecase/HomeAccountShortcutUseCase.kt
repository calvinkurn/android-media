package com.tokopedia.home_account.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountErrorHandler
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountShortcutUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): GraphqlUseCase<ShortcutResponse>(graphqlRepository) {

    fun executeUseCase(onSuccess: (ShortcutResponse) -> Unit, onError: (Throwable) -> Unit){
        rawQueries[AccountConstants.Query.QUERY_USER_REWARDSHORCUT]?.let { query ->
            setTypeClass(ShortcutResponse::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it)
            }, onError)
        }
    }

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
                AccountErrorHandler.logDataNull("Account_GetShortcutDataUseCase",
                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
            }
            return data
        }
    }

}