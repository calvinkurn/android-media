package com.tokopedia.tokopoints.view.tokopointhome.catalog

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CatalogListCarouselAdapter
import com.tokopedia.tokopoints.view.adapter.NonCarouselItemDecoration
import com.tokopedia.tokopoints.view.model.section.SectionContent
import com.tokopedia.tokopoints.view.tokopointhome.TokoPointsHomeViewModel
import com.tokopedia.tokopoints.view.util.*
import com.tokopedia.tokopoints.view.util.CommonConstant.Companion.TIMER_RED_BACKGROUND_HEX
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*


class SectionHorizontalCatalogVH(val view: View, val mPresenter: TokoPointsHomeViewModel)
    : RecyclerView.ViewHolder(view) {
    val countDownView = view.findViewById<TimerUnifySingle>(R.id.tp_count_down_view_column)
    val timerMessage = view.findViewById<View>(R.id.timer_msg)
    val layoutManager: LinearLayoutManager by lazy { LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false) }
    fun bind(content: SectionContent) {

        if (content?.sectionTitle == null || content.layoutCatalogAttr == null) {
            view.visibility = View.GONE
            return
        }
        ImageHandler.loadBackgroundImage(view, content.backgroundImgURLMobile)
        if (content.layoutCatalogAttr.countdownInfo != null && content.layoutCatalogAttr.countdownInfo?.isShown!!
                && content.layoutCatalogAttr.countdownInfo?.countdownUnix != null && content.layoutCatalogAttr.countdownInfo?.countdownUnix!! > 0) {
            if (countDownView.timer != null) {
                countDownView.timer!!.cancel()
            }
            countDownView?.show()
            timerMessage?.show()
            val countDownInfo = content.layoutCatalogAttr?.countdownInfo
            val timerValue = countDownInfo?.countdownUnix
            val timerStr = countDownInfo?.countdownStr
            val timerType = countDownInfo?.type
            val timerFlagType = countDownInfo?.backgroundColor
            if (timerFlagType == CommonConstant.HASH + TIMER_RED_BACKGROUND_HEX) {
                countDownView.timerVariant = TimerUnifySingle.VARIANT_MAIN
            } else {
                countDownView.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
            }
            countDownView?.timerTextWidth = TimerUnifySingle.TEXT_WRAP
            if (!countDownInfo?.label.isNullOrEmpty()) {
                (timerMessage as TextView).text = countDownInfo?.label
            } else {
                timerMessage.hide()
            }
            if (content.layoutCatalogAttr?.countdownInfo != null) {
                if (timerStr != null && timerType != null && timerValue != null)
                    setTimer(timerValue, timerStr, timerType)
            }
            countDownView?.onFinish = {
                if (content.layoutCatalogAttr.countdownInfo?.countdownUnix!! > 0) {
                    if (timerStr != null && timerType != null && timerValue != null)
                        setTimer(timerValue, timerStr, timerType)
                } else {
                    countDownView?.visibility = View.GONE
                }
            }
            countDownView?.onTick = {
                content.layoutCatalogAttr.countdownInfo?.countdownUnix = it / 1000
            }
        } else {
            countDownView?.hide()
            timerMessage.hide()
        }

        if ((content.layoutCatalogAttr.countdownInfo == null || content.layoutCatalogAttr.countdownInfo != null && content.layoutCatalogAttr.countdownInfo?.isShown != null
                && !content.layoutCatalogAttr.countdownInfo?.isShown!!) && content.sectionSubTitle.isNullOrEmpty() && !content.cta.isEmpty) {
            CustomConstraintProvider.setCustomConstraint(view, R.id.parent_layout, R.id.text_see_all_column, R.id.text_title_column, ConstraintSet.BASELINE)
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
        rvCarousel?.isDrawingCacheEnabled = true
        rvCarousel?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        rvCarousel.setHasFixedSize(true)
        rvCarousel.layoutManager = layoutManager
        if (rvCarousel.itemDecorationCount == 0) {
            rvCarousel.addItemDecoration(NonCarouselItemDecoration(convertDpToPixel(16, rvCarousel.context)))
        }
        if (!content.layoutCatalogAttr.catalogList.isNullOrEmpty()) {
            rvCarousel.adapter = CatalogListCarouselAdapter(content.layoutCatalogAttr.catalogList!!, rvCarousel)
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

    private fun setTimer(timerValue: Long, timerStr: String, timerType: Int) {
        if (timerType == 1) {
            val timeToExpire = convertSecondsToHrMmSs(timerValue)
            countDownView.targetDate = timeToExpire
        } else {
            countDownView.timerFormat = TimerUnifySingle.FORMAT_DAY
            val cal = Calendar.getInstance()
            var noOfDay = timerStr?.replace("[^0-9]".toRegex(), "")
            noOfDay?.toInt()?.let { cal.add(Calendar.DAY_OF_MONTH, it + 1) }
            countDownView.targetDate = cal
        }
    }
}