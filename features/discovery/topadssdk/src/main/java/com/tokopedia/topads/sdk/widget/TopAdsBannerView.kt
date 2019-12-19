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
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.topads.sdk.R
import com.tokopedia.topads.sdk.base.Config
import com.tokopedia.topads.sdk.base.adapter.Item
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.listener.TopAdsBannerClickListener
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener
import com.tokopedia.topads.sdk.listener.TopAdsListener
import com.tokopedia.topads.sdk.presenter.BannerAdsPresenter
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.topads.sdk.view.BannerAdsContract
import com.tokopedia.topads.sdk.view.adapter.BannerAdsAdapter
import com.tokopedia.topads.sdk.view.adapter.factory.BannerAdsAdapterTypeFactory
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopProductViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewholder.banner.BannerShopViewHolder
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopProductViewModel
import com.tokopedia.topads.sdk.view.adapter.viewmodel.banner.BannerShopViewModel
import kotlinx.android.synthetic.main.layout_ads_banner_digital.view.*
import org.apache.commons.text.StringEscapeUtils
import java.util.*
import javax.inject.Inject

/**
 * Created by errysuprayogi on 12/28/17.
 */

class TopAdsBannerView : LinearLayout, BannerAdsContract.View {
    private var adsListener: TopAdsListener? = null
    private var topAdsBannerClickListener: TopAdsBannerClickListener? = null
    private var impressionListener: TopAdsItemImpressionListener? = null
    private var bannerAdsAdapter: BannerAdsAdapter? = null
    private val NO_TEMPLATE = 0
    private val SHOP_TEMPLATE = 1
    private val DIGITAL_TEMPLATE = 2
    private final val VARIANT_A = "Headline Ads A"
    private final val VARIANT_B = "Headline Ads B"
    private final val AB_TEST_KEY = "Headline Ads New Design"

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
        if (template == NO_TEMPLATE) {
            var variant = RemoteConfigInstance.getInstance().abTestPlatform.getString(AB_TEST_KEY, VARIANT_A)
            if(variant.equals(VARIANT_B)) {
                View.inflate(getContext(), R.layout.layout_ads_banner_shop_b_pager, this)
                BannerShopProductViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_b_product
                BannerShopViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_b
            } else{
                View.inflate(getContext(), R.layout.layout_ads_banner_shop_a_pager, this)
                BannerShopProductViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a_product
                BannerShopViewHolder.LAYOUT = R.layout.layout_ads_banner_shop_a
            }

            findViewById<TextView>(R.id.shop_name)?.text = escapeHTML(cpmData.cpm.name)
            bannerAdsAdapter = BannerAdsAdapter(BannerAdsAdapterTypeFactory(topAdsBannerClickListener, impressionListener))
            var list = findViewById<RecyclerView>(R.id.list)
            list.layoutManager = LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
            list.adapter = bannerAdsAdapter
            var shop_image = findViewById<ImageView>(R.id.shop_image)
            shop_image?.let {
                Glide.with(context).load(cpmData.cpm.cpmShop.imageShop.getsEcs()).into(shop_image)
                shop_image.addOnImpressionListener(cpmData.cpm.cpmShop.imageShop) {
                    impressionListener?.let {
                        it.onImpressionHeadlineAdsItem(0, cpmData)
                        ImpresionTask().execute(cpmData.cpm.cpmShop.imageShop.getsUrl())
                    }
                }
            }
            if (cpmData.cpm.cpmShop.isPowerMerchant && !cpmData.cpm.cpmShop.isOfficial) {
                list.background = ContextCompat.getDrawable(context, R.drawable.bg_pm_gradient)
            } else if (cpmData.cpm.cpmShop.isOfficial) {
                list.background = ContextCompat.getDrawable(context, R.drawable.bg_os_gradient)
            } else {
                list.background = ContextCompat.getDrawable(context, R.drawable.bg_rm_gradient)
            }
            template = SHOP_TEMPLATE
        }
        setHeadlineShopData(cpmData, appLink, adsClickUrl)
    }

    private fun setHeadlineShopData(cpmData: CpmData?, appLink: String, adsClickUrl: String) {
        if (cpmData != null && cpmData.cpm.cpmShop != null) {
            var shop_badge = findViewById<ImageView>(R.id.shop_badge)
            shop_badge?.let {
                if (cpmData.cpm.badges.size > 0) {
                    shop_badge.visibility = View.VISIBLE
                    Glide.with(shop_badge).load(cpmData.cpm.badges[0].imageUrl).into(shop_badge)
                } else {
                    shop_badge.visibility = View.GONE
                }
            }
            val items = ArrayList<Item<*>>()
            items.add(BannerShopViewModel(cpmData, appLink, adsClickUrl))
            for (i in 0 until cpmData.cpm.cpmShop.products.size) {
                items.add(BannerShopProductViewModel(cpmData, cpmData.cpm.cpmShop.products[i],
                        appLink, adsClickUrl))
            }
            bannerAdsAdapter!!.setList(items)
        }
    }

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
                                ImpresionTask().execute(cpm.cpmImage.fullUrl)
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

    fun setTopAdsImpressionListener(adsImpressionListener: TopAdsItemImpressionListener) {
        this.impressionListener = adsImpressionListener
    }

    override fun showLoading() {

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
                                ImpresionTask().execute(data.adClickUrl)
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
        return !data.cpm.cta.isEmpty() && !data.cpm.promotedText.isEmpty()
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
            try {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(StringEscapeUtils.unescapeHtml4(s), Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    Html.fromHtml(StringEscapeUtils.unescapeHtml4(s)).toString()
                }
            } catch (e: Exception) {
                return ""
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
