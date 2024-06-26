package com.tokopedia.topchat.chatroom.view.custom.messagebubble.base

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * [LinearLayout] that only takes 80% of available space
 */
class TopChatRoomBubbleContainerLayout : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val myWidth = (widthSpecSize * WIDTH_RATIO).toInt()
        val myWidthSpec = MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.EXACTLY)
        super.onMeasure(myWidthSpec, heightMeasureSpec)
    }

    companion object {
        private const val WIDTH_RATIO = 0.8
    }
}
