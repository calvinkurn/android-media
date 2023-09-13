package com.tokopedia.catalogcommon.customview

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.catalogcommon.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.applyIconUnifyColor
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class CatalogToolbar : Toolbar {

    var title: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var subTitle: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var cartCount: Int = 0
        set(value) {
            field = value
            if (value > Int.ZERO) {
                notification?.run {
                    visible()
                    setNotification(
                        notif = value.toString(),
                        notificationType = NotificationUnify.COUNTER_TYPE,
                        colorType = NotificationUnify.COLOR_PRIMARY
                    )
                }
            } else {
                notification?.gone()
            }
            refreshViews()
        }

    private var tpgTitle: Typography? = null
    private var tpgSubTitle: Typography? = null
    private var iconNotifWraper: FrameLayout? = null
    private var notification: NotificationUnify? = null
    private var backIconWhite = getIconUnifyDrawable(
        context,
        IconUnify.ARROW_BACK,
        ContextCompat.getColor(context, Unify_Static_Black)
    )

    var cartButton: IconUnify? = null
    var moreMenuButton: IconUnify? = null
    var shareButton: IconUnify? = null
    var searchButton: IconUnify? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initWithAttr(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initWithAttr(context, attributeSet)
    }

    init {
        View.inflate(context, R.layout.catalog_toolbar, this)
        setPadding(0, 0, 0, 0)
        setContentInsetsAbsolute(0, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentInsetStartWithNavigation = 0
        }

        tpgTitle = findViewById(R.id.tpg_title)
        tpgSubTitle = findViewById(R.id.tpg_sub_title)
        iconNotifWraper = findViewById(R.id.icon_notif_wrapper)
        cartButton = findViewById(R.id.cart)
        notification = findViewById(R.id.notification_counter)
        moreMenuButton = findViewById(R.id.more_menu)
        shareButton = findViewById(R.id.share)
        searchButton = findViewById(R.id.search)
        navigationIcon = backIconWhite
        setupIconRedirection()
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.CatalogToolbar)
        try {
            title = attributeArray.getString(R.styleable.CatalogToolbar_title) ?: ""
            subTitle = attributeArray.getString(R.styleable.CatalogToolbar_sub_title) ?: ""
            cartCount = attributeArray.getInt(R.styleable.CatalogToolbar_cart_count, 0)
        } catch (t: Throwable) {
            Timber.d(t.localizedMessage)
        } finally {
            attributeArray.recycle()
        }
    }

    private fun refreshViews() {
        tpgTitle?.text = title
        tpgSubTitle?.text = subTitle
    }

    fun setColors(@ColorInt color: Int) {
        applyIconUnifyColor(backIconWhite ?: return, color)
        applyIconUnifyColor(cartButton?.drawable ?: return, color)
        applyIconUnifyColor(moreMenuButton?.drawable ?: return, color)
        applyIconUnifyColor(shareButton?.drawable ?: return, color)
        applyIconUnifyColor(searchButton?.drawable ?: return, color)
        tpgTitle?.setTextColor(color)
    }

    fun setupIconRedirection() {
        cartButton?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.CART)
        }
        moreMenuButton?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.HOME_NAVIGATION)
        }
        searchButton?.setOnClickListener {
            RouteManager.route(context, ApplinkConst.DISCOVERY_SEARCH_AUTOCOMPLETE + "?q=" + title)
        }
    }
}
