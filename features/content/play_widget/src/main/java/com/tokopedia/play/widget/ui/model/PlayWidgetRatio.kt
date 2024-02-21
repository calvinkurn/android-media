package com.tokopedia.play.widget.ui.model

import android.view.View

/**
 * Created by Jonathan Darwin on 21 February 2024
 */
data class PlayWidgetRatio(
    val width: Int,
    val height: Int,
    val benchmark: Benchmark,
) {
    private val isValid: Boolean
        get() = width > 0 && height > 0 && benchmark != Benchmark.Unknown

    fun getMeasureSpec(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ): Pair<Int, Int> {
        return try {
            if (isValid) {
                val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
                val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

                when (benchmark) {
                    PlayWidgetRatio.Benchmark.Width -> {
                        widthMeasureSpec to View.MeasureSpec.makeMeasureSpec(
                            (height / width.toFloat() * widthSize).toInt(),
                            View.MeasureSpec.EXACTLY
                        )
                    }
                    PlayWidgetRatio.Benchmark.Height -> {
                        View.MeasureSpec.makeMeasureSpec(
                            (width / height.toFloat() * heightSize).toInt(),
                            View.MeasureSpec.EXACTLY
                        ) to heightMeasureSpec
                    }
                    else -> {
                        widthMeasureSpec to heightMeasureSpec
                    }
                }
            } else {
                throw Exception("Ratio information is not valid")
            }
        } catch (_: Throwable) {
            widthMeasureSpec to heightMeasureSpec
        }
    }
    companion object {

        fun parse(rawDimensionRatio: String): PlayWidgetRatio {
            return try {
                if (rawDimensionRatio.isEmpty()) return Unknown

                val (rawBenchmark, rawRatio) = if (rawDimensionRatio.indexOf(",") == 1) {
                    val split = rawDimensionRatio.split(",")
                    split[0] to split[1]
                } else {
                    "" to rawDimensionRatio
                }

                val (width, height) = if (rawRatio.indexOf(":") == -1) {
                    -1 to -1
                } else {
                    val split = rawRatio.split(":")
                    (split[0].toIntOrNull() ?: -1) to (split[1].toIntOrNull() ?: -1)
                }

                PlayWidgetRatio(
                    width = width,
                    height = height,
                    benchmark = Benchmark.parse(rawBenchmark),
                )
            } catch (_: Throwable) {
                Unknown
            }
        }

        val Unknown get() = PlayWidgetRatio(
            width = 0,
            height = 0,
            benchmark = Benchmark.Unknown,
        )
    }
    enum class Benchmark {
        Width,
        Height,
        Unknown;

        companion object {
            /**
             * "h" means that Height needs to be calculated based on Width
             * "w" means that Width needs to be calculated based on Height
             */
            fun parse(s: String): Benchmark {
                return when (s.lowercase()) {
                    "h" -> Width
                    "w" -> Height
                    else -> Unknown
                }
            }
        }
    }
}
