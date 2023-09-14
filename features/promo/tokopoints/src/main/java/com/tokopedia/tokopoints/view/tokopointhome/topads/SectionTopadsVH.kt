package com.tokopedia.tokopoints.view.tokopointhome.topads

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.databinding.TpTopadsRewardLayoutBinding
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.listener.TopAdsImageVieWApiResponseListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewClickListener
import com.tokopedia.topads.sdk.listener.TopAdsImageViewImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding
import org.json.JSONObject

class SectionTopadsVH(val view: View) : RecyclerView.ViewHolder(view) {

    private var binding: TpTopadsRewardLayoutBinding? by viewBinding()

    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
        }

        binding?.apply {
            val title = tvTopadTitle
            val subtitle = tvTopadsSubTitle
            ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
            val btnSeeAll = tvTopadsSeeAll
            if (!content.cta.isEmpty) {
                btnSeeAll.visibility = View.VISIBLE
                btnSeeAll.text = content.cta.text
                btnSeeAll.setOnClickListener {
                    handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
                }
            }
            if (!TextUtils.isEmpty(content.sectionTitle)) {
                title.show()
                title.text = content.sectionTitle
            }
            if (!TextUtils.isEmpty(content.sectionSubTitle)) {
                subtitle.show()
                subtitle.text = content.sectionSubTitle
            }

            topadsReward.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
                override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                    if (imageDataList.isNotEmpty()) {
                        val topadsBannerData = imageDataList.last()
                        topadsReward.loadImage(topadsBannerData, convertDpToPixel(10, view.context))
                        containerTopads.displayedChild = 1
                        topadsReward.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
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

                        topadsReward.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
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
            topadsReward.getImageData(
                jObject.getString(INVENTORY_ID),
                jObject.getInt(ITEM),
                TOPADS_BANNER_DIMENSION,
                "",
                ""
            )
        }
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
                RouteManager.route(view.context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
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
