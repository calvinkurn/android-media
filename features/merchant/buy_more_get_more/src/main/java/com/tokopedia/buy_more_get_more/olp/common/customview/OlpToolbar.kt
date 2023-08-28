package com.tokopedia.buy_more_get_more.olp.common.customview

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifyprinciples.R.*
import com.tokopedia.unifyprinciples.R.color.Unify_NN0
import com.tokopedia.unifyprinciples.R.color.Unify_NN950
import com.tokopedia.unifyprinciples.R.color.Unify_Static_White
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class OlpToolbar : Toolbar {

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

    var showWhiteToolbar: Boolean = false
        set(value) {
            field = value
            if (value) {
                tpgTitle?.setTextColor(resources.getColor(Unify_NN950))
                tpgSubTitle?.setTextColor(resources.getColor(Unify_NN950))
                cartButton?.setBlackIconValue()
                moreMenuButton?.setBlackIconValue()
                navigationIcon = backIconBlack
            } else {
                tpgTitle?.setTextColor(resources.getColor(Unify_Static_White))
                tpgSubTitle?.setTextColor(resources.getColor(Unify_Static_White))
                cartButton?.setWhiteIconValue()
                moreMenuButton?.setWhiteIconValue()
                navigationIcon = backIconWhite
            }
        }

    private var tpgTitle: Typography? = null
    private var tpgSubTitle: Typography? = null
    private var iconNotifWraper: FrameLayout? = null
    var cartButton: IconUnify? = null
    private var notification: NotificationUnify? = null
    var moreMenuButton: IconUnify? = null
    private var backIconWhite = getIconUnifyDrawable(
        context,
        IconUnify.ARROW_BACK,
        ContextCompat.getColor(context, Unify_Static_White)
    )
    private var backIconBlack = getIconUnifyDrawable(
        context,
        IconUnify.ARROW_BACK,
        ContextCompat.getColor(context, Unify_NN950)
    )

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
        View.inflate(context, R.layout.olp_toolbar, this)
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
        navigationIcon = backIconWhite
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.OlpToolbar)
        try {
            title = attributeArray.getString(R.styleable.OlpToolbar_title) ?: ""
            subTitle = attributeArray.getString(R.styleable.OlpToolbar_sub_title) ?: ""
            cartCount = attributeArray.getInt(R.styleable.OlpToolbar_cart_count, 0)
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

    private fun IconUnify.setBlackIconValue() {
        val colorLightEnable = ContextCompat.getColor(context, Unify_NN950)
        val colorLightDisabled = ContextCompat.getColor(context, Unify_NN950)
        val colorDarkEnable = ContextCompat.getColor(context, Unify_NN950)
        val colorDarkDisable = ContextCompat.getColor(context, Unify_NN950)
        this.setImage(null, colorLightEnable, colorLightDisabled, colorDarkEnable, colorDarkDisable)
    }

    private fun IconUnify.setWhiteIconValue() {
        val colorLightEnable = ContextCompat.getColor(context, Unify_Static_White)
        val colorLightDisabled = ContextCompat.getColor(context, Unify_Static_White)
        val colorDarkEnable = ContextCompat.getColor(context, Unify_Static_White)
        val colorDarkDisable = ContextCompat.getColor(context, Unify_Static_White)
        this.setImage(null, colorLightEnable, colorLightDisabled, colorDarkEnable, colorDarkDisable)
    }
}
