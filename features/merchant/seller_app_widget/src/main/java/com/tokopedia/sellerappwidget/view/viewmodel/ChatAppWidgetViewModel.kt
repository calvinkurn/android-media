package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.usecase.GetChatUseCase
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 01/12/20
 */

@AppWidgetScope
class ChatAppWidgetViewModel @Inject constructor(
        private val getChatUseCase: Lazy<GetChatUseCase>,
        private val dispatchers: CoroutineDispatchers
) : BaseAppWidgetVM<AppWidgetView<List<ChatUiModel>>>(dispatchers) {

    fun getChatList() {
        launchCatchError(block = {
            getChatUseCase.get().params = GetChatUseCase.creteParams()
            val result = Success(withContext(dispatchers.io) {
                return@withContext getChatUseCase.get().executeOnBackground()
            })
            view?.onSuccessGetOrderList(result)
        }, onError = {
            view?.onFailedGetOrderList(Fail(it))
        })
    }
}