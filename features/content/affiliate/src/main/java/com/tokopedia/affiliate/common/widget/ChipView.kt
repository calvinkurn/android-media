package com.tokopedia.affiliate.common.widget

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.View
import com.tokopedia.affiliate.R
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.widget_af_chip.view.*

/**
 * @author by milhamj on 12/03/19.
 */
class ChipView : BaseCustomView {

    companion object {
        const val NO_ICON = -1
    }

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val rootView = View.inflate(context, R.layout.widget_af_chip, this)
        rootView.isSelected = false
        icon.hide()
    }

    fun setTitle(titleText: String) {
        title.text = titleText
    }

    fun setIconDrawable(@DrawableRes iconRes: Int) {
        icon.shouldShowWithAction(iconRes != NO_ICON) {
            icon.loadDrawable(iconRes)
        }
    }

    fun setIconUrl(url: String) {
        icon.show()
        icon.loadImage(url)
    }

    fun clearIcon() {
        icon.clearImage()
    }
}