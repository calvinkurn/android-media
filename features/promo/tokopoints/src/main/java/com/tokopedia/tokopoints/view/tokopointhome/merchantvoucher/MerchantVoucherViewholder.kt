package com.tokopedia.tokopoints.view.tokopointhome.merchantvoucher

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImageBackground
import com.tokopedia.mvcwidget.MVC_ADINFO
import com.tokopedia.mvcwidget.MVC_SOURCE_KEY
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.NonCarouselItemDecoration
import com.tokopedia.tokopoints.view.adapter.SectionMerchantCouponAdapter
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem
import com.tokopedia.mvcwidget.multishopmvc.verticallist.MerchantCouponActivity
import com.tokopedia.mvcwidget.trackers.MvcSource.Companion.REWARDS
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CustomConstraintProvider
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import com.tokopedia.unifyprinciples.Typography
import java.util.HashMap

class MerchantVoucherViewholder(val view: View)
    : RecyclerView.ViewHolder(view) {

    private var tvTitle: Typography? = null
    private var tvSubTitle: Typography? = null

    init {
        tvTitle=view.findViewById(R.id.text_title_merchant)
        tvSubTitle = view.findViewById(R.id.text_sub_title_merchant)
    }
    val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(view.context,
        LinearLayoutManager.HORIZONTAL, false) }
    fun bind(content: SectionContent) {

        if (content.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
        }
        view?.loadImageBackground(content.backgroundImgURLMobile)

        if (content.sectionSubTitle.isNullOrEmpty() && !content.cta.isEmpty) {
            CustomConstraintProvider.setCustomConstraint(view, R.id.parent_layout, R.id.text_see_all_merchant,
                R.id.text_title_merchant, ConstraintSet.BASELINE)
        }

        if (!TextUtils.isEmpty(content.sectionTitle)) {
            tvTitle?.visibility = View.VISIBLE
            tvTitle?.text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            tvSubTitle?.visibility = View.VISIBLE
            tvSubTitle?.text = content.sectionSubTitle
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
        val adapter= SectionMerchantCouponAdapter(arrayList as ArrayList<CatalogMVCWithProductsListItem>)
        rvCarousel.adapter = adapter

        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_merchant)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable(MVC_ADINFO,adapter.getAdInfo())
                bundle.putInt(MVC_SOURCE_KEY, REWARDS)
                view.context.startActivity(MerchantCouponActivity.getMerchantCoupon(this.view.context,bundle))
                seeAllAnalytics()
            }
        }
    }

    private fun seeAllAnalytics() {
        AnalyticsTrackerUtil.sendEvent(
            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
            AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_SEMUA,
            AnalyticsTrackerUtil.EventKeys.EVENT_MVC_SECTION,
            AnalyticsTrackerUtil.EcommerceKeys.BUSINESSUNIT,
            AnalyticsTrackerUtil.EcommerceKeys.CURRENTSITE
        )
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

    interface GetAdInfoData{
        fun getAdInfo():HashSet<String?>
    }
}

