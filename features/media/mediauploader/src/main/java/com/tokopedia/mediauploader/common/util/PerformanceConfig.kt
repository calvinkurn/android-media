package com.tokopedia.mediauploader.common.util

import android.app.ActivityManager
import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import dagger.Reusable
import java.io.RandomAccessFile
import java.util.*
import javax.inject.Inject
import kotlin.math.ceil

@Reusable
class PerformanceConfig @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val performance = getDevicePerformance()

    fun getDevicePerformance(): Result {
        val cpuCount = Runtime.getRuntime().availableProcessors()
        val memoryClass = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .memoryClass

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
            } catch (ignored: Throwable) {}
        }

        val maxCpuFreq = if (freqResolved == 0) -1
        else ceil(totalCpuFreq / freqResolved.toDouble()).toInt()

        return Result(
            cpuCount = cpuCount,
            memoryClass = memoryClass,
            totalCpuFreq = totalCpuFreq,
            maxCpuFreq = maxCpuFreq
        )
    }

    data class Result(
        val cpuCount: Int,
        val memoryClass: Int,
        val totalCpuFreq: Int,
        val maxCpuFreq: Int
    )
}
