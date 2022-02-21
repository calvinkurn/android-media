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
    waitUntil: () -> Unit
) = measureRepeated(
    packageName = MacroIntent.TKPD_PACKAGE_NAME,
    metrics = listOf(StartupTimingMetric()),
    compilationMode = CompilationMode.None(),
    iterations = iterations,
    startupMode = startupMode,
    setupBlock = {
        pressHome()
    },
    measureBlock = {
        Thread.sleep(2000)
        startActivityAndWait(intent.invoke())
        waitUntil.invoke()
    }
)