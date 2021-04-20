package com.tokopedia.stickylogin.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.stickylogin.R
import com.tokopedia.stickylogin.analytics.StickyLoginReminderTracker
import com.tokopedia.stickylogin.analytics.StickyLoginTracking
import com.tokopedia.stickylogin.common.StickyLoginConstant
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_LAST_SEEN_AT_HOME
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_LAST_SEEN_AT_PDP
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_LAST_SEEN_AT_SHOP
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_PROFILE_PICTURE
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_STICKY_LOGIN_REMINDER_HOME
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_STICKY_LOGIN_REMINDER_PDP
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_STICKY_LOGIN_REMINDER_SHOP
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_HOME
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_PDP
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_STICKY_LOGIN_WIDGET_SHOP
import com.tokopedia.stickylogin.common.StickyLoginConstant.KEY_USER_NAME
import com.tokopedia.stickylogin.common.helper.getPrefLoginReminder
import com.tokopedia.stickylogin.common.helper.getPrefStickyLogin
import com.tokopedia.stickylogin.di.DaggerStickyLoginComponent
import com.tokopedia.stickylogin.di.module.StickyLoginModule
import com.tokopedia.stickylogin.domain.data.StickyLoginTickerDataModel
import com.tokopedia.stickylogin.view.viewModel.StickyLoginViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.DarkModeUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StickyLoginView : FrameLayout, CoroutineScope, DarkModeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: StickyLoginViewModel

    @Inject
    lateinit var userSession: UserSessionInterface
    private lateinit var remoteConfig: RemoteConfig

    private lateinit var layoutContainer: ConstraintLayout
    private lateinit var imageViewLeft: ImageUnify
    private lateinit var imageViewRight: ImageUnify
    private lateinit var textContent: EllipsizedTextView

    private var leftImage: Drawable? = null
    private var content = ""
    private var highlight = ""
    private var highlightColor = -1

    lateinit var page: StickyLoginConstant.Page
    lateinit var lifecycleOwner: LifecycleOwner
    lateinit var stickyLoginAction: StickyLoginAction

    private val tracker: StickyLoginTracking
        get() = StickyLoginTracking()

    private val trackerLoginReminder: StickyLoginReminderTracker
        get() = StickyLoginReminderTracker()

    private var timeDelay = DEFAULT_DELAY_TIME_IN_MINUTES

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

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

    private fun initAttrsInBg(attributeSet: AttributeSet): Deferred<Unit> = async(Dispatchers.IO) {
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
            content = styleable.getString(R.styleable.StickyLoginView_sticky_text) ?: ""
            highlight = styleable.getString(R.styleable.StickyLoginView_sticky_text_highlight) ?: ""
            highlightColor = styleable.getColor(R.styleable.StickyLoginView_sticky_highlight_color, -1)

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
        initInjector()
        updateDarkMode()

        if (leftImage != null) {
            imageViewLeft.setImageDrawable(leftImage)
        }

        imageViewRight.setOnClickListener {
            dismiss(page)
        }

        layoutContainer.setOnClickListener {
            if (isLoginReminder()) {
                trackerLoginReminder.clickOnLogin(page)
            } else {
                tracker.clickOnLogin(page)
            }

            stickyLoginAction.onClick()
        }

        layoutContainer.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            stickyLoginAction.onViewChange(isShowing())
        }
    }

    private fun initInjector() {
        context?.let {
            val component = DaggerStickyLoginComponent.builder()
                    .stickyLoginModule(StickyLoginModule(it))
                    .build()
            component.inject(this)

            if (it is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
                viewModel = viewModelProvider[StickyLoginViewModel::class.java]
            }
        }
    }

    private fun initObserver(lifecycleOwner: LifecycleOwner) {
        viewModel.stickyContent.observe(lifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    setContent(it.data.tickerDataModels[0])
                    if (isOnDelay(page)) {
                        hide()
                    } else {
                        tracker.viewOnPage(page)
                        show()
                    }
                }
                is Fail -> {
                    hide()
                }
            }
        })
    }

    fun setStickyAction(stickyLoginAction: StickyLoginAction) {
        this.stickyLoginAction = stickyLoginAction
    }

    fun loadContent() {
        if (::page.isInitialized && ::lifecycleOwner.isInitialized) {
            loadContent(page, lifecycleOwner)
        }
    }

    fun loadContent(page: StickyLoginConstant.Page, lifecycleOwner: LifecycleOwner) {
        this.page = page

        if (!::userSession.isInitialized) {
            userSession = UserSession(context)
        }

        if (userSession.isLoggedIn) {
            hide()
            return
        }

        if (!::remoteConfig.isInitialized) {
            remoteConfig = FirebaseRemoteConfigImpl(context)
        }

        if (isLoginReminder() && isCanShowLoginReminder(page) && !isOnDelay(page)) {
            showLoginReminder(page)
        } else if (isCanShowStickyLogin(page) && !isOnDelay(page)) {
            if (!::viewModel.isInitialized) initInjector()

            initObserver(lifecycleOwner)
            viewModel.getStickyContent(page)
        } else {
            hide()
        }
    }

    private fun setContent(stickyLoginTickerDetailDataModel: StickyLoginTickerDataModel.TickerDetailDataModel) {
        val content = stickyLoginTickerDetailDataModel.message
        if (content.contains(REGEX_HTML_TAG)) {
            val contents = content.split(REGEX_HTML_TAG)
            setContent(contents[0], " ${contents[2]}")
        } else {
            setContent(stickyLoginTickerDetailDataModel.message, "")
        }
    }

    private fun setContent(content: String, highlight: String) {
        textContent.setContent(content, highlight)
    }

    fun dismiss() {
        page?.let {
            dismiss(it)
        }
    }

    fun dismiss(page: StickyLoginConstant.Page) {
        if (this.visibility == View.VISIBLE) {
            if (isLoginReminder()) {
                trackerLoginReminder.clickOnDismiss(page)
            } else {
                tracker.clickOnDismiss(page)
            }
            setLastSeen(page, System.currentTimeMillis())
            hide()
        }
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

    private fun getLastSeen(page: StickyLoginConstant.Page): Long {
        val sharedPref = getPrefStickyLogin(context)
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
        val sharedPref = getPrefStickyLogin(context)
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

    private fun isOnDelay(page: StickyLoginConstant.Page): Boolean {
        val currentTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())
        if ((currentTimeInMinutes - getLastSeen(page)) < timeDelay) {
            return true
        }

        return false
    }

    private fun isCanShowLoginReminder(page: StickyLoginConstant.Page): Boolean {
        return when (page) {
            StickyLoginConstant.Page.HOME -> {
                remoteConfig.getBoolean(KEY_STICKY_LOGIN_REMINDER_HOME, true)
            }
            StickyLoginConstant.Page.PDP -> {
                remoteConfig.getBoolean(KEY_STICKY_LOGIN_REMINDER_PDP, true)
            }
            StickyLoginConstant.Page.SHOP -> {
                remoteConfig.getBoolean(KEY_STICKY_LOGIN_REMINDER_SHOP, true)
            }
        }
    }

    private fun isCanShowStickyLogin(page: StickyLoginConstant.Page): Boolean {
        return when (page) {
            StickyLoginConstant.Page.HOME -> {
                remoteConfig.getBoolean(KEY_STICKY_LOGIN_WIDGET_HOME, true)
            }
            StickyLoginConstant.Page.PDP -> {
                remoteConfig.getBoolean(KEY_STICKY_LOGIN_WIDGET_PDP, true)
            }
            StickyLoginConstant.Page.SHOP -> {
                remoteConfig.getBoolean(KEY_STICKY_LOGIN_WIDGET_SHOP, true)
            }
        }
    }

    fun show() {
        updateDarkMode()
        this.visibility = View.VISIBLE
        layoutContainer.show()
        if (::stickyLoginAction.isInitialized) stickyLoginAction.onViewChange(true)
    }

    fun hide() {
        if (isShowing()) {
            this.visibility = View.GONE
            layoutContainer.hide()
            if (::stickyLoginAction.isInitialized) stickyLoginAction.onViewChange(false)
        }
    }

    /**
     * Login Reminder content
     */
    private fun isLoginReminder(): Boolean {
        val pref = getPrefLoginReminder(context)
        val name = pref.getString(KEY_USER_NAME, "") ?: ""
        val picture = pref.getString(KEY_PROFILE_PICTURE, "") ?: ""
        return name.isNotEmpty() && picture.isNotEmpty()
    }

    @SuppressLint("SetTextI18n")
    private fun showLoginReminder(page: StickyLoginConstant.Page) {
        val name = getPrefLoginReminder(context).getString(KEY_USER_NAME, "")
        val profilePicture = getPrefLoginReminder(context).getString(KEY_PROFILE_PICTURE, "")

        textContent.setContent("$TEXT_RE_LOGIN $name")

        profilePicture?.let {
            imageViewLeft.type = ImageUnify.TYPE_CIRCLE
            imageViewLeft.setImageUrl(it)
        }

        trackerLoginReminder.viewOnPage(page)
        show()
    }

    private fun updateDarkMode() {
        if (context.isDarkMode()) {
            onDarkMode()
        } else {
            onLightMode()
        }
    }

    override fun onDarkMode() {
        if (isLoginReminder()) {
            textContent.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        }

        layoutContainer.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900))
    }

    override fun onLightMode() {
        if (isLoginReminder()) {
            textContent.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        }

        layoutContainer.setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G100))
    }

    companion object {
        private const val DEFAULT_DELAY_TIME_IN_MINUTES = 30
        private const val TEXT_RE_LOGIN = "Masuk sebagai"
        private val REGEX_HTML_TAG = "<[^>]+>".toRegex()
    }
}