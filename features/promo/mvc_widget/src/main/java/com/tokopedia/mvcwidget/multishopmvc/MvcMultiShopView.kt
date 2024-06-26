package com.tokopedia.mvcwidget.multishopmvc

import android.app.Application
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
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvcwidget.MVCActivityCallbacks
import com.tokopedia.mvcwidget.R
import com.tokopedia.mvcwidget.multishopmvc.data.AdInfo
import com.tokopedia.mvcwidget.multishopmvc.data.MultiShopModel
import com.tokopedia.mvcwidget.multishopmvc.data.ProductsItem
import com.tokopedia.mvcwidget.trackers.MvcSource
import com.tokopedia.mvcwidget.trackers.MvcTracker
import com.tokopedia.mvcwidget.trackers.MvcTrackerImpl
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_COUPON_TITLE
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_PRODUCT_CARD
import com.tokopedia.mvcwidget.trackers.Tracker.Event.CLICK_SHOP_NAME
import com.tokopedia.mvcwidget.views.activities.TransParentActivity
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MvcMultiShopView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
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
    private val tracker = MvcTracker()
    private val mvcActivityCallbacks = MVCActivityCallbacks()

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

    fun setTracker(mvcTrackerImpl: MvcTrackerImpl) {
        mvcActivityCallbacks.mvcTrackerImpl = mvcTrackerImpl
        tracker.trackerImpl = mvcTrackerImpl
    }

    fun setMultiShopModel(item: MultiShopModel, @MvcSource source: Int) {
        mvcActivityCallbacks.hashCodeForMVC = item.hashCode()
        item.shopIcon.let {
            if (it.isNotEmpty()) {
                ivShopIcon?.loadImage(it)
                ivShopIcon?.show()
            } else {
                ivShopIcon?.hide()
            }
        }

        renderFirstProductImage(item.products)
        renderSecondProductImage(item.products)

        tvShopName?.text = MethodChecker.fromHtml(item.shopName).toString()
        tvCashBackTitle?.text = item.cashBackTitle
        tvCashBackValue?.text = item.cashBackValue
        tvCouponCount?.text = item.couponCount
        val redirectAppLinkProductOne = item.products?.getOrNull(0)?.redirectAppLink
        val redirectAppLinkProductTwo = item.products?.getOrNull(1)?.redirectAppLink

        if ((this.context).isDarkMode()) {
            parentContainer?.background?.colorFilter = PorterDuffColorFilter(
                MethodChecker.getColor(
                    parentContainer?.context,
                    R.color.mvc_widget_dms_31353b
                ),
                PorterDuff.Mode.SRC_IN
            )
        }

        tvShopName?.setOnClickListener {
            shopClickListener(item, source)
        }

        ivShopChevron?.setOnClickListener {
            shopClickListener(item, source)
        }

        ivCouponOne?.setOnClickListener {
            redirectAppLinkProductOne?.let {
                RouteManager.route(this.context, redirectAppLinkProductOne)
            }
            sendCouponClickEvent(
                item.shopName,
                CLICK_PRODUCT_CARD,
                item.AdInfo ?: AdInfo(),
                FIRST_PRODUCT_POSITION,
                source
            )
        }

        ivCouponTwo?.setOnClickListener {
            redirectAppLinkProductTwo?.let {
                RouteManager.route(this.context, redirectAppLinkProductTwo)
            }
            sendCouponClickEvent(
                item.shopName,
                CLICK_PRODUCT_CARD,
                item.AdInfo ?: AdInfo(),
                SECOND_PRODUCT_POSITION,
                source
            )
        }

        tvCashBackTitle?.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_COUPON_TITLE, source)
        }
        tvCashBackValue?.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_COUPON_TITLE, source)
        }
        tvCouponCount?.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_COUPON_TITLE, source)
        }
        this.setOnClickListener {
            cardClickEvent(it.context, item, CLICK_PRODUCT_CARD, source)
        }
    }

    private fun renderFirstProductImage(products: List<ProductsItem?>?) {
        val productCount = products?.size

        productParentOne?.isVisible = productCount != null && products.isNotEmpty()
        val firstProduct = products?.firstOrNull()

        renderProductImage(ivCouponOne, firstProduct?.imageURL)
        renderBenefitLabel(tvDealsCouponOne, firstProduct?.benefitLabel)
    }

    private fun renderSecondProductImage(products: List<ProductsItem?>?) {
        val productCount = products?.size

        productParentTwo?.isVisible = productCount.orZero() > 1
        if (productParentTwo?.isVisible == true) {
            val secondProduct = products?.get(1)

            renderProductImage(ivCouponTwo, secondProduct?.imageURL)
            renderBenefitLabel(tvDealsCouponTwo, secondProduct?.benefitLabel)
        }
    }

    private fun renderBenefitLabel(view: Typography?, label: String?) {
        view?.isVisible = !label.isNullOrEmpty()

        view?.text = label

        if ((view?.context).isDarkMode()) {
            setStrokeColor(view)
        }
    }

    private fun renderProductImage(view: ImageUnify?, imageURL: String?) {
        if (!imageURL.isNullOrEmpty()) {
            view?.setImageUrl(imageURL, 1f)
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
        (context.applicationContext as Application).let { app ->
            app.unregisterActivityLifecycleCallbacks(mvcActivityCallbacks)
            app.registerActivityLifecycleCallbacks(mvcActivityCallbacks)
        }
        context.startActivity(
            TransParentActivity.getIntent(
                context,
                shopId,
                MvcSource.DEFAULT,
                shopApplink,
                shopName,
                mvcActivityCallbacks.hashCodeForMVC
            )
        )
        sendCouponClickEvent(
            item.shopName,
            eventAction,
            item.AdInfo ?: AdInfo(),
            PRODUCT_POSITION_NA,
            source
        )
    }

    private fun shopClickListener(item: MultiShopModel, @MvcSource source: Int) {
        RouteManager.route(this.context, item.applink)
        sendCouponClickEvent(
            item.shopName,
            CLICK_SHOP_NAME,
            item.AdInfo ?: AdInfo(),
            PRODUCT_POSITION_NA,
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
        productPosition: Int,
        @MvcSource mvcSource: Int
    ) {
        sendTopadsClick(this.context, adInfo)
        tracker.mvcMultiShopCardClick(
            shopName,
            eventAction,
            mvcSource,
            UserSession(context).userId,
            productPosition,
            "mvc section - $shopName"
        )
    }

    private fun setStrokeColor(view: Typography?) {
        val drawable = view?.background as GradientDrawable
        drawable.setStroke(
            view.context.resources.getDimension(unifyprinciplesR.dimen.spacing_lvl1)
                .toInt(),
            ContextCompat.getColor(
                view.context,
                unifyprinciplesR.color.Unify_Static_White
            )
        )
        view.setTextColor(
            ContextCompat.getColor(
                view.context,
                unifyprinciplesR.color.Unify_Static_White
            )
        )
    }

    companion object {
        const val className = "com.tokopedia.mvcwidget.multishopmvc.MvcMultiShopView"
        const val FIRST_PRODUCT_POSITION = 0
        const val SECOND_PRODUCT_POSITION = 1

//        Since we use same method sendCouponClickEvent() for all events, we send product position
//        as -1 for all events other than product click events.
        const val PRODUCT_POSITION_NA = -1
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        (context.applicationContext as Application).unregisterActivityLifecycleCallbacks(mvcActivityCallbacks)
    }
}
