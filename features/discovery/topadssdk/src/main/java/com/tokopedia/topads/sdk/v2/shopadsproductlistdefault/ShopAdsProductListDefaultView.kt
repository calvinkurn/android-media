package com.tokopedia.topads.sdk.v2.shopadsproductlistdefault

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.common.adapter.Item
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.CpmShop
import com.tokopedia.topads.sdk.domain.model.FlashSaleCampaignDetail
import com.tokopedia.topads.sdk.view.reimagine.BannerAdsAdapterTypeFactoryReimagine
import com.tokopedia.topads.sdk.widget.ITEM_3
import com.tokopedia.topads.sdk.widget.TopAdsBannerView
import com.tokopedia.topads.sdk.utils.ApplyItemDecorationReimagineHelper.addItemDecoratorShopAdsReimagine
import com.tokopedia.topads.sdk.utils.MapperUtils
import com.tokopedia.topads.sdk.utils.TopAdsSdkUtil
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.utils.snaphelper.GravitySnapHelper
import com.tokopedia.topads.sdk.v2.base.BaseBannerAdsRendering
import com.tokopedia.topads.sdk.v2.listener.TopAdsAddToCartClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.v2.listener.TopAdsShopFollowBtnClickListener
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.BannerAdsAdapter
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.adapter.factory.BannerAdsAdapterTypeFactory
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerProductShimmerUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopProductUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.uimodel.BannerShopViewMoreUiModel
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopProductReimagineViewHolder
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopProductViewHolder
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShopViewHolder
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShowMoreReimagineViewHolder
import com.tokopedia.topads.sdk.v2.shopadsproductlistdefault.viewholder.BannerShowMoreViewHolder
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import java.lang.ref.WeakReference
import java.util.*

