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
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardListener
import com.tokopedia.shopwidget.shopcard.ShopCardModel
import com.tokopedia.shopwidget.shopcard.ShopCardView
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.base.Config
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.listener.*
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter
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
import kotlinx.android.synthetic.main.layout_ads_banner_digital.view.description
import kotlinx.android.synthetic.main.layout_ads_banner_shop_a_pager.view.*
import kotlinx.android.synthetic.main.layout_ads_banner_shop_b.view.shop_name
import kotlinx.android.synthetic.main.layout_ads_banner_shop_b_pager.view.*
import org.apache.commons.text.StringEscapeUtils
import javax.inject.Inject

/**
 * Created by errysuprayogi on 12/28/17.
 */

private const val NO_TEMPLATE = 0
private const val SHOP_TEMPLATE = 1
private const val DIGITAL_TEMPLATE = 2
private const val LAYOUT_2 = 2

class TopAdsBannerView : LinearLayout, BannerAdsContract.View {
    private var adsListener: TopAdsListener? = null
    private var topAdsBannerClickListener: TopAdsBannerClickListener? = null
    private var impressionListener: TopAdsItemImpressionListener? = null
    private var topAdsShopFollowBtnClickListener: TopAdsShopFollowBtnClickListener? = null
    private var bannerAdsAdapter: BannerAdsAdapter? = null
    private val className: String = "com.tokopedia.topads.sdk.widget.TopAdsBannerView"
    private var showProductShimmer: Boolean = false
    private val topAdsUrlHitter: TopAdsUrlHitter by lazy {
        TopAdsUrlHitter(context)
    }

    @Inject
    lateinit var bannerPresenter: BannerAdsPresenter
    private var template = NO_TEMPLATE

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @Throws(Exception::class)
    private fun renderViewCpmShop(context: Context, cpmData: CpmData, appLink: String, adsClickUrl: String) {
        if (activityIsFinishing(context))
            return
        if (template == NO_TEMPLATE && isEligible(cpmData)) {
            View.inflate(getContext(), R.layout.layout_ads_banner_shop_a_pager, this)
            BannerShopProductViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product
            BannerShopViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a
            BannerShowMoreViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_more
            findViewById<TextView>(R.id.shop_name)?.text = escapeHTML(cpmData.cpm.name)
            bannerAdsAdapter = BannerAdsAdapter(BannerAdsAdapterTypeFactory(topAdsBannerClickListener, impressionListener))
            val list = findViewById<RecyclerView>(R.id.list)
            list.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            list.adapter = bannerAdsAdapter
            list.addOnScrollListener(CustomScrollListner(back_view))
            val snapHelper = GravitySnapHelper(Gravity.START)
            snapHelper.attachToRecyclerView(list)

            template = SHOP_TEMPLATE
        }
        setHeadlineShopData(cpmData, appLink, adsClickUrl)
    }

