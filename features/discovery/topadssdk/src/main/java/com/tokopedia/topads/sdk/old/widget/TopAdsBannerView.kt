package com.tokopedia.topads.sdk.old.widget

import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.getBitmapImageUrl
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.common.adapter.Item
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.DILYANI_TOKOPEDIA
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.FULFILLMENT
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.LAYOUT_10
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.LAYOUT_11
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.LAYOUT_8
import com.tokopedia.topads.sdk.common.constants.TopAdsConstants.LAYOUT_9
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.topads.sdk.old.view.BannerAdsContract
import com.tokopedia.topads.sdk.utils.TopAdsSdkUtil
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.utils.snaphelper.GravitySnapHelper
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsAddToCartClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsShopFollowBtnClickListener
import com.tokopedia.topads.sdk.v2.shopadslayout10.ShopAdsLayout10View
import com.tokopedia.topads.sdk.v2.shopadslayout10.widget.ShopAdsSingleItemHorizontalLayout
import com.tokopedia.topads.sdk.v2.shopadslayout11.ShopAdsLayout11View
import com.tokopedia.topads.sdk.v2.shopadslayout11.widget.ShopAdsSingleItemVerticalLayout
import com.tokopedia.topads.sdk.v2.shopadslayout2.ShopAdsLayout2View
import com.tokopedia.topads.sdk.v2.shopadslayout5.ShopAdsLayout5View
import com.tokopedia.topads.sdk.v2.shopadslayout5.listener.ShopAdsProductListener
import com.tokopedia.topads.sdk.v2.shopadslayout5.uimodel.ShopProductModel
import com.tokopedia.topads.sdk.v2.shopadslayout5.widget.ShopAdsWithOneProductView
import com.tokopedia.topads.sdk.v2.shopadslayout6.ShopAdsLayout6View
import com.tokopedia.topads.sdk.v2.shopadslayout6.listener.TopAdsCarouselListener
import com.tokopedia.topads.sdk.v2.shopadslayout6.uimodel.TopAdsCarouselModel
import com.tokopedia.topads.sdk.v2.shopadslayout6.widget.ToadsCarousel
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.ShopAdsLayout8or9View
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.v2.shopadslayout8or9.widget.ShopAdsWithThreeProducts
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.ShopAdsProductListDefaultView
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.BannerAdsAdapter
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory.BannerAdsAdapterTypeFactory
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerProductShimmerUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopProductViewHolder
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopViewHolder
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShowMoreViewHolder
import com.tokopedia.topads.sdk.v2.uimodel.ShopAdsWithSingleProductModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import org.apache.commons.text.StringEscapeUtils
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by errysuprayogi on 12/28/17.
 */

internal const val NO_TEMPLATE = 0
internal const val SHOP_TEMPLATE = 1
internal const val DIGITAL_TEMPLATE = 2
internal const val LAYOUT_2 = 2
internal const val LAYOUT_5 = 5
internal const val LAYOUT_6 = 6
internal const val ITEM_3 = 3
internal const val ITEM_4 = 4

