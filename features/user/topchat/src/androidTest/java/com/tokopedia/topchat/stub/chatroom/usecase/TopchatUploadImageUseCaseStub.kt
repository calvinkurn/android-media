package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.ChatImageServerUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.TopchatUploadImageUseCase
import javax.inject.Inject

class TopchatUploadImageUseCaseStub @Inject constructor(
        uploadImageUseCase: UploaderUseCase,
        chatImageServerUseCase: ChatImageServerUseCase,
        dispatchers: CoroutineDispatchers
): TopchatUploadImageUseCase(uploadImageUseCase, chatImageServerUseCase, dispatchers) {

    var isError: Boolean = false

    override fun upload(
        image: ImageUploadUiModel,
        onSuccess: (String, ImageUploadUiModel) -> Unit,
        onError: (Throwable, ImageUploadUiModel) -> Unit
    ) {
        if (!isError) {
            onSuccess.invoke("DummyUploadId", image)
        } else {
            onError.invoke(IllegalStateException("Some error"), image)
        }
    }
}