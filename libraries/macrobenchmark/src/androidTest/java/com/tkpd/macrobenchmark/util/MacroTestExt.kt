package com.tkpd.macrobenchmark.util

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry

fun MacrobenchmarkRule.measureTokopediaApps(
    packageName: String = MacroIntent.TKPD_PACKAGE_NAME,
    startupMode: StartupMode? = null,
    metrics: List<Metric>,
    measureBlock: (MacrobenchmarkScope) -> Unit
) = measureRepeated(
    packageName = packageName,
    metrics = metrics,
    compilationMode = CompilationMode.None(),
    iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments()),
    startupMode = startupMode,
    setupBlock = {
        pressHome()
    },
    measureBlock = {
        measureBlock.invoke(this)
    }
)
