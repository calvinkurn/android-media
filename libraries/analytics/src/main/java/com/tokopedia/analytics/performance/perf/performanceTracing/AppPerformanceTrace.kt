package com.tokopedia.analytics.performance.perf.performanceTracing

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.config.AppPerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.PagePerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.TraceType
import com.tokopedia.analytics.performance.perf.performanceTracing.repository.AppPerformanceRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class AppPerformanceTrace {
    companion object {
        private var loadableComponentFlow =
            MutableSharedFlow<LoadableComponent>(1, onBufferOverflow = BufferOverflow.SUSPEND)
        fun submitPerf(loadableComponent: LoadableComponent) {
            loadableComponentFlow.tryEmit(loadableComponent)
        }

        fun getTraceConfig(activityName: String): PagePerformanceConfig? {
            return AppPerformanceConfig.configs[activityName]
        }

        var performanceTrace: PerformanceTrace? = null

        @Synchronized
        fun init(application: Application) {
            application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    val activityName = activity.javaClass.simpleName
                    getTraceConfig(activityName)?.let {
                        when (it.traceType) {
                            TraceType.XML -> {
                                performanceTrace = PagePerformanceTrace(
                                    activity = activity, onPerformanceTraceError = { result ->
                                        Log.d("AppPerformanceTrace", "onPerformanceTraceError: $result")
                                        performanceTrace = null
                                    },
                                    onPerformanceTraceFinished = { result ->
                                        Log.d("AppPerformanceTrace", "onPerformanceTraceFinished: ${result.data.timeToInitialLoad} ms")
                                        performanceTrace = null
                                    },
                                    performanceRepository = AppPerformanceRepository(it.traceName),
                                    loadableComponentFlow = loadableComponentFlow
                                ).apply {
                                    setTraceId(activityName)
                                    startMonitoring()
                                }
                            }
                        }
                    }
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(activity: Activity) {
                    stopOngoingActivityPerformanceTrace(activity.javaClass.simpleName)
                }

                override fun onActivityStopped(activity: Activity) {
                    stopOngoingActivityPerformanceTrace(activity.javaClass.simpleName)
                }

                override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    performanceTrace?.stopMonitoring(
                        Error("Activity destroyed")
                    )
                }

                private fun stopOngoingActivityPerformanceTrace(activityName: String) {
                    performanceTrace?.let { trace ->
                        if (trace.traceId() == activityName) {
                            trace.stopMonitoring(
                                Error("Activity paused")
                            )
                        }
                    }
                }
            })
        }
    }
}
