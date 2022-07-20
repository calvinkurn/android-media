package com.tokopedia.broadcaster.data.consts

const val THROW_FORGET_TO_INIT = "please initialize first with call init()"
const val THROW_DEVICE_NOT_SUPPORTED = "please use device with armeabi-v7a or arm64-v8a"
const val THROW_NO_CAMERA_AVAILABLE = "unable to live stream as no camera available"
const val THROW_STREAMER_ALREADY_PREPARED = "the streamer already prepared"

const val ERROR_NETWORK_FAIL = "network: network fail"
const val ERROR_CONNECTION_FAIL = "network: network fail"
const val ERROR_AUTH_FAIL = "connect fail: Can not connect to server authentication failure, please check stream credentials."
const val ERROR_UNKNOWN_FAIL = "network: unknown network fail"
const val ERROR_VIDEO_ENCODING_FAIL = "system: Video encoding failure, try to change video resolution"
const val ERROR_VIDEO_CAPTURE_FAIL = "system: Video capture failure"
const val ERROR_AUDIO_ENCODING_FAIL = "system: Audio encoding failure"
const val ERROR_AUDIO_CAPTURE_FAIL = "system: Audio capture failure"