package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.mediauploader.domain.UploaderUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.ChatImageServerUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.TopchatUploadImageUseCase
import javax.inject.Inject

class TopchatUploadImageUseCaseStub @Inject constructor(
        uploadImageUseCase: UploaderUseCase,
        chatImageServerUseCase: ChatImageServerUseCase,
        dispatchers: CoroutineDispatchers
): TopchatUploadImageUseCase(uploadImageUseCase, chatImageServerUseCase, dispatchers) {

    override fun upload(
            image: ImageUploadViewModel,
            onSuccess: (String, ImageUploadViewModel) -> Unit,
            onError: (Throwable, ImageUploadViewModel) -> Unit
    ) {
        onSuccess.invoke("DummyUploadId", image)
    }
}