open class TopAdsBannerView : LinearLayout, BannerAdsContract.View {
    protected var topAdsBannerViewClickListener: TopAdsBannerClickListener? = null
    protected var impressionListener: TopAdsItemImpressionListener? = null
    private var topAdsShopFollowBtnClickListener: TopAdsShopFollowBtnClickListener? = null
    private var topAdsAddToCartClickListener: TopAdsAddToCartClickListener? = null
    private var shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener? = null
    protected var bannerAdsAdapter: BannerAdsAdapter? = null
    protected open val className: String = "com.tokopedia.topads.sdk.widget.TopAdsBannerView"
    private var showProductShimmer: Boolean = false
    protected var hasAddProductToCartButton: Boolean = false
    private var isFlashSaleTokoLabel: Boolean = false
    private var isShowCta: Boolean = true
    var impressionCount: Int = 0
    private var flashSaleTimerData: Date? = null
    private var topAdsFlashSaleTimer: TimerUnifySingle? = null
    protected var linearLayoutMerchantVoucher: LinearLayout? = null
    protected val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(context.applicationContext)
    }

    // this approach to support the separate the type of layout
    private var currentView: View? = null
    private var bannerAdsRendering: BaseBannerAdsRendering? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isFlashSaleTokoLabel && flashSaleTimerData != null) {
            if (Calendar.getInstance().time > flashSaleTimerData) {
                hideFlashSaleToko()
            } else {
                showFlashSaleToko()
            }
        } else {
            topAdsFlashSaleTimer?.hide()
        }
    }

    internal var template = NO_TEMPLATE

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    @Throws(Exception::class)
    protected open fun renderViewCpmShop(context: Context, cpmModel: CpmModel?, appLink: String, adsClickUrl: String, index: Int) {
        if (activityIsFinishing(context)) {
            return
        }
        val cpmData = cpmModel?.data?.firstOrNull()
        if (template == NO_TEMPLATE && isEligible(cpmData)) {
            View.inflate(getContext(), R.layout.layout_ads_banner_shop_a_pager, this)
            BannerShopProductViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product
            BannerShopViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a
            BannerShowMoreViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_more
            findViewById<TextView>(R.id.shop_name)?.text = escapeHTML(cpmData?.cpm?.name ?: "")
            bannerAdsAdapter = BannerAdsAdapter(BannerAdsAdapterTypeFactory(topAdsBannerViewClickListener, impressionListener, topAdsAddToCartClickListener))
            val list = findViewById<RecyclerView>(R.id.list)
            list.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            list.adapter = bannerAdsAdapter
            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(list)

            template = SHOP_TEMPLATE
        }
        setHeadlineShopData(cpmModel, appLink, adsClickUrl, index)
    }

    private fun setHeadlineShopData(cpmModel: CpmModel?, appLink: String, adsClickUrl: String, index: Int) {
        val adsBannerShopCardView = findViewById<ShopCardView?>(R.id.adsBannerShopCardView)
        val shopDetail = findViewById<View?>(R.id.shop_detail)
        val topAdsCarousel = findViewById<ToadsCarousel>(R.id.TopAdsCarousel)
        val shopAdsProductView = findViewById<ShopAdsWithOneProductView>(R.id.shopAdsProductView)
        val list = findViewById<RecyclerView?>(R.id.list)
        val container = findViewById<View>(R.id.container)
        val cpmData = cpmModel?.data?.getOrNull(index)
        val shopAdsWithThreeProducts = findViewById<ShopAdsWithThreeProducts>(R.id.shopAdsWithThreeProducts)
        val shopAdsWithSingleProductHorizontal = findViewById<ShopAdsSingleItemHorizontalLayout>(R.id.shopAdsWithSingleProductHorizontal)
        val shopAdsWithSingleProductVertical = findViewById<ShopAdsSingleItemVerticalLayout>(R.id.shopAdsWithSingleProductVertical)
        linearLayoutMerchantVoucher = findViewById(R.id.linearLayoutMerchantVoucher)
        topAdsFlashSaleTimer = findViewById(R.id.topAdsFlashSaleTimer)

        if (cpmData?.cpm?.layout != LAYOUT_6 && cpmData?.cpm?.layout != LAYOUT_5 && cpmData?.cpm?.layout != LAYOUT_8 && cpmData?.cpm?.layout != LAYOUT_9 && cpmData?.cpm?.layout != LAYOUT_10 && cpmData?.cpm?.layout != LAYOUT_11) {
            if (isEligible(cpmData)) {
                if (cpmData != null && (cpmData.cpm.layout == LAYOUT_2)) {
                    list?.gone()
                    shopDetail?.gone()
                    topAdsCarousel.hide()
                    shopAdsProductView.hide()
                    adsBannerShopCardView?.visible()
                    shopAdsWithThreeProducts.hide()
                    shopAdsWithSingleProductHorizontal.hide()
                    shopAdsWithSingleProductVertical.hide()
                    container?.setBackgroundResource(0)
                    (container?.layoutParams as? MarginLayoutParams)?.setMargins(0, 4.toPx(), 0, 0)

                    setHeadlineShopDataCardWidget(cpmData, adsBannerShopCardView, appLink, adsClickUrl)
                } else if (cpmData != null) {
                    list?.visible()
                    list?.scrollToPosition(0)
                    shopDetail?.visible()
                    topAdsCarousel.hide()
                    shopAdsProductView.hide()
                    shopAdsWithThreeProducts.hide()
                    shopAdsWithSingleProductHorizontal.hide()
                    shopAdsWithSingleProductVertical.hide()
                    adsBannerShopCardView?.gone()
                    list.isNestedScrollingEnabled = false

                    if (cpmData.cpm.cpmShop.isPowerMerchant && !cpmData.cpm.cpmShop.isOfficial) {
                        container?.background = ContextCompat.getDrawable(context, R.drawable.bg_pm_gradient)
                    } else if (cpmData.cpm.cpmShop.isOfficial) {
                        container?.background = ContextCompat.getDrawable(context, R.drawable.bg_os_gradient)
                    } else {
                        container?.background = ContextCompat.getDrawable(context, R.drawable.bg_rm_gradient)
                    }

                    val shop_badge = findViewById<ImageView>(R.id.shop_badge)
                    shop_badge?.let {
                        if (cpmData.cpm?.badges?.size ?: 0 > 0) {
                            shop_badge.show()
                            it.loadImage(cpmData.cpm.badges?.firstOrNull()?.imageUrl)
                        } else {
                            shop_badge.hide()
                        }
                    }
                    findViewById<Typography>(R.id.shop_name)?.text = MethodChecker.fromHtml(cpmData.cpm.cpmShop.name)
                    findViewById<Typography>(R.id.description)?.text = cpmData.cpm.cpmShop.slogan
                    val btnFollow = findViewById<UnifyButton>(R.id.btnFollow)
                    if (cpmData.cpm.cpmShop.isFollowed != null && topAdsShopFollowBtnClickListener != null) {
                        bindFavorite(cpmData.cpm.cpmShop.isFollowed)
                        btnFollow.setOnClickListener {
                            cpmData.cpm.cpmShop.id.let { it1 -> topAdsShopFollowBtnClickListener?.onFollowClick(it1, cpmData.id) }
                            if (!cpmData.cpm.cpmShop.isFollowed) {
                                topAdsUrlHitter.hitClickUrl(className, cpmData.adClickUrl, "", "", "")
                            }
                        }
                        btnFollow.show()
                    } else {
                        btnFollow.hide()
                    }

                    shopDetail.setOnClickListener {
                        if (topAdsBannerViewClickListener != null) {
                            topAdsBannerViewClickListener!!.onBannerAdsClicked(1, cpmData.applinks, cpmData)
                            topAdsUrlHitter.hitClickUrl(className, cpmData.adClickUrl, "", "", "")
                        }
                    }

                    val shop_image = findViewById<ImageView>(R.id.shop_image)
                    shop_image?.let {
                        it.loadImage(cpmData.cpm.cpmImage.fullEcs)
                        cpmData.cpm.cpmShop.imageShop?.let { it1 ->
                            shop_image.addOnImpressionListener(it1) {
                                impressionListener?.let {
                                    it.onImpressionHeadlineAdsItem(0, cpmData)
                                    topAdsUrlHitter.hitImpressionUrl(className, cpmData.cpm.cpmImage.fullUrl, "", "", "")
                                }
                            }
                        }
                    }

                    renderLabelMerchantVouchers(cpmData)

                    renderFlashSaleTimer(cpmData.cpm.flashSaleCampaignDetail)

                    val items = ArrayList<Item<*>>()
                    items.add(
                        BannerShopUiModel(
                            cpmData,
                            appLink,
                            adsClickUrl,
                            isShowCta
                        )
                    )
                    if (cpmData.cpm.cpmShop.products.isNotEmpty()) {
                        val productCardModelList: ArrayList<ProductCardModel> = getProductCardModels(cpmData.cpm.cpmShop.products)
                        for (i in 0 until productCardModelList.size) {
                            if (i < ITEM_3) {
                                val model = BannerShopProductUiModel(
                                    cpmData,
                                    productCardModelList[i],
                                    cpmData.cpm.cpmShop.products[i].applinks,
                                    cpmData.cpm.cpmShop.products[i].image.m_url,
                                    cpmData.cpm.cpmShop.products[i].imageProduct.imageClickUrl
                                )
                                val product = cpmData.cpm.cpmShop.products[i]
                                model.apply {
                                    productId = product.id
                                    productName = product.name
                                    productMinOrder = product.productMinimumOrder
                                    productCategory = product.categoryBreadcrumb
                                    productPrice = product.priceFormat
                                    shopId = cpmData.cpm.cpmShop.id
                                }
                                items.add(model)
                            }
                        }
                        if (productCardModelList.size < ITEM_3) {
                            items.add(BannerShopViewMoreUiModel(cpmData, appLink, adsClickUrl))
                        }
                    } else {
                        repeat(ITEM_3) { items.add(BannerProductShimmerUiModel()) }
                    }
                    bannerAdsAdapter?.setList(items)
                }
            }
        } else if (cpmData.cpm?.layout == LAYOUT_6 && isEligible(cpmData)) {
            adsBannerShopCardView?.gone()
            shopDetail?.gone()
            list?.gone()
            shopAdsWithThreeProducts.hide()
            shopAdsWithSingleProductHorizontal.hide()
            shopAdsWithSingleProductVertical.hide()
            topAdsCarousel.show()
            container?.background = ContextCompat.getDrawable(context, R.drawable.bg_os_gradient)
            setTopAdsCarousel(cpmModel, topAdsCarousel)
        } else if (cpmData.cpm?.layout == LAYOUT_5 && isEligible(cpmData)) {
            adsBannerShopCardView?.gone()
            shopDetail?.gone()
            list?.gone()
            shopAdsProductView.show()
            shopAdsWithThreeProducts.hide()
            shopAdsWithSingleProductHorizontal.hide()
            shopAdsWithSingleProductVertical.hide()
            container?.setBackgroundResource(0)
            setShopAdsProduct(cpmModel, shopAdsProductView)
        } else if ((cpmData.cpm?.layout == LAYOUT_8 || cpmData.cpm?.layout == LAYOUT_9) && isEligible(cpmData)) {
            topAdsCarousel.hide()
            shopDetail?.hide()
            shopAdsProductView.hide()
            adsBannerShopCardView?.hide()
            shopAdsWithThreeProducts.show()
            shopAdsWithSingleProductHorizontal.hide()
            shopAdsWithSingleProductVertical.hide()
            list?.hide()
            setWidget(cpmData, appLink, adsClickUrl, shopAdsWithThreeProducts, topAdsBannerViewClickListener, hasAddProductToCartButton)
        } else if (cpmData?.cpm?.layout == LAYOUT_10) {
            topAdsCarousel.hide()
            shopDetail?.hide()
            shopAdsProductView.hide()
            adsBannerShopCardView?.hide()
            shopAdsWithThreeProducts.hide()
            list?.hide()
            shopAdsWithSingleProductHorizontal.show()
            shopAdsWithSingleProductVertical.hide()
            shopAdsWithSingleProductHorizontal.setShopProductModel(getSingleAdsProductModel(cpmData, appLink, adsClickUrl, topAdsBannerViewClickListener, hasAddProductToCartButton))
        } else if (cpmData?.cpm?.layout == LAYOUT_11) {
            topAdsCarousel.hide()
            shopDetail?.hide()
            shopAdsProductView.hide()
            adsBannerShopCardView?.hide()
            shopAdsWithThreeProducts.hide()
            list?.hide()
            shopAdsWithSingleProductHorizontal.hide()
            shopAdsWithSingleProductVertical.show()
            shopAdsWithSingleProductVertical.setShopProductModel(getSingleAdsProductModel(cpmData, appLink, adsClickUrl, topAdsBannerViewClickListener, hasAddProductToCartButton))
        }
    }

    protected open fun renderTopAdsBannerV2(cpmModel: CpmModel, appLink: String, adsClickUrl: String, index: Int, isReimagine: Boolean) {
        val cpmData = cpmModel.data.getOrNull(index)

        when {
            cpmData?.cpm?.layout != LAYOUT_6 && cpmData?.cpm?.layout != LAYOUT_5 &&
                cpmData?.cpm?.layout != LAYOUT_8 && cpmData?.cpm?.layout != LAYOUT_9 &&
                cpmData?.cpm?.layout != LAYOUT_10 && cpmData?.cpm?.layout != LAYOUT_11 -> {
                if (isEligible(cpmData)) {
                    when (cpmData?.cpm?.layout) {
                        LAYOUT_2 -> {
                            renderShopAdsLayout2(cpmModel, cpmData, appLink, adsClickUrl)
                        }

                        else -> {
                            cpmData?.let {
                                renderShopAdsProductListDefault(cpmModel, it, appLink, adsClickUrl, isReimagine)
                            }
                        }
                    }
                }
            }

            cpmData.cpm.layout == LAYOUT_6 && isEligible(cpmData) -> {
                renderShopAdsLayout6(cpmModel, cpmData, appLink, adsClickUrl)
            }

            cpmData.cpm.layout == LAYOUT_5 && isEligible(cpmData) -> {
                renderShopAdsLayout5(cpmModel, cpmData, appLink, adsClickUrl, isReimagine)
            }

            (cpmData.cpm.layout == LAYOUT_8 || cpmData.cpm.layout == LAYOUT_9) && isEligible(cpmData) -> {
                renderShopAdsLayout8or9(cpmModel, cpmData, appLink, adsClickUrl)
            }

            cpmData.cpm.layout == LAYOUT_10 -> {
                renderShopAdsLayout10(cpmModel, cpmData, appLink, adsClickUrl)
            }

            cpmData.cpm.layout == LAYOUT_11 -> {
                renderShopAdsLayout11(cpmModel, cpmData, appLink, adsClickUrl)
            }
        }
    }

    private fun renderShopAdsLayout11(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsLayout11View) {
            currentView = null
            val view = getCurrentView(R.layout.item_ads_shop_layout_11)
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsLayout11View(
                view,
                WeakReference(context),
                topAdsBannerViewClickListener,
                impressionListener
            )
        }

        (bannerAdsRendering as? ShopAdsLayout11View)?.let {
            it.setHasAddProductToCartButton(hasAddProductToCartButton)
            it.render(cpmModel, cpmData, appLink, adsClickUrl)
        }
    }

    private fun renderShopAdsLayout10(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsLayout10View) {
            currentView = null
            val view = getCurrentView(R.layout.item_ads_shop_layout_10)
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsLayout10View(
                view,
                WeakReference(context),
                topAdsBannerViewClickListener,
                impressionListener
            )
        }

        (bannerAdsRendering as? ShopAdsLayout10View)?.let {
            it.setHasAddProductToCartButton(hasAddProductToCartButton)
            it.render(cpmModel, cpmData, appLink, adsClickUrl)
        }
    }

    private fun renderShopAdsLayout8or9(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsLayout8or9View) {
            currentView = null
            val view = getCurrentView(R.layout.item_ads_shop_layout_8_or_9)
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsLayout8or9View(
                view,
                WeakReference(context),
                topAdsBannerViewClickListener,
                impressionListener,
                shopWidgetAddToCartClickListener
            )
        }

        (bannerAdsRendering as? ShopAdsLayout8or9View)?.let {
            it.setIsShowCta(isShowCta)
            it.setHasAddProductToCartButton(hasAddProductToCartButton)
            it.render(cpmModel, cpmData, appLink, adsClickUrl)
        }
    }

    private fun renderShopAdsLayout5(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String, isReimagine: Boolean) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsLayout5View) {
            currentView = null
            val view = if (isReimagine) {
                getCurrentView(R.layout.item_ads_shop_layout_5_reimagine)
            } else {
                getCurrentView(R.layout.item_ads_shop_layout_5)
            }
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsLayout5View(
                view,
                WeakReference(context),
                topAdsUrlHitter,
                topAdsBannerViewClickListener,
                impressionListener,
                isReimagine
            )
        }
        bannerAdsRendering?.render(cpmModel, cpmData, appLink, adsClickUrl)
    }

    private fun renderShopAdsLayout6(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsLayout6View) {
            currentView = null
            val view = getCurrentView(R.layout.item_ads_shop_layout_6)
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsLayout6View(
                view,
                WeakReference(context),
                topAdsUrlHitter,
                topAdsBannerViewClickListener,
                impressionListener
            )
        }
        bannerAdsRendering?.render(cpmModel, cpmData, appLink, adsClickUrl)
    }

    private fun renderShopAdsProductListDefault(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String, isReimagine: Boolean) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsProductListDefaultView) {
            currentView = null
            val view = if (isReimagine) {
                getCurrentView(R.layout.item_ads_banner_product_list_default_reimagine)
            } else {
                getCurrentView(R.layout.item_ads_banner_product_list_default)
            }
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsProductListDefaultView(
                view,
                WeakReference(context),
                topAdsUrlHitter,
                topAdsBannerViewClickListener,
                impressionListener,
                topAdsAddToCartClickListener,
                topAdsShopFollowBtnClickListener,
                isReimagine
            )
        }

        (bannerAdsRendering as? ShopAdsProductListDefaultView)?.let {
            it.setIsShowCta(isShowCta)
            it.setHasAddProductToCartButton(hasAddProductToCartButton)
            it.render(cpmModel, cpmData, appLink, adsClickUrl)
        }
    }

    private fun renderShopAdsLayout2(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (bannerAdsRendering == null || bannerAdsRendering !is ShopAdsLayout2View) {
            currentView = null
            val view = getCurrentView(R.layout.item_ads_shop_card_view)
            bannerAdsRendering = null
            bannerAdsRendering = ShopAdsLayout2View(
                view,
                WeakReference(context),
                topAdsUrlHitter,
                impressionListener,
                topAdsBannerViewClickListener
            )
        }

        bannerAdsRendering?.render(cpmModel, cpmData, appLink, adsClickUrl)
    }

    fun getCurrentView(layoutId: Int): View? {
        if (currentView == null) {
            currentView = View.inflate(context, layoutId, this)
        }
        return currentView
    }

    private fun hideFlashSaleToko() {
        linearLayoutMerchantVoucher?.hide()
        topAdsFlashSaleTimer?.hide()
    }

    private fun showFlashSaleToko() {
        linearLayoutMerchantVoucher?.show()
        topAdsFlashSaleTimer?.show()
    }

    private fun renderFlashSaleTimer(flashSaleCampaignDetail: FlashSaleCampaignDetail) {
        flashSaleTimerData = null
        val startDate = TopAdsSdkUtil.parseData(flashSaleCampaignDetail.startTime)
        val endDate = TopAdsSdkUtil.parseData(flashSaleCampaignDetail.endTime)
        val isTimerValid = TopAdsSdkUtil.isTimerValid(startDate, endDate)
        if (isTimerValid && endDate != null) {
            flashSaleTimerData = endDate
            val currentSystemTime = Calendar.getInstance().time
            val saleTimeMillis = endDate.time - currentSystemTime.time
            if (saleTimeMillis > Int.ZERO) {
                val parsedCalendar: Calendar = Calendar.getInstance()
                parsedCalendar.time = endDate
                topAdsFlashSaleTimer?.targetDate = parsedCalendar
                showFlashSaleToko()
                topAdsFlashSaleTimer?.onFinish = {
                    hideFlashSaleToko()
                }
            }
        } else {
            if (isFlashSaleTokoLabel) {
                hideFlashSaleToko()
            }
        }
    }

    protected open fun setWidget(
        cpmData: CpmData,
        appLink: String,
        adsClickUrl: String,
        shopAdsWithThreeProducts: ShopAdsWithThreeProducts?,
        topAdsBannerClickListener: TopAdsBannerClickListener?,
        hasAddToCartButton: Boolean
    ) {
        shopAdsWithThreeProducts?.setWidgetModel(
            ShopAdsWithThreeProductModel(
                isOfficial = cpmData.cpm.cpmShop.isPowerMerchant,
                isPMPro = cpmData.cpm.cpmShop.isPMPro,
                isPowerMerchant = cpmData.cpm.cpmShop.isPowerMerchant,
                shopBadge = cpmData.cpm.badges.firstOrNull()?.imageUrl ?: "",
                shopName = cpmData.cpm.cpmShop.name,
                shopImageUrl = cpmData.cpm.cpmImage.fullEcs,
                shopWidgetImageUrl = cpmData.cpm.widgetImageUrl,
                merchantVouchers = cpmData.cpm.cpmShop.merchantVouchers,
                listItems = getItems(cpmData, appLink, adsClickUrl),
                items = cpmData,
                shopApplink = appLink,
                adsClickUrl = adsClickUrl,
                topAdsBannerClickListener = topAdsBannerClickListener,
                hasAddToCartButton = hasAddToCartButton,
                impressionListener = impressionListener,
                shopWidgetAddToCartClickListener = shopWidgetAddToCartClickListener,
                variant = cpmData.cpm?.layout ?: 0
            )
        )
    }

    private fun getItems(cpmData: CpmData, appLink: String, adsClickUrl: String): MutableList<Item<*>> {
        val items = ArrayList<Item<*>>()
        items.add(
            BannerShopUiModel(
                cpmData,
                appLink,
                adsClickUrl,
                isShowCta
            )
        )
        if (cpmData.cpm.cpmShop.products.isNotEmpty()) {
            val productCardModelList: ArrayList<ProductCardModel> =
                getProductCardModels(cpmData.cpm.cpmShop.products)
            for (i in 0 until productCardModelList.size) {
                if (i < ITEM_3) {
                    val model = BannerShopProductUiModel(
                        cpmData,
                        productCardModelList[i],
                        cpmData.cpm.cpmShop.products[i].applinks,
                        cpmData.cpm.cpmShop.products[i].image.m_url,
                        cpmData.cpm.cpmShop.products[i].imageProduct.imageClickUrl
                    )
                    val product = cpmData.cpm.cpmShop.products[i]
                    model.apply {
                        productId = product.id
                        productName = product.name
                        productMinOrder = product.productMinimumOrder
                        productCategory = product.categoryBreadcrumb
                        productPrice = product.priceFormat
                        shopId = cpmData.cpm.cpmShop.id
                    }
                    items.add(model)
                }
            }
            if (productCardModelList.size < ITEM_3) {
                items.add(
                    BannerShopViewMoreUiModel(
                        cpmData,
                        appLink,
                        adsClickUrl
                    )
                )
            }
        } else {
            repeat(ITEM_3) { items.add(BannerProductShimmerUiModel()) }
        }
        return items
    }

    private fun getSingleAdsProductModel(
        cpmData: CpmData,
        appLink: String,
        adsClickUrl: String,
        topAdsBannerClickListener: TopAdsBannerClickListener?,
        hasAddProductToCartButton: Boolean
    ): ShopAdsWithSingleProductModel {
        return ShopAdsWithSingleProductModel(
            isOfficial = cpmData.cpm.cpmShop.isOfficial,
            isPMPro = cpmData.cpm.cpmShop.isPMPro,
            isPowerMerchant = cpmData.cpm.cpmShop.isPowerMerchant,
            shopBadge = cpmData.cpm.badges.firstOrNull()?.imageUrl ?: "",
            shopName = cpmData.cpm.cpmShop.name,
            shopImageUrl = cpmData.cpm.cpmImage.fullEcs,
            slogan = cpmData.cpm.cpmShop.slogan,
            shopWidgetImageUrl = cpmData.cpm.widgetImageUrl,
            merchantVouchers = cpmData.cpm.cpmShop.merchantVouchers,
            listItem = cpmData.cpm.cpmShop.products.firstOrNull(),
            shopApplink = appLink,
            adsClickUrl = adsClickUrl,
            hasAddToCartButton = hasAddProductToCartButton,
            variant = cpmData.cpm.layout,
            topAdsBannerClickListener = topAdsBannerClickListener,
            impressionListener = impressionListener,
            cpmData = cpmData,
            impressHolder = cpmData.cpm.cpmShop.imageShop
        )
    }

    private fun setShopAdsProduct(cpmModel: CpmModel, shopAdsProductView: ShopAdsWithOneProductView) {
        shopAdsProductView.setShopProductModel(
            ShopProductModel(
                title = cpmModel.data.firstOrNull()?.cpm?.widgetTitle ?: "",
                items = getShopProductItem(cpmModel)
            ),
            object : ShopAdsProductListener {
                override fun onItemImpressed(position: Int) {
                    val cpmData = cpmModel.data.getOrNull(position)
                    impressionCount = position + 1
                    cpmData?.let { impressionListener?.onImpressionHeadlineAdsItem(position, it) }
                    topAdsUrlHitter.hitImpressionUrl(
                        className,
                        cpmData?.cpm?.cpmImage?.fullUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onItemClicked(position: Int) {
                    val cpmData = cpmModel.data.getOrNull(position)
                    topAdsBannerViewClickListener?.onBannerAdsClicked(position, cpmData?.applinks, cpmData)
                    topAdsUrlHitter.hitClickUrl(
                        className,
                        cpmData?.adClickUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }
            },
            null
        )
    }

    protected open fun getShopProductItem(cpmModel: CpmModel): List<ShopProductModel.ShopProductModelItem> {
        val list = arrayListOf<ShopProductModel.ShopProductModelItem>()
        cpmModel.data?.forEachIndexed { index, it ->
            val item = ShopProductModel.ShopProductModelItem(
                imageUrl = it.cpm.cpmShop.products.firstOrNull()?.imageProduct?.imageUrl ?: "",
                shopIcon = it.cpm.cpmImage.fullEcs,
                shopName = it.cpm.cpmShop.name,
                ratingCount = it.cpm.cpmShop.products.firstOrNull()?.countReviewFormat ?: "",
                ratingAverage = it.cpm.cpmShop.products.firstOrNull()?.headlineProductRatingAverage
                    ?: "",
                isOfficial = it.cpm.cpmShop.isOfficial,
                isPMPro = it.cpm.cpmShop.isPMPro,
                goldShop = if (it.cpm.cpmShop.isPowerMerchant) 1 else 0,
                impressHolder = it.cpm.cpmShop.imageShop,
                location = it.cpm.cpmShop.location,
                position = index
            )
            list.add(item)
        }
        return list
    }

    protected open fun setTopAdsCarousel(cpmModel: CpmModel?, topAdsCarousel: ToadsCarousel?) {
        topAdsCarousel?.setTopAdsCarouselModel(
            TopAdsCarouselModel(
                title = cpmModel?.data?.firstOrNull()?.cpm?.widgetTitle ?: "",
                items = getTopAdsItem(cpmModel)
            ),
            object : TopAdsCarouselListener {
                override fun onItemImpressed(position: Int) {
                    val cpmData = cpmModel?.data?.getOrNull(position)
                    impressionCount = position + 1
                    cpmData?.let { impressionListener?.onImpressionHeadlineAdsItem(position, it) }
                    topAdsUrlHitter.hitImpressionUrl(
                        className,
                        cpmData?.cpm?.cpmImage?.fullUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onItemClicked(position: Int) {
                    val cpmData = cpmModel?.data?.getOrNull(position)
                    topAdsBannerViewClickListener?.onBannerAdsClicked(
                        position,
                        cpmData?.applinks,
                        cpmData
                    )
                    topAdsUrlHitter.hitClickUrl(
                        className,
                        cpmData?.adClickUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onProductItemClicked(productIndex: Int, shopIndex: Int) {
                    val cpmData = cpmModel?.data?.getOrNull(shopIndex) ?: return
                    val cpmDataProducts = cpmData.cpm.cpmShop.products

                    if (!cpmDataProducts.isNullOrEmpty()) {
                        val product = cpmDataProducts[productIndex]
                        topAdsBannerViewClickListener?.onBannerAdsClicked(
                            productIndex,
                            product.applinks,
                            cpmData
                        )
                        topAdsUrlHitter.hitClickUrl(
                            className,
                            product.imageProduct.imageClickUrl,
                            product.id,
                            product.uri,
                            product.imageProduct.imageUrl
                        )
                    }
                }
            }
        )
    }

    private fun getTopAdsItem(cpmModel: CpmModel?): List<TopAdsCarouselModel.TopAdsCarouselItem> {
        val list = arrayListOf<TopAdsCarouselModel.TopAdsCarouselItem>()
        cpmModel?.data?.forEachIndexed { index, it ->
            val item = TopAdsCarouselModel.TopAdsCarouselItem(
                imageUrlOne = it.cpm.cpmShop.products.getOrNull(0)?.imageProduct?.imageUrl ?: "",
                imageUrlTwo = it.cpm.cpmShop.products.getOrNull(1)?.imageProduct?.imageUrl ?: "",
                brandIcon = it.cpm.cpmImage.fullEcs,
                brandName = it.cpm.cpmShop.name,
                isOfficial = it.cpm.cpmShop.isOfficial,
                isPMPro = it.cpm.cpmShop.isPMPro,
                goldShop = if (it.cpm.cpmShop.isPowerMerchant) 1 else 0,
                impressHolder = it.cpm.cpmShop.imageShop,
                position = index
            )
            list.add(item)
        }
        return list
    }

    protected open fun getProductCardModels(products: List<Product>): ArrayList<ProductCardModel> {
        return ArrayList<ProductCardModel>().apply {
            products.map {
                add(getProductCardViewModel(it))
            }
        }
    }

    private fun getProductCardViewModel(product: Product): ProductCardModel {
        val isAvailAble = checkIfDTAvailable(product.labelGroupList)
        val productCardModel = ProductCardModel(
            productImageUrl = product.imageProduct.imageUrl,
            productName = product.name,
            discountPercentage = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
            formattedPrice = product.priceFormat,
            reviewCount = product.countReviewFormat.toIntOrZero(),
            ratingString = product.productRatingFormat,
            freeOngkir = ProductCardModel.FreeOngkir(
                product.freeOngkir.isActive,
                product.freeOngkir.imageUrl
            ),
            hasAddToCartButton = this.hasAddProductToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN,
            stockBarPercentage = product.stock_info.soldStockPercentage,
            stockBarLabel = product.stock_info.stockWording,
            stockBarLabelColor = product.stock_info.stockColour,
            shopBadgeList = product.badges.map {
                ProductCardModel.ShopBadge(
                    imageUrl = it.imageUrl,
                    title = it.title,
                    isShown = it.isShow
                )
            }
        )
        return getProductModelOnCondition(product, isAvailAble, productCardModel)
    }

    private fun getProductModelOnCondition(
        product: Product,
        isAvailAble: Boolean,
        productCardModel: ProductCardModel
    ): ProductCardModel {
        if (isAvailAble) {
            return if (!product.campaign.originalPrice.isNullOrEmpty()) {
                productCardModel.copy(
                    slashedPrice = product.campaign.originalPrice,
                    labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                        product.labelGroupList.map {
                            if (it.position != "integrity") {
                                add(
                                    ProductCardModel.LabelGroup(
                                        position = it.position,
                                        title = it.title,
                                        type = it.type,
                                        imageUrl = it.imageUrl,
                                        styleList = it.styleList.map { style ->
                                            ProductCardModel.LabelGroup.Style(style.key, style.value)
                                        }
                                    )
                                )
                            }
                        }
                    }
                )
            } else {
                productCardModel.copy(
                    labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                        product.labelGroupList.map {
                            if (it.position != "integrity") {
                                add(
                                    ProductCardModel.LabelGroup(
                                        position = it.position,
                                        title = it.title,
                                        type = it.type,
                                        imageUrl = it.imageUrl,
                                        styleList = it.styleList.map { style ->
                                            ProductCardModel.LabelGroup.Style(style.key, style.value)
                                        }
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }

        return productCardModel.copy(
            slashedPrice = product.campaign.originalPrice,
            countSoldRating = product.headlineProductRatingAverage,
            labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                product.labelGroupList.map {
                    add(
                        ProductCardModel.LabelGroup(
                            position = it.position,
                            title = it.title,
                            type = it.type,
                            imageUrl = it.imageUrl,
                            styleList = it.styleList.map { style ->
                                ProductCardModel.LabelGroup.Style(style.key, style.value)
                            }
                        )
                    )
                }
            }
        )
    }

    private fun checkIfDTAvailable(labelGroupList: List<LabelGroup>): Boolean {
        var isAvailable = false
        run breaking@{
            labelGroupList.forEach {
                if (it.position == FULFILLMENT && it.title == DILYANI_TOKOPEDIA) {
                    isAvailable = true
                    return@breaking
                }
            }
        }
        return isAvailable
    }

    private fun bindFavorite(isFollowed: Boolean) {
        val btnFollow = findViewById<UnifyButton>(R.id.btnFollow)
        if (isFollowed) {
            btnFollow.buttonVariant = UnifyButton.Variant.GHOST
            btnFollow.buttonType = UnifyButton.Type.ALTERNATE
            btnFollow.text = btnFollow.context.getString(R.string.topads_following)
        } else {
            btnFollow.buttonVariant = UnifyButton.Variant.FILLED
            btnFollow.buttonType = UnifyButton.Type.MAIN
            btnFollow.text = btnFollow.context.getString(R.string.topads_follow)
        }
    }

    protected open fun renderLabelMerchantVouchers(cpmData: CpmData?) {
        val context = context ?: return
        linearLayoutMerchantVoucher?.hide()
        val merchantVouchers = mutableListOf<String>()
        val campaignType = cpmData?.cpm?.flashSaleCampaignDetail?.campaignType
        val merchantVoucherList = cpmData?.cpm?.cpmShop?.merchantVouchers
        isFlashSaleTokoLabel = false
        if (!campaignType.isNullOrEmpty()) {
            isFlashSaleTokoLabel = true
            merchantVouchers.add(campaignType)
        } else if (!merchantVoucherList.isNullOrEmpty()) {
            merchantVouchers.addAll(merchantVoucherList)
        } else {
            return
        }
        linearLayoutMerchantVoucher?.show()
        linearLayoutMerchantVoucher?.removeAllViews()

        merchantVouchers.forEachIndexed { index, voucher ->
            val isFirstItem = index == 0
            val labelVoucher = createLabelVoucher(context, voucher, isFirstItem)

            linearLayoutMerchantVoucher?.addView(labelVoucher)
        }
    }

    private fun createLabelVoucher(context: Context, voucherText: String, isFirstItem: Boolean): Label {
        val layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.marginStart = if (isFirstItem) 0 else 4.toPx()

        val labelVoucher = Label(context)

        labelVoucher.setLabelType(Label.HIGHLIGHT_LIGHT_RED)
        labelVoucher.text = voucherText
        labelVoucher.layoutParams = layoutParams

        return labelVoucher
    }

    protected open fun setHeadlineShopDataCardWidget(cpmData: CpmData, adsBannerShopCardView: ShopCardView?, appLink: String, adsClickUrl: String) {
        val productList = cpmData.cpm.cpmShop.products

        adsBannerShopCardView?.setShopCardModel(
            mapToShopCardModel(cpmData),
            object : ShopCardListener {
                override fun onItemImpressed() {
                    impressionListener?.onImpressionHeadlineAdsItem(0, cpmData)

                    topAdsUrlHitter.hitImpressionUrl(
                        className,
                        cpmData.cpm.cpmImage.fullUrl,
                        cpmData.cpm.cpmShop.id,
                        cpmData.cpm.uri,
                        cpmData.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onItemClicked() {
                    topAdsBannerViewClickListener?.onBannerAdsClicked(0, appLink, cpmData)

                    topAdsUrlHitter.hitClickUrl(
                        className,
                        adsClickUrl,
                        cpmData.cpm.cpmShop.id,
                        cpmData.cpm.uri,
                        cpmData.cpm?.cpmImage?.fullEcs
                    )
                }

                override fun onProductItemImpressed(productPreviewIndex: Int) {
                    impressionListener?.onImpressionHeadlineAdsItem(productPreviewIndex, cpmData)
                }

                override fun onProductItemClicked(productPreviewIndex: Int) {
                    val product = productList.getOrNull(productPreviewIndex) ?: return

                    topAdsBannerViewClickListener?.onBannerAdsClicked(productPreviewIndex, product.applinks, cpmData)

                    topAdsUrlHitter.hitClickUrl(
                        className,
                        product.imageProduct.imageClickUrl,
                        product.id,
                        product.uri,
                        product.imageProduct.imageUrl
                    )
                }
            }
        )
    }

    protected open fun mapToShopCardModel(cpmData: CpmData): ShopCardModel {
        val productList = cpmData.cpm.cpmShop.products
        return ShopCardModel(
            id = cpmData.cpm.cpmShop.id,
            name = cpmData.cpm.cpmShop.name,
            image = cpmData.cpm.cpmImage.fullEcs,
            location = context.getString(R.string.title_promote_by),
            goldShop = if (cpmData.cpm.cpmShop.isPowerMerchant) 1 else 0,
            productList = productList.map {
                ShopCardModel.ShopItemProduct(
                    id = it.id,
                    name = it.name,
                    priceFormat = it.priceFormat,
                    imageUrl = it.imageProduct.imageUrl
                )
            },
            isOfficial = cpmData.cpm.cpmShop.isOfficial,
            isPMPro = cpmData.cpm.cpmShop.isPMPro,
            impressHolder = cpmData.cpm.cpmShop.imageShop
        )
    }

    protected open fun isEligible(cpmData: CpmData?): Boolean {
        return if (cpmData?.cpm?.layout == LAYOUT_10 || cpmData?.cpm?.layout == LAYOUT_11) {
            cpmData.cpm.cpmShop.products.size > 0 || showProductShimmer
        } else {
            cpmData != null && (cpmData.cpm.cpmShop.products.size > 1 || showProductShimmer)
        }
    }

    protected open fun activityIsFinishing(context: Context): Boolean {
        return if (context is Activity) {
            context.isFinishing
        } else {
            false
        }
    }

    @Throws(Exception::class)
    protected open fun renderViewCpmDigitalV2(context: Context, data: CpmData) {
        if (activityIsFinishing(context)) {
            return
        }
        if (template == NO_TEMPLATE) {
            View.inflate(getContext(), R.layout.layout_ads_banner_digital, this)
            template = DIGITAL_TEMPLATE
        }
        setHeadlineDigitalData(context, data.cpm)

        setOnClickListener {
            topAdsBannerViewClickListener?.onBannerAdsClicked(0, data.applinks, data)
            topAdsUrlHitter.hitClickUrl(className, data.adClickUrl, "", "", "")
        }
    }

    @Throws(Exception::class)
    protected open fun renderViewCpmDigital(context: Context, cpm: Cpm) {
        if (activityIsFinishing(context)) {
            return
        }
        if (template == NO_TEMPLATE) {
            View.inflate(getContext(), R.layout.layout_ads_banner_digital, this)
            template = DIGITAL_TEMPLATE
        }
        setHeadlineDigitalData(context, cpm)
    }

    private fun setHeadlineDigitalData(context: Context, cpm: Cpm) {
        try {
            cpm.cpmImage.fullEcs.getBitmapImageUrl(context) {
                val image = findViewById<ImageView>(R.id.banner_digital_image)
                if (image != null) {
                    image.setImageBitmap(it)
                    topAdsUrlHitter.hitImpressionUrl(className, cpm.cpmImage.fullUrl, "", "", "")
                }
            }

            findViewById<Typography>(R.id.banner_digital_name).text = escapeHTML(cpm.name)
            findViewById<Typography>(R.id.banner_digital_description).text = escapeHTML(cpm.decription)
            findViewById<Typography>(R.id.banner_digital_cta_button).text = cpm.cta
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setTopAdsBannerClickListener(topAdsBannerClickListener: TopAdsBannerClickListener) {
        this.topAdsBannerViewClickListener = topAdsBannerClickListener
    }

    fun setTopAdsShopFollowClickListener(topAdsShopFollowBtnClickListener: TopAdsShopFollowBtnClickListener) {
        this.topAdsShopFollowBtnClickListener = topAdsShopFollowBtnClickListener
    }

    fun setTopAdsImpressionListener(adsImpressionListener: TopAdsItemImpressionListener) {
        this.impressionListener = adsImpressionListener
    }

    fun setHasAddToCartButton(hasAddToCartButton: Boolean) {
        this.hasAddProductToCartButton = hasAddToCartButton
    }

    fun setAddToCartClickListener(topAdsAddToCartClickListener: TopAdsAddToCartClickListener) {
        this.topAdsAddToCartClickListener = topAdsAddToCartClickListener
    }

    fun setShopWidgetAddToCartClickListener(shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener) {
        this.shopWidgetAddToCartClickListener = shopWidgetAddToCartClickListener
    }

    fun setShowCta(isShowCta: Boolean) {
        this.isShowCta = isShowCta
    }

    override fun showLoading() {
    }

    fun displayAdsWithProductShimmer(cpmModel: CpmModel?, showProductShimmer: Boolean = false, index: Int = 0) {
        this.showProductShimmer = showProductShimmer
        displayAds(cpmModel, index)
    }

    fun displayHeadlineAds(cpmModel: CpmModel?, index: Int = 0) {
        displayAds(cpmModel, index)
    }

    // this function to support the reimagine component type
    fun displayHeadlineAdsReimagine(cpmModel: CpmModel?, index: Int = 0) {
        displayAds(cpmModel, index, true)
    }

    override fun displayAds(cpmModel: CpmModel?, index: Int, isReimagine: Boolean) {
        try {
            if (cpmModel != null && cpmModel.data.size > 0) {
                val data = cpmModel.data[0]

                if (data?.cpm != null) {
                    // new approach
                    if (TopAdsSdkUtil.isEnableTopAdsSdkV2()) {
                        if (data.cpm.cpmShop != null && isResponseValid(data)) {
                            renderTopAdsBannerV2(cpmModel, data.applinks, data.adClickUrl, index, isReimagine)
                        } else if (data.cpm.templateId == ITEM_4) {
                            renderViewCpmDigitalV2(context, data)
                        }
                    } else {
                        // old approach
                        if (data.cpm.cpmShop != null && isResponseValid(data)) {
                            renderViewCpmShop(context, cpmModel, data.applinks, data.adClickUrl, index)
                        } else if (data.cpm.templateId == ITEM_4) {
                            renderViewCpmDigital(context, data.cpm)
                            setOnClickListener {
                                if (topAdsBannerViewClickListener != null) {
                                    topAdsBannerViewClickListener!!.onBannerAdsClicked(0, data.applinks, data)
                                    topAdsUrlHitter.hitClickUrl(className, data.adClickUrl, "", "", "")
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isResponseValid(data: CpmData): Boolean {
        return data.cpm.cta.isNotEmpty() && data.cpm.promotedText.isNotEmpty()
    }

    override fun onCanceled() {
    }

    override fun hideLoading() {
    }

    companion object {

        fun escapeHTML(s: String): String {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(StringEscapeUtils.unescapeHtml4(s), Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    Html.fromHtml(StringEscapeUtils.unescapeHtml4(s)).toString()
                }
            } catch (e: Exception) {
                ""
            }
        }

        fun setTextColor(view: TextView, fulltext: String, subtext: String, color: Int) {
            view.setText(fulltext, TextView.BufferType.SPANNABLE)
            val str = view.text as Spannable
            val i = fulltext.indexOf(subtext)
            str.setSpan(ForegroundColorSpan(color), i, i + subtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            str.setSpan(TypefaceSpan("sans-serif"), i, i + subtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }
}
