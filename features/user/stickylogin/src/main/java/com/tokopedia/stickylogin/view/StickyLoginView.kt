package com.tokopedia.stickylogin.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.stickylogin.R
import com.tokopedia.stickylogin.analytics.StickyLoginReminderTracker
import com.tokopedia.stickylogin.analytics.StickyLoginTracking
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.stickylogin.utils.StripedUnderlineUtil
import com.tokopedia.unifycomponents.setBodyText
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class StickyLoginView : FrameLayout, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var layoutContainer: ConstraintLayout
    private lateinit var imageViewLeft: ImageView
    private lateinit var imageViewRight: ImageView
    private lateinit var textContent: TextView
    private var leftImage: Drawable? = null
    private var spannable: SpannableString? = null

    val tracker: StickyLoginTracking
        get() = StickyLoginTracking()

    val trackerLoginReminder: StickyLoginReminderTracker
        get() = StickyLoginReminderTracker()

    private var timeDelay = DEFAULT_DELAY_TIME_IN_MINUTES

    constructor(context: Context) : super(context) {
        inflateLayout()
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        inflateLayout()
        launch {
            val result = initAttrsInBg(attributeSet)
            result.await()
            initView()
        }
    }

    constructor(context: Context, attributeSet: AttributeSet, styleAttr: Int) : super(context, attributeSet, styleAttr) {
        inflateLayout()
        launch {
            val result = initAttrsInBg(attributeSet)
            result.await()
            initView()
        }
    }

    fun initAttrsInBg(attributeSet: AttributeSet) : Deferred<Unit> = async(Dispatchers.IO) {
        initAttributeSet(attributeSet)
    }

    private fun inflateLayout() {
        val layout: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layout.inflate(R.layout.layout_widget_sticky_login, this, true)

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

    private fun getSharedPreference(file: String): SharedPreferences {
        return context.getSharedPreferences(file, Context.MODE_PRIVATE)
    }

    private fun getLastSeen(page: StickyLoginConstant.Page): Long {
        val sharedPref = getSharedPreference(STICKY_PREF)
        val lastSeen = when (page) {
            StickyLoginConstant.Page.HOME -> {
                sharedPref.getLong(KEY_LAST_SEEN_AT_HOME, 0)
            }
            StickyLoginConstant.Page.PDP -> {
                sharedPref.getLong(KEY_LAST_SEEN_AT_PDP, 0)
            }
            StickyLoginConstant.Page.SHOP -> {
                sharedPref.getLong(KEY_LAST_SEEN_AT_SHOP, 0)
            }
        }
        return if (lastSeen > 0) lastSeen
        else 0
    }

    private fun setLastSeen(page: StickyLoginConstant.Page, epoch: Long) {
        val inMinute = TimeUnit.MILLISECONDS.toMinutes(epoch)
        val sharedPref = getSharedPreference(STICKY_PREF)
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

    /**
     * Login Reminder content
     */
    fun isLoginReminder(): Boolean {
        val pref = getSharedPreference(STICKY_LOGIN_REMINDER_PREF)
        val name = pref.getString(KEY_USER_NAME, "") ?: ""
        val picture = pref.getString(KEY_PROFILE_PICTURE, "") ?: ""
        return name.isNotEmpty() && picture.isNotEmpty()
    }

    @SuppressLint("SetTextI18n")
    fun showLoginReminder(page: StickyLoginConstant.Page) {
        val name = getSharedPreference(STICKY_LOGIN_REMINDER_PREF).getString(KEY_USER_NAME, "")
        val names = name?.split(" ") ?: emptyList()
        val profilePicture = getSharedPreference(STICKY_LOGIN_REMINDER_PREF).getString(KEY_PROFILE_PICTURE, "")

        textContent.text = TEXT_RE_LOGIN + names[0]
        textContent.setBodyText(isBold = true)
        textContent.setTextColor(ContextCompat.getColor(context, R.color.Green_G500))

        profilePicture?.let {
            imageViewLeft.loadImageCircle(it)
        }
        show(page)
    }

    companion object {
        const val TAG = "StickyTextButton"

        private const val DEFAULT_DELAY_TIME_IN_MINUTES = 30

        private const val STICKY_PREF = "sticky_login_widget.pref"
        private const val KEY_LAST_SEEN_AT_HOME = "last_seen_at_home"
        private const val KEY_LAST_SEEN_AT_PDP = "last_seen_at_pdp"
        private const val KEY_LAST_SEEN_AT_SHOP = "last_seen_at_shop"

        private const val STICKY_LOGIN_REMINDER_PREF = "sticky_login_reminder.pref"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_PROFILE_PICTURE = "profile_picture"

        private const val TEXT_RE_LOGIN = "Masuk sebagai "
    }
}