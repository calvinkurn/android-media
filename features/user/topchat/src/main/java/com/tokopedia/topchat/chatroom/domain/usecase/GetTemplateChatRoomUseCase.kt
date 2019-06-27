package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel
import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject


/**
 * @author : Steven 03/01/19
 */
class GetTemplateChatRoomUseCase @Inject constructor(val api: ChatRoomApi,
                                                     val mapper: GetTemplateChatRoomMapper)
    : UseCase<GetTemplateViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<GetTemplateViewModel> {
        return api.getTemplate(requestParams.parameters).map(mapper)
    }

}