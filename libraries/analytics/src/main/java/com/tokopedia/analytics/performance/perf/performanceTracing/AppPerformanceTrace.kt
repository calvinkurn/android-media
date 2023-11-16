package com.tokopedia.analytics.performance.perf.performanceTracing

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.config.AppPerformanceConfigRepository
import com.tokopedia.analytics.performance.perf.performanceTracing.config.PagePerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.config.TraceType
import com.tokopedia.analytics.performance.perf.performanceTracing.repository.AppPerformanceRepository
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class AppPerformanceTrace {
    companion object {
        private var loadableComponentFlow =
            MutableSharedFlow<LoadableComponent>(1, onBufferOverflow = BufferOverflow.SUSPEND)
        private var performanceTrace: PerformanceTrace? = null
        private var performanceConfigRepository: AppPerformanceConfigRepository? = null
        var currentAppPerformanceTraceData: PerformanceTraceData? = null
        var currentAppPerformanceDevState: DevState = DevState(
            state = State.PERF_ENABLED
        )
        fun submitPerf(loadableComponent: LoadableComponent) {
            loadableComponentFlow.tryEmit(loadableComponent)
        }

        fun getTraceConfig(activityName: String): PagePerformanceConfig? {
            if (performanceConfigRepository == null) {
                Log.d("AppPerformanceTrace", "Error: Config not defined.")
            }
            return performanceConfigRepository?.getConfig(activityName)
        }

        @Synchronized
        fun init(
            application: Application,
            performanceConfigRepository: AppPerformanceConfigRepository,
            onPerformanceTraceFinished: () -> Unit
        ) {
            this.performanceConfigRepository = performanceConfigRepository

            application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    val activityName = activity.javaClass.simpleName
                    val traceConfig = getTraceConfig(activityName)

                    if (traceConfig != null) {
                        currentAppPerformanceDevState = DevState(
                            activityName = activity.javaClass.simpleName,
                            state = State.PERF_MEASURING
                        )
                        when (traceConfig.traceType) {
                            TraceType.XML -> {
                                performanceTrace = PagePerformanceTrace(
                                    activity = activity,
                                    onPerformanceTraceError = { result ->
                                        Log.d("AppPerformanceTrace", "onPerformanceTraceError: $result")
                                        currentAppPerformanceDevState = DevState(
                                            activityName = activity.javaClass.simpleName,
                                            state = State.PERF_ENABLED
                                        )
                                        performanceTrace = null
                                    },
                                    onPerformanceTraceFinished = { result ->
                                        Log.d("AppPerformanceTrace", "onPerformanceTraceFinished: ${result.data.timeToInitialLoad} ms")
                                        Log.d("AppPerformanceTrace", result.data.toString())
                                        currentAppPerformanceTraceData = result.data
                                        performanceTrace = null
                                        onPerformanceTraceFinished.invoke()
                                        currentAppPerformanceDevState = DevState(
                                            activityName = activity.javaClass.simpleName,
                                            state = State.PERF_ENABLED
                                        )
                                    },
                                    performanceRepository = AppPerformanceRepository(traceConfig.traceName),
                                    loadableComponentFlow = loadableComponentFlow
                                ).apply {
                                    setTraceId(activityName)
                                    startMonitoring()
                                }
                            }
                        }
                    } else {
                        currentAppPerformanceDevState = DevState(
                            activityName = activity.javaClass.simpleName,
                            state = State.PERF_DISABLED
                        )
                    }
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                    if (performanceTrace == null) {
                        if (currentAppPerformanceDevState?.state == State.PERF_DISABLED) {
                            if (activity.javaClass.simpleName != currentAppPerformanceDevState.activityName) {
                                currentAppPerformanceDevState = DevState(
                                    activityName = activity.javaClass.simpleName,
                                    state = State.PERF_RESUMED
                                )
                            }
                        }
                    }
                }

                override fun onActivityPaused(activity: Activity) {
                    stopOngoingActivityPerformanceTrace(activity.javaClass.simpleName)
                    resetCurrentAppPerformanceData(activity.javaClass.simpleName)
                }

                override fun onActivityStopped(activity: Activity) {
                    stopOngoingActivityPerformanceTrace(activity.javaClass.simpleName)
                    resetCurrentAppPerformanceData(activity.javaClass.simpleName)
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

                private fun resetCurrentAppPerformanceData(activityName: String) {
                    currentAppPerformanceTraceData?.let { trace ->
                        if (trace.activityName == activityName) {
                            currentAppPerformanceTraceData = null
                        }
                    }
                }
            })
        }
    }
}
