package com.tokopedia.topchat.common.custom

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.viewmodel.ChatTabCounterViewModel

class ToolTipSearchPopupWindow(
        private val context: Context?,
        private val viewModel: ChatTabCounterViewModel
) : PopupWindow(context) {

    private lateinit var view: View
    private var topArrow: ImageView? = null
    private var ivClose: ImageView? = null
    private val defaultWidth = 269f.toPx().toInt()
    private val marginRight = 16f.toPx().toInt()
    private val marginTop = 4f.toPx().toInt() * -1

    init {
        initMenuView()
        initConfig()
        initCloseListener()
    }

    private fun initMenuView() {
        view = LayoutInflater
                .from(context)
                .inflate(R.layout.partial_custom_tooltip_view_search, null).also {
                    ivClose = it.findViewById(R.id.tooltip_close)
                    topArrow = it.findViewById(R.id.iv_arrow_top)
                }
        contentView = view
    }

    private fun initConfig() {
        setSize(defaultWidth, WindowManager.LayoutParams.WRAP_CONTENT)
        setBackgroundDrawable(null)
    }

    private fun initCloseListener() {
        ivClose?.setOnClickListener {
            dismissOnBoarding()
        }
    }

    private fun setSize(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }


    fun showAtBottom(anchorView: View?) {
        if (anchorView == null) return
        anchorView.post {
            measureRequiredViews()
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
            val anchorRect = Rect().apply {
                anchorView.getGlobalVisibleRect(this)
            }
            val viewWidth = view.measuredWidth
            val anchorLeftToRightScreenWidth = screenWidth - anchorRect.left
            val anchorLeftToLeftPopup = viewWidth - anchorLeftToRightScreenWidth
            val x = (anchorLeftToLeftPopup + marginRight) * -1
            val y = marginTop
            setArrowMarginLeft(anchorView, anchorLeftToLeftPopup)
            showAsDropDown(anchorView, x, y)
        }
    }

    private fun setArrowMarginLeft(anchorView: View, anchorLeftToLeftPopup: Int) {
        val iconWidth = anchorView.measuredWidth
        val arrowWidth = topArrow?.measuredWidth ?: 0
        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val marginLeft = anchorLeftToLeftPopup + marginRight + (iconWidth / 2) - (arrowWidth / 2)
        params.setMargins(marginLeft, 0, 0, 0)
        topArrow?.layoutParams = params
    }

    private fun measureRequiredViews() {
        View.MeasureSpec.makeMeasureSpec(defaultWidth, View.MeasureSpec.EXACTLY).apply {
            view.measure(this, View.MeasureSpec.UNSPECIFIED)
        }
        topArrow?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
    }

    fun dismissOnBoarding() {
        if (!isShowing) return
        viewModel.toolTipOnBoardingShown()
        dismiss()
    }
}