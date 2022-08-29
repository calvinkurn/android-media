package com.tokopedia.play_common.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import dagger.Reusable
import java.io.RandomAccessFile
import java.util.*
import javax.inject.Inject
import kotlin.math.ceil


/**
 * Created by kenny.hadisaputra on 25/05/22
 */
@Reusable
class PerformanceClassConfig @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val performanceClass = getDevicePerformanceClass()

    private fun getDevicePerformanceClass(): Int {
        return try {
            val androidVersion = Build.VERSION.SDK_INT
            val cpuCount = Runtime.getRuntime().availableProcessors()
            val memoryClass =
                (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).memoryClass
            var totalCpuFreq = 0
            var freqResolved = 0
            for (i in 0 until cpuCount) {
                try {
                    val reader = RandomAccessFile(
                        String.format(
                            Locale.ENGLISH,
                            "/sys/devices/system/cpu/cpu%d/cpufreq/cpuinfo_max_freq",
                            i
                        ), "r"
                    )
                    val line = reader.readLine()
                    if (line != null) {
                        totalCpuFreq += line.toInt() / 1000
                        freqResolved++
                    }
                    reader.close()
                } catch (ignore: Throwable) {
                }
            }
            val maxCpuFreq = if (freqResolved == 0) CPU_FREQ_UNKNOWN
            else ceil(totalCpuFreq / freqResolved.toDouble()).toInt()

            if (androidVersion < Build.VERSION_CODES.LOLLIPOP ||
                cpuCount <= CPU_COUNT_LOW ||
                memoryClass <= MEM_CLASS_LOW ||
                cpuCount <= CPU_COUNT_MEDIUM && maxCpuFreq != CPU_FREQ_UNKNOWN && maxCpuFreq <= CPU_FREQ_LOW ||
                cpuCount <= CPU_COUNT_MEDIUM && maxCpuFreq <= CPU_FREQ_HIGH && memoryClass <= MEM_CLASS_MEDIUM && androidVersion <= Build.VERSION_CODES.LOLLIPOP ||
                cpuCount <= CPU_COUNT_MEDIUM && maxCpuFreq <= CPU_FREQ_MEDIUM && memoryClass <= MEM_CLASS_MEDIUM && androidVersion <= Build.VERSION_CODES.N) {
                PERFORMANCE_CLASS_LOW
            } else if (cpuCount < CPU_COUNT_HIGH ||
                memoryClass <= MEM_CLASS_HIGH ||
                maxCpuFreq != CPU_FREQ_UNKNOWN && maxCpuFreq <= CPU_FREQ_V_HIGH ||
                maxCpuFreq == CPU_FREQ_UNKNOWN && cpuCount == CPU_COUNT_HIGH && androidVersion <= Build.VERSION_CODES.M) {
                PERFORMANCE_CLASS_AVERAGE
            } else {
                PERFORMANCE_CLASS_HIGH
            }
        } catch (e: Throwable) {
            PERFORMANCE_CLASS_AVERAGE
        }
    }

    companion object {
        const val PERFORMANCE_CLASS_LOW = 0
        const val PERFORMANCE_CLASS_AVERAGE = 1
        const val PERFORMANCE_CLASS_HIGH = 2

        private const val CPU_COUNT_LOW = 2
        private const val CPU_COUNT_MEDIUM = 4
        private const val CPU_COUNT_HIGH = 8

        private const val MEM_CLASS_LOW = 100
        private const val MEM_CLASS_MEDIUM = 128
        private const val MEM_CLASS_HIGH = 160

        private const val CPU_FREQ_UNKNOWN = -1
        private const val CPU_FREQ_LOW = 1250
        private const val CPU_FREQ_MEDIUM = 1300
        private const val CPU_FREQ_HIGH = 1600
        private const val CPU_FREQ_V_HIGH = 2050
    }
}