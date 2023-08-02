package com.tokopedia.shop_widget.common.customview

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.TextUtils
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ShopPageWidgetLabelViewBinding

/**
 * Created by zulfikarrahman on 12/29/16.
 */
class ShopPageLabelView : ShopPageBaseCustomView {
    var imageView: ImageView? = null
        private set
    private var titleTextView: TextView? = null
    private var subTitleTextView: TextView? = null
    private var contentTextView: TextView? = null
    private var badgeTextView: TextView? = null
    private var rightArrow: ImageView? = null
    private var imageDrawable: Drawable? = null
    private var imageWidth = 0
    private var imageHeight = 0
    private var imageMarginRight = 0
    private var titleText: String? = null
    private var subTitleText: String? = null

    @ColorInt
    private var titleColorValue = 0

    @ColorInt
    private var subtitleColorValue = 0
    private var contentTextStyleValue = 0
    private var badgeCounter = 0
    private var contentText: String? = null

    @ColorInt
    private var contentColorValue = 0
    private var titleTextStyleValue = 0
    private var maxLines = 0
    private var titleTextSize = 0f
    private var subTitleTextSize = 0f
    private var contentTextSize = 0f
    private var contentMaxWidthPercentage = 0f
    private var minTitleWidth = 0
    private var isArrowShown = false
    private var enabled = false
    private var viewBinding : ShopPageWidgetLabelViewBinding? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ShopPageLabelView)
        try {
            val drawableId = styledAttributes.getResourceId(R.styleable.ShopPageLabelView_shop_lv_image, -1)
            if (drawableId >= 0) {
                imageDrawable = AppCompatResources.getDrawable(context, drawableId)
            }
            imageWidth = styledAttributes.getDimension(R.styleable.ShopPageLabelView_shop_lv_image_width, resources.getDimension(R.dimen.dp_32)).toInt()
            imageHeight = styledAttributes.getDimension(R.styleable.ShopPageLabelView_shop_lv_image_height, imageWidth.toFloat()).toInt()
            imageMarginRight = styledAttributes.getDimension(R.styleable.ShopPageLabelView_shop_lv_image_margin_right, resources.getDimension(R.dimen.dp_8)).toInt()
            titleText = styledAttributes.getString(R.styleable.ShopPageLabelView_shop_lv_title)
            titleColorValue = styledAttributes.getColor(R.styleable.ShopPageLabelView_shop_lv_title_color, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
            subtitleColorValue = styledAttributes.getColor(R.styleable.ShopPageLabelView_shop_lv_sub_title_color, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
            subTitleText = styledAttributes.getString(R.styleable.ShopPageLabelView_shop_lv_sub_title)
            contentText = styledAttributes.getString(R.styleable.ShopPageLabelView_shop_lv_content)
            badgeCounter = styledAttributes.getInt(R.styleable.ShopPageLabelView_shop_lv_badge, 0)
            contentColorValue = styledAttributes.getColor(R.styleable.ShopPageLabelView_shop_lv_content_color, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
            contentTextStyleValue = styledAttributes.getInt(R.styleable.ShopPageLabelView_shop_lv_content_text_style, Typeface.NORMAL)
            titleTextStyleValue = styledAttributes.getInt(R.styleable.ShopPageLabelView_shop_lv_title_text_style, Typeface.NORMAL)
            maxLines = styledAttributes.getInt(R.styleable.ShopPageLabelView_shop_lv_content_max_lines, 1)
            contentTextSize = styledAttributes.getDimension(R.styleable.ShopPageLabelView_shop_lv_content_text_size, resources.getDimension(R.dimen.sp_16))
            contentMaxWidthPercentage = styledAttributes.getFloat(R.styleable.ShopPageLabelView_shop_lv_content_max_width_percentage, MAX_WIDTH_PERCENT_CONTENT)
            titleTextSize = styledAttributes.getDimension(R.styleable.ShopPageLabelView_shop_lv_title_text_size, contentTextSize)
            subTitleTextSize = styledAttributes.getDimension(R.styleable.ShopPageLabelView_shop_lv_sub_title_text_size, resources.getDimension(R.dimen.sp_12))
            minTitleWidth = styledAttributes.getDimensionPixelSize(R.styleable.ShopPageLabelView_shop_lv_title_min_width, 0)
            isArrowShown = styledAttributes.getBoolean(R.styleable.ShopPageLabelView_shop_lv_show_arrow, false)
        } finally {
            styledAttributes.recycle()
        }
        init()
    }

    private fun init() {
        viewBinding = ShopPageWidgetLabelViewBinding.inflate(LayoutInflater.from(context), this, true)
        imageView = viewBinding?.imageView
        titleTextView = viewBinding?.textViewTitle
        subTitleTextView = viewBinding?.textViewSubTitle
        contentTextView = viewBinding?.textViewContent
        rightArrow = viewBinding?.imageArrow
        badgeTextView = viewBinding?.textViewBadge
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (imageDrawable != null) {
            imageView?.setImageDrawable(imageDrawable)
            imageView?.visibility = VISIBLE
            titleTextView?.setPadding(imageMarginRight, 0, 0, 0)
            subTitleTextView?.setPadding(imageMarginRight, 0, 0, 0)
        }
        val layoutParams = imageView?.layoutParams
        layoutParams?.width = imageWidth
        layoutParams?.height = imageHeight
        imageView?.layoutParams = layoutParams
        titleTextView?.text = titleText
        titleTextView?.setTypeface(null, titleTextStyleValue)
        titleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        titleTextView?.setTextColor(titleColorValue)
        titleTextView?.minWidth = minTitleWidth
        subTitleTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTitleTextSize)
        subTitleTextView?.setTextColor(subtitleColorValue)
        if (!TextUtils.isEmpty(subTitleText)) {
            subTitleTextView?.text = subTitleText
            subTitleTextView?.visibility = VISIBLE
        } else {
            subTitleTextView?.visibility = GONE
        }
        if (badgeCounter > 0) {
            badgeTextView?.text = badgeCounter(badgeCounter)
            badgeTextView?.visibility = VISIBLE
        } else {
            badgeTextView?.visibility = GONE
        }
        contentTextView?.text = contentText
        contentTextView?.setTextColor(contentColorValue)
        contentTextView?.setTypeface(null, contentTextStyleValue)
        contentTextView?.maxLines = maxLines
        contentTextView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize)
        rightArrow?.visibility = if (isArrowShown) VISIBLE else GONE
        val dm = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(dm)
        rightArrow?.visibility = if (isArrowShown) VISIBLE else GONE
        contentTextView?.maxWidth = (dm.widthPixels * contentMaxWidthPercentage).toInt()
        invalidate()
        requestLayout()
    }

    @Suppress("unused")
    fun resetContentText() {
        content = contentText
        invalidate()
        requestLayout()
    }

    @Suppress("unused")
    val isContentDefault: Boolean
        get() = contentTextView?.text == contentText

    override fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
        isClickable = enabled
        if (enabled) {
            titleTextView?.setTextColor(titleColorValue)
            contentTextView?.setTextColor(contentColorValue)
            subTitleTextView?.setTextColor(subtitleColorValue)
        } else {
            titleTextView?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
            contentTextView?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
            subTitleTextView?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_32))
        }
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    var title: String?
        get() = titleTextView?.text.toString()
        set(textTitle) {
            titleTextView?.text = textTitle
            invalidate()
            requestLayout()
        }

    fun setTitle(spannableString: SpannableString?) {
        titleTextView?.text = spannableString
        invalidate()
        requestLayout()
    }

    fun setSubTitle(text: String?) {
        subTitleTextView?.text = text
        subTitleTextView?.visibility = if (TextUtils.isEmpty(text)) GONE else VISIBLE
        invalidate()
        requestLayout()
    }

    @Suppress("unused")
    fun setTitleContentTypeFace(typeFace: Int) {
        titleTextView?.setTypeface(null, typeFace)
        invalidate()
        requestLayout()
    }

    var content: CharSequence?
        get() = contentTextView?.text.toString()
        set(textValue) {
            contentTextView?.text = textValue
            invalidate()
            requestLayout()
        }

    private fun badgeCounter(badge: Int): String {
        var counter = badge.toString()
        if (badge > MAXIMUM_COUNTER) {
            counter = "99+"
        }
        return counter
    }
    companion object {
        private const val MAX_WIDTH_PERCENT_CONTENT = 0.3f
        private const val MAXIMUM_COUNTER = 99
    }
}
