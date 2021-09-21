package com.tokopedia.shop.settings.common.view.customview

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

import com.tokopedia.shop.settings.R
import com.tokopedia.unifyprinciples.Typography

class ImageLabelView : FrameLayout {
    private var titleText: String? = null
    private var titleTextSize: Float = 0.toFloat()
    private var titleTextStyleValue: Int = 0
    private var titleTextView: Typography? = null
    @ColorInt
    private var titleColorValue: Int = 0

    internal var imageView: ImageView? = null
    private var drawableRes: Int = 0
    private var tvContent: Typography? = null
    private var contentHint: String? = null

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
        applyAttrs(attrs)
        val view = LayoutInflater.from(context).inflate(R.layout.widget_label_view_image,
                this, true)
        titleTextView = view.findViewById(R.id.tvTitle)
        titleTextView?.text = titleText
        titleTextView?.setTypeface(null, titleTextStyleValue)
        titleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        titleTextView?.setTextColor(titleColorValue)
        imageView = view.findViewById(R.id.imageView)
        tvContent = view.findViewById(R.id.tvContent)
        tvContent?.hint = contentHint
        tvContent?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
        setImage(drawableRes)
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ImageLabelView)
        try {
            titleText = styledAttributes.getString(R.styleable.ImageLabelView_ilv_title)
            titleColorValue = styledAttributes.getColor(R.styleable.ImageLabelView_ilv_title_color,
                    ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            titleTextStyleValue = styledAttributes.getInt(R.styleable.ImageLabelView_ilv_title_text_style, Typeface.NORMAL)
            titleTextSize = styledAttributes.getDimension(R.styleable.ImageLabelView_ilv_title_text_size, resources.getDimension(R.dimen.sp_12))
            drawableRes = styledAttributes.getResourceId(R.styleable.ImageLabelView_ilv_drawable, 0)
            contentHint = styledAttributes.getString(R.styleable.ImageLabelView_ilv_content_hint)
        } finally {
            styledAttributes.recycle()
        }
    }

    fun setImage(drawableRes: Int) {
        this.drawableRes = drawableRes
        if (drawableRes == 0) {
            imageView?.visibility = View.GONE
        } else {
            imageView?.setImageResource(drawableRes)
            imageView?.visibility = View.VISIBLE
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isClickable = enabled
        if (enabled) {
            tvContent?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700))
            titleTextView?.setTextColor(titleColorValue)
        } else {
            tvContent?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
            titleTextView?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32))
        }
    }

    fun setContent(content: String) {
        tvContent?.text = content
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }
}
