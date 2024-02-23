package com.tkpd.macrobenchmark.util

import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry

fun MacrobenchmarkRule.measureTokopediaApps(
    packageName: String = MacroIntent.TKPD_PACKAGE_NAME,
    startupMode: StartupMode? = null,
    metrics: List<Metric>,
    setupBlock: () -> Unit = {},
    measureBlock: (MacrobenchmarkScope) -> Unit
) = measureRepeated(
    packageName = packageName,
    metrics = metrics,
    compilationMode = CompilationMode.Partial(
        BaselineProfileMode.UseIfAvailable,
        3
    ),
    iterations = MacroArgs.getIterations(InstrumentationRegistry.getArguments()),
    startupMode = startupMode,
    setupBlock = {
        setupBlock.invoke()
        pressHome()
    },
    measureBlock = {
        measureBlock.invoke(this)
    }
)
