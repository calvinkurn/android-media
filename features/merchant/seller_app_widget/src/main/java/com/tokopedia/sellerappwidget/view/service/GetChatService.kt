package com.tokopedia.sellerappwidget.view.service

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.di.DaggerAppWidgetComponent
import com.tokopedia.sellerappwidget.view.appwidget.ChatAppWidget
import com.tokopedia.sellerappwidget.view.model.ChatItemUiModel
import com.tokopedia.sellerappwidget.view.model.ChatUiModel
import com.tokopedia.sellerappwidget.view.state.chat.ChatWidgetLoadingState
import com.tokopedia.sellerappwidget.view.state.chat.ChatWidgetStateHelper
import com.tokopedia.sellerappwidget.view.viewmodel.ChatAppWidgetViewModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.sellerappwidget.view.work.GetChatWorker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 04/12/20
 */

class GetChatService : JobIntentService(), AppWidgetView<ChatUiModel> {

    companion object {
        private const val JOB_ID = 1411

        @JvmStatic
        fun startService(context: Context) {
            try {
                val work = Intent(context, GetChatService::class.java)
                enqueueWork(context, GetChatService::class.java, JOB_ID, work)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    @Inject
    lateinit var viewModel: ChatAppWidgetViewModel

    @Inject
    lateinit var sharedPref: SellerAppWidgetPreferences

    override fun onCreate() {
        super.onCreate()

        initInjector()
        viewModel.bindView(this)
    }

    override fun onHandleWork(intent: Intent) {
        showLoadingState()
        viewModel.getChatList()
    }

    override fun onSuccessGetOrderList(result: Success<ChatUiModel>) {
        sharedPref.putLong(Const.SharedPrefKey.CHAT_LAST_UPDATED, System.currentTimeMillis())
        ChatAppWidget.setOnSuccess(applicationContext, result.data)
        GetChatWorker.runWorkerPeriodically(applicationContext)
    }

    override fun onFailedGetOrderList(fail: Fail) {
        Timber.e(fail.throwable)
        ChatAppWidget.setOnError(applicationContext)
        GetChatWorker.runWorkerPeriodically(applicationContext)
    }

    private fun initInjector() {
        DaggerAppWidgetComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun showLoadingState() {
        val remoteViews = AppWidgetHelper.getChatWidgetRemoteView(applicationContext)
        val awm = AppWidgetManager.getInstance(applicationContext)
        val ids = AppWidgetHelper.getAppWidgetIds<ChatAppWidget>(applicationContext, awm)
        ids.forEach {
            ChatWidgetStateHelper.updateViewOnLoading(remoteViews)
            ChatWidgetLoadingState.setupLoadingState(applicationContext, awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }
    }
}