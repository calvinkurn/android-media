package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.AccountErrorHandler
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.ShortcutResponse
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

open class HomeAccountShortcutUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatcher,
    private val rawQueries: Map<String, String>
): CoroutineUseCase<Unit, ShortcutResponse>(dispatcher) {

    override fun graphqlQuery(): String {
        return rawQueries[AccountConstants.Query.QUERY_USER_REWARDSHORCUT] ?: ""
    }

    override suspend fun execute(params: Unit): ShortcutResponse {
        val gqlRequest = GraphqlRequest(graphqlQuery(),
                ShortcutResponse::class.java, mapOf<String, Any>())
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
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