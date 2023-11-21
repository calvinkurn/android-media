package com.tokopedia.analytics.performance.perf.performanceTracing

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.config.AppPerformanceConfigRepository
import com.tokopedia.analytics.performance.perf.performanceTracing.config.PagePerformanceConfig
import com.tokopedia.analytics.performance.perf.performanceTracing.data.DevState
import com.tokopedia.analytics.performance.perf.performanceTracing.data.PerformanceTraceData
import com.tokopedia.analytics.performance.perf.performanceTracing.data.State
import com.tokopedia.analytics.performance.perf.performanceTracing.repository.AppPerformanceRepository
import com.tokopedia.analytics.performance.perf.performanceTracing.strategy.PerfParsingType
import com.tokopedia.analytics.performance.perf.performanceTracing.trace.Error
import com.tokopedia.analytics.performance.perf.performanceTracing.trace.PerformanceTrace
import com.tokopedia.analytics.performance.perf.performanceTracing.trace.Success
import com.tokopedia.analytics.performance.perf.performanceTracing.trace.XMLPagePerformanceTrace
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

        fun getTraceConfig(activityName: String): PagePerformanceConfig<out PerfParsingType>? {
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
                        when (val type = traceConfig.parsingType) {
                            is PerfParsingType.XML -> {
                                val strategy = type.parsingStrategy
                                performanceTrace = XMLPagePerformanceTrace(
                                    activity = activity,
                                    onPerformanceTraceError = { result ->
                                        cancelPerformanceTracing(result, activity)
                                    },
                                    onPerformanceTraceFinished = { result ->
                                        finishPerformanceTracing(result, activity)
                                    },
                                    performanceRepository = AppPerformanceRepository(traceConfig.traceName),
                                    loadableComponentFlow = loadableComponentFlow,
                                    parsingStrategy = strategy
                                ).apply {
                                    setTraceId(activityName)
                                    startMonitoring()
                                }
                            }
                            else -> {
                                Log.d("AppPerformanceTrace", "Parsing not supported")
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

                private fun cancelPerformanceTracing(result: Error, activity: Activity) {
                    Log.d("AppPerformanceTrace", "onPerformanceTraceError: $result")
                    currentAppPerformanceDevState = DevState(
                        activityName = activity.javaClass.simpleName,
                        state = State.PERF_ENABLED
                    )
                    performanceTrace = null
                }

                private fun finishPerformanceTracing(result: Success<PerformanceTraceData>, activity: Activity) {
                    Log.d("AppPerformanceTrace", "onPerformanceTraceFinished: ${result.data.timeToInitialLoad} ms")
                    Log.d("AppPerformanceTrace", result.data.toString())
                    currentAppPerformanceTraceData = result.data
                    performanceTrace = null
                    currentAppPerformanceDevState = DevState(
                        activityName = activity.javaClass.simpleName,
                        state = State.PERF_ENABLED
                    )
                    onPerformanceTraceFinished.invoke()
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
