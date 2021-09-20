package com.tkpd.macrobenchmark.util

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule

fun MacrobenchmarkRule.measureStartup(
    profileCompiled: Boolean,
    startupMode: StartupMode,
    iterations: Int = 3,
    intent: () -> Intent
) = measureRepeated(
    packageName = MacroIntent.TKPD_PACKAGE_NAME,
    metrics = listOf(StartupTimingMetric()),
    compilationMode = if (profileCompiled) {
        CompilationMode.SpeedProfile(warmupIterations = 3)
    } else {
        CompilationMode.None
    },
    iterations = iterations,
    startupMode = startupMode
) {
    pressHome()
    startActivityAndWait(intent.invoke())
}