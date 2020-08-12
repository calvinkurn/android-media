package com.tokopedia.topchat.common.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.listener.TopChatContract

class ToolTipStickerPopupWindow(
        private val context: Context?,
        private val presenter: TopChatContract.Presenter
) : PopupWindow(context) {

    private lateinit var view: View
    private var ivClose: ImageView? = null
    private val defaultWidth = 225f.toPx().toInt()
    private val marginTop = 4f.toPx().toInt()

    init {
        initMenuView()
        initConfig()
        initCloseListener()
    }

    private fun initMenuView() {
        view = LayoutInflater
                .from(context)
                .inflate(R.layout.partial_custom_menu_view, null).also {
                    ivClose = it.findViewById(R.id.tooltip_close)
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

    fun showAtTop(anchorView: View?) {
        if (anchorView == null) return
        anchorView.post {
            val heightSpec = View.MeasureSpec.makeMeasureSpec(defaultWidth, View.MeasureSpec.EXACTLY)
            view.measure(heightSpec, View.MeasureSpec.UNSPECIFIED)
            val x = 0
            val y = (anchorView.height + view.measuredHeight - marginTop) * -1
            showAsDropDown(anchorView, x, y)
        }
    }

    fun dismissOnBoarding() {
        presenter.toolTipOnBoardingShown()
        dismiss()
    }
}