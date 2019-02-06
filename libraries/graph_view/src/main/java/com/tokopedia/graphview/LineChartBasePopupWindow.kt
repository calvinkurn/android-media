package com.tokopedia.graphview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView


/**
 * Extend this LineChartBasePopupWindow
 */
open class LineChartBasePopupWindow : PopupWindow {

    private var context: Context? = null
    private var popupTitle: TextView? = null


    public constructor(context: Context) : super(context) {
        this.context = context
        init()
    }

    public constructor(contentView: View, width: Int, height: Int) : super(contentView, width, height) {}

    public constructor(contentView: View, width: Int, height: Int, focusable: Boolean) : super(contentView, width, height, focusable) {}


    protected val layoutRes: Int
        get() = R.layout.gv_popup_layout

    protected val popupWindowHeight: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT

    protected val popupWindowWidth: Int
        get() = ViewGroup.LayoutParams.WRAP_CONTENT


    private fun init() {
        // Set popup window animation style.
        val popupView = LayoutInflater.from(context).inflate(layoutRes, null)
        popupTitle = popupView.findViewById(R.id.gv_text_title)
        animationStyle = R.style.popup_window_animation
        contentView = popupView
        height = popupWindowHeight
        width = popupWindowWidth
        setBackgroundDrawable(ColorDrawable(Color.WHITE))
        isOutsideTouchable = true
        update()

    }


    fun setPopupTitleColor(color: Int) {
        if (popupTitle != null) {
            popupTitle!!.setTextColor(color)
        }
    }

    fun setPopupTitleSize(size: Float) {
        if (popupTitle != null) {
            popupTitle!!.textSize = size
        }
    }

    fun setPopupTitleText(text: String) {
        if (popupTitle != null) {
            popupTitle!!.text = text
        }
    }


}
