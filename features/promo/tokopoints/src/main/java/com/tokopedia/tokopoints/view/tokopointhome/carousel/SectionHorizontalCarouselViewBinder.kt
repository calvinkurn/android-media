package com.tokopedia.tokopoints.view.tokopointhome.carousel

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CarouselItemDecorationNew
import com.tokopedia.tokopoints.view.adapter.SectionCarouselAdapter
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.SectionItemViewBinder
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.tokopoints.view.util.CommonConstant
import com.tokopedia.tokopoints.view.util.CustomConstraintProvider
import com.tokopedia.tokopoints.view.util.convertDpToPixel

class SectionHorizontalCarouselViewBinder()
    : SectionItemViewBinder<SectionContent, SectionHorizontalCarouselVH>(
        SectionContent::class.java) {
    override fun createViewHolder(parent: ViewGroup): SectionHorizontalCarouselVH {
        return SectionHorizontalCarouselVH(
                LayoutInflater.from(parent.context).inflate(getSectionItemType(), parent, false))
    }

    override fun bindViewHolder(model: SectionContent, viewHolder: SectionHorizontalCarouselVH) {
        viewHolder.bind(model)
    }

    override fun getSectionItemType() = R.layout.tp_layout_carousel
}

class SectionHorizontalCarouselVH(val view: View) : RecyclerView.ViewHolder(view) {
     val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false) }
    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutBannerAttr == null) {
            view.visibility = View.GONE
            return
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (content.sectionSubTitle.isNullOrEmpty() && !content.cta.isEmpty) {
            CustomConstraintProvider.setCustomConstraint(view, R.id.parent_layout, R.id.text_see_all_carousel, R.id.text_title_carousel,ConstraintSet.BASELINE)
        }

        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_carousel)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_BANNER, "")
            }
        }
        if (!TextUtils.isEmpty(content.sectionTitle)) {
            view.findViewById<View>(R.id.text_title_carousel).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_title_carousel) as TextView).text = content.sectionTitle
        }
        if (!TextUtils.isEmpty(content.sectionSubTitle)) {
            view.findViewById<View>(R.id.text_sub_title_carousel).visibility = View.VISIBLE
            (view.findViewById<View>(R.id.text_sub_title_carousel) as TextView).text = content.sectionSubTitle
        }

        val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel_31)
        rvCarousel.layoutManager = layoutManager
        if (rvCarousel?.itemDecorationCount == 0) {
            rvCarousel?.addItemDecoration(CarouselItemDecorationNew(convertDpToPixel(10, rvCarousel.context),convertDpToPixel(20, rvCarousel.context)))
        }
        rvCarousel.adapter = SectionCarouselAdapter(content.layoutBannerAttr.imageList, content.layoutBannerAttr.bannerType)

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
