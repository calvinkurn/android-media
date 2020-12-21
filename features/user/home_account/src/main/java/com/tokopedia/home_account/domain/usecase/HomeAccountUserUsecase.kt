package com.tokopedia.home_account.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.home_account.AccountConstants.Query.NEW_QUERY_BUYER_ACCOUNT_HOME
import com.tokopedia.home_account.AccountErrorHandler
import com.tokopedia.home_account.Utils
import com.tokopedia.home_account.data.model.UserAccountDataModel
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class HomeAccountUserUsecase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val rawQueries: Map<String, String>
): GraphqlUseCase<UserAccountDataModel>(graphqlRepository) {

    fun executeUseCase(onSuccess: (UserAccountDataModel) -> Unit, onError: (Throwable) -> Unit){
        rawQueries[NEW_QUERY_BUYER_ACCOUNT_HOME]?.let { query ->
            setTypeClass(UserAccountDataModel::class.java)
            setGraphqlQuery(query)
            execute({
                onSuccess(it)
            }, onError)
        }
    }
    override suspend fun executeOnBackground(): UserAccountDataModel {
        val rawQuery = rawQueries[NEW_QUERY_BUYER_ACCOUNT_HOME]
        val gqlRequest = GraphqlRequest(rawQuery,
                UserAccountDataModel::class.java, mapOf<String, Any>())
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        val errors = gqlResponse.getError(UserAccountDataModel::class.java)
        if (!errors.isNullOrEmpty()) {
            throw MessageErrorException(errors[0].message)
        } else {
            var data: UserAccountDataModel? = gqlResponse.getData(UserAccountDataModel::class.java)
            if (data == null) {
                val mapResponse = Utils.convertResponseToJson(gqlResponse)
                data = UserAccountDataModel()
                AccountErrorHandler.logDataNull("Account_DataUseCase",
                        Throwable("Results : ${mapResponse[Utils.M_RESULT]} - Errors : ${mapResponse[Utils.M_ERRORS]}"))
            }
            return data
        }
    }
}