    private fun setHeadlineShopData(cpmData: CpmData?, appLink: String, adsClickUrl: String) {
        if (isEligible(cpmData)) {
            val container = findViewById<View>(R.id.container)
            val list = findViewById<RecyclerView?>(R.id.list)
            val shopDetail = findViewById<View?>(R.id.shop_detail)
            val adsBannerShopCardView = findViewById<ShopCardView?>(R.id.adsBannerShopCardView)

            if (cpmData != null && cpmData.cpm.layout == LAYOUT_2) {
                list?.gone()
                shopDetail?.gone()
                adsBannerShopCardView?.visible()
                container?.setBackgroundResource(0)
                (container?.layoutParams as? MarginLayoutParams)?.setMargins(0, 4.toPx(), 0, 0)

                setHeadlineShopDataCardWidget(cpmData, adsBannerShopCardView, appLink, adsClickUrl)
            } else if (cpmData != null) {
                list?.visible()
                shopDetail?.visible()
                adsBannerShopCardView?.gone()
                (container?.layoutParams as? MarginLayoutParams)?.setMargins(0, 12.toPx(), 0, 0)

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
                        Glide.with(shop_badge).load(cpmData.cpm.badges[0].imageUrl).into(shop_badge)
                    } else {
                        shop_badge.hide()
                    }
                }
                shop_name?.text = MethodChecker.fromHtml(cpmData.cpm?.cpmShop?.name)
                description?.text = cpmData.cpm?.cpmShop?.slogan
                if (cpmData.cpm?.cpmShop?.isFollowed != null && topAdsShopFollowBtnClickListener != null) {
                    bindFavorite(cpmData.cpm.cpmShop.isFollowed)
                    btnFollow.setOnClickListener {
                        cpmData.cpm?.cpmShop?.id?.let { it1 -> topAdsShopFollowBtnClickListener?.onFollowClick(it1) }
                        if (!cpmData.cpm.cpmShop.isFollowed) {
                            topAdsUrlHitter.hitClickUrl(className, cpmData.adClickUrl, "", "", "")
                        }
                    }
                    btnFollow.show()
                } else {
                    btnFollow.hide()
                }

                val shopdetail = findViewById<View>(R.id.shop_detail)

                shopdetail.setOnClickListener {
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
                                topAdsUrlHitter.hitImpressionUrl(className, cpmData.cpm.cpmImage.fullUrl, "", "", "")
                            }
                        }
                    }
                }

                renderLabelMerchantVouchers(cpmData)

                val items = ArrayList<Item<*>>()
                items.add(BannerShopViewModel(cpmData, appLink, adsClickUrl))
                if (cpmData.cpm?.cpmShop?.products?.isNotEmpty() == true) {
                    val productCardModelList: ArrayList<ProductCardModel> = getProductCardModels(cpmData.cpm.cpmShop.products)
                    for (i in 0 until productCardModelList.size) {
                        if (i < 3) {
                            items.add(BannerShopProductViewModel(cpmData, productCardModelList[i],
                                    cpmData.cpm.cpmShop.products[i].applinks,
                                    cpmData.cpm.cpmShop.products[i].image.m_url,
                                    cpmData.cpm.cpmShop.products[i].imageProduct.imageClickUrl))
                        }
                    }
                    if (productCardModelList.size < 3) {
                        items.add(BannerShopViewMoreModel(cpmData, appLink, adsClickUrl))
                    }
                } else {
                    repeat(3) { items.add(BannerProductShimmerViewModel()) }
                }
                bannerAdsAdapter?.setList(items)
            }
        }
    }

    private fun getProductCardModels(products: List<Product>): ArrayList<ProductCardModel> {
        return ArrayList<ProductCardModel>().apply {
            products.map {
                add(getProductCardViewModel(it))
            }
        }
    }

    private fun getProductCardViewModel(product: Product): ProductCardModel {
        return ProductCardModel(productImageUrl = product.imageProduct.imageUrl,
                productName = product.name, discountPercentage = if (product.campaign.discountPercentage != 0) "${product.campaign.discountPercentage}%" else "",
                slashedPrice = product.campaign.originalPrice, formattedPrice = product.priceFormat,
                reviewCount = product.countReviewFormat.toIntOrZero(), ratingCount = product.productRating,
                ratingString = product.productRatingFormat, countSoldRating = product.headlineProductRatingAverage,
                freeOngkir = ProductCardModel.FreeOngkir(product.freeOngkir.isActive, product.freeOngkir.imageUrl),
                labelGroupList = ArrayList<ProductCardModel.LabelGroup>().apply {
                    product.labelGroupList.map {
                        add(ProductCardModel.LabelGroup(it.position, it.title, it.type))
                    }
                })
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
                                    name = it.name,
                                    priceFormat = it.priceFormat,
                                    imageUrl = it.imageProduct?.imageUrl ?: ""
                            )
                        },
                        isOfficial = cpmData.cpm?.cpmShop?.isOfficial ?: false,
                        impressHolder = cpmData.cpm?.cpmShop?.imageShop
                ),
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
                        topAdsBannerClickListener?.onBannerAdsClicked(0, appLink, cpmData)

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

                        topAdsBannerClickListener?.onBannerAdsClicked(productPreviewIndex, product.applinks, cpmData)

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

    private fun isEligible(cpmData: CpmData?) =
            cpmData != null
                    && cpmData.cpm.cpmShop != null
                    && (cpmData.cpm.cpmShop.products.size > 1 || showProductShimmer)

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
                    .load(cpm.cpmImage.fullEcs)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            if (image != null) {
                                image.setImageBitmap(resource)
                                topAdsUrlHitter.hitImpressionUrl(className, cpm.cpmImage.fullUrl, "", "", "")
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            name.text = escapeHTML(if (cpm.name == null) "" else cpm.name)
            description.text = escapeHTML(if (cpm.decription == null) "" else cpm.decription)
            cta_btn.text = if (cpm.cta == null) "" else cpm.cta
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setConfig(config: Config) {
        bannerPresenter.setConfig(config)
    }

    fun setAdsListener(adsListener: TopAdsListener) {
        this.adsListener = adsListener
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

    override fun showLoading() {

    }

    fun displayAdsWithProductShimmer(cpmModel: CpmModel?, showProductShimmer: Boolean = false) {
        this.showProductShimmer = showProductShimmer
        displayAds(cpmModel)
    }

    override fun displayAds(cpmModel: CpmModel?) {
        try {
            if (cpmModel != null && cpmModel.data.size > 0) {
                val data = cpmModel.data[0]
                if (data != null && data.cpm != null) {
                    if (data.cpm.cpmShop != null && isResponseValid(data)) {
                        renderViewCpmShop(context, data, data.applinks, data.adClickUrl)
                    } else if (data.cpm.templateId == 4) {
                        renderViewCpmDigital(context, data.cpm)
                        setOnClickListener {
                            if (topAdsBannerClickListener != null) {
                                topAdsBannerClickListener!!.onBannerAdsClicked(0, data.applinks, data)
                                topAdsUrlHitter.hitClickUrl(className, data.adClickUrl, "", "", "")
                            }
                        }
                    }
                }
            }
            if (adsListener != null) {
                adsListener!!.onTopAdsLoaded(null)
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

    override fun loadTopAds() {
        bannerPresenter.loadTopAds()
    }

    override fun notifyAdsErrorLoaded(errorCode: Int, message: String) {
        if (adsListener != null) {
            adsListener!!.onTopAdsFailToLoad(errorCode, message)
        }
    }

    fun init() {
        val application = context.applicationContext as BaseMainApplication
        val component = DaggerTopAdsComponent.builder()
                .baseAppComponent(application.baseAppComponent)
                .build()
        component.inject(this)
        component.inject(bannerPresenter)
        bannerPresenter.attachView(this)
    }

    companion object {

        private val TAG = TopAdsBannerView::class.java.simpleName

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
