package com.tokopedia.shop.settings.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.shop.settings.R

class ImageLabelView : FrameLayout {
    private var titleText: String? = null
    private var titleTextSize: Float = 0.toFloat()
    private var titleTextStyleValue: Int = 0
    private var titleTextView: TextView? = null
    @ColorInt
    private var titleColorValue: Int = 0

    internal var imageView: ImageView? = null
    private var drawableRes: Int = 0
    private var tvContent: TextView? = null
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        applyAttrs(attrs)
        val view = LayoutInflater.from(context).inflate(R.layout.widget_label_view_image,
                this, true)
        titleTextView = view.findViewById(R.id.tvTitle)
        titleTextView!!.text = titleText
        titleTextView!!.setTypeface(null, titleTextStyleValue)
        titleTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        titleTextView!!.setTextColor(titleColorValue)
        imageView = view.findViewById(R.id.imageView)
        tvContent = view.findViewById(R.id.tvContent)
        tvContent!!.hint = contentHint
        setImage(drawableRes)
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ImageLabelView)
        try {
            titleText = styledAttributes.getString(R.styleable.ImageLabelView_ilv_title)
            titleColorValue = styledAttributes.getColor(R.styleable.ImageLabelView_ilv_title_color,
                    ContextCompat.getColor(context, com.tokopedia.design.R.color.font_black_secondary_54))
            titleTextStyleValue = styledAttributes.getInt(R.styleable.ImageLabelView_ilv_title_text_style, Typeface.NORMAL)
            titleTextSize = styledAttributes.getDimension(R.styleable.ImageLabelView_ilv_title_text_size, resources.getDimension(com.tokopedia.design.R.dimen.sp_12))
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

    fun setTitle(title: String) {
        if (TextUtils.isEmpty(title)) {
            titleTextView!!.visibility = View.GONE
        } else {
            titleTextView!!.text = title
            titleTextView!!.visibility = View.VISIBLE
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isClickable = enabled
        if (enabled) {
            tvContent!!.setTextColor(ContextCompat.getColor(context, com.tokopedia.design.R.color.font_black_primary_70))
            titleTextView!!.setTextColor(titleColorValue)
        } else {
            tvContent!!.setTextColor(ContextCompat.getColor(context, com.tokopedia.design.R.color.font_black_disabled_38))
            titleTextView!!.setTextColor(ContextCompat.getColor(context, com.tokopedia.design.R.color.font_black_disabled_38))
        }
    }

    fun setContent(content: String) {
        tvContent!!.text = content
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return true
    }
}
