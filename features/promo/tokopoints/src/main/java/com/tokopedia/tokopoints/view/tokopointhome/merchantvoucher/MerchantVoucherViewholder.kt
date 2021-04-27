package com.tokopedia.tokopoints.view.tokopointhome.merchantvoucher

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.NonCarouselItemDecoration
import com.tokopedia.tokopoints.view.adapter.SectionMerchantCouponAdapter
import com.tokopedia.tokopoints.view.model.merchantcoupon.CatalogMVCWithProductsListItem
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CustomConstraintProvider
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import java.util.HashMap

class MerchantVoucherViewholder(val view: View)
    : RecyclerView.ViewHolder(view) {

    val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false) }
    fun bind(content: SectionContent) {

        if (content.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)

        if (content.sectionSubTitle.isNullOrEmpty() && !content.cta.isEmpty) {
            CustomConstraintProvider.setCustomConstraint(view, R.id.parent_layout, R.id.text_see_all_merchant, R.id.text_title_merchant, ConstraintSet.BASELINE)
        }

        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_merchant)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url, content.sectionTitle)
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title_merchant).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title_merchant) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title_merchant).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title_merchant) as TextView).text = content.sectionSubTitle
        }
        sendCouponImpression(content.sectionTitle)

        val rvCarousel: RecyclerView = view.findViewById(R.id.rv_merchant_coupon)
        rvCarousel?.isDrawingCacheEnabled = true
        rvCarousel.setHasFixedSize(true)
        rvCarousel?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        rvCarousel.layoutManager = layoutManager
        val arrayList = content.layoutMerchantCouponAttr.catalogMVCWithProductsList
        if (rvCarousel.itemDecorationCount == 0) {
            rvCarousel.addItemDecoration(NonCarouselItemDecoration(convertDpToPixel(10, rvCarousel.context)))
        }
        rvCarousel.adapter = SectionMerchantCouponAdapter(arrayList as MutableList<CatalogMVCWithProductsListItem> , rvCarousel.context)
    }

    fun handledClick(appLink: String?, webLink: String?, action: String?) {
        try {
            if (view.context == null) {
                return
            }
            if (TextUtils.isEmpty(appLink)) {
                RouteManager.getIntent(view.context, ApplinkConstInternalGlobal.WEBVIEW, webLink)
            } else {
                RouteManager.route(view.context, appLink)
            }
            AnalyticsTrackerUtil.sendEvent(
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_SEMUA,
                    AnalyticsTrackerUtil.EventKeys.EVENT_MVC_SECTION,
                    AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                    AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendCouponImpression(bannerName: String) {
        val promotionItem = HashMap<String, Any>()
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.NAME] = AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.POSITION] = -1
        promotionItem[AnalyticsTrackerUtil.EcommerceKeys.CREATIVE] = AnalyticsTrackerUtil.CategoryKeys.KUPON_TOKO

        val promotionMap = HashMap<String, Any>()
        promotionMap[AnalyticsTrackerUtil.EcommerceKeys.PROMOTIONS] = listOf(promotionItem)
        AnalyticsTrackerUtil.sendECommerceEvent(AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_PROMO,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.VIEW_MVC_COUPON_ON_REWARDS, "",
                AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
                AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE, promotionMap)
    }
}

