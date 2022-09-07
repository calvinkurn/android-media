package com.tokopedia.topads.sdk.widget

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.TopAdsConstants.DILYANI_TOKOPEDIA
import com.tokopedia.topads.sdk.TopAdsConstants.FULFILLMENT
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_8
import com.tokopedia.topads.sdk.TopAdsConstants.LAYOUT_9
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.domain.model.*
import com.tokopedia.topads.sdk.listener.*
import com.tokopedia.topads.sdk.shopwidgetthreeproducts.listener.ShopWidgetAddToCartClickListener
import com.tokopedia.topads.sdk.snaphelper.GravitySnapHelper
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.topads.sdk.view.BannerAdsContract
import com.tokopedia.topads.sdk.view.adapter.BannerAdsAdapter
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsAdapterTypeFactory
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShowMoreViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerProductShimmerViewModel
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewMoreModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.layout_ads_banner_digital.view.*
import kotlinx.android.synthetic.main.layout_ads_banner_shop_a_pager.view.*
import kotlinx.android.synthetic.main.layout_ads_banner_shop_b_pager.view.*
import org.apache.commons.text.StringEscapeUtils

/**
 * Created by errysuprayogi on 12/28/17.
 */

private const val NO_TEMPLATE = 0
private const val SHOP_TEMPLATE = 1
private const val DIGITAL_TEMPLATE = 2
private const val LAYOUT_2 = 2
private const val LAYOUT_5 = 5
private const val LAYOUT_6 = 6
private const val ITEM_3 = 3
private const val ITEM_4 = 4

