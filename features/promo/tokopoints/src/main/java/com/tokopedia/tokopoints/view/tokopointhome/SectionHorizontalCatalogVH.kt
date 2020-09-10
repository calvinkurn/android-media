package com.tokopedia.tokopoints.view.tokopointhome

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CatalogListCarouselAdapter
import com.tokopedia.tokopoints.view.adapter.NonCarouselItemDecoration
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.convertDpToPixel
import com.tokopedia.unifycomponents.TimerUnify

class SectionHorizontalCatalogVH(val view: View, val mPresenter: TokoPointsHomeViewModel)
    : RecyclerView.ViewHolder(view) {
    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
            return
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (content.countdownAttr != null &&
                content.countdownAttr.isShowTimer && content.countdownAttr.expiredCountDown > 0) {
            val countDownView = view.findViewById<TimerUnify>(R.id.tp_count_down_view)
            countDownView?.findViewById<View>(R.id.tp_count_down_view_column)?.visibility = View.VISIBLE
            countDownView?.remainingMilliseconds = content.countdownAttr.expiredCountDown * 1000
            countDownView?.onFinish = { view?.visibility = View.GONE }
            view.findViewById<View>(R.id.text_title_column).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_180)
        } else {
            view.findViewById<View>(R.id.text_title_column).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_280)
        }
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_column)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_CATALOG, content.sectionTitle)
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title_column).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title_column) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title_column).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title_column) as TextView).text = content.sectionSubTitle
        }
        val rvCarousel: RecyclerView = view.findViewById(R.id.rv_column)
        rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        if (rvCarousel.itemDecorationCount == 0) {
            rvCarousel.addItemDecoration(NonCarouselItemDecoration(convertDpToPixel(16, rvCarousel.context)))
        }
        rvCarousel.adapter = CatalogListCarouselAdapter(mPresenter, content.layoutCatalogAttr.catalogList, rvCarousel)

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

}