class ShopAdsProductListDefaultView(
    view: View?,
    contextRef: WeakReference<Context>,
    topAdsUrlHitter: TopAdsUrlHitter,
    private val topAdsBannerViewClickListener: TopAdsBannerClickListener?,
    private val impressionListener: TopAdsItemImpressionListener?,
    private val topAdsAddToCartClickListener: TopAdsAddToCartClickListener?,
    private val topAdsShopFollowBtnClickListener: TopAdsShopFollowBtnClickListener?,
    isReimagine: Boolean = false
) : BaseBannerAdsRendering(view, contextRef, topAdsUrlHitter, isReimagine) {

    private val bannerAdsAdapterTypeFactory by lazy {
        if (isReimagine) {
            BannerAdsAdapterTypeFactoryReimagine(topAdsBannerViewClickListener, impressionListener)
        } else {
            BannerAdsAdapterTypeFactory(topAdsBannerViewClickListener, impressionListener, topAdsAddToCartClickListener)
        }
    }

    private val bannerAdsAdapter by lazy {
        BannerAdsAdapter(bannerAdsAdapterTypeFactory)
    }

    private val currentClassName = className(ShopAdsProductListDefaultView::class.java.simpleName)

    private var isFlashSaleTokoLabel: Boolean = false
    private var flashSaleTimerData: Date? = null
    private var isShowCta: Boolean = false
    private var hasAddProductToCartButton: Boolean = false

    private val linearLayoutMerchantVoucher: LinearLayout? = view?.findViewById(R.id.linearLayoutMerchantVoucher)
    private val list = view?.findViewById<RecyclerView>(R.id.list_v2)

    init {
        initAdapter()
    }

    override fun render(cpmModel: CpmModel, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val container = view?.findViewById<View>(R.id.container)
        list?.scrollToPosition(0)
        list?.isNestedScrollingEnabled = false

        if (isReimagine) {
            renderAllComponentsReimagine(cpmData, appLink, adsClickUrl)
        } else {
            renderAllComponentsNonReimagine(container, cpmData, appLink, adsClickUrl)
        }
    }

    fun setIsShowCta(isShowCta: Boolean) {
        this.isShowCta = isShowCta
    }

    fun setHasAddProductToCartButton(hasAddProductToCartButton: Boolean) {
        this.hasAddProductToCartButton = hasAddProductToCartButton
    }

    private fun renderAllComponentsNonReimagine(container: View?, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        setContainerBackground(container, cpmData)
        setShopView(cpmData)
        setShopBtnFollow(cpmData)
        setShopDetailClickListener(cpmData)
        setShopImage(cpmData)
        renderLabelMerchantVouchers(cpmData)
        renderFlashSaleTimer(cpmData.cpm.flashSaleCampaignDetail)
        renderBannerAdapterList(cpmData, appLink, adsClickUrl)
    }

    private fun renderAllComponentsReimagine(cpmData: CpmData, appLink: String, adsClickUrl: String) {
        setShopView(cpmData)
        setShopDetailClickListener(cpmData)
        setShopImage(cpmData)
        renderLabelMerchantVouchers(cpmData)
        renderBannerAdapterListReimagine(cpmData, appLink, adsClickUrl)
    }

    private fun setContainerBackground(container: View?, cpmData: CpmData) {
        contextRef.get()?.let {
            container?.background = if (cpmData.cpm.cpmShop.isPowerMerchant && !cpmData.cpm.cpmShop.isOfficial) {
                ContextCompat.getDrawable(it, R.drawable.bg_pm_gradient)
            } else if (cpmData.cpm.cpmShop.isOfficial) {
                ContextCompat.getDrawable(it, R.drawable.bg_os_gradient)
            } else {
                ContextCompat.getDrawable(it, R.drawable.bg_rm_gradient)
            }
        }
    }

    private fun setShopView(cpmData: CpmData) {
        val shopBadge = view?.findViewById<ImageView>(R.id.shop_badge)
        shopBadge?.let {
            if (cpmData.cpm.badges.size > 0) {
                it.show()
                it.loadImage(cpmData.cpm.badges.firstOrNull()?.imageUrl)
            } else {
                it.hide()
            }
        }

        view?.findViewById<Typography>(R.id.shop_name)?.text = MethodChecker.fromHtml(cpmData.cpm.cpmShop.name)
        view?.findViewById<Typography>(R.id.description)?.text = cpmData.cpm.cpmShop.slogan
    }

    private fun setShopBtnFollow(cpmData: CpmData) {
        val btnFollow = view?.findViewById<UnifyButton>(R.id.btnFollow)
        if (cpmData.cpm.cpmShop.isFollowed != null && topAdsShopFollowBtnClickListener != null) {
            bindFavorite(cpmData.cpm.cpmShop.isFollowed)
            btnFollow?.setOnClickListener {
                cpmData.cpm.cpmShop.id.let { it1 -> topAdsShopFollowBtnClickListener?.onFollowClick(it1, cpmData.id) }
                if (!cpmData.cpm.cpmShop.isFollowed) {
                    topAdsUrlHitter?.hitClickUrl(currentClassName, cpmData.adClickUrl, "", "", "")
                }
            }
            btnFollow?.show()
        } else {
            btnFollow?.hide()
        }
    }

    private fun setShopDetailClickListener(cpmData: CpmData) {
        val shopDetail = view?.findViewById<View>(R.id.shop_detail_v2)

        shopDetail?.setOnClickListener {
            topAdsBannerViewClickListener?.onBannerAdsClicked(1, cpmData.applinks, cpmData)
            topAdsUrlHitter?.hitClickUrl(currentClassName, cpmData.adClickUrl, "", "", "")
        }
    }

    protected fun renderLabelMerchantVouchers(cpmData: CpmData?) {
        val context = contextRef.get() ?: return

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

    private fun renderFlashSaleTimer(flashSaleCampaignDetail: FlashSaleCampaignDetail) {
        val topAdsFlashSaleTimer: TimerUnifySingle? = view?.findViewById(R.id.topAdsFlashSaleTimer)

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
                showFlashSaleToko(topAdsFlashSaleTimer)
                topAdsFlashSaleTimer?.onFinish = {
                    hideFlashSaleToko(topAdsFlashSaleTimer)
                }
            }
        } else {
            if (isFlashSaleTokoLabel) {
                hideFlashSaleToko(topAdsFlashSaleTimer)
            }
        }
    }

    private fun renderBannerAdapterListReimagine(cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val items = ArrayList<Item<*>>()
        val shop = cpmData.cpm.cpmShop

        if (cpmData.cpm.cpmShop.products.isNotEmpty()) {
            val productCardModelList = MapperUtils.getProductCardModels(cpmData.cpm.cpmShop.products, hasAddProductToCartButton)

            setProductCardItems(productCardModelList, shop, cpmData, items)

            renderHeaderSeeMoreReimagine(cpmData, appLink, adsClickUrl)

            if (productCardModelList.size < ITEM_3) {
                items.add(BannerShopViewMoreUiModel(cpmData, appLink, adsClickUrl))
            }
        } else {
            repeat(ITEM_3) { items.add(BannerProductShimmerUiModel()) }
        }

        bannerAdsAdapter.setList(items)
    }

    private fun renderBannerAdapterList(cpmData: CpmData, appLink: String, adsClickUrl: String) {
        val items = ArrayList<Item<*>>()
        val shop = cpmData.cpm.cpmShop

        items.add(
            BannerShopUiModel(
                cpmData,
                appLink,
                adsClickUrl,
                isShowCta
            )
        )
        if (cpmData.cpm.cpmShop.products.isNotEmpty()) {
            val productCardModelList = MapperUtils.getProductCardModels(cpmData.cpm.cpmShop.products, hasAddProductToCartButton)
            setProductCardItems(productCardModelList, shop, cpmData, items)

            if (productCardModelList.size < ITEM_3) {
                items.add(BannerShopViewMoreUiModel(cpmData, appLink, adsClickUrl))
            }
        } else {
            repeat(ITEM_3) { items.add(BannerProductShimmerUiModel()) }
        }

        bannerAdsAdapter.setList(items)
    }

    private fun setProductCardItems(productCardModelList: ArrayList<ProductCardModel>, shop: CpmShop, cpmData: CpmData, items: ArrayList<Item<*>>) {
        for (i in 0 until productCardModelList.size) {
            if (i < ITEM_3) {
                val product = shop.products[i]
                val model = BannerShopProductUiModel(
                    cpmData,
                    productCardModelList[i],
                    product.applinks,
                    product.image.m_url,
                    product.imageProduct.imageClickUrl
                ).apply {
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
    }

    private fun hideFlashSaleToko(topAdsFlashSaleTimer: TimerUnifySingle?) {
        linearLayoutMerchantVoucher?.hide()
        topAdsFlashSaleTimer?.hide()
    }

    private fun showFlashSaleToko(topAdsFlashSaleTimer: TimerUnifySingle?) {
        linearLayoutMerchantVoucher?.show()
        topAdsFlashSaleTimer?.show()
    }

    private fun createLabelVoucher(context: Context, voucherText: String, isFirstItem: Boolean): Label {
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.marginStart = if (isFirstItem) 0 else 4.toPx()

        val labelVoucher = Label(context)

        labelVoucher.setLabelType(Label.HIGHLIGHT_LIGHT_RED)
        labelVoucher.text = voucherText
        labelVoucher.layoutParams = layoutParams

        return labelVoucher
    }

    private fun setShopImage(cpmData: CpmData) {
        val shopImage = view?.findViewById<ImageView>(R.id.shop_image)
        shopImage?.let {
            shopImage.loadImage(cpmData.cpm.cpmImage.fullEcs)
            cpmData.cpm.cpmShop.imageShop?.let { it1 ->
                shopImage.addOnImpressionListener(it1) {
                    impressionListener?.let {
                        it.onImpressionHeadlineAdsItem(0, cpmData)
                        topAdsUrlHitter?.hitImpressionUrl(currentClassName, cpmData.cpm.cpmImage.fullUrl, "", "", "")
                    }
                }
            }
        }
    }

    private fun bindFavorite(isFollowed: Boolean) {
        val btnFollow = view?.findViewById<UnifyButton>(R.id.btnFollow)

        btnFollow?.run {
            if (isFollowed) {
                buttonVariant = UnifyButton.Variant.GHOST
                buttonType = UnifyButton.Type.ALTERNATE
                text = btnFollow.context.getString(R.string.topads_following)
            } else {
                buttonVariant = UnifyButton.Variant.FILLED
                buttonType = UnifyButton.Type.MAIN
                text = btnFollow.context.getString(R.string.topads_follow)
            }
        }
    }

    private fun initAdapter() {
        if (isReimagine) {
            initAdapterReimagine()
        } else {
            initAdapterOld()
        }
    }

    private fun initAdapterOld() {
        BannerShopProductViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product
        BannerShopViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a
        BannerShowMoreViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_more

        list?.run {
            layoutManager = LinearLayoutManager(contextRef.get(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bannerAdsAdapter

            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun initAdapterReimagine() {
        BannerShopProductReimagineViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product_reimagine
        BannerShowMoreReimagineViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_more_reimagine

        list?.run {
            layoutManager = LinearLayoutManager(contextRef.get(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bannerAdsAdapter
            addItemDecoratorShopAdsReimagine()

            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(this)
        }
    }

    private fun renderHeaderSeeMoreReimagine(cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (isReimagine) {
            val containerSeeMore = view?.findViewById<View>(R.id.topAdsBtnSeeMore)
            if (appLink.isNotBlank()) {
                showHeaderSeeMore(containerSeeMore, cpmData, appLink, adsClickUrl)
            } else {
                containerSeeMore?.hide()
            }
        }
    }

    private fun showHeaderSeeMore(btnSeeMore: View?, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        btnSeeMore?.show()
        val ctaSeeMoreBtn: IconUnify? = btnSeeMore?.findViewById(R.id.topAdsIconCTASeeMore)
        ctaSeeMoreBtn?.setOnClickListener {
            topAdsBannerViewClickListener?.onBannerAdsClicked(0, appLink, cpmData)
            TopAdsUrlHitter(contextRef.get()).hitClickUrl(
                TopAdsBannerView::class.java.simpleName,
                adsClickUrl,
                "",
                "",
                ""
            )
        }
    }
}
