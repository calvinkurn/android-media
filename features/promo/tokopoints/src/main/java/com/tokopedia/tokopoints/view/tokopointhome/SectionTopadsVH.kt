package com.tokopedia.tokopoints.view.tokopointhome

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
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

        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.tv_topads_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.tv_topad_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.tv_topad_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.tv_topads_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.tv_topads_sub_title) as TextView).text = content.sectionSubTitle
        }
        val containerTopads = view.findViewById<ViewFlipper>(R.id.container_topads)

        val jObject = JSONObject(content.layoutTopAdsAttr.jsonTopAdsDisplayParam)
        view.topads_reward.getImageData(
                jObject.getString(INVENTORY_ID),
                jObject.getInt(ITEM),
                TOPADS_BANNER_DIMENSION,
                "",
                ""
        )

        view.topads_reward.setApiResponseListener(object : TopAdsImageVieWApiResponseListener {
            override fun onImageViewResponse(imageDataList: ArrayList<TopAdsImageViewModel>) {
                if (imageDataList.isNotEmpty()) {
                    containerTopads.displayedChild = 1
                    val stack: Stack<TopAdsImageViewModel> = Stack()
                    stack.addAll(imageDataList.toMutableList())
                    stack.reverse()
                    view.topads_reward.loadImage(stack.pop())
                    view.topads_reward.setTopAdsImageViewImpression(object : TopAdsImageViewImpressionListener {
                        override fun onTopAdsImageViewImpression(viewUrl: String) {
                            sendBannerImpression(content.sectionTitle)
                            TopAdsUrlHitter(packageName).hitImpressionUrl(
                                    view.context,
                                    viewUrl,
                                    "",
                                    "",
                                    ""
                            )
                        }
                    })

                    view.topads_reward.setTopAdsImageViewClick(object : TopAdsImageViewClickListener {
                        override fun onTopAdsImageViewClicked(applink: String?) {
                            RouteManager.route(view.context, applink)
                        }
                    })
                } else
                    view.hide()
            }

            override fun onError(t: Throwable) {
                view.hide()
            }
        })
    }

    private fun sendBannerImpression(bannerName: String) {
        val promotionItem = HashMap<String, Any>()
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.NAME] = "/tokopoints - p{x} - promo lis"
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

    companion object {
        val packageName = SectionTopadsVH::class.java.`package`.toString()
        const val ITEM = "item"
        const val INVENTORY_ID = "inventory_id"
        const val TOPADS_BANNER_DIMENSION = 3
    }

}