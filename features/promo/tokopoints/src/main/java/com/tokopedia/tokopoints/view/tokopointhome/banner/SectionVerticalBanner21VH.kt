package com.tokopedia.tokopoints.view.tokopointhome.banner

import android.text.TextUtils
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CustomConstraintProvider
import java.util.HashMap

class SectionVerticalBanner21VH(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(content: SectionContent) {
        if (content.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)

        if (content.sectionSubTitle.isNullOrEmpty() && !content.cta.isEmpty){
            CustomConstraintProvider.setCustomConstraint(view, R.id.parent_layout, R.id.text_see_all, R.id.text_title, ConstraintSet.BASELINE)
        }

        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title) as TextView).text = content.sectionSubTitle
        }
        if (content.layoutBannerAttr.imageList != null && !content.layoutBannerAttr.imageList.isEmpty()
                && URLUtil.isValidUrl(content.layoutBannerAttr.imageList[0].imageURLMobile)) {
            val imgBanner = view.findViewById<ImageView>(R.id.img_banner)
            val data = content.layoutBannerAttr.imageList[0]
            ImageHandler.loadImageFitCenter(imgBanner.context, imgBanner, data.imageURLMobile)
            imgBanner.setOnClickListener { v: View? ->
                handledClick(data.redirectAppLink, data.redirectURL, "", "")
                if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerClick(content.sectionTitle)
            }
            (view.findViewById<View>(R.id.text_title_banner) as TextView).text = data.inBannerTitle
            (view.findViewById<View>(R.id.text_sub_title_banner) as TextView).text = data.inBannerSubTitle
            if (!TextUtils.isEmpty(data.title)) {
                view.findViewById<View>(R.id.text_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_title_bottom) as TextView).text = data.title
            }
            if (!TextUtils.isEmpty(data.subTitle)) {
                view.findViewById<View>(R.id.text_sub_title_bottom).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.text_sub_title_bottom) as TextView).text = data.subTitle
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) sendBannerImpression(content.sectionTitle)
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
}