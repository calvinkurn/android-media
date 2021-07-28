package com.tkpd.macrobenchmark.util

import android.os.Bundle
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode

object MacroArgs {
    const val iteration = "iteration"
    const val rvIteration = "rviteration"

    const val compilation = "compilation"
    const val compilationNone = "none"
    const val compilationInterpreted = "interpreted"
    const val compilationSpeed = "speed"

    const val startup = "startup"
    const val startupCold = "cold"
    const val startupWarm = "warm"
    const val startupHot = "hot"

    fun getCompilationMode(args: Bundle?): CompilationMode {
        val compilationMode = args?.getString(compilation) ?: ""
        return when(compilationMode) {
            compilationNone -> CompilationMode.None
            compilationInterpreted -> CompilationMode.Interpreted
            compilationSpeed -> CompilationMode.Speed
            else -> CompilationMode.None
        }
    }

    fun getStartupMode(args: Bundle?): StartupMode {
        val startupMode = args?.getString(startup) ?: ""
        return when(startupMode) {
            startupCold -> StartupMode.COLD
            startupWarm -> StartupMode.WARM
            startupHot -> StartupMode.HOT
            else -> StartupMode.WARM
        }
    }

    fun getIterations(args: Bundle?): Int {
        return args?.getString(iteration)?.toInt() ?: 1
    }

    fun getRecyclerViewScrollIterations(args: Bundle?): Int {
        return args?.getString(rvIteration)?.toInt() ?: 1
    }
}