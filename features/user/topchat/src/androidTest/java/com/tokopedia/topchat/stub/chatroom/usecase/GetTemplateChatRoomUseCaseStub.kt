package com.tokopedia.topchat.stub.chatroom.usecase

import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chatroom.domain.usecase.GetTemplateChatRoomUseCase
import com.tokopedia.topchat.chattemplate.domain.pojo.TemplateData
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import com.tokopedia.topchat.stub.chatroom.usecase.api.ChatRoomApiStub
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import javax.inject.Inject

class GetTemplateChatRoomUseCaseStub @Inject constructor(
        private val apiStub: ChatRoomApiStub,
        mapper: GetTemplateChatRoomMapper
) : GetTemplateChatRoomUseCase(
        apiStub,
        mapper
) {

    var response: TemplateData = TemplateData()
        set(value) {
            apiStub.templateResponse = value
            field = value
        }

    override fun execute(
            requestParams: RequestParams?,
            subscriber: Subscriber<GetTemplateUiModel>?
    ) {
        createObservable(requestParams!!).subscribe(subscriber)
    }
}