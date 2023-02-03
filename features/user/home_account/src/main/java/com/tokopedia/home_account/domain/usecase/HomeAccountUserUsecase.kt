package com.tokopedia.home_account.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.data.model.UserAccountDataModel
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2019-11-08.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

open class HomeAccountUserUsecase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<Unit, UserAccountDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String =
        """
             query {
               status
               profile(skipCache: true) {
                 name
                 full_name
                 completion
                 user_id
                 profilePicture
                 phone_verified
               }
               openDebitSettings {
                   data {
                     redirectURL
                   }
                }
             }
        """.trimIndent()

    override suspend fun execute(params: Unit): UserAccountDataModel {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}
