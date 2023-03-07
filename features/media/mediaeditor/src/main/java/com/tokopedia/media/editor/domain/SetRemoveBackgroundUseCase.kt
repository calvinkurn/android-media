package com.tokopedia.media.editor.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.media.editor.data.repository.RemoveBackgroundRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class SetRemoveBackgroundUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val repository: RemoveBackgroundRepository
) : FlowUseCase<String, File?>(dispatchers.io) {

    override suspend fun execute(params: String): Flow<File?> {
        return repository(params)
    }

    // since we not use graphql service, then marks this as no-op.
    override fun graphqlQuery() = "" //no-op

}