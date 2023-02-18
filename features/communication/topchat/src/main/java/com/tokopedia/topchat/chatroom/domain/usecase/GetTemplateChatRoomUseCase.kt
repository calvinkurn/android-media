package com.tokopedia.topchat.chatroom.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chattemplate.view.uimodel.GetTemplateResultModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author : Steven 03/01/19
 */
open class GetTemplateChatRoomUseCase @Inject constructor(
    private val dispatcher: CoroutineDispatchers
) {

    open suspend fun getTemplateChat(isSeller: Boolean): GetTemplateResultModel {
        return withContext(dispatcher.io) {
            val params = generateParam(isSeller)
//            val response = api.getTemplateSuspend(params)
//            mapper.map(response)
            GetTemplateResultModel()
        }
    }

    private fun generateParam(isSeller: Boolean): HashMap<String, Any> {
        return hashMapOf(
            PARAM_IS_SELLER to isSeller
        )
    }

    companion object {
        private const val PARAM_IS_SELLER: String = "is_seller"
    }
}
