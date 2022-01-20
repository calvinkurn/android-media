package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.topchat.chatroom.domain.usecase.CompressImageUseCase
import javax.inject.Inject

class CompressImageUseCaseStub @Inject constructor(
    dispatchers: CoroutineDispatchers
): CompressImageUseCase(dispatchers) {

    var compressedResult = "image"
    var isError = false

    override suspend fun compressImage(imageUrl: String): String {
        if (isError) {
            throw MessageErrorException("Oops!")
        } else {
            return compressedResult
        }
    }
}