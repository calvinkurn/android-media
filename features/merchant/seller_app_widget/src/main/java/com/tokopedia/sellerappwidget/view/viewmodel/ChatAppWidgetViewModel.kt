package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerappwidget.domain.usecase.GetChatUseCase
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import kotlinx.coroutines.withContext

/**
 * Created By @ilhamsuaib on 01/12/20
 */

class ChatAppWidgetViewModel(
        private val getChatUseCase: GetChatUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseAppWidgetVM<AppWidgetView<ChatUiModel>>(dispatchers) {

    fun getChatList() {
        launchCatchError(block = {
            getChatUseCase.params = GetChatUseCase.creteParams()
            val result = withContext(dispatchers.io) {
                return@withContext getChatUseCase.executeOnBackground()
            }
            view?.onSuccess(result)
        }, onError = {
            view?.onError(it)
        })
    }
}