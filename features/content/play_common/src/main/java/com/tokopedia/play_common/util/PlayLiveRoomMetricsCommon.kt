package com.tokopedia.play_common.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import java.lang.Exception

/**
 * @author by astidhiyaa on 22/03/22
 */
object PlayLiveRoomMetricsCommon {
    /***
     * Get user's avg internet connection speed, in Kbps
     */
    @JvmStatic
    fun getInetSpeed(context: Context): Int {
        return try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork
                val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities)
                val avgSpeed = (actNw?.linkDownstreamBandwidthKbps?.plus(actNw.linkUpstreamBandwidthKbps))?.div(2) ?: 0
                avgSpeed
            }else{
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    /***
     * Get buffering event from video [duration, timestamp]
     */
    @JvmStatic
    fun getBufferingEventData(bufferCount: Int, timestamp: Long){
        val currentTimeStamp = System.currentTimeMillis()
        val duration = currentTimeStamp - timestamp
    }

    /***
     * Get watch duration from video
     */
    @JvmStatic
    fun getWatchingDuration(duration: Long){
        /***
         * Send to logger
         */
    }
}