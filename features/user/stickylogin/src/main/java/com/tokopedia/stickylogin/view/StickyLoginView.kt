package com.tokopedia.stickylogin.view

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.stickylogin.R
import com.tokopedia.stickylogin.analytics.StickyLoginTracking
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import java.util.concurrent.TimeUnit
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.StripedUnderlineUtil


class StickyLoginView : BaseCustomView {

    private lateinit var layoutContainer: LinearLayout
    private lateinit var imageViewLeft: ImageView
    private lateinit var imageViewRight: ImageView
    private lateinit var textContent: TextView
    private var leftImage: Drawable? = null
    private var spannable: SpannableString? = null

    val tracker: StickyLoginTracking
        get() = StickyLoginTracking()

    private var timeDelay = DEFAULT_DELAY_TIME_IN_MINUTES

    constructor(context: Context) : super(context) {
        inflateLayout()
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflateLayout()
        initAttributeSet(attributeSet)
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, styleAttr: Int) : super(context, attributeSet, styleAttr) {
        inflateLayout()
        initAttributeSet(attributeSet)
        initView()
    }

    private fun inflateLayout() {
        val layout: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layout.inflate(R.layout.widget_sticky_login_text, this, true)

        layoutContainer = view.findViewById(R.id.layout_sticky_container)
        textContent = view.findViewById(R.id.layout_sticky_content)
        imageViewLeft = view.findViewById(R.id.layout_sticky_image_left)
        imageViewRight = view.findViewById(R.id.layout_sticky_image_right)
    }

    private fun initAttributeSet(attributeSet: AttributeSet) {
        val styleable = context.obtainStyledAttributes(attributeSet, R.styleable.StickyLoginView, 0, 0)
        try {
            var text = styleable.getString(R.styleable.StickyLoginView_sticky_text)
            val textHighlight = styleable.getString(R.styleable.StickyLoginView_sticky_text_highlight)
            val highlightColor = styleable.getColor(R.styleable.StickyLoginView_sticky_highlight_color, -1)

            spannable = SpannableString("")
            if (text != null) {
                if (textHighlight != null) {
                    text += " $textHighlight"
                    spannable = SpannableString(text)
                    if (highlightColor != -1) {
                        spannable.run { this?.setSpan(ForegroundColorSpan(highlightColor), text.length - textHighlight.length, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
                    }
                    spannable.run { this?.setSpan(StyleSpan(Typeface.BOLD), text.length - textHighlight.length, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE) }
                }
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                leftImage = styleable.getDrawable(R.styleable.StickyLoginView_sticky_left_icon)
            } else {
                val leftImageId = styleable.getResourceId(R.styleable.StickyLoginView_sticky_left_icon, -1)
                if (leftImageId != -1) {
                    leftImage = AppCompatResources.getDrawable(context, leftImageId)
                }
            }
        } finally {
            styleable.recycle()
        }
    }

    private fun initView() {
        if (!spannable.isNullOrEmpty() || !spannable.isNullOrEmpty()) {
            textContent.text = spannable
        }

        if (leftImage != null) {
            imageViewLeft.setImageDrawable(leftImage)
        }
    }

    override fun setBackgroundColor(color: Int) {
        layoutContainer.setBackgroundColor(color)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        layoutContainer.setOnClickListener(listener)
    }

    fun setOnDismissListener(listener: OnClickListener?) {
        imageViewRight.setOnClickListener(listener)
    }

    fun setContent(stickyLoginTickerDetail: StickyLoginTickerPojo.TickerDetail) {
        textContent.text = MethodChecker.fromHtml(stickyLoginTickerDetail.message)
        StripedUnderlineUtil.stripUnderlines(textContent)
    }

    fun show(page: StickyLoginConstant.Page) {
        val currentTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())
        if ((currentTimeInMinutes - getLastSeen(page)) < timeDelay) {
            this.visibility = View.GONE
            return
        }

        if (this.visibility == View.GONE || this.visibility == View.INVISIBLE) {
            this.visibility = View.VISIBLE
        }
    }

    fun dismiss(page: StickyLoginConstant.Page) {
        if (this.visibility == View.VISIBLE) {
            this.visibility = View.GONE

            setLastSeen(page, System.currentTimeMillis())
        }
    }

    /**
     * @param minutes delay time in minutes
     **/
    fun setDelayTime(minutes: Int) {
        this.timeDelay = minutes
    }

    fun isShowing(): Boolean {
        return this.visibility == View.VISIBLE
    }

    fun getLocation(): IntArray? {
        if (isShowing()) {
            val location = IntArray(2)
            this.getLocationOnScreen(location)
            return location
        }
        return null
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(STICKY_PREF, Context.MODE_PRIVATE)
    }

    private fun getLastSeen(page: StickyLoginConstant.Page): Long {
        val lastSeen = when (page) {
            StickyLoginConstant.Page.HOME -> {
                getSharedPreference().getLong(KEY_LAST_SEEN_AT_HOME, 0)
            }
            StickyLoginConstant.Page.PDP -> {
                getSharedPreference().getLong(KEY_LAST_SEEN_AT_PDP, 0)
            }
            StickyLoginConstant.Page.SHOP -> {
                getSharedPreference().getLong(KEY_LAST_SEEN_AT_SHOP, 0)
            }
        }
        return if (lastSeen > 0) lastSeen
        else 0
    }

    private fun setLastSeen(page: StickyLoginConstant.Page, epoch: Long) {
        val inMinute = TimeUnit.MILLISECONDS.toMinutes(epoch)
        val sharedPref = getSharedPreference()
        when (page) {
            StickyLoginConstant.Page.HOME -> {
                sharedPref.edit().putLong(KEY_LAST_SEEN_AT_HOME, inMinute).apply()
            }
            StickyLoginConstant.Page.PDP -> {
                sharedPref.edit().putLong(KEY_LAST_SEEN_AT_PDP, inMinute).apply()
            }
            StickyLoginConstant.Page.SHOP -> {
                sharedPref.edit().putLong(KEY_LAST_SEEN_AT_SHOP, inMinute).apply()
            }
        }
    }

    companion object {
        const val TAG = "StickyTextButton"

        private const val DEFAULT_DELAY_TIME_IN_MINUTES = 30

        private const val STICKY_PREF = "sticky_login_widget.pref"
        private const val KEY_LAST_SEEN_AT_HOME = "last_seen_at_home"
        private const val KEY_LAST_SEEN_AT_PDP = "last_seen_at_pdp"
        private const val KEY_LAST_SEEN_AT_SHOP = "last_seen_at_shop"
    }
}