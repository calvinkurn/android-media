package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerappwidget.coroutine.AppWidgetDispatcherProviderImpl
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.usecase.GetChatUseCase
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 01/12/20
 */

@AppWidgetScope
class ChatAppWidgetViewModel @Inject constructor(
        private val getChatUseCase: GetChatUseCase
) : BaseAppWidgetVM<AppWidgetView<List<ChatUiModel>>>(AppWidgetDispatcherProviderImpl()) {

    fun getChatList() {
        launchCatchError(block = {
            getChatUseCase.params = GetChatUseCase.creteParams()
            val result = Success(withContext(dispatcherProvider.io) {
                return@withContext getChatUseCase.executeOnBackground()
            })
            view?.onSuccessGetOrderList(result)
        }, onError = {
            view?.onFailedGetOrderList(Fail(it))
        })
    }
}