package com.tokopedia.mediauploader.common.compressor.video.mp4

// This handler type comes from [com.googlecode.mp4parser]
sealed class Mp4Handler(val name: String) {
    object Vide: Mp4Handler("vide")
    object Soun: Mp4Handler("soun")
    object Text: Mp4Handler("text")
    object Subt: Mp4Handler("subt")
    object Hint: Mp4Handler("hint")
    object Sbtl: Mp4Handler("sbtl")
}

const val SoundHandle = "SoundHandle"
const val VideoHandle = "VideoHandle"
