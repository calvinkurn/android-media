package com.tokopedia.utils.network

import android.net.TrafficStats
import timber.log.Timber
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

object NetworkTrafficUtils {

    fun getUidRxBytes(uid: Int = android.os.Process.myUid()): Long {
        return getSumTrafficBytes(uid, "/proc/uid_stat/$uid/tcp_rcv") {
            TrafficStats.getUidRxBytes(uid)
        }
    }

    fun getUidTxBytes(uid: Int = android.os.Process.myUid()): Long {
        return getSumTrafficBytes(uid, "/proc/uid_stat/$uid/tcp_snd") {
            TrafficStats.getUidTxBytes(uid)
        }
    }

    private fun getSumTrafficBytes(uid: Int, filePath: String, nativeMethod: () -> Long = {0}): Long {
        val reader: BufferedReader
        var trafficBytes = 0L
        try {
            if (File(filePath).exists()) {
                reader = BufferedReader(FileReader(filePath))
                trafficBytes = reader.readLine().toLong()
                reader.close()
            }
        } catch (e: Exception) {
            Timber.d(e)
        }
        if (trafficBytes <= 0) {
            try {
                trafficBytes = nativeMethod()
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
        return trafficBytes
    }
}