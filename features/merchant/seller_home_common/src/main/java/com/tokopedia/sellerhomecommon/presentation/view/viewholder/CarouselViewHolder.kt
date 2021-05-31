package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.CarouselBannerAdapter
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import kotlinx.android.synthetic.main.shc_carousel_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_carousel_widget_shimmering.view.*
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class CarouselViewHolder(
        itemView: View?,
        private val listener: Listener
) : AbstractViewHolder<CarouselWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_carousel_widget
    }

    private var hasSetSnapHelper = false

    override fun bind(element: CarouselWidgetUiModel) {
        itemView.rvCarouselBanner.isNestedScrollingEnabled = false
        observeState(element)
    }

    private fun observeState(element: CarouselWidgetUiModel) {
        val data = element.data
        when {
            null == data -> setOnLoadingState()
            data.error.isNotBlank() -> {
                setOnErrorState()
                listener.setOnErrorWidget(adapterPosition, element, data.error)
            }
            else -> setOnSuccessState(element)
        }
    }

    private fun setOnLoadingState() = with(itemView) {
        tvCarouselBannerTitle.gone()
        rvCarouselBanner.gone()
        indicatorCarouselBanner.gone()
        bannerImagesShimmering.visible()
        commonWidgetErrorState.gone()
    }

    private fun setOnErrorState() = with(itemView) {
        tvCarouselBannerTitle.visible()
        commonWidgetErrorState.visible()
        rvCarouselBanner.gone()
        bannerImagesShimmering.gone()
        indicatorCarouselBanner.gone()
        btnCarouselSeeAll.gone()
        ImageHandler.loadImageWithId(itemView.imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
    }

    private fun setOnSuccessState(element: CarouselWidgetUiModel) {
        val data = element.data?.items

        if (!data.isNullOrEmpty()) {
            with(itemView) {
                tvCarouselBannerTitle.text = element.title
                tvCarouselBannerTitle.visible()
                rvCarouselBanner.visible()
                commonWidgetErrorState.gone()
                bannerImagesShimmering.gone()

                setupBanner(element)
                setupCta(element)
            }
        } else {
            listener.removeWidget(adapterPosition, element)
        }
    }

    private fun setupBanner(element: CarouselWidgetUiModel) = with(itemView) {
        val banners = element.data?.items.orEmpty()

        val indicatorVisibility = if (banners.size <= 1) View.GONE else View.VISIBLE
        indicatorCarouselBanner.visibility = indicatorVisibility
        indicatorCarouselBanner.setIndicator(banners.size)

        val bannerAdapter = CarouselBannerAdapter(element.dataKey, banners, listener)
        val linearLayoutManager = getLayoutManager()

        rvCarouselBanner.layoutManager = linearLayoutManager
        rvCarouselBanner.adapter = bannerAdapter

        rvCarouselBanner.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                indicatorCarouselBanner.setCurrentIndicator(position)
            }
        })

        if (!hasSetSnapHelper) {
            PagerSnapHelper().attachToRecyclerView(rvCarouselBanner)
            hasSetSnapHelper = !hasSetSnapHelper
        }
    }

    private fun getLayoutManager(): LinearLayoutManager {
        return object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    private fun setupCta(element: CarouselWidgetUiModel) = with(itemView) {
        if (element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()) {
            btnCarouselSeeAll.visible()
            btnCarouselSeeAll.text = element.ctaText
            btnCarouselSeeAll.setOnClickListener {
                if (RouteManager.route(context, element.appLink)) {
                    listener.sendCarouselCtaClickEvent(element.dataKey)
                }
            }
        } else {
            btnCarouselSeeAll.gone()
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendCarouselImpressionEvent(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {}

        fun sendCarouselClickTracking(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {}

        fun sendCarouselCtaClickEvent(dataKey: String) {}
    }
}