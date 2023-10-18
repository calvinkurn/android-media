package com.tokopedia.editor.ui.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.text.style.LineBackgroundSpan
import com.tokopedia.unifycomponents.toPx
import kotlin.math.sign
import kotlin.math.abs

class RoundedSpan(
    backgroundColor: Int,
    private val padding: Int = DEFAULT_PADDING.toPx(),
    private val radius: Int = DEFAULT_CORNER.toPx()
    ) : LineBackgroundSpan {

    private val rect = RectF()
    private val paint = Paint()
    private val paintStroke = Paint()
    private val path = Path()
    private var prevWidth = -1f
    private var prevLeft = -1f
    private var prevRight = -1f
    private var prevBottom = -1f
    private var prevTop = -1f

    private val listOfEmptyLine = mutableMapOf<Int, String>()

    init {
        paint.color = backgroundColor
        paintStroke.color = backgroundColor
    }

    private var align = ALIGN_CENTER

    fun setAlignment(alignment: Int) {
        align = alignment
    }

    override fun drawBackground(
        c: Canvas,
        p: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        val width = p.measureText(text, start, end) + 2f * padding
        val shiftLeft: Float
        val shiftRight: Float


        when (align) {
            ALIGN_LEFT -> {
                shiftLeft = 0f - padding
                shiftRight = width + shiftLeft
            }

            ALIGN_RIGHT -> {
                shiftLeft = right - width + padding
                shiftRight = (right + padding).toFloat()
            }

            else -> {
                shiftLeft = (right - width) / 2
                shiftRight = right - shiftLeft
            }
        }

        rect.set(shiftLeft, top.toFloat(), shiftRight, bottom.toFloat())

        if (lnum != 0 && end == text.length) {
            rect.bottom += SPACE_GAP_EXTRA
        }

        if (lnum == 0) {
            rect.top -= SPACE_GAP_EXTRA
            rect.bottom += SPACE_GAP_EXTRA

            c.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
        } else {
            path.reset()

            // check if line is empty only contain \n then skip background
            val textOnLine = text.substring(start, end)
            if (textOnLine.replace("\n", "").isEmpty()) {
                prevWidth = 0f
                prevLeft = rect.left
                prevRight = rect.right
                prevBottom = rect.bottom
                prevTop = rect.top

                listOfEmptyLine[lnum] = "-"

                return
            }

            // check if draw need to skip spike when prev line is empty (only contain new line)
            val isSkipSpike = listOfEmptyLine[lnum - 1] != null

            val difference = width - prevWidth
            val diff = -sign(difference) * (2f * radius).coerceAtMost(abs(difference / 2f)) / 2f
            path.moveTo(
                prevLeft, rect.top
            )

            if (align != ALIGN_LEFT) {
                if (!isSkipSpike) {
                    path.cubicTo(//1
                        prevLeft, prevBottom - radius,
                        prevLeft, rect.top,
                        prevLeft + diff, rect.top
                    )
                }
            } else {
                path.lineTo(prevLeft, prevBottom + radius)
            }
            path.lineTo(
                rect.left - diff, rect.top
            )
            path.cubicTo(//2 rounded kiri atas
                rect.left - diff, rect.top,
                rect.left, rect.top,
                rect.left, rect.top + radius
            )
            path.lineTo(
                rect.left, rect.bottom - radius
            )
            path.cubicTo(//3 rounded kiri bawah
                rect.left, rect.bottom - radius,
                rect.left, rect.bottom,
                rect.left + radius, rect.bottom
            )
            path.lineTo(
                rect.right - radius, rect.bottom
            )
            path.cubicTo(//4 rounded kanan bawah
                rect.right - radius, rect.bottom,
                rect.right, rect.bottom,
                rect.right, rect.bottom - radius
            )

            path.lineTo(
                rect.right, rect.top + radius
            )

            if (align != ALIGN_RIGHT) {
                path.cubicTo(//5 rounded kanan atas
                    rect.right, rect.top + radius,
                    rect.right, rect.top,
                    rect.right + diff, rect.top
                )
                path.lineTo(
                    prevRight - diff, rect.top
                )
                if (!isSkipSpike) {
                    path.cubicTo(//6
                        prevRight - diff, rect.top,
                        prevRight, rect.top,
                        prevRight, prevBottom - radius
                    )
                }
            } else {
                path.lineTo(prevRight, prevBottom - radius)
            }

            if (!isSkipSpike) {
                path.cubicTo(//7
                    prevRight, prevBottom - radius,
                    prevRight, prevBottom,
                    prevRight - radius, prevBottom
                )
            }

            path.lineTo(
                prevLeft + radius, rect.top
            )

            if (!isSkipSpike) {
                path.cubicTo(//8
                    prevLeft + radius, prevBottom,
                    prevLeft, prevBottom,
                    prevLeft, rect.top - radius
                )
            }

            c.drawPath(path, paintStroke)

        }


        prevWidth = width
        prevLeft = rect.left
        prevRight = rect.right
        prevBottom = rect.bottom
        prevTop = rect.top
    }

    companion object {
        const val ALIGN_CENTER = 0
        const val ALIGN_LEFT = 1
        const val ALIGN_RIGHT = 2

        private const val DEFAULT_PADDING = 8
        private const val DEFAULT_CORNER = 8

        private const val SPACE_GAP_EXTRA = 4f
    }
}