class TopAdsBannerView : LinearLayout, BannerAdsContract.View {
    private var topAdsBannerClickListener: TopAdsBannerClickListener? = null
    private var impressionListener: TopAdsItemImpressionListener? = null
    private var topAdsShopFollowBtnClickListener: TopAdsShopFollowBtnClickListener? = null
    private var topAdsAddToCartClickListener: TopAdsAddToCartClickListener? = null
    private var shopWidgetAddToCartClickListener: ShopWidgetAddToCartClickListener? = null
    private var bannerAdsAdapter: BannerAdsAdapter? = null
    private val className: String = "com.tokopedia.topads.sdk.widget.TopAdsBannerView"
    private var showProductShimmer: Boolean = false
    private var hasAddToCartButton: Boolean = false
    private var isShowCta: Boolean = true
    var impressionCount: Int = 0
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(context)
    }

    private var template = NO_TEMPLATE

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }

    @Throws(Exception::class)
    private fun renderViewCpmShop(context: Context, cpmModel: CpmModel?, appLink: String, adsClickUrl: String, index: Int) {
        if (activityIsFinishing(context))
            return
        val cpmData = cpmModel?.data?.firstOrNull()
        if (template == NO_TEMPLATE && isEligible(cpmData)) {
            View.inflate(getContext(), R.layout.layout_ads_banner_shop_a_pager, this)
            BannerShopProductViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product
            BannerShopViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a
            BannerShowMoreViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_more
            findViewById<TextView>(R.id.shop_name)?.text = escapeHTML(cpmData?.cpm?.name ?: "")
            bannerAdsAdapter = BannerAdsAdapter(BannerAdsAdapterTypeFactory(topAdsBannerClickListener, impressionListener, topAdsAddToCartClickListener))
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

        if (cpmData?.cpm?.layout != LAYOUT_6 && cpmData?.cpm?.layout != LAYOUT_5 && cpmData?.cpm?.layout != LAYOUT_8 && cpmData?.cpm?.layout != LAYOUT_9) {
            if (isEligible(cpmData)) {
                if (cpmData != null && (cpmData.cpm?.layout == LAYOUT_2)) {
                    list?.gone()
                    shopDetail?.gone()
                    topAdsCarousel.hide()
                    shopAdsProductView.hide()
                    adsBannerShopCardView?.visible()
                    shopAdsWithThreeProducts.hide()
                    container?.setBackgroundResource(0)
                    (container?.layoutParams as? MarginLayoutParams)?.setMargins(0, 4.toPx(), 0, 0)

                    setHeadlineShopDataCardWidget(cpmData, adsBannerShopCardView, appLink, adsClickUrl)
                }
                else if (cpmData != null) {
                    list?.visible()
                    list?.scrollToPosition(0)
                    shopDetail?.visible()
                    topAdsCarousel.hide()
                    shopAdsProductView.hide()
                    shopAdsWithThreeProducts.hide()
                    adsBannerShopCardView?.gone()
                    list.isNestedScrollingEnabled = false

                    if (cpmData.cpm?.cpmShop?.isPowerMerchant == true && cpmData.cpm?.cpmShop?.isOfficial == false) {
                        container?.background = ContextCompat.getDrawable(context, R.drawable.bg_pm_gradient)
                    } else if (cpmData.cpm?.cpmShop?.isOfficial == true) {
                        container?.background = ContextCompat.getDrawable(context, R.drawable.bg_os_gradient)
                    } else {
                        container?.background = ContextCompat.getDrawable(context, R.drawable.bg_rm_gradient)
                    }

                    val shop_badge = findViewById<ImageView>(R.id.shop_badge)
                    shop_badge?.let {
                        if (cpmData.cpm?.badges?.size ?: 0 > 0) {
                            shop_badge.show()
                            Glide.with(shop_badge).load(cpmData.cpm?.badges?.firstOrNull()?.imageUrl).into(shop_badge)
                        } else {
                            shop_badge.hide()
                        }
                    }
                    shop_name?.text = MethodChecker.fromHtml(cpmData.cpm?.cpmShop?.name)
                    description?.text = cpmData.cpm?.cpmShop?.slogan
                    if (cpmData.cpm?.cpmShop?.isFollowed != null && topAdsShopFollowBtnClickListener != null) {
                        cpmData.cpm?.cpmShop?.isFollowed?.let { bindFavorite(it) }
                        btnFollow.setOnClickListener {
                            cpmData.cpm?.cpmShop?.id?.let { it1 -> cpmData.id?.let { id ->
                                topAdsShopFollowBtnClickListener?.onFollowClick(it1,
                                    id
                                )
                            } }
                            if (cpmData.cpm?.cpmShop?.isFollowed == false) {
                                topAdsUrlHitter.hitClickUrl(className, cpmData.adClickUrl, "", "", "")
                            }
                        }
                        btnFollow.show()
                    } else {
                        btnFollow.hide()
                    }

                    shopDetail.setOnClickListener {
                        if (topAdsBannerClickListener != null) {
                            topAdsBannerClickListener!!.onBannerAdsClicked(1, cpmData.applinks, cpmData)
                            topAdsUrlHitter.hitClickUrl(className, cpmData.adClickUrl, "", "", "")
                        }
                    }

                    val shop_image = findViewById<ImageView>(R.id.shop_image)
                    shop_image?.let {
                        Glide.with(context).load(cpmData.cpm?.cpmImage?.fullEcs).into(shop_image)
                        cpmData.cpm?.cpmShop?.imageShop?.let { it1 ->
                            shop_image.addOnImpressionListener(it1) {
                                impressionListener?.let {
                                    it.onImpressionHeadlineAdsItem(0, cpmData)
                                    topAdsUrlHitter.hitImpressionUrl(className, cpmData.cpm?.cpmImage?.fullUrl, "", "", "")
                                }
                            }
                        }
                    }

                    renderLabelMerchantVouchers(cpmData)

                    val items = ArrayList<Item<*>>()
                    items.add(BannerShopViewModel(cpmData, appLink, adsClickUrl, isShowCta))
                    if (cpmData.cpm?.cpmShop?.products?.isNotEmpty() == true) {
                        val productCardModelList: ArrayList<ProductCardModel>? =  cpmData.cpm?.cpmShop?.products?.let { getProductCardModels(it) }
                        if (productCardModelList != null) {
                            for (i in 0 until productCardModelList.size) {
                                if (i < ITEM_3) {
                                    val model = BannerShopProductViewModel(cpmData, productCardModelList[i],
                                        cpmData.cpm?.cpmShop?.products?.getOrNull(i)?.applinks,
                                        cpmData.cpm?.cpmShop?.products?.getOrNull(i)?.image?.m_url,
                                        cpmData.cpm?.cpmShop?.products?.getOrNull(i)?.imageProduct?.imageClickUrl)
                                    val product = cpmData.cpm?.cpmShop?.products?.getOrNull(i)
                                    model.apply {
                                        productId = product?.id
                                        productName = product?.name
                                        productMinOrder = product?.productMinimumOrder?:0
                                        productCategory = product?.categoryBreadcrumb
                                        productPrice = product?.priceFormat
                                        shopId = cpmData.cpm?.cpmShop?.id
                                    }
                                    items.add(model)
                                }
                            }
                            if (productCardModelList.size < ITEM_3) {
                                items.add(BannerShopViewMoreModel(cpmData, appLink, adsClickUrl))
                            }
                        }

                    } else {
                        repeat(ITEM_3) { items.add(BannerProductShimmerViewModel()) }
                    }
                    bannerAdsAdapter?.setList(items)
                }
            }
        } else if (cpmData.cpm?.layout == LAYOUT_6 && isEligible(cpmData)){
            adsBannerShopCardView?.gone()
            shopDetail?.gone()
            list?.gone()
            shopAdsWithThreeProducts.hide()
            topAdsCarousel.show()
            container?.background = ContextCompat.getDrawable(context, R.drawable.bg_os_gradient)
            setTopAdsCarousel(cpmModel, topAdsCarousel)
        } else if(cpmData.cpm?.layout == LAYOUT_5 && isEligible(cpmData)){
            adsBannerShopCardView?.gone()
            shopDetail?.gone()
            list?.gone()
            shopAdsProductView.show()
            shopAdsWithThreeProducts.hide()
            container?.setBackgroundResource(0)
            setShopAdsProduct(cpmModel, shopAdsProductView)
        }else if((cpmData.cpm?.layout == LAYOUT_8 || cpmData.cpm?.layout == LAYOUT_9) && isEligible(cpmData)){
            topAdsCarousel.hide()
            shopDetail?.hide()
            shopAdsProductView.hide()
            adsBannerShopCardView?.hide()
            shopAdsWithThreeProducts.show()
            list?.hide()
            setWidget(cpmData, appLink, adsClickUrl, shopAdsWithThreeProducts, topAdsBannerClickListener, hasAddToCartButton)
        }

    }

    private fun setWidget(
        cpmData: CpmData,
        appLink: String,
        adsClickUrl: String,
        shopAdsWithThreeProducts: ShopAdsWithThreeProducts?,
        topAdsBannerClickListener: TopAdsBannerClickListener?,
        hasAddToCartButton: Boolean
    ) {
        shopAdsWithThreeProducts?.setWidgetModel(
            ShopAdsWithThreeProductModel(
                isOfficial = cpmData.cpm?.cpmShop?.isPowerMerchant ?: false,
                isPMPro = cpmData.cpm?.cpmShop?.isPMPro ?: false,
                isPowerMerchant = cpmData.cpm?.cpmShop?.isPowerMerchant ?: false,
                shopBadge = cpmData.cpm?.badges?.firstOrNull()?.imageUrl?:"",
                shopName = cpmData.cpm?.cpmShop?.name?:"",
                shopImageUrl = cpmData.cpm?.cpmImage?.fullEcs?:"",
                shopWidgetImageUrl = cpmData.cpm?.widgetImageUrl?:"",
                merchantVouchers = cpmData.cpm?.cpmShop?.merchantVouchers?: mutableListOf(),
                listItems = getItems(cpmData,appLink,adsClickUrl),
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
        items.add(BannerShopViewModel(cpmData, appLink, adsClickUrl, isShowCta))
        if (cpmData.cpm?.cpmShop?.products?.isNotEmpty() == true) {
            val productCardModelList: ArrayList<ProductCardModel> =
                getProductCardModels(cpmData.cpm?.cpmShop?.products)
            for (i in 0 until productCardModelList.size) {
                if (i < ITEM_3) {
                    val model = BannerShopProductViewModel(
                        cpmData, productCardModelList[i],
                        cpmData.cpm?.cpmShop?.products?.getOrNull(i)?.applinks,
                        cpmData.cpm?.cpmShop?.products?.getOrNull(i)?.image?.m_url,
                        cpmData.cpm?.cpmShop?.products?.getOrNull(i)?.imageProduct?.imageClickUrl,
                    )
                    val product = cpmData.cpm?.cpmShop?.products?.getOrNull(i)
                    model.apply {
                        productId = product?.id
                        productName = product?.name
                        productMinOrder = product?.productMinimumOrder ?: 0
                        productCategory = product?.categoryBreadcrumb
                        productPrice = product?.priceFormat
                        shopId = cpmData.cpm?.cpmShop?.id
                    }
                    items.add(model)
                }
            }
            if (productCardModelList.size < ITEM_3) {
                items.add(BannerShopViewMoreModel(cpmData, appLink, adsClickUrl))
            }
        } else {
            repeat(ITEM_3) { items.add(BannerProductShimmerViewModel()) }
        }
        return items
    }

    private fun setShopAdsProduct(cpmModel: CpmModel, shopAdsProductView: ShopAdsWithOneProductView) {
        shopAdsProductView.setShopProductModel(
                ShopProductModel(
                        title = cpmModel.data?.firstOrNull()?.cpm?.widgetTitle ?: "",
                        items = getShopProductItem(cpmModel)
                ), object : ShopAdsProductListener{
            override fun onItemImpressed(position: Int) {
                val cpmData = cpmModel.data?.getOrNull(position)
                impressionCount = position + 1
                impressionListener?.onImpressionHeadlineAdsItem(position, cpmData)
                topAdsUrlHitter.hitImpressionUrl(
                        className,
                        cpmData?.cpm?.cpmImage?.fullUrl,
                        cpmData?.cpm?.cpmShop?.id,
                        cpmData?.cpm?.uri,
                        cpmData?.cpm?.cpmImage?.fullEcs
                )
            }

            override fun onItemClicked(position: Int) {
                val cpmData = cpmModel.data?.getOrNull(position)
                topAdsBannerClickListener?.onBannerAdsClicked(position, cpmData?.applinks, cpmData)
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

    private fun getShopProductItem(cpmModel: CpmModel): List<ShopProductModel.ShopProductModelItem> {
        val list = arrayListOf<ShopProductModel.ShopProductModelItem>()
        cpmModel.data?.forEachIndexed { index, it ->
            val item = ShopProductModel.ShopProductModelItem(
                imageUrl = it.cpm?.cpmShop?.products?.firstOrNull()?.imageProduct?.imageUrl ?: "",
                shopIcon = it.cpm?.cpmImage?.fullEcs ?: "",
                shopName = it.cpm?.cpmShop?.name ?: "",
                ratingCount = it.cpm?.cpmShop?.products?.firstOrNull()?.countReviewFormat ?: "",
                ratingAverage = it.cpm?.cpmShop?.products?.firstOrNull()?.headlineProductRatingAverage
                    ?: "",
                isOfficial = it.cpm?.cpmShop?.isOfficial ?: false,
                isPMPro = it.cpm?.cpmShop?.isPMPro ?: false,
                goldShop = if (it.cpm?.cpmShop?.isPowerMerchant == true) 1 else 0,
                impressHolder = it.cpm?.cpmShop?.imageShop,
                location = it.cpm?.cpmShop?.location ?: "",
                position = index
            )
            list.add(item)
        }
        return list
    }

    private fun setTopAdsCarousel(cpmModel: CpmModel?, topAdsCarousel: ToadsCarousel?) {
        topAdsCarousel?.setTopAdsCarouselModel(
            TopAdsCarouselModel(
                title = cpmModel?.data?.firstOrNull()?.cpm?.widgetTitle ?: "",
                items = getTopAdsItem(cpmModel)
            ), object : TopAdsCarouselListener {
                override fun onItemImpressed(position: Int) {
                    val cpmData = cpmModel?.data?.getOrNull(position)
                    impressionCount = position + 1
                    impressionListener?.onImpressionHeadlineAdsItem(position, cpmData)
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
                    topAdsBannerClickListener?.onBannerAdsClicked(
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
                    val cpmDataProducts = cpmData.cpm?.cpmShop?.products

                    if (!cpmDataProducts.isNullOrEmpty()) {
                        val product = cpmDataProducts[productIndex]
                        topAdsBannerClickListener?.onBannerAdsClicked(
                            productIndex,
                            product.applinks,
                            cpmData
                        )
                        topAdsUrlHitter.hitClickUrl(
                            className,
                            product.imageProduct?.imageClickUrl,
                            product.id,
                            product.uri,
                            product.imageProduct?.imageUrl
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
                imageUrlOne = it.cpm?.cpmShop?.products?.getOrNull(0)?.imageProduct?.imageUrl ?: "",
                imageUrlTwo = it.cpm?.cpmShop?.products?.getOrNull(1)?.imageProduct?.imageUrl ?: "",
                brandIcon = it.cpm?.cpmImage?.fullEcs ?: "",
                brandName = it.cpm?.cpmShop?.name ?: "",
                isOfficial = it.cpm?.cpmShop?.isOfficial ?: false,
                isPMPro = it.cpm?.cpmShop?.isPMPro ?: false,
                goldShop = if (it.cpm?.cpmShop?.isPowerMerchant == true) 1 else 0,
                impressHolder = it.cpm?.cpmShop?.imageShop,
                position = index
            )
            list.add(item)
        }
        return list
    }

    private fun getProductCardModels(products: List<Product>?): ArrayList<ProductCardModel> {
        return ArrayList<ProductCardModel>().apply {
            products?.map {
                add(getProductCardViewModel(it))
            }
        }
    }

    private fun getProductCardViewModel(product: Product): ProductCardModel {
        val isAvailAble = checkIfDTAvailable(product.labelGroupList)
        val productCardModel = ProductCardModel(
            productImageUrl = product.imageProduct?.imageUrl ?: "",
            productName = product.name ?: "",
            discountPercentage = if (product.campaign?.discountPercentage != 0) "${product.campaign?.discountPercentage}%" else "",
            formattedPrice = product.priceFormat ?: "",
            reviewCount = product.countReviewFormat.toIntOrZero(),
            ratingString = product.productRatingFormat ?: "",
            freeOngkir = ProductCardModel.FreeOngkir(
                product.freeOngkir?.isActive ?: false,
                product.freeOngkir?.imageUrl ?: ""
            ),
            hasAddToCartButton = this.hasAddToCartButton,
            addToCartButtonType = UnifyButton.Type.MAIN
        )
        return getProductModelOnCondition(product, isAvailAble, productCardModel)

    }

    private fun getProductModelOnCondition(
        product: Product,
        isAvailAble: Boolean,
        productCardModel: ProductCardModel
    ): ProductCardModel {
        if (isAvailAble) {
            return if (!product.campaign?.originalPrice.isNullOrEmpty()) {
                productCardModel.copy(
                    slashedPrice = product.campaign?.originalPrice ?: "",
                    labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                        product.labelGroupList?.map {
                            if (it.position != "integrity") {
                                add(
                                    ProductCardModel.LabelGroup(
                                        it.position ?: "",
                                        it.title ?: "",
                                        it.type ?: ""
                                    )
                                )
                            }
                        }
                    })
            } else {
                productCardModel.copy(
                    labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                        product.labelGroupList?.map {
                            if (it.position != "integrity") {
                                add(
                                    ProductCardModel.LabelGroup(
                                        it.position ?: "",
                                        it.title ?: "",
                                        it.type ?: "",
                                        it.imageUrl ?: ""
                                    )
                                )
                            }
                        }
                    })
            }

        }

        return productCardModel.copy(
            slashedPrice = product.campaign?.originalPrice ?: "",
            countSoldRating = product.headlineProductRatingAverage ?: "",
            labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                product.labelGroupList?.map {
                    add(
                        ProductCardModel.LabelGroup(
                            it.position ?: "",
                            it.title ?: "",
                            it.type ?: ""
                        )
                    )
                }
            })
    }


    private fun checkIfDTAvailable(labelGroupList: List<LabelGroup>?): Boolean {
        var isAvailable = false
        run breaking@ {
            labelGroupList?.forEach {
                if (it.position == FULFILLMENT && it.title == DILYANI_TOKOPEDIA){
                    isAvailable = true
                    return@breaking
                }
            }
        }
        return isAvailable
    }

    private fun bindFavorite(isFollowed: Boolean) {
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

    private fun renderLabelMerchantVouchers(cpmData: CpmData?) {
        val context = context ?: return
        val linearLayoutMerchantVoucher = findViewById<LinearLayout?>(R.id.linearLayoutMerchantVoucher)
                ?: return
        val merchantVouchers = cpmData?.cpm?.cpmShop?.merchantVouchers ?: return

        linearLayoutMerchantVoucher.removeAllViews()

        merchantVouchers.forEachIndexed { index, voucher ->
            val isFirstItem = index == 0
            val labelVoucher = createLabelVoucher(context, voucher, isFirstItem)

            linearLayoutMerchantVoucher.addView(labelVoucher)
        }
    }

    private fun createLabelVoucher(context: Context, voucherText: String, isFirstItem: Boolean): Label {
        val layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.marginStart = if (isFirstItem) 0 else 4.toPx()

        val labelVoucher = Label(context)

        labelVoucher.setLabelType(Label.GENERAL_LIGHT_GREEN)
        labelVoucher.text = voucherText
        labelVoucher.layoutParams = layoutParams

        return labelVoucher
    }

    private fun setHeadlineShopDataCardWidget(cpmData: CpmData, adsBannerShopCardView: ShopCardView?, appLink: String, adsClickUrl: String) {
        val productList = cpmData.cpm?.cpmShop?.products ?: listOf()

        adsBannerShopCardView?.setShopCardModel(
                ShopCardModel(
                        id = cpmData.cpm?.cpmShop?.id ?: "",
                        name = cpmData.cpm?.cpmShop?.name ?: "",
                        image = cpmData.cpm?.cpmImage?.fullEcs ?: "",
                        location = context.getString(R.string.title_promote_by),
                        goldShop = if (cpmData.cpm?.cpmShop?.isPowerMerchant == true) 1 else 0,
                        productList = productList.map {
                            ShopCardModel.ShopItemProduct(
                                    id = it.id.toIntOrZero(),
                                    name = it.name?:"",
                                    priceFormat = it.priceFormat?:"",
                                    imageUrl = it.imageProduct?.imageUrl ?: ""
                            )
                        },
                        isOfficial = cpmData.cpm?.cpmShop?.isOfficial ?: false,
                        isPMPro = cpmData.cpm?.cpmShop?.isPMPro ?: false,
                        impressHolder = cpmData.cpm?.cpmShop?.imageShop
                ),
                object : ShopCardListener {
                    override fun onItemImpressed() {
                        impressionListener?.onImpressionHeadlineAdsItem(0, cpmData)

                        topAdsUrlHitter.hitImpressionUrl(
                                className,
                                cpmData.cpm?.cpmImage?.fullUrl,
                                cpmData.cpm?.cpmShop?.id,
                                cpmData.cpm?.uri,
                                cpmData.cpm?.cpmImage?.fullEcs
                        )
                    }

                    override fun onItemClicked() {
                        topAdsBannerClickListener?.onBannerAdsClicked(0, appLink, cpmData)

                        topAdsUrlHitter.hitClickUrl(
                                className,
                                adsClickUrl,
                                cpmData.cpm?.cpmShop?.id,
                                cpmData.cpm?.uri,
                                cpmData.cpm?.cpmImage?.fullEcs
                        )
                    }

                    override fun onProductItemImpressed(productPreviewIndex: Int) {
                        impressionListener?.onImpressionHeadlineAdsItem(productPreviewIndex, cpmData)
                    }

                    override fun onProductItemClicked(productPreviewIndex: Int) {
                        val product = productList.getOrNull(productPreviewIndex) ?: return

                        topAdsBannerClickListener?.onBannerAdsClicked(productPreviewIndex, product.applinks, cpmData)

                        topAdsUrlHitter.hitClickUrl(
                                className,
                                product.imageProduct?.imageClickUrl,
                                product.id,
                                product.uri,
                                product.imageProduct?.imageUrl
                        )
                    }
                }
        )
    }

    private fun isEligible(cpmData: CpmData?) =
            cpmData != null
                    && cpmData.cpm?.cpmShop != null
                    && (cpmData.cpm?.cpmShop?.products?.size?:0 > 1 || showProductShimmer)

    private fun activityIsFinishing(context: Context): Boolean {
        return if (context is Activity) {
            context.isFinishing
        } else false
    }

    @Throws(Exception::class)
    private fun renderViewCpmDigital(context: Context, cpm: Cpm) {
        if (activityIsFinishing(context))
            return
        if (template == NO_TEMPLATE) {
            View.inflate(getContext(), R.layout.layout_ads_banner_digital, this)
            template = DIGITAL_TEMPLATE
        }
        setHeadlineDigitalData(context, cpm)
    }

    private fun setHeadlineDigitalData(context: Context, cpm: Cpm) {
        try {
            Glide.with(context)
                    .asBitmap()
                    .load(cpm.cpmImage?.fullEcs)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            if (image != null) {
                                image.setImageBitmap(resource)
                                topAdsUrlHitter.hitImpressionUrl(className, cpm.cpmImage?.fullUrl, "", "", "")
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            name.text = escapeHTML(cpm.name ?: "")
            description.text = escapeHTML(cpm.decription ?: "")
            cta_btn.text = if (cpm.cta == null) "" else cpm.cta
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setTopAdsBannerClickListener(topAdsBannerClickListener: TopAdsBannerClickListener) {
        this.topAdsBannerClickListener = topAdsBannerClickListener
    }

    fun setTopAdsShopFollowClickListener(topAdsShopFollowBtnClickListener: TopAdsShopFollowBtnClickListener) {
        this.topAdsShopFollowBtnClickListener = topAdsShopFollowBtnClickListener
    }

    fun setTopAdsImpressionListener(adsImpressionListener: TopAdsItemImpressionListener) {
        this.impressionListener = adsImpressionListener
    }

    fun setHasAddToCartButton(hasAddToCartButton: Boolean) {
        this.hasAddToCartButton = hasAddToCartButton
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

    fun displayHeadlineAds(cpmModel: CpmModel?, index: Int = 0){
        displayAds(cpmModel, index)
    }

    override fun displayAds(cpmModel: CpmModel?, index: Int) {
        try {
            if (cpmModel?.data?.size?:0 > 0) {
                val data = cpmModel?.data?.first()
                if (data?.cpm != null) {
                    if (data.cpm?.cpmShop != null && isResponseValid(data)) {
                        if (data.applinks != null && data.adClickUrl != null) {
                            renderViewCpmShop(
                                context,
                                cpmModel,
                                data.applinks ?: "",
                                data.adClickUrl ?: "",
                                index
                            )
                        }
                    } else if (data.cpm?.templateId == ITEM_4) {
                        data.cpm?.let {renderViewCpmDigital(context, it)  }
                        setOnClickListener {
                            if (topAdsBannerClickListener != null) {
                                topAdsBannerClickListener!!.onBannerAdsClicked(0, data.applinks, data)
                                topAdsUrlHitter.hitClickUrl(className, data.adClickUrl, "", "", "")
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
        return data.cpm?.cta?.isNotEmpty()?:false && data.cpm?.promotedText?.isNotEmpty()?:false
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
