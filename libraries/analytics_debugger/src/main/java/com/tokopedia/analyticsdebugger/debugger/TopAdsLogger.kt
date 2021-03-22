package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.TopAdsLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.TopAdsLogModel
import com.tokopedia.analyticsdebugger.debugger.helper.NotificationHelper
import com.tokopedia.analyticsdebugger.debugger.ui.activity.TopAdsDebuggerActivity
import com.tokopedia.config.GlobalConfig
import rx.Subscriber
import rx.schedulers.Schedulers


class TopAdsLogger private constructor(private val context: Context) : TopAdsLoggerInterface {
    private val dbSource: TopAdsLogDBSource
    private val cache: LocalCacheHandler

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_TOPADS_DEBUGGER_NOTIF_ENABLED, false)!!

    init {
        this.dbSource = TopAdsLogDBSource(context)
        this.cache = LocalCacheHandler(context, TOPADS_DEBUGGER)
    }

    override fun save(url: String,
                      eventType: String,
                      sourceName: String,
                      productId: String,
                      productName: String,
                      imageUrl: String,
                      componentName: String) {
        try {
            val topAdsLogModel = TopAdsLogModel()
            topAdsLogModel.url = url
            topAdsLogModel.eventType = eventType
            topAdsLogModel.sourceName = sourceName
            topAdsLogModel.productId = productId
            topAdsLogModel.productName = productName
            topAdsLogModel.imageUrl = imageUrl
            topAdsLogModel.componentName = componentName

            dbSource.insertAll(topAdsLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())

            if (cache.getBoolean(IS_TOPADS_DEBUGGER_NOTIF_ENABLED, false)!!) {
                NotificationHelper.show(context, topAdsLogModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun openActivity() {
        context.startActivity(TopAdsDebuggerActivity.newInstance(context))
    }

    override fun enableNotification(isEnabled: Boolean) {
        cache.putBoolean(IS_TOPADS_DEBUGGER_NOTIF_ENABLED, isEnabled)
        cache.applyEditor()
    }

    private fun defaultSubscriber(): Subscriber<in Boolean> {
        return object : Subscriber<Boolean>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(aBoolean: Boolean?) {
                // no-op
            }
        }
    }

    companion object {
        private val TOPADS_DEBUGGER = "TOPADS_DEBUGGER"
        private val IS_TOPADS_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled"

        private var instance: TopAdsLoggerInterface? = null

        @JvmStatic
        fun getInstance(context: Context) : TopAdsLoggerInterface {
            if (instance == null) {
                if (GlobalConfig.isAllowDebuggingTools()!!) {
                    instance = TopAdsLogger(context)
                } else {
                    instance = emptyInstance()
                }
            }
            return instance as TopAdsLoggerInterface
        }

        private fun emptyInstance(): TopAdsLoggerInterface {
            return object : TopAdsLoggerInterface {

                override val isNotificationEnabled: Boolean
                    get() = false

                override fun save(url: String,
                                  eventType: String,
                                  sourceName: String,
                                  productId: String,
                                  productName: String,
                                  imageUrl: String,
                                  componentName: String) {

                }

                override fun openActivity() {

                }

                override fun enableNotification(status: Boolean) {

                }
            }
        }
    }
}
