package com.tokopedia.tokopoints.view.tokopointhome.recommendation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.PercentageScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.promoui.common.dpToPx
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.adapter.CarouselItemDecorationNew
import com.tokopedia.tokopoints.view.model.homeresponse.RecommendationWrapper
import com.tokopedia.tokopoints.view.model.homeresponse.RewardsRecommendation
import com.tokopedia.tokopoints.view.recommwidget.RewardsRecommAdapter
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.tokopoints.view.util.convertDpToPixel

class SectionRecomVH(val view: View , val listener: RewardsRecomListener) : RecyclerView.ViewHolder(view) {

    private val percentageScrollListener by lazy(LazyThreadSafetyMode.NONE) {
        PercentageScrollListener()
    }

    val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            view.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private val rvCarousel: RecyclerView = view.findViewById(R.id.rv_recomm)

    init {
        rvCarousel.addOnScrollListener(percentageScrollListener)
    }

    fun bind(data: RewardsRecommendation) {
        val btnSeeAll = view.findViewById<TextView>(R.id.text_see_all_recomm)
        if (data.appLink.isNotEmpty()) {
            btnSeeAll.visibility = View.VISIBLE
            btnSeeAll.setOnClickListener {
                RouteManager.route(view.context, data.appLink)
            }
        }

        (view.findViewById<View>(R.id.text_title_recomm) as TextView).text =
            view.context.resources.getString(R.string.tp_recom_title)

        (view.findViewById<View>(R.id.text_sub_title_recomm) as TextView).text =
            view.context.resources.getString(R.string.tp_recom_subtitle)
        rvCarousel?.isDrawingCacheEnabled = true
        rvCarousel.setHasFixedSize(true)
        rvCarousel?.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        rvCarousel.layoutManager = layoutManager
        if (rvCarousel.itemDecorationCount == 0) {
            rvCarousel?.addItemDecoration(
                CarouselItemDecorationNew(
                    convertDpToPixel(
                        dpToPx(2).toInt(),
                        rvCarousel.context
                    ), convertDpToPixel(16, rvCarousel.context)
                )
            )
        }

        rvCarousel.adapter =
            RewardsRecommAdapter(data.recommendationWrapper as ArrayList<RecommendationWrapper> ,listener)

    }
}
