package com.tokopedia.mvcwidget.multishopmvc

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.multishopmvc.data.AdInfo
import com.tokopedia.mvcwidget.multishopmvc.data.MultiShopModel
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_COUPON_TITLE
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_PRODUCT_CARD
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_SHOP_NAME
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

class MvcMultiShopView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var ivShopIcon: AppCompatImageView? = null
    private var ivShopChevron: AppCompatImageView? = null
    private var ivCouponOne: ImageUnify? = null
    private var ivCouponTwo: ImageUnify? = null
    private var productParentOne: ConstraintLayout? = null
    private var productParentTwo: ConstraintLayout? = null
    private var parentContainer: ConstraintLayout? = null
    private var tvShopName: Typography? = null
    private var tvCashBackTitle: Typography? = null
    private var tvCashBackValue: Typography? = null
    private var tvCouponCount: Typography? = null
    private var tvDealsCouponOne: Typography? = null
    private var tvDealsCouponTwo: Typography? = null

    init {
        View.inflate(context, R.layout.mvc_layout_mulitshop_item, this)

        ivShopIcon = findViewById(R.id.iv_shop_icon)
        ivShopChevron = findViewById(R.id.iv_shop_arrow)
        ivCouponOne = findViewById(R.id.iv_coupon1)
        ivCouponTwo = findViewById(R.id.iv_coupon2)
        productParentOne = findViewById(R.id.container_coupon1)
        productParentTwo = findViewById(R.id.container_coupon2)
        parentContainer = findViewById(R.id.parent_container)
        tvShopName = findViewById(R.id.tv_shop_name)
        tvCashBackTitle = findViewById(R.id.tv_cashback_title)
        tvCashBackValue = findViewById(R.id.tv_cashback_value)
        tvCouponCount = findViewById(R.id.tv_coupon_count)
        tvDealsCouponOne = findViewById(R.id.tv_deals_coupon1)
        tvDealsCouponTwo = findViewById(R.id.tv_deals_coupon2)
    }

    fun setMultiShopModel(item: MultiShopModel , @MvcSource source: Int) {

        item.shopIcon.let {
            if (it.isNotEmpty()) {
                ivShopIcon?.loadImage(it)
            }
        }
        if (item.products?.size != null && item.products.isNotEmpty()) {
            if (item.products.size == 1) {
                productParentOne?.show()
                item.products[0]?.imageURL?.let {
                    if (it.isNotEmpty()) {
                        ivCouponOne?.setImageUrl(it, 1f)
                    }
                }
                if (!item.products[0]?.benefitLabel.isNullOrEmpty()) {
                    tvDealsCouponOne?.show()
                    tvDealsCouponOne?.text = item.products[0]?.benefitLabel
                    if ((tvDealsCouponOne?.context).isDarkMode()) {
                        setStrokeColor(tvDealsCouponOne)
                    }
                } else {
                    tvDealsCouponOne?.hide()
                }
            }

            if (item.products.size > 1) {
                productParentTwo?.show()
                item.products[1]?.imageURL?.let {
                    if (it.isNotEmpty()) {
                        ivCouponTwo?.setImageUrl(it, 1f)
                    }
                }
                if (!item.products[1]?.benefitLabel.isNullOrEmpty()) {
                    tvDealsCouponTwo?.show()
                    tvDealsCouponTwo?.text = item.products[1]?.benefitLabel
                    if ((tvDealsCouponTwo?.context).isDarkMode()) {
                        setStrokeColor(tvDealsCouponTwo)
                    }
                } else {
                    tvDealsCouponTwo?.hide()
                }
                productParentOne?.show()
                item.products[0]?.imageURL?.let {
                    if (it.isNotEmpty()) {
                        ivCouponOne?.loadImage(it)
                    }
                }
                if (!item.products[0]?.benefitLabel.isNullOrEmpty()) {
                    tvDealsCouponOne?.show()
                    tvDealsCouponOne?.text = item.products[0]?.benefitLabel
                    if ((tvDealsCouponOne?.context).isDarkMode()) {
                        setStrokeColor(tvDealsCouponOne)
                    }
                } else {
                    tvDealsCouponOne?.hide()
                }
            }
        }
        tvShopName?.text = item.shopName
        tvCashBackTitle?.text = item.cashBackTitle
        tvCashBackValue?.text = item.cashBackValue
        tvCouponCount?.text = item.couponCount

        if ((this.context).isDarkMode()) {
            parentContainer?.background?.colorFilter = PorterDuffColorFilter(
                MethodChecker.getColor(
                    parentContainer?.context,
                    com.tokopedia.unifyprinciples.R.color.dark_N75
                ), PorterDuff.Mode.SRC_IN
            )
        }

        tvShopName?.setOnClickListener {
            shopClickListener(item,source)
        }

        ivShopChevron?.setOnClickListener {
            shopClickListener(item,source)
        }

        ivCouponOne?.setOnClickListener {
            RouteManager.route(this.context, item.products?.get(0)?.redirectAppLink)
            sendCouponClickEvent(
                item.shopName,
                CLICK_PRODUCT_CARD,
                item.AdInfo ?: AdInfo(),
                source
            )
        }

        ivCouponTwo?.setOnClickListener {
            RouteManager.route(this.context, item.products?.get(1)?.redirectAppLink)
            sendCouponClickEvent(
                item.shopName,
                CLICK_PRODUCT_CARD,
                item.AdInfo ?: AdInfo(),
                source
            )
        }

        tvCashBackTitle?.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_COUPON_TITLE,source)
        }
        tvCashBackValue?.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_COUPON_TITLE,source)
        }
        tvCouponCount?.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_COUPON_TITLE,source)
        }
        this.setOnClickListener {
            cardClickEvent(it.context,item,CLICK_PRODUCT_CARD,source)
        }
    }

    private fun cardClickEvent(
        context: Context,
        item: MultiShopModel,
        eventAction: String,
        source: Int
    ) {
        val shopName = item.shopName
        val shopApplink = item.applink
        val shopId = item.id
        context.startActivity(
            TransParentActivity.getIntent(
                context,
                shopId,
                0,
                shopApplink,
                shopName
            )
        )
        sendCouponClickEvent(
            item.shopName,
            eventAction,
            item.AdInfo ?: AdInfo(),
            source
        )
    }

    private fun shopClickListener(item: MultiShopModel, @MvcSource source: Int) {
        RouteManager.route(this.context, item.applink)
        sendCouponClickEvent(
            item.shopName,
            CLICK_SHOP_NAME,
            item.AdInfo ?: AdInfo(),
            source
        )
    }

    private fun sendTopadsClick(context: Context, adInfo: AdInfo?) {
        adInfo?.let {
            TopAdsUrlHitter(context).hitClickUrl(
                className,
                it.AdClickUrl,
                it.AdID,
                "",
                "",
                ""
            )
        }
    }

    private fun sendCouponClickEvent(
        shopName: String,
        eventAction: String,
        adInfo: AdInfo,
        @MvcSource mvcSource: Int
    ) {
        sendTopadsClick(this.context, adInfo)
        MvcTracker().mvcMultiShopCardClick(
            shopName,
            eventAction,
            mvcSource,
            UserSession(context).userId,
            "mvc section - $shopName"
        )
    }

    private fun setStrokeColor(view: Typography?) {
        val drawable = view?.background as GradientDrawable
        drawable.setStroke(
            view.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1)
                .toInt(),
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        )
        view.setTextColor(
            ContextCompat.getColor(
                view.context,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_White
            )
        )
    }

    companion object {
        const val className = "com.tokopedia.mvcwidget.multishopmvc.MvcMultiShopView"
    }
}