package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.presentation.adapter.CarouselBannerAdapter
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
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

    private val emptyState: Group? = itemView?.findViewById(R.id.group_shc_carousel_empty)
    private val emptyStateImage: ImageUnify? = itemView?.findViewById(R.id.iv_shc_carousel_empty)
    private val emptyStateTitle: Typography? = itemView?.findViewById(R.id.tv_shc_carousel_empty_title)
    private val emptyStateDesc: Typography? = itemView?.findViewById(R.id.tv_shc_carousel_empty_desc)
    private val emptyStateButton: UnifyButton? = itemView?.findViewById(R.id.btn_shc_carousel_empty)

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
        emptyState?.gone()
    }

    private fun setOnErrorState() = with(itemView) {
        tvCarouselBannerTitle.visible()
        commonWidgetErrorState.visible()
        rvCarouselBanner.gone()
        bannerImagesShimmering.gone()
        indicatorCarouselBanner.gone()
        btnCarouselSeeAll.gone()
        emptyState?.gone()
        ImageHandler.loadImageWithId(itemView.imgWidgetOnError, com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
    }

    private fun setOnSuccessState(element: CarouselWidgetUiModel) {
        with(itemView) {
            tvCarouselBannerTitle.text = element.title
            tvCarouselBannerTitle.visible()
        }
        if (element.isEmpty()) {
            setupEmptyState(element)
        } else {
            setupCarousel(element)
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

    private fun setupCarousel(element: CarouselWidgetUiModel) {
        with(itemView) {
            rvCarouselBanner.visible()
            commonWidgetErrorState.gone()
            bannerImagesShimmering.gone()
            emptyState?.gone()

            setupBanner(element)
            setupCta(element)
        }
    }

    private fun setupEmptyState(element: CarouselWidgetUiModel) {
        with(itemView) {
            commonWidgetErrorState.gone()
            rvCarouselBanner.gone()
            bannerImagesShimmering.gone()
            indicatorCarouselBanner.gone()
            btnCarouselSeeAll.gone()

            emptyState?.visible()
            emptyStateTitle?.run {
                text = element.emptyState.title.takeIf { it.isNotBlank() } ?: getString(R.string.shc_empty_state_title)
                visible()
            }
            emptyStateDesc?.run {
                text = element.emptyState.description
                showWithCondition(element.emptyState.description.isNotBlank())
            }
            emptyStateButton?.run {
                text = element.emptyState.ctaText
                showWithCondition(element.emptyState.ctaText.isNotBlank())
                setOnClickListener {
                    if (RouteManager.route(itemView.context, element.emptyState.appLink)) {
                        listener.sendCarouselEmptyStateCtaClickEvent(element)
                    }
                }
            }
            ImageHandler.loadImageWithoutPlaceholderAndError(emptyStateImage, element.emptyState.imageUrl.takeIf { it.isNotBlank() } ?: SellerHomeUrl.IMG_EMPTY_STATE)
        }
    }

    private fun CarouselWidgetUiModel.isEmpty(): Boolean =
            data?.items.isNullOrEmpty() && isShowEmpty && emptyState.title.isNotBlank()
                    && emptyState.description.isNotBlank() && emptyState.ctaText.isNotBlank()
                    && emptyState.appLink.isNotBlank()

    interface Listener : BaseViewHolderListener {

        fun sendCarouselImpressionEvent(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {}

        fun sendCarouselClickTracking(dataKey: String, carouselItems: List<CarouselItemUiModel>, position: Int) {}

        fun sendCarouselCtaClickEvent(dataKey: String) {}

        fun sendCarouselEmptyStateCtaClickEvent(element: CarouselWidgetUiModel) {}
    }
}