package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.layout_widget_switcher.view.*

/**
 * @author by furqan on 25/04/19
 */
class SwitcherCustomView : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setLeftSubtitleText(value: String) {
        tv_switcher_left_subtitle.text = value
    }

    fun setLeftTitleText(value: String) {
        tv_switcher_left_title.text = value
    }

    fun setRightSubtitleText(value: String) {
        tv_switcher_right_subtitle.text = value
    }

    fun setRightTitleText(value: String) {
        tv_switcher_right_title.text = value
    }

    private fun init(attrs: AttributeSet? = null) {
        inflate(context, R.layout.layout_widget_switcher, this)

        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.SwitcherCustomView)

            tv_switcher_left_subtitle.text = attributeArray.getString(R.styleable.SwitcherCustomView_leftSubtitleText)
            tv_switcher_left_title.text = attributeArray.getString(R.styleable.SwitcherCustomView_leftTitleText)
            tv_switcher_right_subtitle.text = attributeArray.getString(R.styleable.SwitcherCustomView_rightSubtitleText)
            tv_switcher_right_title.text = attributeArray.getString(R.styleable.SwitcherCustomView_rightTitleText)
        }
    }

}