package com.tokopedia.analyticsdebugger.debugger

import android.content.Context
import android.os.Environment
import android.util.Log

import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.FpmLogDBSource
import com.tokopedia.analyticsdebugger.debugger.domain.model.FpmFileLogModel
import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel
import com.tokopedia.analyticsdebugger.debugger.helper.NotificationHelper
import com.tokopedia.analyticsdebugger.debugger.ui.activity.FpmDebuggerActivity
import com.tokopedia.config.GlobalConfig

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.text.SimpleDateFormat
import java.util.Date

import rx.Subscriber
import rx.schedulers.Schedulers


class FpmLogger private constructor(private val context: Context) : PerformanceLogger {
    private val dbSource: FpmLogDBSource
    private val cache: LocalCacheHandler

    override val isAutoLogFileEnabled: Boolean
        get() = cache.getBoolean(IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED, false)!!

    override val isNotificationEnabled: Boolean
        get() = cache.getBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, false)!!

    init {
        this.dbSource = FpmLogDBSource(context)
        this.cache = LocalCacheHandler(context, FPM_DEBUGGER)
    }

    override fun save(traceName: String,
                      startTime: Long,
                      endTime: Long,
                      attributes: Map<String, String>,
                      metrics: Map<String, Long>) {
        try {
            val performanceLogModel = PerformanceLogModel(traceName)
            performanceLogModel.startTime = startTime
            performanceLogModel.endTime = endTime
            performanceLogModel.setAttributes(attributes)
            performanceLogModel.setMetrics(metrics)

            dbSource.insertAll(performanceLogModel).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())

            if (cache.getBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, false)!!) {
                NotificationHelper.show(context, performanceLogModel)
            }

            if (cache.getBoolean(IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED, false)!!) {
                writeToFile(createFormattedDataString(performanceLogModel) + ",\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun createFormattedDataString(model: PerformanceLogModel): String {

        val fpmFileLogModel = FpmFileLogModel()
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()

        fpmFileLogModel.setTraceName(model.traceName ?: "")
        fpmFileLogModel.setDurationMs(model.endTime - model.startTime)
        fpmFileLogModel.setMetrics(model.getMetrics())
        fpmFileLogModel.setAttributes(model.getAttributes())

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        val timestamp = Date()

        fpmFileLogModel.setTimestampMs(timestamp.time)
        fpmFileLogModel.setTimestampFormatted(dateFormat.format(timestamp))

        try {
            return URLDecoder.decode(gson.toJson(fpmFileLogModel), "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            return gson.toJson(fpmFileLogModel)
        }

    }

    private fun writeToFile(data: String) {
        try {
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(path, "fpm-auto-log.txt")
            val stream = FileOutputStream(file, true)
            stream.write(data.toByteArray())
            stream.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }

    }

    override fun wipe() {
        dbSource.deleteAll().subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribe(defaultSubscriber())
    }

    override fun openActivity() {
        context.startActivity(FpmDebuggerActivity.newInstance(context))
    }

    override fun enableAutoLogFile(isEnabled: Boolean) {
        cache.putBoolean(IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED, isEnabled)
        cache.applyEditor()
    }

    override fun enableNotification(isEnabled: Boolean) {
        cache.putBoolean(IS_FPM_DEBUGGER_NOTIF_ENABLED, isEnabled)
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
        private val FPM_DEBUGGER = "FPM_DEBUGGER"
        private val IS_FPM_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled"
        private val IS_FPM_DEBUGGER_AUTO_LOG_FILE_ENABLED = "is_auto_log_file_enabled"

        private var instance: PerformanceLogger? = null

        @JvmStatic
        fun getInstance() : PerformanceLogger? {
            return instance
        }

        @JvmStatic
        fun init(context: Context) {
            if (GlobalConfig.isAllowDebuggingTools()) {
                instance = FpmLogger(context)
            } else {
                instance = emptyInstance()
            }
        }

        private fun emptyInstance(): PerformanceLogger {
            return object : PerformanceLogger {

                override val isAutoLogFileEnabled: Boolean
                    get() = false

                override val isNotificationEnabled: Boolean
                    get() = false

                override fun save(traceName: String,
                                  startTime: Long,
                                  endTime: Long,
                                  attributes: Map<String, String>,
                                  metrics: Map<String, Long>) {

                }

                override fun wipe() {

                }

                override fun openActivity() {

                }

                override fun enableAutoLogFile(status: Boolean) {

                }

                override fun enableNotification(status: Boolean) {

                }
            }
        }
    }
}
