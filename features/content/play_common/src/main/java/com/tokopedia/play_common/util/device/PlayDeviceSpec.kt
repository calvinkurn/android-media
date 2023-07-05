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
interface PlayDeviceSpec {
    val deviceType: String

    val api: Int

    val cpuInfo: String

    val totalRam: Long

    val availableRam: Long

    val chipset: String

    val manufacturer: String

    val supportedABIs: String
}
