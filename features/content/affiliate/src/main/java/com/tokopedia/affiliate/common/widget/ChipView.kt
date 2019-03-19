package com.tokopedia.affiliate.common.widget

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.tokopedia.affiliate.R
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.*
import kotlinx.android.synthetic.main.widget_af_chip.view.*

/**
 * @author by milhamj on 12/03/19.
 */
class ChipView : BaseCustomView {

    private var mSize: Int = LARGE

    companion object {
        const val NO_ICON = -1

        private const val LARGE = 1
        private const val SMALL = 2

    }

    constructor(context: Context) : super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.ChipView)
            mSize = attributeArray.getInteger(R.styleable.ChipView_chipViewSize, LARGE)
            attributeArray.recycle()
        }
    }

    private fun initView() {
        val rootView = View.inflate(context, R.layout.widget_af_chip, this)
        rootView.isSelected = false
        icon.hide()

        when(mSize) {
            LARGE -> {
                title.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        title.resources.getDimension(R.dimen.sp_12)
                )
                title.typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
            }
            SMALL -> {
                title.setTextSize(
                        TypedValue.COMPLEX_UNIT_PX,
                        title.resources.getDimension(R.dimen.sp_10)
                )
                title.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            }
        }
    }

    fun setTitle(titleText: String) {
        title.text = titleText
    }

    fun setIconDrawable(@DrawableRes iconRes: Int) {
        icon.shouldShowWithAction(iconRes != NO_ICON) {
            icon.loadImageDrawable(iconRes)
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