package com.tokopedia.loginregister.shopcreation.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.shopcreation.domain.pojo.GetUserProfileCompletionPojo
import com.tokopedia.loginregister.shopcreation.domain.query.QueryUserProfileCompletion
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

class GetUserProfileCompletionUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetUserProfileCompletionPojo>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return QueryUserProfileCompletion.getQuery()
    }

    override suspend fun execute(params: Unit): GetUserProfileCompletionPojo {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}