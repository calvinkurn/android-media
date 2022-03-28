package com.tokopedia.play_common.util

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
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

        /***
         * Send to logger - put it on view model first fetch - buffer
         */
    }

    /***
     * Get buffering event from video [duration, timestamp]
     */
    @JvmStatic
    fun getBufferingEventData(bufferCount: Int, timestamp: Long){
        val currentTimeStamp = System.currentTimeMillis()
        val duration = currentTimeStamp - timestamp

        Log.d("LOG VIDEO", "watching buffer duration: $duration")
        Log.d("LOG VIDEO", "watching buffer count: $bufferCount")

        /***
         * Send to logger - data from analytics
         */
    }

    /***
     * Get watch duration from video
     */
    @JvmStatic
    fun getWatchingDuration(duration: Long){
        Log.d("LOG VIDEO", "watching duration: $duration")
        /***
         * Send to logger - data from analytics
         */
    }

    /***
     * Get time to first byte
     */
    @JvmStatic
    fun getTimeToFirstByte(duration: Long){
        Log.d("LOG VIDEO", "time to first byte: $duration")
        /***
         * Send to logger - data from Video Latency
         */
    }
}