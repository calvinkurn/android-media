package com.tokopedia.tokomart.common.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.searchbar.SearchBarConstant
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.common.view.badge.BadgeView
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by meta on 22/06/18.
 */
@Deprecated( message = "this class is used as oldtoolbar")
open class MainToolbar : Toolbar {

    protected var btnSharing: ImageView? = null
    protected var btnCart: ImageView? = null
    protected var btnBack: ImageView? = null
    protected var editTextSearch: TextView? = null
    protected var searchBarAnalytics: SearchBarAnalytics? = null
    protected var userSession: UserSessionInterface? = null
    protected var screenName: String? = ""

    private var badgeViewCart: BadgeView? = null
    private var remoteConfig: RemoteConfig? = null

    var searchApplink = ApplinkConstInternalDiscovery.AUTOCOMPLETE

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun setCartCounter(badgeNumber: Int) {
        if (btnCart != null) {
            if (badgeViewCart == null) badgeViewCart = BadgeView(context)
            badgeViewCart?.bindTarget(btnCart)
            badgeViewCart?.badgeGravity = Gravity.END or Gravity.TOP
            badgeViewCart?.badgeNumber = badgeNumber
        }
    }

    fun hideBtnSharing() {
        btnSharing?.hide()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        userSession = UserSession(context)
        searchBarAnalytics = SearchBarAnalytics()
        inflateResource(context)
    }

    protected fun actionAfterInflation(context: Context?, view: View) {
        btnSharing = view.findViewById(R.id.btn_sharing)
        btnCart = view.findViewById(R.id.btn_cart)
        btnBack = view.findViewById(R.id.btn_back)
        editTextSearch = view.findViewById(R.id.et_search)
        remoteConfig = FirebaseRemoteConfigImpl(context)

        if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
            editTextSearch?.textSize = 18f
        }

        btnSharing?.setOnClickListener { _ ->
            if (userSession!!.isLoggedIn) {
                searchBarAnalytics?.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName)
                // to do : open bottomsheet?
            } else {
                searchBarAnalytics?.eventTrackingWishlist(SearchBarConstant.WISHLIST, screenName)
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        }
        btnCart?.setOnClickListener { _ ->
            if (userSession!!.isLoggedIn) {
                searchBarAnalytics?.eventTrackingWishlist(SearchBarConstant.INBOX, screenName)
                RouteManager.route(context, ApplinkConst.CART)
            } else {
                searchBarAnalytics?.eventTrackingWishlist(SearchBarConstant.INBOX, screenName)
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        }
        editTextSearch?.setOnClickListener{ _ ->
            searchBarAnalytics?.eventTrackingSearchBar(screenName, "")
            RouteManager.route(context, searchApplink)
        }
        btnBack?.setOnClickListener {
            (context as? Activity)?.onBackPressed()
        }
    }

    open fun inflateResource(context: Context) {
        inflate(context, R.layout.main_toolbar, this)
        actionAfterInflation(context, this)
    }
}