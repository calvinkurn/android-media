package com.tokopedia.tokopoints.view.tokopointhome

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CarouselItemDecoration
import com.tokopedia.tokopoints.view.adapter.SectionCarouselAdapter
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant

class SectionHorizontalCarousel21ViewBinder(val block: SectionContent)
    : SectionItemViewBinder<SectionContent, SectionHorizontalCarousel21VH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return SectionHorizontalCarousel21VH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionHorizontalCarousel21VH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_carousel21

}

class SectionHorizontalCarousel21VH(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_21)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title_21).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title_21) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title_21).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title_21) as TextView).text = content.sectionSubTitle
        }

        itemView.apply {


            val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel_21)
            rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            rvCarousel.adapter = SectionCarouselAdapter(content.layoutBannerAttr.imageList, CommonConstant.BannerType.CAROUSEL_2_1)
        }
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