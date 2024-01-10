package com.tokopedia.analytics.performance.perf.performanceTracing

//noinspection SuspiciousImport,MissingResourceImportAlias
import android.R
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentOnAttachListener
import com.tokopedia.analytics.performance.perf.performanceTracing.components.LoadableComponent
import com.tokopedia.analytics.performance.perf.performanceTracing.config.AppPerformanceConfigRepository
import com.tokopedia.analytics.performance.perf.performanceTracing.config.FragmentPerfConfig
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

        var currentCreatedActivity = ""
        var currentAppPerformanceTraceData: PerformanceTraceData? = null
        var currentAppPerformanceDevState: DevState = DevState(
            state = State.PERF_ENABLED
        )
        var perfNotes: String = ""
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
            onPerformanceTraceStarted: (Activity) -> Unit,
            onPerformanceTraceFinished: () -> Unit
        ) {
            this.performanceConfigRepository = performanceConfigRepository

            application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
                    if (performanceTrace != null) {
                        cancelPerformanceTracing(
                            Error("err: Activity Closed. Performance trace cancelled"),
                            activity
                        )
                    }

                    onPerformanceTraceStarted.invoke(activity)

                    currentAppPerformanceDevState = DevState(
                        activityName = activity.javaClass.simpleName,
                        state = State.PERF_MEASURING
                    )

                    perfNotes = ""

                    val activityName = activity.javaClass.simpleName
                    val traceConfig = getTraceConfig(activityName)

                    currentCreatedActivity = activityName

                    if (traceConfig != null) {
                        if (traceConfig.fragmentConfigs.isNotEmpty()) {
                            startFragmentPerformanceTrace(
                                activity = activity,
                                traceConfig = traceConfig
                            )
                        } else {
                            startActivityPerformanceTrace(
                                activity = activity,
                                traceConfig = traceConfig
                            )
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
                    if (currentCreatedActivity != activity.javaClass.simpleName) {
                        currentAppPerformanceDevState = DevState(
                            activityName = activity.javaClass.simpleName,
                            state = State.PERF_RESUMED
                        )
                    }
                }

                override fun onActivityPaused(activity: Activity) {
                    if (performanceTrace != null) {
                        cancelPerformanceTracing(
                            Error("err: Activity Closed. Performance trace cancelled"),
                            activity
                        )
                    }
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }

                private fun startActivityPerformanceTrace(
                    activity: Activity,
                    traceConfig: PagePerformanceConfig<out PerfParsingType>
                ) {
                    startPerformanceBasedOnParsingType(
                        parsingType = traceConfig.parsingType,
                        activity = activity,
                        traceName = traceConfig.traceName
                    )
                }

                private fun startFragmentPerformanceTrace(
                    activity: Activity,
                    traceConfig: PagePerformanceConfig<out PerfParsingType>
                ) {
                    (activity as? FragmentActivity)?.let {
                        val fragmentManager = activity.supportFragmentManager // Use getFragmentManager() if you are not using the support library
                        fragmentManager.addFragmentOnAttachListener(object : FragmentOnAttachListener {
                            override fun onAttachFragment(fragmentManager: FragmentManager, fragment: Fragment) {
                                val fragmentTag = fragment.tag

                                if (fragmentTag != null) {
                                    traceConfig.fragmentConfigs.forEach { fragmentConfig ->
                                        if (fragmentTag == fragmentConfig.fragmentTag) {
                                            (fragmentConfig as? FragmentPerfConfig<*>)?.let {
                                                startPerformanceBasedOnParsingType(
                                                    parsingType = it.parsingType,
                                                    activity = activity,
                                                    traceName = it.fragmentTag
                                                )
                                            }
                                        } else {
                                            currentAppPerformanceDevState = DevState(
                                                activityName = activity.javaClass.simpleName,
                                                state = State.PERF_DISABLED
                                            )
                                            fragmentManager.removeFragmentOnAttachListener(this)
                                        }
                                    }
                                }
                            }
                        })
                    }
                }

                private fun startPerformanceBasedOnParsingType(parsingType: PerfParsingType?, activity: Activity, traceName: String) {
                    when (val type = parsingType) {
                        is PerfParsingType.XML -> {
                            val strategy = type.parsingStrategy
                            val rootView = activity.window.decorView.findViewById<View>(R.id.content)
                            performanceTrace = XMLPagePerformanceTrace(
                                activityName = activity.javaClass.simpleName,
                                rootView = rootView,
                                onPerformanceTraceError = { result ->
                                    cancelPerformanceTracing(result, activity)
                                },
                                onPerformanceTraceFinished = { result ->
                                    if (result.data.ttilMeasured()) {
                                        finishPerformanceTracing(result, activity)
                                    } else {
                                        performanceTrace?.recordPerformanceData(result)
                                    }
                                },
                                performanceRepository = AppPerformanceRepository(traceName, rootView.context.applicationContext),
                                loadableComponentFlow = loadableComponentFlow,
                                parsingStrategy = strategy
                            ).apply {
                                setTraceId(activity.javaClass.simpleName)
                                startMonitoring()
                            }
                        }

                        else -> {
                            Log.d("AppPerformanceTrace", "Parsing not supported")
                        }
                    }
                }

                private fun finishPerformanceTracing(result: Success<PerformanceTraceData>, activity: Activity) {
                    if (performanceTrace != null) {
                        Log.d("AppPerformanceTrace", "onPerformanceTraceFinished: ${result.data.timeToInitialLoad} ms")
                        Log.d("AppPerformanceTrace", result.data.toString())
                        currentAppPerformanceTraceData = result.data
                        performanceTrace?.recordPerformanceData(result)
                        performanceTrace = null
                        currentAppPerformanceDevState = DevState(
                            activityName = activity.javaClass.simpleName,
                            state = State.PERF_ENABLED
                        )
                        perfNotes = result.message
                        onPerformanceTraceFinished.invoke()
                    }
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

        fun cancelPerformanceTracing(result: Error, activity: Activity) {
            if (performanceTrace != null) {
                Log.d("AppPerformanceTrace", "onPerformanceTraceError: ${result.message}")
                currentAppPerformanceDevState = DevState(
                    activityName = activity.javaClass.simpleName,
                    state = State.PERF_ERROR
                )
                perfNotes = result.message
                performanceTrace?.recordPerformanceData(result)
                performanceTrace = null
            }
        }
    }
}
