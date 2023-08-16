package com.tokopedia.buy_more_get_more.olp.common.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.iconnotification.IconNotification
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import timber.log.Timber

class OlpToolbar : Toolbar {

    companion object {
        private const val ICON_DEFAULT_PERCENTAGE_X_POSITION = 1.25f
        private const val ICON_DEFAULT_PERCENTAGE_Y_POSITION = -0.45f
    }

    private val shape1 = ShapeDrawable()
    private val shape2 = ShapeDrawable()
    private val shapeListDrawable = LayerDrawable(arrayOf(shape1, shape2))

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
                cartButton?.apply {
                    notificationRef.setNotification(
                        notif = value.toString(),
                        notificationType = NotificationUnify.COUNTER_TYPE,
                        colorType = NotificationUnify.COLOR_PRIMARY
                    )
                    setNotifXY(ICON_DEFAULT_PERCENTAGE_X_POSITION, ICON_DEFAULT_PERCENTAGE_Y_POSITION)
                    notificationGravity = Gravity.TOP or Gravity.END
                    notificationRef.visible()
                }
            } else {
                cartButton?.notificationRef?.gone()
            }
            refreshViews()
        }

    private var tpgTitle: Typography? = null
    private var tpgSubTitle: Typography? = null
    var cartButton: IconNotification? = null
    var moreMenuButton: IconUnify? = null
    private var backIconWhite = getIconUnifyDrawable(
        context,
        IconUnify.ARROW_BACK,
        ContextCompat.getColor(context, R.color.Unify_Static_White)
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
        setPadding(0,0,0,0)
        setContentInsetsAbsolute(0,0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentInsetStartWithNavigation = 0
        }

        tpgTitle = findViewById(R.id.tpg_title)
        tpgSubTitle = findViewById(R.id.tpg_sub_title)
        cartButton = findViewById(R.id.cart)
        moreMenuButton = findViewById(R.id.more_menu)
        navigationIcon = backIconWhite
        setBackgroundColor(Color.TRANSPARENT)

        cartButton?.apply {
            imageDrawable = getIconUnifyDrawable(
                context,
                IconUnify.CART,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN0)
            )
        }
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        shape1.apply {
            setBounds(0, 0, measuredWidth, measuredHeight - 1.toPx())
            paint.color = ContextCompat.getColor(context, R.color.Unify_Header_Background)
        }

        shape2.apply {
            setBounds(0, measuredHeight - 1.toPx(), measuredWidth, measuredHeight)
            paint.color = ContextCompat.getColor(context, R.color.Unify_NN200)
        }

        backIconWhite?.setBounds(4.toPx(), 4.toPx(), 29.toPx(), 29.toPx())
    }

    private fun refreshViews() {
        tpgTitle?.text = title
        tpgSubTitle?.text = subTitle
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(
            44.toPx(),
            MeasureSpec.EXACTLY))
    }

}
