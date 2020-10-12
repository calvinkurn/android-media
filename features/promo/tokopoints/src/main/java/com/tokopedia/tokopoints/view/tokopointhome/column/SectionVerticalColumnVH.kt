package com.tokopedia.tokopoints.view.tokopointhome.column

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.ColumnItemDecoration
import com.tokopedia.tokopoints.view.adapter.SectionColumnAdapter
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant

class SectionVerticalColumnVH(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink,
                        content.cta.url, AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
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
        val rvCarousel: RecyclerView = view.findViewById(R.id.rv_column)
        var spanCount = 2
        if (content.layoutBannerAttr.bannerType == CommonConstant.BannerType.COLUMN_3_1_BY_1) {
            spanCount = 3
        } else if (content.layoutBannerAttr.bannerType == CommonConstant.BannerType.COLUMN_2_1_BY_1) {
            if (rvCarousel.itemDecorationCount == 0) {
                rvCarousel.addItemDecoration(ColumnItemDecoration(0, view.context.resources.getDimensionPixelOffset(R.dimen.tp_margin_small),
                        view.context.resources.getDimensionPixelOffset(R.dimen.tp_margin_small)))
            }
        }
        rvCarousel.layoutManager = GridLayoutManager(view.context, spanCount, GridLayoutManager.VERTICAL, false)
        rvCarousel.setHasFixedSize(true)
        rvCarousel.adapter = SectionColumnAdapter(content.layoutBannerAttr.imageList, content.layoutBannerAttr.bannerType)

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