package com.tokopedia.play_common.util

/**
 * @author by astidhiyaa on 22/03/22
 */
object PlayLiveRoomMetricsCommon {
    
    private var inetSpeed: Float = 0f
    /***
     * Get user's avg internet connection speed, in Kbps
     */
    @JvmStatic
    fun getInetSpeed(): Float = this.inetSpeed
    
    fun setInetSpeed(inetSpeed: Float){
        this.inetSpeed = inetSpeed
    }

    /***
     * Get buffering event from video [duration, timestamp]
     */
    @JvmStatic
    fun getBufferingEventData(bufferCount: Int, timestamp: Long): Pair<BufferEvent, Int>{
        val currentTimeStamp = System.currentTimeMillis()
        val duration = currentTimeStamp - timestamp

        return Pair(BufferEvent(duration, timestamp), bufferCount)
    }

    data class BufferEvent(val duration: Long, val timestamp: Long)
}