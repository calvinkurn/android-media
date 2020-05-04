package com.tokopedia.play.view.type


/**
 * Created by mzennis on 15/04/20.
 */
sealed class VideoOrientation {

    abstract val value: String

    object Vertical : VideoOrientation() {
        override val value: String = VERTICAL
    }
    data class Horizontal(val widthRatio: Int, val heightRatio: Int) : VideoOrientation() {
        override val value: String = HORIZONTAL

        val aspectRatio = "$widthRatio:$heightRatio"
    }
    object Unknown : VideoOrientation() {
        override val value: String = UNKNOWN
    }

    val isHorizontal: Boolean
        get() = this is Horizontal

    companion object {
        private const val VERTICAL = "vertical"
        private const val HORIZONTAL = "horizontal"
        private const val UNKNOWN = "unknown"

        fun getByValue(value: String): VideoOrientation {
            return when (value) {
                VERTICAL -> Vertical
                HORIZONTAL -> Horizontal(widthRatio = 16, heightRatio = 9) //for now only support 16:9
                else -> Unknown
            }
        }
    }
}