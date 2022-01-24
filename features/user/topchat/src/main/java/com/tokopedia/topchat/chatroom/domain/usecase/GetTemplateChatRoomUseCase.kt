package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.topchat.chatroom.data.api.ChatRoomApi
import com.tokopedia.topchat.chatroom.domain.mapper.GetTemplateChatRoomMapper
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import javax.inject.Inject


/**
 * @author : Steven 03/01/19
 */
open class GetTemplateChatRoomUseCase @Inject constructor(
        val api: ChatRoomApi,
        val mapper: GetTemplateChatRoomMapper
) : UseCase<GetTemplateResultModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<GetTemplateResultModel> {
        return api.getTemplate(requestParams.parameters).map(mapper)
    }

    companion object {

        private const val PARAM_IS_SELLER: String = "is_seller"

        fun generateParam(isSeller: Boolean): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putBoolean(PARAM_IS_SELLER, isSeller)
            return requestParams
        }
    }

}