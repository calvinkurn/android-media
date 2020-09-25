package com.tokopedia.feedplus.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.design.component.badge.BadgeView
import com.tokopedia.feedplus.R
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.searchbar.util.NotifAnalytics
import com.tokopedia.searchbar.util.NotifPreference
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

const val RED_DOT_GIMMICK_REMOTE_CONFIG_KEY: String = "android_red_dot_gimmick_view"

class FeedMainToolbar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : Toolbar(context, attrs, defStyleAttr) {

    var userSession: UserSessionInterface = UserSession(context)
    var remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
    var notifPreference: NotifPreference = NotifPreference(context)
    var notifAnalytics: NotifAnalytics = NotifAnalytics()

    private var onToolBarClickListener: OnToolBarClickListener? = null
    private var badgeViewInbox: BadgeView? = null
    private var badgeViewNotification: BadgeView? = null
    private var btnNotification: ImageView
    private var btnInbox: ImageView

    init {
        View.inflate(context, R.layout.feed_main_toolbar, this)
        btnNotification = findViewById(R.id.btn_notification)
        btnInbox = findViewById(R.id.btn_inbox)
        initImageSearch()
        initButtonInbox()
        initNotificationButton()
    }

    fun setToolBarClickListener(toolBarClickListener: OnToolBarClickListener) {
        onToolBarClickListener = toolBarClickListener
    }

    private fun initNotificationButton() {
        btnNotification.setOnClickListener {
            onToolBarClickListener?.onNotificationClick()
            if (userSession.isLoggedIn) {
                RouteManager.route(context, ApplinkConst.NOTIFICATION)
            } else {
                val redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(RED_DOT_GIMMICK_REMOTE_CONFIG_KEY, false)
                val redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif
                if (redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus) {
                    notifAnalytics.trackClickGimmickNotif()
                }
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        }
    }

    private fun initButtonInbox() {
        btnInbox.setOnClickListener {
            onToolBarClickListener?.onInboxButtonClick()
            if (userSession.isLoggedIn) {
                RouteManager.route(context, ApplinkConst.INBOX)
            } else {
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        }
    }

    private fun initImageSearch() {
        val imageViewImageSearch = findViewById<ImageView>(R.id.btn_search)

        imageViewImageSearch.setOnClickListener {
            onToolBarClickListener?.onImageSearchClick()
            RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
        }
    }

    fun setNotificationNumber(badgeNum: Int) {
        var badgeNumber = badgeNum
        if (badgeViewNotification == null) badgeViewNotification = BadgeView(context)
        val redDotGimmickRemoteConfigStatus = remoteConfig.getBoolean(RED_DOT_GIMMICK_REMOTE_CONFIG_KEY, false)
        val redDotGimmickLocalStatus = notifPreference.isDisplayedGimmickNotif
        if (redDotGimmickRemoteConfigStatus && !redDotGimmickLocalStatus) {
            badgeNumber += 1
            if (!notifPreference.isViewedGimmickNotif && !userSession.isLoggedIn) {
                notifPreference.isViewedGimmickNotif = true
                notifAnalytics.trackImpressionOnGimmickNotif()
            }
        }
        badgeViewNotification?.run {
            bindTarget(btnNotification)
            badgeGravity = Gravity.END or Gravity.TOP
            setBadgeNumber(badgeNumber)
        }
    }

    fun setInboxNumber(badgeNumber: Int) {
        if (badgeViewInbox == null) badgeViewInbox = BadgeView(context)
        badgeViewInbox?.run {
            bindTarget(btnInbox)
            badgeGravity = Gravity.END or Gravity.TOP
            setBadgeNumber(badgeNumber)
        }
    }

    interface OnToolBarClickListener {
        fun onImageSearchClick()
        fun onInboxButtonClick()
        fun onNotificationClick()
    }
}
