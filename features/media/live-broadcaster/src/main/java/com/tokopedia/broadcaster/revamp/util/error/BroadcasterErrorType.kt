package com.tokopedia.broadcaster.revamp.util.error

/**
 * Created by meyta.taliti on 06/04/22.
 */
@Suppress("MagicNumber")
enum class BroadcasterErrorType(val code: Int, val message: String) {
    InternetUnavailable(10001, "No internet connection"),
    StreamFailed(30002, "Failed to stream, please try again"),

    AuthFailed(30001, "Authentication failed"),
    UrlEmpty(10002, "RTMP URL is empty"),

    ServiceNotReady(20001, "Streamer is not ready, please wait"),
    StartFailed(20002, "Unknown error, please try again later"),

    ContextNotFound(40001, "Context is null"),
    CameraNotFound(40002, "No camera available"),
    ServiceUnrecoverable(40003, "Streamer failed to initialize"),
    VideoFailed(40004, "Video encoder failed"),
    AudioFailed(40005, "Audio encoder failed"),
}
