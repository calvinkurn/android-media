package com.tokopedia.contactus.inboxtickets.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.contactus.inboxtickets.data.gqlqueries.CHIP_UPLOAD_HOST_GQL
import com.tokopedia.contactus.inboxtickets.data.model.ChipUploadHostConfig
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class ChipUploadHostConfigUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<Unit, ChipUploadHostConfig>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return CHIP_UPLOAD_HOST_GQL
    }

    override suspend fun execute(params: Unit): ChipUploadHostConfig {
        return repository.request(graphqlQuery(), params)
    }
}
