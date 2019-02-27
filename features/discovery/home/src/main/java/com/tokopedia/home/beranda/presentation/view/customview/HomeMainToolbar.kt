package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

import com.tokopedia.home.R
import com.tokopedia.searchbar.MainToolbar
import com.tokopedia.searchbar.SearchBarConstant
import com.tokopedia.searchbar.SearchBarRouter
import kotlinx.android.synthetic.main.home_main_toolbar.view.*

class HomeMainToolbar : MainToolbar {

    var toolbarType: Int = 0
        private set

    private var toolbar: Toolbar? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        toolbar = findViewById(R.id.toolbar)
        toolbar!!.background = ColorDrawable(resources.getColor(R.color.white))
        icon_search.setImageResource(R.drawable.ic_searchbar_search_grey)
        setBackgroundAlpha(0f)
        toolbarType = TOOLBAR_LIGHT_TYPE
        switchToLightToolbar()

        btn_inbox.setOnClickListener { v ->
            if (userSession.isLoggedIn) {
                searchBarAnalytics.eventTrackingWishlist(SearchBarConstant.INBOX, screenName)
                getContext().startActivity((this.context.applicationContext as SearchBarRouter)
                        .gotoInboxMainPage(getContext()))
            } else {
                RouteManager.route(context, ApplinkConst.LOGIN)
            }
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
            btnWishlist.setImageResource(com.tokopedia.searchbar.R.drawable.ic_searchbar_wishlist_grey)
            btnNotification.setImageResource(com.tokopedia.searchbar.R.drawable.ic_searchbar_notif_grey)
            btn_inbox.setImageResource(R.drawable.ic_inbox_searcbar);
            toolbarType = TOOLBAR_DARK_TYPE
        }
    }

    fun switchToLightToolbar() {
        if (toolbarType != TOOLBAR_LIGHT_TYPE) {
            btnWishlist.setImageResource(com.tokopedia.searchbar.R.drawable.ic_searchbar_wishlist_white)
            btnNotification.setImageResource(com.tokopedia.searchbar.R.drawable.ic_searchbar_notif_white)
            btn_inbox.setImageResource(R.drawable.ic_inbox_searcbar);
            toolbarType = TOOLBAR_LIGHT_TYPE
        }
    }

    override fun showInboxIconForAbTest(shouldShowInbox: Boolean) {
        if (shouldShowInbox) {
            btnWishlist.tag = TAG_INBOX
            btnWishlist.setImageResource(R.drawable.ic_inbox_searcbar)
        } else {
            btnWishlist.tag = ""
            btnWishlist.setImageResource(R.drawable.ic_searchbar_wishlist_white)
        }
    }

    companion object {
        private val TOOLBAR_LIGHT_TYPE = 0
        private val TOOLBAR_DARK_TYPE = 1
    }
}
