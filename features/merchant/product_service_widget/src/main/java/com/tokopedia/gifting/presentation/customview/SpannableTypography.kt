package com.tokopedia.gifting.presentation.customview

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import androidx.core.graphics.drawable.DrawableCompat

import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getColor
import com.tokopedia.abstraction.common.utils.view.MethodChecker.getDrawable


class SpannableTypography : BaseCustomView {

    var text: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var tvLabel: Typography? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_spannable_typography, this)
        tvLabel = view.findViewById(R.id.tv_label)

        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SpannableTypography, Int.ZERO, Int.ZERO)

            try {
                text = styledAttributes.getString(R.styleable.SpannableTypography_spannable_text).orEmpty()
                val drawableRes = styledAttributes.getResourceId(R.styleable.SpannableTypography_spannable_drawable, Int.ZERO)
                val drawableStart = styledAttributes.getInteger(R.styleable.SpannableTypography_spannable_drawable_position, Int.ZERO)
                val useSmallText = styledAttributes.getBoolean(R.styleable.SpannableTypography_spannable_use_small_text, false)
                setDrawable(drawableRes, drawableStart)
                if (useSmallText) setAsSmallText()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvLabel?.text = MethodChecker.fromHtml(text)
    }

    fun setDrawable(drawableRes: Int, drawableStart: Int) {
        if (drawableRes.isMoreThanZero()) {
            val imageSpan = ImageSpan(context, drawableRes)
            val spannableString = SpannableString(tvLabel?.text)
            val drawableColor = getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN900)
            DrawableCompat.setTint(imageSpan.drawable, drawableColor)
            spannableString.setSpan(imageSpan, drawableStart, drawableStart.inc(), Int.ZERO)
            tvLabel?.text = spannableString
        }
    }

    fun setAsSmallText() {
        tvLabel?.setType(Typography.BODY_3)
    }
}