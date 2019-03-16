package com.tokopedia.searchbar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.component.badge.BadgeContainer
import com.tokopedia.searchbar.helper.ViewHelper
import kotlinx.android.synthetic.main.home_main_toolbar.view.*

class HomeMainToolbar : MainToolbar {

    var toolbarType: Int = 0

    var shadowApplied: Boolean = false

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)

        showShadow()

        setBackgroundAlpha(0f)
        toolbarType = TOOLBAR_LIGHT_TYPE
        switchToLightToolbar()

        btnInbox.setOnClickListener { v ->
            if (userSession.isLoggedIn) {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.INBOX, screenName)
                getContext().startActivity((this.context.applicationContext as SearchBarRouter)
                        .gotoInboxMainPage(getContext()))
            } else {
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
        }
    }

    fun hideShadow() {
        if(isShadowApplied()){
            shadowApplied = false
            toolbar!!.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))
        }
    }

    fun showShadow() {
        if(!isShadowApplied()){
            shadowApplied = true
            val pL = toolbar.paddingLeft
            var pT = ViewHelper.getStatusBarHeight(context)
            val pR = toolbar.paddingRight
            val pB = toolbar.paddingBottom

            toolbar!!.background = ContextCompat.getDrawable(context, R.drawable.searchbar_bg_shadow_bottom)
//            toolbar!!.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))

            toolbar!!.setPadding(pL, pT, pR, pB)
        }
    }

    override fun inflateResource(context: Context) {
        View.inflate(context, R.layout.home_main_toolbar, this)
    }

    fun setBackgroundAlpha(alpha: Float) {
        val drawable = toolbar!!.background
        drawable.alpha = alpha.toInt()
        toolbar!!.background = drawable
    }


    fun switchToDarkToolbar() {
        if (toolbarType != TOOLBAR_DARK_TYPE) {
            if (btnWishlist is BadgeContainer) {
                val badgeInbox = btn_wishlist as BadgeContainer
                badgeInbox.imageViewFromContainer.setImageResource(R.drawable.ic_searchbar_wishlist_grey)
            } else {
                btnWishlist.setImageResource(R.drawable.ic_searchbar_wishlist_grey)
            }

            if (btnNotification is BadgeContainer) {
                val badgeInbox = btn_inbox as BadgeContainer
                badgeInbox.imageViewFromContainer.setImageResource(R.drawable.ic_searchbar_notif_grey)
            } else {
                btnNotification.setImageResource(R.drawable.ic_searchbar_notif_grey)
            }

            if (btnInbox is BadgeContainer) {
                val badgeInbox = btn_inbox as BadgeContainer
                badgeInbox.imageViewFromContainer.setImageResource(R.drawable.ic_searchbar_inbox_grey)
            } else {
                btnInbox.setImageResource(R.drawable.ic_searchbar_inbox_grey)
            }
            toolbarType = TOOLBAR_DARK_TYPE
        }
    }

    fun switchToLightToolbar() {
        if (toolbarType != TOOLBAR_LIGHT_TYPE) {
            if (btnWishlist is BadgeContainer) {
                val badgeInbox = btn_wishlist as BadgeContainer
                badgeInbox.imageViewFromContainer.setImageResource(R.drawable.ic_searchbar_wishlist_white)
            } else {
                btnWishlist.setImageResource(R.drawable.ic_searchbar_wishlist_white)
            }

            if (btnNotification is BadgeContainer) {
                val badgeInbox = btn_inbox as BadgeContainer
                badgeInbox.imageViewFromContainer.setImageResource(R.drawable.ic_searchbar_notif_white)
            } else {
                btnNotification.setImageResource(R.drawable.ic_searchbar_notif_white)
            }

            if (btnInbox is BadgeContainer) {
                val badgeInbox = btn_inbox as BadgeContainer
                badgeInbox.imageViewFromContainer.setImageResource(R.drawable.ic_searchbar_inbox_white)
            } else {
                btnInbox.setImageResource(R.drawable.ic_searchbar_inbox_white)
            }
            toolbarType = TOOLBAR_LIGHT_TYPE
        }
    }

    fun isShadowApplied() : Boolean {
        return shadowApplied
    }

    companion object {
        val TOOLBAR_LIGHT_TYPE = 0
        val TOOLBAR_DARK_TYPE = 1
    }
}
