package com.tokopedia.topchat.chatroom.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.topchat.R
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.htmltags.HtmlUtil

class TopChatRoomOrderCancellationLayout: FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    private var title: Typography? = null
    private var layoutOrderCancellation: ConstraintLayout? = null

    init {
        initViewLayout()
        initViewBind()
    }

    private fun initViewLayout() {
        View.inflate(context, LAYOUT, this)
    }

    private fun initViewBind() {
        title = findViewById(R.id.topchat_chatroom_tv_order_cancellation)
        layoutOrderCancellation = findViewById(R.id.topchat_chatroom_cl_order_cancellation)
    }

    fun setText(message: String) {
        title?.text = HtmlUtil.fromHtml(message)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val myWidth = (widthSpecSize * WIDTH_RATIO).toInt()
        val myWidthSpec = MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.EXACTLY)
        super.onMeasure(myWidthSpec, heightMeasureSpec)
    }

    companion object {
        @LayoutRes
        private val LAYOUT = R.layout.topchat_chatroom_partial_order_cancellation
        private const val WIDTH_RATIO = 0.8
    }
}
