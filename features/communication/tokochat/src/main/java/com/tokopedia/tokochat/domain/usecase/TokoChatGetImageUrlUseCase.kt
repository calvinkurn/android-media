package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import javax.inject.Inject

class TokoChatGetImageUrlUseCase @Inject constructor(
    private val repository: TokoChatRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<String, TokoChatImageResult>(dispatchers.io) {

    // Not used
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: String): TokoChatImageResult {
        return repository.getImageUrl(imageId = params)
    }
}
