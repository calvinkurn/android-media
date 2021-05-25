package com.tokopedia.tokomart.common.view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.searchbar.R
import com.tokopedia.searchbar.SearchBarConstant
import com.tokopedia.searchbar.util.NotifAnalytics
import com.tokopedia.searchbar.util.NotifPreference
import com.tokopedia.tokomart.common.view.badge.BadgeView
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by meta on 22/06/18.
 */
@Deprecated("")
open class MainToolbar : Toolbar {
    private var wishlistNewPage = false
    var btnWishlist: ImageView? = null
        protected set
    var btnInbox: ImageView? = null
        protected set
    protected var editTextSearch: TextView? = null
    private var badgeViewInbox: BadgeView? = null
    private var badgeViewNotification: BadgeView? = null
    protected var searchBarAnalytics: SearchBarAnalytics? = null
    protected var userSession: UserSessionInterface? = null
    protected var notifPreference: NotifPreference? = null
    protected var remoteConfig: RemoteConfig? = null
    protected var notifAnalytics: NotifAnalytics? = null
    var searchApplink = ApplinkConstInternalDiscovery.AUTOCOMPLETE
    protected var screenName: String? = ""

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun setInboxNumber(badgeNumber: Int) {
        if (btnInbox != null) {
            if (badgeViewInbox == null) badgeViewInbox = BadgeView(context)
            badgeViewInbox!!.bindTarget(btnInbox)
            badgeViewInbox!!.badgeGravity = Gravity.END or Gravity.TOP
            badgeViewInbox!!.badgeNumber = badgeNumber
        }
    }

    protected fun init(context: Context, attrs: AttributeSet?) {
        notifAnalytics = NotifAnalytics()
        userSession = UserSession(context)
        notifPreference = NotifPreference(context)
        searchBarAnalytics = SearchBarAnalytics()
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        wishlistNewPage = firebaseRemoteConfig.getBoolean(RemoteConfigKey.ENABLE_NEW_WISHLIST_PAGE, true)
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.MainToolbar, 0, 0)
            screenName = try {
                ta.getString(R.styleable.MainToolbar_screenName)
            } finally {
                ta.recycle()
            }
        }
        inflateResource(context)
    }

    protected fun actionAfterInflation(context: Context?, view: View) {
        btnInbox = view.findViewById(R.id.btn_inbox)
        btnWishlist = view.findViewById(R.id.btn_wishlist)
        editTextSearch = view.findViewById(R.id.et_search)
        remoteConfig = FirebaseRemoteConfigImpl(context)
        if (resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            editTextSearch?.setTextSize(18f)
        }
        btnWishlist?.setOnClickListener(OnClickListener { v: View? ->
            if (userSession!!.isLoggedIn) {
                searchBarAnalytics!!.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName)
                if (wishlistNewPage) RouteManager.route(context, ApplinkConst.NEW_WISHLIST) else RouteManager.route(context, ApplinkConst.WISHLIST)
            } else {
                searchBarAnalytics!!.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName)
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        })
        btnInbox?.setOnClickListener(OnClickListener { v: View? ->
            if (userSession!!.isLoggedIn) {
                searchBarAnalytics!!.eventTrackingWishlist(SearchBarConstant.INBOX, screenName)
                RouteManager.route(context, ApplinkConst.INBOX)
            } else {
                searchBarAnalytics!!.eventTrackingWishlist(SearchBarConstant.INBOX, screenName)
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        })
        editTextSearch?.setOnClickListener(OnClickListener { v: View? ->
            searchBarAnalytics!!.eventTrackingSearchBar(screenName, "")
            RouteManager.route(context, searchApplink)
        })
    }

    fun setQuerySearch(querySearch: String?) {
        if (editTextSearch != null) {
            editTextSearch!!.hint = querySearch
        }
    }

    open fun inflateResource(context: Context) {
        inflate(context, R.layout.main_toolbar, this)
        actionAfterInflation(context, this)
    }

    companion object {
        private const val RED_DOT_GIMMICK_REMOTE_CONFIG_KEY = "android_red_dot_gimmick_view"
    }
}