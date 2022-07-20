package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
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
import com.tokopedia.sellerhomecommon.databinding.ShcCarouselWidgetBinding
import com.tokopedia.sellerhomecommon.databinding.ShcPartialCarouselWidgetEmptyBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.CarouselBannerAdapter
import com.tokopedia.sellerhomecommon.presentation.model.CarouselItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CarouselWidgetUiModel
import com.tokopedia.sellerhomecommon.utils.toggleWidgetHeight
import com.tokopedia.unifycomponents.NotificationUnify

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class CarouselViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<CarouselWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_carousel_widget
    }

    private val binding by lazy { ShcCarouselWidgetBinding.bind(itemView) }
    private val emptyStateBinding by lazy {
        ShcPartialCarouselWidgetEmptyBinding.bind(binding.root)
    }
    private val shimmeringBinding by lazy { binding.shcCarouselLoadingState }
    private val errorStateBinding by lazy { binding.shcCarouselErrorState }

    private var hasSetSnapHelper = false

    override fun bind(element: CarouselWidgetUiModel) {
        if (!listener.getIsShouldRemoveWidget()) {
            itemView.toggleWidgetHeight(true)
        }
        binding.rvCarouselBanner.isNestedScrollingEnabled = false
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

    private fun setOnLoadingState() = with(binding) {
        tvCarouselBannerTitle.gone()
        rvCarouselBanner.gone()
        indicatorCarouselBanner.gone()
        notifTagCarousel.gone()
        shimmeringBinding.bannerImagesShimmering.visible()
        errorStateBinding.commonWidgetErrorState.gone()
        emptyStateBinding.groupShcCarouselEmpty.gone()
    }

    private fun setOnErrorState() = with(binding) {
        tvCarouselBannerTitle.visible()
        errorStateBinding.commonWidgetErrorState.visible()
        rvCarouselBanner.gone()
        notifTagCarousel.gone()
        shimmeringBinding.bannerImagesShimmering.gone()
        indicatorCarouselBanner.gone()
        btnCarouselSeeAll.gone()
        emptyStateBinding.groupShcCarouselEmpty.gone()
        ImageHandler.loadImageWithId(
            errorStateBinding.imgWidgetOnError,
            com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
        )
    }

    private fun setOnSuccessState(element: CarouselWidgetUiModel) {
        with(binding) {
            tvCarouselBannerTitle.text = element.title
            tvCarouselBannerTitle.visible()
        }
        setTagNotification(element.tag)
        if (element.isEmpty()) {
            if (element.isShowEmpty && element.shouldShowEmptyStateIfEmpty()) {
                setupEmptyState(element)
            } else {
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(adapterPosition, element)
                } else {
                    listener.onRemoveWidget(adapterPosition)
                    itemView.toggleWidgetHeight(false)
                }
            }
        } else {
            setupCarousel(element)
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding) {
            notifTagCarousel.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagCarousel.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun setupBanner(element: CarouselWidgetUiModel) = with(binding) {
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

    private fun setupCta(element: CarouselWidgetUiModel) = with(binding) {
        if (element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()) {
            btnCarouselSeeAll.visible()
            btnCarouselSeeAll.text = element.ctaText
            btnCarouselSeeAll.setOnClickListener {
                if (RouteManager.route(root.context, element.appLink)) {
                    listener.sendCarouselCtaClickEvent(element.dataKey)
                }
            }
        } else {
            btnCarouselSeeAll.gone()
        }
    }

    private fun setupCarousel(element: CarouselWidgetUiModel) {
        with(binding) {
            rvCarouselBanner.visible()
            errorStateBinding.commonWidgetErrorState.gone()
            shimmeringBinding.bannerImagesShimmering.gone()
            emptyStateBinding.groupShcCarouselEmpty.gone()

            setupBanner(element)
            setupCta(element)
        }
    }

    private fun setupEmptyState(element: CarouselWidgetUiModel) {
        with(binding) {
            errorStateBinding.commonWidgetErrorState.gone()
            rvCarouselBanner.gone()
            shimmeringBinding.bannerImagesShimmering.gone()
            indicatorCarouselBanner.gone()
            btnCarouselSeeAll.gone()

            emptyStateBinding.groupShcCarouselEmpty.visible()
            emptyStateBinding.tvShcCarouselEmptyTitle.run {
                text = element.emptyState.title.takeIf { it.isNotBlank() }
                    ?: getString(R.string.shc_empty_state_title)
                visible()
            }
            emptyStateBinding.tvShcCarouselEmptyDesc.run {
                text = element.emptyState.description
                showWithCondition(element.emptyState.description.isNotBlank())
            }
            emptyStateBinding.btnShcCarouselEmpty.run {
                text = element.emptyState.ctaText
                showWithCondition(element.emptyState.ctaText.isNotBlank())
                setOnClickListener {
                    if (RouteManager.route(itemView.context, element.emptyState.appLink)) {
                        listener.sendCarouselEmptyStateCtaClickEvent(element)
                    }
                }
            }
            ImageHandler.loadImageWithoutPlaceholderAndError(
                emptyStateBinding.imgShcCarouselEmpty,
                element.emptyState.imageUrl.takeIf { it.isNotBlank() }
                    ?: SellerHomeUrl.IMG_EMPTY_STATE)
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendCarouselImpressionEvent(
            dataKey: String,
            carouselItems: List<CarouselItemUiModel>,
            position: Int
        ) {
        }

        fun sendCarouselClickTracking(
            dataKey: String,
            carouselItems: List<CarouselItemUiModel>,
            position: Int
        ) {
        }

        fun sendCarouselCtaClickEvent(dataKey: String) {}

        fun sendCarouselEmptyStateCtaClickEvent(element: CarouselWidgetUiModel) {}
    }
}