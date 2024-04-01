package com.tokopedia.topads.sdk.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_8
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_9
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.topads.sdk.listener.*
import com.tokopedia.topads.sdk.snaphelper.GravitySnapHelper
import com.tokopedia.topads.sdk.utils.ApplyItemDecorationReimagineHelper.addItemDecoratorShopAdsReimagine
import com.tokopedia.topads.sdk.utils.MapperUtils
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.adapter.BannerAdsAdapter
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductReimagineViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShowMoreReimagineViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerUiModel
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductUiModel
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreUiModel
import com.tokopedia.topads.sdk.view.reimagine.BannerAdsAdapterTypeFactoryReimagine
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class TopAdsBannerViewReimagine : TopAdsBannerView {
    override val className: String = "com.tokopedia.topads.sdk.widget.TopAdsBannerViewReimagine"

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    @Throws(Exception::class)
    override fun renderViewCpmShop(context: Context, cpmModel: CpmModel?, appLink: String, adsClickUrl: String, index: Int) {
        if (activityIsFinishing(context)) {
            return
        }
        val cpmData = cpmModel?.data?.firstOrNull()
        if (template == NO_TEMPLATE && isEligible(cpmData)) {
            View.inflate(getContext(), R.layout.layout_ads_banner_shop_a_pager_reimagine, this)
            BannerShopProductReimagineViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product_reimagine
            BannerShowMoreReimagineViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_more_reimagine

            findViewById<TextView>(R.id.topAdsShopName)?.text = escapeHTML(cpmData?.cpm?.name.orEmpty())
            bannerAdsAdapter = BannerAdsAdapter(
                BannerAdsAdapterTypeFactoryReimagine(topAdsBannerViewClickListener, impressionListener)
            )
            val list = findViewById<RecyclerView>(R.id.topAdsList)
            list.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            list.adapter = bannerAdsAdapter
            list.addItemDecoratorShopAdsReimagine()

            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(list)

            template = SHOP_TEMPLATE
        }
        setHeadlineShopData(cpmModel, appLink, adsClickUrl, index)
    }

    private fun setHeadlineShopData(cpmModel: CpmModel?, appLink: String, adsClickUrl: String, index: Int) {
        val adsBannerShopCardView = findViewById<ShopCardView?>(R.id.adsBannerShopCardView)
        val shopDetail = findViewById<View?>(R.id.topAdsShopDetail)
        val topAdsCarousel = findViewById<ToadsCarousel>(R.id.TopAdsCarousel)
        val shopAdsProductView = findViewById<ShopAdsWithOneProductReimagineView>(R.id.shopAdsProductView)
        val list = findViewById<RecyclerView?>(R.id.topAdsList)
        val container = findViewById<View>(R.id.topAdsContainer)
        val cpmData = cpmModel?.data?.getOrNull(index)
        val shopAdsWithThreeProducts = findViewById<ShopAdsWithThreeProducts>(R.id.shopAdsWithThreeProducts)
        val shopAdsWithSingleProductHorizontal = findViewById<ShopAdsSingleItemHorizontalLayout>(R.id.shopAdsWithSingleProductHorizontal)
        val shopAdsWithSingleProductVertical = findViewById<ShopAdsSingleItemVerticalLayout>(R.id.shopAdsWithSingleProductVertical)
        linearLayoutMerchantVoucher = findViewById(R.id.topAdsLinearLayoutMerchantVoucher)

        if (cpmData?.cpm?.layout != LAYOUT_6 && cpmData?.cpm?.layout != LAYOUT_5 && cpmData?.cpm?.layout != LAYOUT_8 && cpmData?.cpm?.layout != LAYOUT_9 && cpmData?.cpm?.layout != TopAdsConstants.LAYOUT_10 && cpmData?.cpm?.layout != TopAdsConstants.LAYOUT_11) {
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
                    adsBannerShopCardView?.gone()
                    shopAdsWithSingleProductHorizontal.hide()
                    shopAdsWithSingleProductVertical.hide()
                    list.isNestedScrollingEnabled = false

                    val shop_badge = findViewById<ImageView>(R.id.topAdsShopBadge)
                    shop_badge?.let {
                        if ((cpmData.cpm.badges.size.orZero()) > 0) {
                            shop_badge.show()
                            Glide.with(shop_badge).load(cpmData.cpm.badges.firstOrNull()?.imageUrl).into(shop_badge)
                        } else {
                            shop_badge.hide()
                        }
                    }
                    findViewById<TextView>(R.id.topAdsShopName)?.text = MethodChecker.fromHtml(cpmData.cpm.cpmShop.name)
                    findViewById<Typography>(R.id.description)?.text = cpmData.cpm.cpmShop.slogan

                    shopDetail.setOnClickListener {
                        if (topAdsBannerViewClickListener != null) {
                            topAdsBannerViewClickListener!!.onBannerAdsClicked(1, cpmData.applinks, cpmData)
                            topAdsUrlHitter.hitClickUrl(className, cpmData.adClickUrl, "", "", "")
                        }
                    }

                    val shopImage = findViewById<ImageView>(R.id.topAdsShopImage)
                    shopImage?.let {
                        Glide.with(context).load(cpmData.cpm.cpmImage.fullEcs).into(shopImage)
                        cpmData.cpm.cpmShop.imageShop?.let { it1 ->
                            shopImage.addOnImpressionListener(it1) {
                                impressionListener?.let {
                                    it.onImpressionHeadlineAdsItem(0, cpmData)
                                    topAdsUrlHitter.hitImpressionUrl(className, cpmData.cpm.cpmImage.fullUrl, "", "", "")
                                }
                            }
                        }
                    }

                    renderLabelMerchantVouchers(cpmData)

                    val items = ArrayList<Item<*>>()
                    if (cpmData.cpm.cpmShop.products.isNotEmpty()) {
                        val productCardModelList: ArrayList<ProductCardModel> = MapperUtils.getProductCardModels(cpmData.cpm.cpmShop.products, hasAddProductToCartButton)
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

                        renderHeaderSeeMore(cpmData, appLink, adsClickUrl)
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
            topAdsCarousel.show()
            shopAdsWithSingleProductHorizontal.hide()
            shopAdsWithSingleProductVertical.hide()
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
        } else if (cpmData?.cpm?.layout == TopAdsConstants.LAYOUT_10) {
            topAdsCarousel.hide()
            shopDetail?.hide()
            shopAdsProductView.hide()
            adsBannerShopCardView?.hide()
            shopAdsWithThreeProducts.hide()
            list?.hide()
            shopAdsWithSingleProductHorizontal.show()
            shopAdsWithSingleProductVertical.hide()
            shopAdsWithSingleProductHorizontal.setShopProductModel(getSingleAdsProductModel(cpmData, appLink, adsClickUrl, topAdsBannerViewClickListener, hasAddProductToCartButton))
        } else if (cpmData?.cpm?.layout == TopAdsConstants.LAYOUT_11) {
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

    override fun mapToShopCardModel(cpmData: CpmData): ShopCardModel {
        return super.mapToShopCardModel(cpmData).copy(isReimagine = true)
    }

    private fun renderHeaderSeeMore(cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val containerSeeMore = findViewById<View>(R.id.topAdsBtnSeeMore)
        val isApplinkNotEmpty = appLink.isNotEmpty()
        if (isApplinkNotEmpty) {
            showHeaderSeeMore(containerSeeMore, cpmData, appLink, adsClickUrl)
        } else {
            containerSeeMore.gone()
        }
    }

    private fun showHeaderSeeMore(btnSeeMore: View, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        btnSeeMore.visible()
        val btnSeeMore: IconUnify = btnSeeMore.findViewById(R.id.topAdsIconCTASeeMore)
        btnSeeMore.setOnClickListener {
            if (topAdsBannerViewClickListener != null) {
                topAdsBannerViewClickListener?.onBannerAdsClicked(0, appLink, cpmData)
                TopAdsUrlHitter(context).hitClickUrl(
                    TopAdsBannerView::class.java.simpleName,
                    adsClickUrl,
                    "",
                    "",
                    ""
                )
            }
        }
    }

    private fun setShopAdsProduct(cpmModel: CpmModel, shopAdsProductView: ShopAdsWithOneProductReimagineView) {
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
}
