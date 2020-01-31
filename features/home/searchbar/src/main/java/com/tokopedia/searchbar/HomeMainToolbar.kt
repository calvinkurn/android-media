package com.tokopedia.searchbar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.searchbar.helper.ViewHelper
import kotlinx.android.synthetic.main.home_main_toolbar.view.*
import java.net.URLEncoder
import kotlin.text.Charsets.UTF_8


class HomeMainToolbar : MainToolbar {
    private var toolbarType: Int = 0

    private var shadowApplied: Boolean = false

    private lateinit var wishlistCrossfader: TransitionDrawable

    private lateinit var notifCrossfader: TransitionDrawable

    private lateinit var inboxCrossfader: TransitionDrawable

    private lateinit var wishlistBitmapWhite: Drawable

    private lateinit var notifBitmapWhite: Drawable

    private lateinit var inboxBitmapWhite: Drawable

    private lateinit var wishlistBitmapGrey: Drawable

    private lateinit var notifBitmapGrey: Drawable

    private lateinit var inboxBitmapGrey: Drawable

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)

        showShadow()

        setBackgroundAlpha(0f)

        toolbarType = TOOLBAR_LIGHT_TYPE

        initToolbarIcon()

        switchToLightToolbar()
    }

    private fun initToolbarIcon() {
        wishlistBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_wishlist_white)
        notifBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_system_action_notification_pressed_24)
        inboxBitmapWhite = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_inbox_white)

        wishlistBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_searchbar_wishlist_grey)
        notifBitmapGrey = getBitmapDrawableFromVectorDrawable(context, R.drawable.ic_system_action_notification_normal_24)
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
            val pL = toolbar.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = ViewHelper.getStatusBarHeight(context)
            }
            val pR = toolbar.paddingRight
            val pB = 0
            toolbar!!.background = ColorDrawable(ContextCompat.getColor(context, R.color.white))
            toolbar!!.setPadding(pL, pT, pR, pB)
        }
    }

    fun showShadow() {
        if(!isShadowApplied()){
            shadowApplied = true
            val pL = toolbar.paddingLeft
            var pT = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pT = ViewHelper.getStatusBarHeight(context)
            }
            val pR = toolbar.paddingRight
            val pB = resources.getDimensionPixelSize(R.dimen.dp_8)

            toolbar!!.background = ContextCompat.getDrawable(context, R.drawable.searchbar_bg_shadow_bottom)
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

    private fun getBitmapDrawableFromVectorDrawable(context: Context, drawableId: Int): Drawable {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getDrawable(context, drawableId) as Drawable
        } else BitmapDrawable(context.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
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

    fun setHint(placeholder: String, keyword: String){
        val editTextSearch = findViewById<TextView>(R.id.et_search)
        editTextSearch.hint = if(placeholder.isEmpty()) context.getString(R.string.search_tokopedia) else placeholder
        editTextSearch.setSingleLine()
        editTextSearch.ellipsize = TextUtils.TruncateAt.END
        editTextSearch.setOnClickListener {
            searchBarAnalytics.eventTrackingSearchBar(screenName)
            if(placeholder.isEmpty()){
                RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE)
            }else{
                RouteManager.route(context, ApplinkConstInternalDiscovery.AUTOCOMPLETE + "?navsource={source}&hint={hint}", HOME_SOURCE, safeEncodeUTF8(keyword))
            }
        }
    }

    private fun safeEncodeUTF8(value: String): String {
        return try {
            URLEncoder.encode(value, UTF_8.toString())
        }
        catch (e: Throwable) {
            value
        }
    }

    companion object {
        const val TOOLBAR_LIGHT_TYPE = 0
        const val TOOLBAR_DARK_TYPE = 1
        private const val HOME_SOURCE = "home"
    }
}
