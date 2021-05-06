package com.tokopedia.sellerappwidget.view.executor

import android.appwidget.AppWidgetManager
import android.content.Context
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.domain.mapper.ChatMapper
import com.tokopedia.sellerappwidget.domain.usecase.GetChatUseCase
import com.tokopedia.sellerappwidget.view.appwidget.ChatAppWidget
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.state.chat.ChatWidgetLoadingState
import com.tokopedia.sellerappwidget.view.state.chat.ChatWidgetStateHelper
import com.tokopedia.sellerappwidget.view.viewmodel.ChatAppWidgetViewModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.sellerappwidget.view.work.GetChatWorker
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 04/12/20
 */

class GetChatExecutor(private val context: Context) : AppWidgetView<ChatUiModel> {

    companion object {
        private var INSTANCE: GetChatExecutor? = null

        fun run(context: Context, showLoadingState: Boolean = false) {
            if (INSTANCE == null) {
                INSTANCE = GetChatExecutor(context)
            }
            INSTANCE?.run(showLoadingState)
        }
    }

    private val viewModel by lazy {
        val getChatUseCase = GetChatUseCase(GraphqlInteractor.getInstance().graphqlRepository, ChatMapper())
        return@lazy ChatAppWidgetViewModel(getChatUseCase, CoroutineDispatchersProvider)
    }
    private val cacheHandler by lazy { AppWidgetHelper.getCacheHandler(context) }

    fun run(showLoadingState: Boolean) {
        if (showLoadingState) {
            showLoadingState()
        }
        viewModel.bindView(this)
        viewModel.getChatList()
    }

    override fun onSuccess(result: ChatUiModel) {
        cacheHandler.putLong(Const.SharedPrefKey.CHAT_LAST_UPDATED, System.currentTimeMillis())
        cacheHandler.applyEditor()
        ChatAppWidget.setOnSuccess(context, result)
        GetChatWorker.runWorkerPeriodically(context)
        viewModel.unbind()
    }

    override fun onError(t: Throwable) {
        ChatAppWidget.setOnError(context)
        GetChatWorker.runWorkerPeriodically(context)
        viewModel.unbind()
        Timber.e(t)
    }

    private fun showLoadingState() {
        val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(context)
        val awm = AppWidgetManager.getInstance(context)
        val ids = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)
        ids.forEach {
            ChatWidgetStateHelper.updateViewOnLoading(remoteViews)
            ChatWidgetLoadingState.setupLoadingState(context, awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }
    }
}