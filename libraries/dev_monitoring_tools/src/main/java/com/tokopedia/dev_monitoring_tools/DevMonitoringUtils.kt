package com.tokopedia.dev_monitoring_tools

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.anrwatchdog.ANRWatchDog
import com.github.moduth.blockcanary.BlockCanary
import com.github.moduth.blockcanary.BlockCanaryContext
import com.gu.toolargetool.Formatter
import com.gu.toolargetool.Logger
import com.gu.toolargetool.TooLargeTool
import com.gu.toolargetool.TooLargeTool.startLogging
import timber.log.Timber

/**
 * Created by nathan on 9/16/17.
 */
object DevMonitoringUtils {

    @JvmStatic
    fun initCrashMonitoring() {
        val exceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.w("P1#DEV_CRASH#'%s'", Log.getStackTraceString(throwable))
            exceptionHandler.uncaughtException(thread, throwable)
        }
    }

    @JvmStatic
    fun initANRWatcher() {
        ANRWatchDog().setANRListener { anrError -> Timber.w("P1#DEV_ANR'%s'", Log.getStackTraceString(anrError)) }.start()
    }

    @JvmStatic
    fun initTooLargeTool(application: Application) {
        startLogging(application, object : Formatter {
            override fun format(activity: Activity, bundle: Bundle): String {
                return activity.javaClass.simpleName + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(bundle)
            }

            override fun format(fragmentManager: FragmentManager, fragment: Fragment, bundle: Bundle): String {
                var message = fragment.javaClass.simpleName + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(bundle)
                val fragmentArguments = fragment.arguments
                if (fragmentArguments != null) {
                    message += "\n* fragment arguments = " + TooLargeTool.bundleBreakdown(fragmentArguments)
                }

                return message
            }
        }, object : Logger {
            override fun log(msg: String) {
                Timber.w("P1#DEV_TOO_LARGE#'%s'", msg)
            }
            override fun logException(e: Exception) {
                Timber.w("P1#DEV_TOO_LARGE#'%s'", Log.getStackTraceString(e))
            }
        })
    }

    @JvmStatic
    fun initBlockCanary(context: Context) {
        BlockCanary.install(context, BlockCanaryContext()).start()
    }
}