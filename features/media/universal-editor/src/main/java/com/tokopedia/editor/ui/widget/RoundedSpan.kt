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
    private val radius: Int = DEFAULT_CORNER.toPx(),
    private val lineHeightExtra: Float
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

        val width = p.measureText(text, start, end) + (DOUBLE * padding)
        val shiftLeft: Float
        val shiftRight: Float

        when (align) {
            ALIGN_LEFT -> {
                shiftLeft = 0f - padding
                shiftRight = width + shiftLeft
            }

            ALIGN_RIGHT -> {
                shiftLeft = right - (width - (padding * ALIGN_RIGHT_PADDING_REDUCTION))
                shiftRight = (right + padding).toFloat()
            }

            else -> {
                shiftLeft = (right - width) / DOUBLE
                shiftRight = right - shiftLeft
            }
        }

        rect.set(shiftLeft, top.toFloat(), shiftRight, bottom.toFloat())

        // ==================
        if (lnum == 0) {
            rect.top -= TOP_GAP_FIRST_ROW
        } else {
            rect.top -= TOP_GAP_ON_BETWEEN_ROW
        }

        if (end == text.length && text.substring(end - 1, end) != "\n") {
            // if last row, increase bottom gap by parent line spacing value
            rect.bottom += lineHeightExtra
        } else {
            rect.bottom -= BOTTOM_GAP_BETWEEN_ROW
        }

        // ==================
        if (lnum == 0) {
            // reset value
            prevWidth = width + 1 // modified to be wider on index 0
            prevLeft = rect.left
            prevTop = rect.top
            prevRight = rect.right
            prevBottom = rect.bottom
            listOfEmptyLine.clear()
        }
        // ==================

        if (text.substring(start, end).replace("\n", "").isEmpty()) {
            listOfEmptyLine[lnum] = "-"
            return
        }

        // skip spike on index 0
        val skipSpike = if (lnum == 0) true else listOfEmptyLine[lnum - 1]?.isNotEmpty() ?: false
        val isWiderThenPrev = (width + (radius * ONE_AND_HALF)) > prevWidth

        val bridgeBlendWidth = radius * DOUBLE

        val onTransitionSize =
            ((prevLeft - bridgeBlendWidth) <= rect.left && (prevRight + bridgeBlendWidth) >= rect.right)

        /**
         * For comment note number please refer to the path flow on
         * universal-editor/src/debug/assets/center_alignment.png || etc
         */

        val startPoint = Pair(prevRight - ((prevRight - prevLeft) / DOUBLE), rect.top)
        path.moveTo(startPoint.first, startPoint.second)

        if (isWiderThenPrev && !skipSpike) {
            // 12
            path.lineTo(prevLeft + radius, rect.top)

            // 13
            path.cubicTo(
                prevLeft, rect.top,
                prevLeft, rect.top,
                prevLeft, rect.top - radius
            )

            if (align != ALIGN_LEFT) {
                // 14
                val pathFourteenPosX =
                    if (onTransitionSize) rect.left else prevLeft - bridgeBlendWidth
                path.cubicTo(
                    prevLeft, rect.top,
                    prevLeft, rect.top,
                    pathFourteenPosX, rect.top
                )

                // 15
                val pathFifteenPosX = if (onTransitionSize) prevLeft else rect.left + radius
                path.lineTo(pathFifteenPosX, rect.top)
            }
        } else {
            // 1
            path.lineTo(rect.left + radius, rect.top)
        }

        if (!isWiderThenPrev && !skipSpike) {
            // 10
            var pathTenPosY = prevBottom
            var pathTenPosX = rect.left - bridgeBlendWidth

            if (pathTenPosX <= prevLeft) {
                pathTenPosX = prevLeft
            }

            if (align == ALIGN_LEFT) {
                pathTenPosY -= radius
            }
            path.lineTo(pathTenPosX, pathTenPosY)
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
                var pathElevenPosX = rect.right + bridgeBlendWidth

                if (pathElevenPosX >= prevRight) {
                    pathElevenPosX = prevRight
                }

                path.cubicTo(
                    rect.right, rect.top,
                    rect.right, rect.top,
                    pathElevenPosX, rect.top
                )
            }
        } else {
            // 8
            if (align == ALIGN_RIGHT && !skipSpike) {
                path.lineTo(rect.right, rect.top - radius)
            } else {
                val pathEightPosX =
                    if (onTransitionSize && !skipSpike) prevRight else rect.right - radius
                path.cubicTo(
                    rect.right, rect.top,
                    rect.right, rect.top,
                    pathEightPosX, rect.top
                )
            }
        }

        if (isWiderThenPrev && !skipSpike) {
            if (align != ALIGN_RIGHT) {
                // 16
                val pathSixteenPosX =
                    if (onTransitionSize) rect.right else prevRight + bridgeBlendWidth
                path.lineTo(pathSixteenPosX, rect.top)

                // 17
                path.cubicTo(
                    prevRight, rect.top,
                    prevRight, rect.top,
                    prevRight, rect.top - radius
                )

                // 18
                path.cubicTo(
                    prevRight, rect.top,
                    prevRight, rect.top,
                    prevRight - radius, rect.top
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

        private const val TOP_GAP_FIRST_ROW = 6f

        private const val TOP_GAP_ON_BETWEEN_ROW = 6f
        private const val BOTTOM_GAP_BETWEEN_ROW = 6f

        private const val ONE_AND_HALF = 1.5f
        private const val DOUBLE = 2f

        private const val ALIGN_RIGHT_PADDING_REDUCTION = 1.2f
    }
}
