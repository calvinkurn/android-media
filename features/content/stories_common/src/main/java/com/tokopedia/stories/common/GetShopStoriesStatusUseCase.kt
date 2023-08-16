package com.tokopedia.stories.common

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 16/08/23
 */
class GetShopStoriesStatusUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<Unit, Unit>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: Unit) {
        return graphqlRepository.request(graphqlQuery(), params)
    }
}
