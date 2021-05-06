package com.tokopedia.tokopoints.view.tokopointhome.recommendation

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CarouselItemDecoration
import com.tokopedia.tokopoints.view.adapter.CouponListAdapter
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommAdapter
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecommendation
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CustomConstraintProvider
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import com.tokopedia.unifycomponents.TimerUnify

class SectionRecomVH(val view: View) : RecyclerView.ViewHolder(view) {
    val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false) }
    fun bind( data : RewardsRecommendation) {

        /*if (content.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
        }

        if (content.sectionSubTitle.isNullOrEmpty() && !content.cta.isEmpty) {
            CustomConstraintProvider.setCustomConstraint(view, R.id.parent_layout, R.id.text_see_all_recomm, R.id.text_title_recomm, ConstraintSet.BASELINE)
        }

        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_recomm)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url, content.sectionTitle)
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title_recomm).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title_recomm) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title_recomm).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title_recomm) as TextView).text = content.sectionSubTitle
        }*/

        val rvCarousel: RecyclerView = view.findViewById(R.id.rv_recomm)
        rvCarousel?.isDrawingCacheEnabled = true
        rvCarousel.setHasFixedSize(true)
        rvCarousel?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        rvCarousel.layoutManager = layoutManager
        if (rvCarousel.itemDecorationCount == 0) {
            rvCarousel.addItemDecoration(CarouselItemDecoration(convertDpToPixel(8, rvCarousel.context)))
        }
        rvCarousel.adapter = RewardsRecommAdapter(data.recomData as ArrayList<ProductCardModel>)

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
            AnalyticsTrackerUtil.sendEvent(view.context,
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_CTA_COUPON,
                    " ")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}