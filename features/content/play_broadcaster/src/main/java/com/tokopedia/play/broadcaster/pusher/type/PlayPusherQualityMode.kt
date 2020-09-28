package com.tokopedia.play.broadcaster.pusher.type


/**
 * Created by mzennis on 24/05/20.
 */
sealed class PlayPusherQualityMode {

    //  indicates the resolution-first mode in which the SDK sets the bitrate to prioritize the resolution of video streams.
    object ResolutionFirst: PlayPusherQualityMode()

    //  indicates the fluency-first mode in which the SDK sets the bitrate to prioritize the fluency of video streams.
    object FluencyFirst: PlayPusherQualityMode()

    //  indicates the fluency-first mode in which the SDK sets the bitrate to prioritize the fluency of video streams.
    data class CustomBitrate(
            val target: Int = DEFAULT_TARGET_BITRATE,
            val min: Int = DEFAULT_MINIMUM_BITRATE,
            val init: Int = DEFAULT_INITIAL_BITRATE
    ): PlayPusherQualityMode()


    companion object {
       const val DEFAULT_TARGET_BITRATE = 1200 // Set the target bitrate to 1200 Kbit/s.
       const val DEFAULT_MINIMUM_BITRATE = 400 // Set the minimum bitrate to 400 Kbit/s.
       const val DEFAULT_INITIAL_BITRATE = 900 // Set the initial bitrate to 900 Kbit/s.
    }
}