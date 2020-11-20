package com.tokopedia.contactus.inboxticket2.view.customview

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.contactus.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.UnifyImageButton
import com.tokopedia.unifyprinciples.Typography

class ChatWidgetToolTip @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : PopupWindow(context, attrs, defStyleAttr) {

    private var view: View
    private var toolTipCloseButton: UnifyImageButton? = null
    private var toolTipButton: UnifyButton? = null
    private var toolTipText: Typography? = null
    private var chatWidgetToolTipListener: ChatWidgetToolTipListener? = null
    private val defaultWidth = context.resources.getDimension(R.dimen.chat_widget_margin).toInt()

    init {
        view = LayoutInflater
                .from(context)
                .inflate(R.layout.chat_widget_tool_tip, null).also {
                    toolTipText = it.findViewById(R.id.text)
                    toolTipCloseButton = it.findViewById(R.id.close_button)
                    toolTipButton = it.findViewById(R.id.chat_button)
                }
        contentView = view
        chatWidgetToolTipListener = context as? ChatWidgetToolTipListener
        setSize()
        setUpToolTipCloseButton()
        setUpToolTipButton()
    }

    private fun setSize() {
        width = defaultWidth
        height = WindowManager.LayoutParams.WRAP_CONTENT
    }

    private fun setUpToolTipButton() {
        toolTipButton?.setOnClickListener {
            chatWidgetToolTipListener?.onClickToolTipButton()
        }
    }

    private fun setUpToolTipCloseButton() {
        toolTipCloseButton?.setOnClickListener {
            dismiss()
        }
    }

    fun setToolTipDescription(descripton: CharSequence){
        toolTipText?.text = descripton
    }

    fun showAtTop(anchorView: View?) {
        if (anchorView == null) return
        anchorView.post {
            val widthSpec = View.MeasureSpec.makeMeasureSpec(defaultWidth, View.MeasureSpec.EXACTLY)
            view.measure(widthSpec, View.MeasureSpec.UNSPECIFIED)
            val x = (view.measuredWidth - anchorView.width) * -1
            val y = (anchorView.height + view.measuredHeight) * -1
            showAsDropDown(anchorView, x, y)
        }
    }

    fun getScrollListener(): CustomChatWidgetOnScrollListener {
        return CustomChatWidgetOnScrollListener()
    }

    interface ChatWidgetToolTipListener {
        fun onClickToolTipButton()
    }

    inner class CustomChatWidgetOnScrollListener() : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            dismiss()
        }
    }
}