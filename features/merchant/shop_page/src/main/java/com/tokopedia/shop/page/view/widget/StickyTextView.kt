package com.tokopedia.shop.page.view.widget

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.content.res.AppCompatResources
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.shop.R
import java.util.concurrent.TimeUnit

class StickyTextView : FrameLayout {

    private lateinit var layoutContainer: LinearLayout
    private lateinit var imageViewLeft: ImageView
    private lateinit var imageViewRight: ImageView
    private lateinit var textContent: TextView

    private lateinit var leftImage: Drawable
    private var spannable: SpannableString? = null

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

    override fun setBackgroundColor(color: Int) {
        layoutContainer.setBackgroundColor(color)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        layoutContainer.setOnClickListener(listener)
    }

    fun setOnDismissListener(listener: OnClickListener?) {
        imageViewRight.setOnClickListener(listener)
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
        val styleable = context!!.obtainStyledAttributes(attributeSet, R.styleable.StickyTextView, 0, 0)
        try {
            val text = styleable.getString(R.styleable.StickyTextView_sticky_text)
            val textHighlight = styleable.getString(R.styleable.StickyTextView_sticky_text_highlight)
            val highlightColor = styleable.getColor(R.styleable.StickyTextView_sticky_highlight_color, -1)

            spannable = SpannableString("")
            if (text != null) {
                if (textHighlight != null) {
                    spannable = SpannableString("$text $textHighlight")
                    if (highlightColor != -1) {
                        spannable!!.setSpan(ForegroundColorSpan(highlightColor), text.length - textHighlight.length, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    spannable!!.setSpan(StyleSpan(Typeface.BOLD), text.length - textHighlight.length, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                leftImage = styleable.getDrawable(R.styleable.StickyTextView_sticky_left_icon)!!
            } else {
                val leftImageId = styleable.getResourceId(R.styleable.StickyTextView_sticky_left_icon, -1)
                if (leftImageId != -1) {
                    leftImage = AppCompatResources.getDrawable(context, leftImageId)!!
                }
            }
        } finally {
            styleable.recycle()
        }
    }

    private fun initView() {
        textContent.text = spannable
        imageViewLeft.setImageDrawable(leftImage)
    }

    fun show() {
        val currentTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())
        if ((currentTimeInMinutes - getLastSeen()) < INTERVAL_TIME_IN_MINUTES) {
            this.visibility = View.GONE
            return
        }

        if (this.visibility == View.GONE || this.visibility == View.INVISIBLE) {
            this.visibility = View.VISIBLE
        }
    }

    fun dismiss() {
        if (this.visibility == View.VISIBLE) {
            this.visibility = View.GONE

            setLastSeen(System.currentTimeMillis())
        }
    }

    fun isShowing(): Boolean {
        return this.visibility == View.VISIBLE
    }

    fun getLocation(): IntArray? {
        if(isShowing()) {
            val location = IntArray(2)
            this.getLocationOnScreen(location)
            return location
        }
        return null
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(STICKY_PREF, Context.MODE_PRIVATE)
    }

    private fun getLastSeen(): Long {
        val lastSeen = getSharedPreference().getLong(KEY_LAST_SEEN, 0)
        if (lastSeen != null) return lastSeen
        else return 0
    }

    private fun setLastSeen(epoch: Long) {
        val inMinute = TimeUnit.MILLISECONDS.toMinutes(epoch)
        getSharedPreference().edit().putLong(KEY_LAST_SEEN, inMinute).apply()
    }

    companion object {
        const val TAG = "StickyTextButton"
        const val STICKY_LOGIN_VIEW_KEY = "android_customer_sticky_login_shop"

        private const val STICKY_PREF = "sticky_login_widget.pref"
        private const val KEY_LAST_SEEN = "last_seen_at_shop"
        private const val INTERVAL_TIME_IN_MINUTES = 30
    }
}