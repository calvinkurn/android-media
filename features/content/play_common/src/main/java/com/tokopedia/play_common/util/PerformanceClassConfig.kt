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
            val maxCpuFreq = if (freqResolved == 0) -1
            else ceil(totalCpuFreq / freqResolved.toDouble()).toInt()

            if (androidVersion < Build.VERSION_CODES.LOLLIPOP ||
                cpuCount <= 2 ||
                memoryClass <= 100 ||
                cpuCount <= 4 && maxCpuFreq != -1 && maxCpuFreq <= 1250 ||
                cpuCount <= 4 && maxCpuFreq <= 1600 && memoryClass <= 128 && androidVersion <= Build.VERSION_CODES.LOLLIPOP ||
                cpuCount <= 4 && maxCpuFreq <= 1300 && memoryClass <= 128 && androidVersion <= Build.VERSION_CODES.N) {
                PERFORMANCE_CLASS_LOW
            } else if (cpuCount < 8 ||
                memoryClass <= 160 ||
                maxCpuFreq != -1 && maxCpuFreq <= 2050 ||
                maxCpuFreq == -1 && cpuCount == 8 && androidVersion <= Build.VERSION_CODES.M) {
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
    }
}