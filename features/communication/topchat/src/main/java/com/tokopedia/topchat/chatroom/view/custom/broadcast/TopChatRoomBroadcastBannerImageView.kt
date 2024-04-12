package com.tokopedia.topchat.chatroom.view.custom.broadcast

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.unifycomponents.toPx

class TopChatRoomBroadcastBannerImageView : AppCompatImageView {

    private val path: Path = Path()
    private var rect: RectF = RectF()
    private val topLeftRadius = 8.toPx().toFloat()
    private val topRightRadius = 8.toPx().toFloat()
    private val outerRadius = floatArrayOf(
        topLeftRadius,
        topLeftRadius,
        topRightRadius,
        topRightRadius,
        0f,
        0f,
        0f,
        0f
    )

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas?) {
        rect.apply {
            left = 0f
            top = 0f
            right = width.toFloat()
            bottom = height.toFloat()
        }
        path.addRoundRect(rect, outerRadius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.onDraw(canvas)
    }
}
