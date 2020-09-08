package com.tokopedia.tokopoints.view.tokopointhome

import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CouponListAdapter
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil
import com.tokopedia.unifycomponents.TimerUnify

class SectionHorizontalViewHolder(val view: View)
    : RecyclerView.ViewHolder(view) {
    private var layoutManager: RecyclerView.LayoutManager? = null

    fun bind(content: SectionContent, layoutManagerState: Parcelable?) {

        if (content.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (content.countdownAttr != null &&
                content.countdownAttr.isShowTimer && content.countdownAttr.expiredCountDown > 0) {
            val countDownView = view.findViewById<TimerUnify>(R.id.tp_count_down_view)
            countDownView.findViewById<View>(R.id.tp_count_down_view)?.visibility = View.VISIBLE
            countDownView.remainingMilliseconds = content.countdownAttr.expiredCountDown * 1000
            countDownView.onFinish = { view.visibility = View.GONE }

            view.findViewById<View>(R.id.text_title).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_180)
        } else {
            view.findViewById<View>(R.id.text_title).layoutParams.width = view.resources.getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_280)
        }
        if (!content.cta.isEmpty) {
            val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all)
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.text = content.cta.text
            btnSeeAll.setOnClickListener { v: View? ->
                handledClick(content.cta.appLink, content.cta.url,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_SEE_ALL_EXPLORE_CATALOG, content.sectionTitle)
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
        val rvCarousel: RecyclerView = view.findViewById(R.id.rv_carousel)
        rvCarousel.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager = rvCarousel.layoutManager
        rvCarousel.adapter = CouponListAdapter(content.layoutCouponAttr.couponList)

        if (layoutManagerState != null) {
            // restoring the old instance so that scroll position is synced
            rvCarousel.layoutManager?.onRestoreInstanceState(layoutManagerState)
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

    fun getLayoutManagerState(): Parcelable? = layoutManager?.onSaveInstanceState()

}

