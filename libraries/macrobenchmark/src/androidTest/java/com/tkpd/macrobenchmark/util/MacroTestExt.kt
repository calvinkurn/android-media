package com.tkpd.macrobenchmark.util

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule

fun MacrobenchmarkRule.measureStartup(
    startupMode: StartupMode,
    iterations: Int = 3,
    intent: () -> Intent,
    waitUntil: () -> Unit,
    setupEnvironment: () -> Unit,
    traceName: String
) = measureRepeated(
    packageName = MacroIntent.TKPD_PACKAGE_NAME,
    metrics = listOf(
        StartupTimingMetric()
    ).plus(MacroMetrics.getPltMetrics(traceName)),
    compilationMode = CompilationMode.None(),
    iterations = iterations,
    startupMode = startupMode,
    setupBlock = {
        pressHome()
        setupEnvironment.invoke()
    },
    measureBlock = {
        startActivityAndWait(intent.invoke())
        waitUntil.invoke()
    }
)
