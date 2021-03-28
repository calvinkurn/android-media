package com.tokopedia.tokopoints.view.tokopointhome.topads

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlinx.android.synthetic.main.tp_topads_reward_layout.view.*
import org.json.JSONObject
import java.util.*

class SectionTopadsVH(val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
        }

        val title = view.findViewById<View>(R.id.tv_topad_title)
        val subtitle = view.findViewById<View>(R.id.tv_topads_sub_title)
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        val btnSeeAll = view.findViewById<TextView>(R.id.tv_topads_see_all)
        if (!content.cta.isEmpty) {
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            title.show()
            (title as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            subtitle.show()
            (subtitle as TextView).text = content.sectionSubTitle
        }
        val containerTopads = view.findViewById<ViewFlipper>(R.id.container_topads)

        view.topads_reward.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                if (imageDataList.isNotEmpty()) {
                    val topadsBannerData = imageDataList.last()
                    view.topads_reward.loadImage(topadsBannerData, convertDpToPixel(10, view.context))
                    containerTopads.displayedChild = 1
                    view.topads_reward.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
                        override fun onTopAdsImageViewImpression(viewUrl: String) {
                            sendBannerImpression(content.sectionTitle)
                            TopAdsUrlHitter(packageName).hitImpressionUrl(
                                    view.context,
                                    viewUrl,
                                    topadsBannerData.bannerId,
                                    topadsBannerData.bannerName,
                                    topadsBannerData.imageUrl
                            )
                        }
                    })

                    view.topads_reward.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                        override fun onTopAdsImageViewClicked(applink: String?) {
                            RouteManager.route(view.context, applink)
                            TopAdsUrlHitter(itemView.context).hitClickUrl(
                                    this::class.java.simpleName,
                                    topadsBannerData.adClickUrl,
                                    topadsBannerData.bannerId,
                                    topadsBannerData.bannerName,
                                    topadsBannerData.imageUrl,
                                    ""
                            )
                            sendBannerClick(content.sectionTitle)
                        }
                    })
                } else {
                    hideView()

                }
            }

            override fun onError(t: Throwable) {
                hideView()
            }

            fun hideView() {
                title.hide()
                subtitle.hide()
                btnSeeAll.hide()
                containerTopads.hide()
            }
        })

        val jObject = JSONObject(content.layoutTopAdsAttr.jsonTopAdsDisplayParam)
        view.topads_reward.getImageData(
                jObject.getString(INVENTORY_ID),
                jObject.getInt(ITEM),
                TOPADS_BANNER_DIMENSION,
                "",
                ""
        )
    }

    private fun sendBannerImpression(bannerName: String) {
        val promotionItem = HashMap<String, Any>()
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.NAME] = CommonConstant.IMPRESSION_LIST
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.POSITION] = -1
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.CREATIVE] = bannerName
        val promotionMap = HashMap<String, Any>()
        promotionMap[AnalyticsTrackerUtil.EcommerceKeys.PROMOTIONS] = listOf(promotionItem)
        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.VIEW_BANNERS_ON_HOME_TOKOPOINTS,
                bannerName, promotionMap)
    }


    fun handledClick(appLink: String?, webLink: String?, action: String?, label: String?) {
        try {
            if (view.context == null) {
                return
            }
            if (TextUtils.isEmpty(appLink)) {
                RouteManager.getIntent(view.context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
            } else {
                RouteManager.route(view.context, appLink)
            }
            AnalyticsTrackerUtil.sendEvent(view.context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    action,
                    label)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendBannerClick(bannerName: String) {
        val promotionItem = HashMap<String, Any>()
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.NAME] = CommonConstant.IMPRESSION_LIST
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.POSITION] = -1
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.CREATIVE] = bannerName
        val promotionMap = HashMap<String, Any>()
        promotionMap[AnalyticsTrackerUtil.EcommerceKeys.PROMOTIONS] = listOf(promotionItem)
        AnalyticsTrackerUtil.sendECommerceEventBanner(AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_BANNERS_ON_HOME_TOKOPOINTS,
                bannerName, promotionMap)
    }

    companion object {
        val packageName = SectionTopadsVH::class.java.`package`.toString()
        const val ITEM = "item"
        const val INVENTORY_ID = "inventory_id"
        const val TOPADS_BANNER_DIMENSION = 3
    }

}