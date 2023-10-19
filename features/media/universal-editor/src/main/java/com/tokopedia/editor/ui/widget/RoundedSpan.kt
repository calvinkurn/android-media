package com.tokopedia.editor.ui.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.text.style.LineBackgroundSpan
import com.tokopedia.unifycomponents.toPx

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
        path.reset()

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

        // ==================
        if (lnum == 0) {
            // reset value
            prevWidth = width + 1 // modified to be wider on index 0
            prevLeft = left.toFloat()
            prevTop = top.toFloat()
            prevRight = right.toFloat()
            prevBottom = bottom.toFloat()
            listOfEmptyLine.clear()
        }
        // ==================

        if (text.substring(start, end).replace("\n","").isEmpty()) {
            listOfEmptyLine[lnum] = "-"
            return
        }

        // skip spike on index 0
        val skipSpike = if (lnum == 0 ) true else listOfEmptyLine[lnum - 1]?.isNotEmpty() ?: false
        val isWiderThenPrev = width > prevWidth

        val startPoint = Pair(prevRight - (prevRight - prevLeft), top.toFloat())
        path.moveTo(startPoint.first, startPoint.second)

        if (isWiderThenPrev && !skipSpike) {
            // 12
            path.lineTo(prevLeft + radius, top.toFloat())

            // 13
            path.cubicTo(
                prevLeft, top.toFloat(),
                prevLeft, top.toFloat(),
                prevLeft, top.toFloat() - radius
            )

            if (align != ALIGN_LEFT) {
                // 14
                path.cubicTo(
                    prevLeft, top.toFloat(),
                    prevLeft, top.toFloat(),
                    prevLeft - radius, top.toFloat()
                )

                // 15
                path.lineTo(rect.left + radius, top.toFloat())
            } else {
                // 19
                path.lineTo(rect.left, rect.top + radius)
            }
        } else {
            // 1
            path.lineTo(rect.left + radius, top.toFloat())
        }

        if (!isWiderThenPrev && !skipSpike) {
            // 10
            var pathTenPosY =  top.toFloat()
            if (align == ALIGN_LEFT) {
                pathTenPosY -= radius
            }
            path.lineTo(prevLeft, pathTenPosY)
        }

        // 2
        path.cubicTo(
            rect.left, rect.top,
            rect.left, rect.top,
            rect.left, rect.top + radius
        )

        // 3
        path.lineTo(rect.left, rect.bottom - radius)

        // 4
        path.cubicTo(
            rect.left, rect.bottom,
            rect.left, rect.bottom,
            rect.left + radius, rect.bottom
        )

        // 5
        path.lineTo(rect.right - radius, rect.bottom)

        // 6
        path.cubicTo(
            rect.right, rect.bottom,
            rect.right, rect.bottom,
            rect.right, rect.bottom - radius
        )

        // 7
        path.lineTo(rect.right, rect.top + radius)

        if (!isWiderThenPrev && !skipSpike) {
            // 11
            if (align == ALIGN_RIGHT) {
                path.lineTo(rect.right, rect.top - radius)
            } else {
                path.cubicTo(
                    rect.right, rect.top,
                    rect.right, rect.top,
                    prevRight, rect.top
                )
            }
        } else {
            // 8
            if (align == ALIGN_RIGHT && !skipSpike) {
                path.lineTo(rect.right, rect.top - radius)
            } else {
                path.cubicTo(
                    rect.right, rect.top,
                    rect.right, rect.top,
                    rect.right - radius, rect.top
                )
            }

        }

        if (isWiderThenPrev && !skipSpike) {
            if (align != ALIGN_RIGHT) {
                // 16
                path.lineTo(prevRight + radius, top.toFloat())

                // 17
                path.cubicTo(
                    prevRight, top.toFloat(),
                    prevRight, top.toFloat(),
                    prevRight, top.toFloat() - radius
                )

                // 18
                path.cubicTo(
                    prevRight, top.toFloat(),
                    prevRight, top.toFloat(),
                    prevRight - radius, top.toFloat()
                )
            }
        }

        // 9
        path.lineTo(startPoint.first, startPoint.second)

        c.drawPath(path, paintStroke)

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
