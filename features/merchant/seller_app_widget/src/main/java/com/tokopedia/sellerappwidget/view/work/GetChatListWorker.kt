package com.tokopedia.sellerappwidget.view.work

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.work.*
import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.domain.mapper.ChatMapper
import com.tokopedia.sellerappwidget.domain.usecase.GetChatUseCase
import com.tokopedia.sellerappwidget.view.appwidget.ChatAppWidget
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.state.chat.ChatWidgetLoadingState
import com.tokopedia.sellerappwidget.view.state.chat.ChatWidgetStateHelper
import com.tokopedia.sellerappwidget.view.viewmodel.ChatAppWidgetViewModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 01/12/20
 */

class GetChatListWorker(
        private val context: Context,
        workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), AppWidgetView<List<ChatUiModel>> {

    private val getChatUseCase by lazy {
        val gqlRepository = GraphqlInteractor.getInstance()
        val chatMapper = ChatMapper()
        GetChatUseCase(gqlRepository.graphqlRepository, chatMapper)
    }
    private val chatViewModel: ChatAppWidgetViewModel by lazy {
        ChatAppWidgetViewModel(getChatUseCase)
    }

    override suspend fun doWork(): Result {
        chatViewModel.bindView(this)
        showLoadingState()
        chatViewModel.getChatList()
        return Result.success()
    }

    override fun onSuccessGetOrderList(result: Success<List<ChatUiModel>>) {
        println("AppWidget : ${Gson().toJsonTree(result.data)}")
        SellerAppWidgetPreferences.getInstance(applicationContext)
                .putLong(Const.SharedPrefKey.CHAT_LAST_UPDATED, System.currentTimeMillis())
        ChatAppWidget.setOnSuccess(context, result.data)
    }

    override fun onFailedGetOrderList(fail: Fail) {
        Timber.e(fail.throwable)
        ChatAppWidget.setOnError(context)
    }

    private fun showLoadingState() {
        val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(context)
        val awm = AppWidgetManager.getInstance(applicationContext)
        val ids = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(context, awm)
        ids.forEach {
            ChatWidgetStateHelper.updateViewOnLoading(remoteViews)
            ChatWidgetLoadingState.setupLoadingState(awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }
    }
}

object GetChatListWorkerExecutor {

    private const val TAG_WORKER = "get_chat_worker"
    private const val REPEAT_INTERVAL = 15L
    private var workRequest: PeriodicWorkRequest? = null

    @JvmStatic
    fun runPeriodicWork(context: Context) {
        if (workRequest == null) {
            val constraints = Constraints.Builder()
                    .setRequiresDeviceIdle(false)
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

            workRequest = PeriodicWorkRequestBuilder<GetChatListWorker>(REPEAT_INTERVAL, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .setInitialDelay(0, TimeUnit.MINUTES)
                    .build()
        }

        workRequest?.let {
            WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork(TAG_WORKER, ExistingPeriodicWorkPolicy.REPLACE, it)
        }
    }
}