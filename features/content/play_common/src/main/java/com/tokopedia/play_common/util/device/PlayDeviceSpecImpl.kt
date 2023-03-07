package com.tokopedia.play_common.util.device

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import java.io.BufferedReader
import java.io.FileReader
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on March 07, 2023
 */
class PlayDeviceSpecImpl @Inject constructor(
    private val context: Context
) : PlayDeviceSpec {

    override val deviceType: String
        get() = DEVICE_TYPE

    override val api: Int
        get() = Build.VERSION.SDK_INT

    override val cpuInfo: String
        get() {
            val br = BufferedReader(FileReader("/proc/cpuinfo"))

            var str: String

            val output: MutableMap<String, String> = HashMap()

            while (br.readLine().also { str = it ?: "" } != null) {
                val data = str.split(":").toTypedArray()
                if (data.size > 1) {
                    var key = data[0].trim { it <= ' ' }.replace(" ", "_")
                    if (key == "model_name") key = "cpu_model"
                    output[key] = data[1].trim { it <= ' ' }
                }
            }

            br.close()

            return output.toString()
        }

    override val totalRam: Long
        get() {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)

            /**
             * Using (1024 * 1024) gives a quite diff result if we compare it to
             * total & available memory on Developer Options
             */
            val totalMemory = memInfo.totalMem / (1000 * 1000)

            return totalMemory
        }


    override val availableRam: Long
        get() {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)
            val availMemory = memInfo.availMem / (1000 * 1000)

            return availMemory
        }

    override val chipset: String
        get() = Build.HARDWARE

    override val manufacture: String
        get() = Build.MANUFACTURER

    override val supportedABIs: String
        get() {
            /**
             * armeabi-v7a -> ARM 32 bit
             * arm64-v8a -> ARM 64 bit
             *
             * x86 -> x86 32 bit
             * x86_64 -> x86 64 bit
             *
             * Source : https://developer.android.com/games/optimize/64-bit
             */
            return buildString {
                Build.SUPPORTED_ABIS.forEach {
                    append(it)
                    append(", ")
                }
            }
        }

    companion object {
        private const val DEVICE_TYPE = "android"
    }
}
