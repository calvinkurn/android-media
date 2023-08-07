package com.tokopedia.mediauploader.common.compressor.video.mp4

enum class Rotation(val value: Int) {
    R0(0),
    R90(90),
    R180(180),
    R270(270);

    companion object {
        private val map = values().associateBy(Rotation::value)

        @JvmStatic
        fun map(type: Int) = map[type]
    }
}
