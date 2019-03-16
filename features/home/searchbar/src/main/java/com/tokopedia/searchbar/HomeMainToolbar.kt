package com.tokopedia.searchbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.searchbar.helper.ViewHelper
import kotlinx.android.synthetic.main.home_main_toolbar.view.*
import android.support.v4.graphics.drawable.DrawableCompat


class HomeMainToolbar : MainToolbar {

    var toolbarType: Int = 0

    var shadowApplied: Boolean = false

    lateinit var wishlistBitmapWhite: BitmapDrawable

    lateinit var notifBitmapWhite: BitmapDrawable

    lateinit var inboxBitmapWhite: BitmapDrawable

    lateinit var wishlistBitmapGrey: BitmapDrawable

    lateinit var notifBitmapGrey: BitmapDrawable

    lateinit var inboxBitmapGrey: BitmapDrawable

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)

        showShadow()

        setBackgroundAlpha(0f)

        toolbarType = TOOLBAR_LIGHT_TYPE

        initToolbarIcon()

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

    private lateinit var toolbarShadowBitmap: BitmapDrawable

    private lateinit var toolbarWhiteBitmap: BitmapDrawable

    private lateinit var wishlistCrossfader: TransitionDrawable

    private lateinit var notifCrossfader: TransitionDrawable

    private lateinit var inboxCrossfader: TransitionDrawable

    private fun initToolbarIcon() {
        wishlistBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_wishlist_white)
        notifBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_notif_white)
        inboxBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_inbox_white)

        wishlistBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_wishlist_grey)
        notifBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_notif_grey)
        inboxBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_inbox_grey)

        wishlistCrossfader = TransitionDrawable(arrayOf<Drawable>(wishlistBitmapGrey, wishlistBitmapWhite))
        notifCrossfader = TransitionDrawable(arrayOf<Drawable>(notifBitmapGrey, notifBitmapWhite))
        inboxCrossfader = TransitionDrawable(arrayOf<Drawable>(inboxBitmapGrey, inboxBitmapWhite))

        btnWishlist.setImageDrawable(wishlistCrossfader)
        btnNotification.setImageDrawable(notifCrossfader)
        btnInbox.setImageDrawable(inboxCrossfader)

        wishlistCrossfader.startTransition(0)
        notifCrossfader.startTransition(0)
        inboxCrossfader.startTransition(0)
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
            wishlistCrossfader.reverseTransition(200)
            notifCrossfader.reverseTransition(200)
            inboxCrossfader.reverseTransition(200)

            toolbarType = TOOLBAR_DARK_TYPE
        }
    }

    fun getBitmapDrawableFromVectorDrawable(context: Context, drawableId: Int): BitmapDrawable {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getDrawable(context, drawableId) as BitmapDrawable
        } else BitmapDrawable(context.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)

        return bitmap
    }

    fun switchToLightToolbar() {
        if (toolbarType != TOOLBAR_LIGHT_TYPE) {
            wishlistCrossfader.reverseTransition(200)
            notifCrossfader.reverseTransition(200)
            inboxCrossfader.reverseTransition(200)

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
