package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostListPagerAdapter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import kotlinx.android.synthetic.main.shc_partial_common_widget_state_error.view.*
import kotlinx.android.synthetic.main.shc_partial_post_list_widget.view.*
import kotlinx.android.synthetic.main.shc_partial_post_list_widget_error.view.*
import kotlinx.android.synthetic.main.shc_partial_shimmering_post_list_widget.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostListViewHolder(
        view: View?,
        private val listener: Listener
) : AbstractViewHolder<PostListWidgetUiModel>(view) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_post_list_card_widget
        private const val IMG_EMPTY_STATE = "https://ecs7.tokopedia.net/android/others/shc_post_list_info_empty_state.png"
    }

    private var dataKey: String = ""

    private val pagerAdapter by lazy {
        PostListPagerAdapter {
            if (RouteManager.route(itemView.context, it.appLink)) {
                listener.sendPosListItemClickEvent(dataKey, it.title)
            }
        }
    }

    override fun bind(element: PostListWidgetUiModel) {
        itemView.rvPostList.isNestedScrollingEnabled = false
        itemView.visible()
        observeState(element)
    }

    private fun observeState(postListWidgetUiModel: PostListWidgetUiModel) {
        val data = postListWidgetUiModel.data
        when {
            data == null -> onLoading()
            data.error.isNotEmpty() -> {
                onError(postListWidgetUiModel.title)
                listener.setOnErrorWidget(adapterPosition, postListWidgetUiModel, data.error)
            }
            else -> onSuccessLoadData(postListWidgetUiModel)
        }
    }

    private fun onLoading() {
        showLoadingState()
    }

    private fun onError(cardTitle: String) {
        hideListLayout()
        hideShimmeringLayout()
        with(itemView) {
            tvPostListTitleOnError.text = cardTitle
            imgWidgetOnError.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
            showErrorLayout()
        }
    }

    private fun onSuccessLoadData(postListWidgetUiModel: PostListWidgetUiModel) {
        val isEmpty = postListWidgetUiModel.data?.isEmptyPost().orFalse()
        when {
            isEmpty && !postListWidgetUiModel.isShowEmpty -> {
                itemView.gone()
                listener.removeWidget(adapterPosition, postListWidgetUiModel)
            }
            else -> showSuccessState(postListWidgetUiModel)
        }
    }

    private fun showEmptyState(element: PostListWidgetUiModel) {
        with(itemView) {
            rvPostList.gone()
            tvPostListSeeDetails.gone()
            icPostListSeeDetails.gone()
            imgShcPostEmpty.visible()
            tvShcPostEmptyTitle.run {
                text = element.emptyState.title.takeIf { it.isNotBlank() }
                        ?: getString(R.string.shc_empty_state_title_post_list)
                visible()
            }
            tvShcPostEmptyDescription.run {
                text = element.emptyState.description
                showWithCondition(element.emptyState.description.isNotBlank())
            }
            btnShcPostEmpty.run {
                text = element.emptyState.ctaText
                showWithCondition(element.emptyState.ctaText.isNotBlank())
                setOnClickListener { goToSellerEducationCenter(element) }
            }
            ImageHandler.loadImageWithoutPlaceholderAndError(imgShcPostEmpty, element.emptyState.imageUrl.takeIf { it.isNotBlank() }
                    ?: IMG_EMPTY_STATE)
        }
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideListLayout()
        showShimmeringLayout()
    }

    private fun showSuccessState(element: PostListWidgetUiModel) {
        with(itemView) {
            tvShcPostEmptyTitle.gone()
            tvShcPostEmptyDescription.gone()
            btnShcPostEmpty.gone()
            imgShcPostEmpty.gone()
            rvPostList.visible()
        }

        element.data?.run {
            hideErrorLayout()
            hideShimmeringLayout()
            setupTooltip(element.tooltip)
            itemView.tvPostListTitle.text = element.title
            setupPostFilter(element)
            showCtaButtonIfNeeded(element)
            showListLayout()
            addImpressionTracker(element)

            if (isEmptyPost()) {
                showEmptyState(element)
            } else {
                setupPostPager(postPagers)
            }
        }
    }

    private fun setupPostFilter(element: PostListWidgetUiModel) {
        with(itemView) {
            val isFilterAvailable = element.postFilter.isNotEmpty()
            if (isFilterAvailable) {
                val selectedFilter = element.postFilter.find { it.isSelected }
                filterShcPostList.visible()
                filterShcPostList.text = selectedFilter?.name.orEmpty()
                filterShcPostList.setUnifyDrawableEnd(IconUnify.CHEVRON_DOWN)
                filterShcPostList.setOnClickListener {
                    listener.showPostFilter(element, adapterPosition)
                    listener.sendPostListFilterClick(element)
                }
            } else {
                filterShcPostList.gone()
            }
        }
    }

    private fun addImpressionTracker(element: PostListWidgetUiModel) {
        this@PostListViewHolder.dataKey = element.dataKey
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.sendPostListImpressionEvent(element)
        }
    }

    private fun showShimmeringLayout() {
        itemView.sahPostListOnLoadingLayout.visible()
    }

    private fun hideShimmeringLayout() {
        itemView.sahPostListOnLoadingLayout.gone()
    }

    private fun showListLayout() {
        itemView.sahPostListOnSuccessLayout.visible()
    }

    private fun hideListLayout() {
        itemView.sahPostListOnSuccessLayout.gone()
    }

    private fun hideErrorLayout() {
        itemView.sahPostListOnErrorLayout.gone()
    }

    private fun showErrorLayout() {
        itemView.sahPostListOnErrorLayout.visible()
    }

    private fun setupTooltip(tooltip: TooltipUiModel?) = with(itemView) {
        tooltip?.run {
            val shouldShowTooltip = shouldShow && (content.isNotBlank() || list.isNotEmpty())
            if (shouldShowTooltip) {
                tvPostListTitle.setUnifyDrawableEnd(IconUnify.INFORMATION)
                tvPostListTitle.setOnClickListener { showBottomSheet(tooltip) }
            } else {
                tvPostListTitle.clearUnifyDrawableEnd()
            }
        }
    }

    private fun showCtaButtonIfNeeded(element: PostListWidgetUiModel) {
        val isCtaVisible = element.ctaText.isNotEmpty() && element.appLink.isNotEmpty()
        if (isCtaVisible) {
            setupCtaButton(element)
        }
        toggleCtaButtonVisibility(isCtaVisible)
    }

    private fun setupCtaButton(element: PostListWidgetUiModel) {
        val (ctaText, appLink) = if (element.data?.cta?.text?.isNotBlank() == true && element.data?.cta?.appLink?.isNotBlank() == true) {
            Pair(element.data?.cta?.text.orEmpty(), element.data?.cta?.appLink.orEmpty())
        } else {
            Pair(element.ctaText, element.appLink)
        }
        itemView.tvPostListSeeDetails.text = ctaText
        itemView.tvPostListSeeDetails.setOnClickListener { goToDetails(element, appLink) }
    }

    private fun toggleCtaButtonVisibility(isShow: Boolean) = with(itemView) {
        when {
            isShow -> {
                tvPostListSeeDetails.visible()
                icPostListSeeDetails.visible()
            }
            else -> {
                tvPostListSeeDetails.gone()
                icPostListSeeDetails.gone()
            }
        }
    }

    private fun showBottomSheet(tooltip: TooltipUiModel) {
        listener.onTooltipClicked(tooltip)
    }

    private fun goToDetails(element: PostListWidgetUiModel, appLink: String) {
        if (RouteManager.route(itemView.context, appLink)) {
            listener.sendPostListCtaClickEvent(element)
        }
    }

    private fun goToSellerEducationCenter(element: PostListWidgetUiModel) {
        if (RouteManager.route(itemView.context, element.emptyState.appLink)) {
            listener.sendPostListEmptyStateCtaClickEvent(element)
        }
    }

    private fun setupPostPager(pagers: List<PostListPagerUiModel>) {
        with(itemView) {
            pageControlShcPostPager.setIndicator(pagers.size)
            pageControlShcPostPager.isVisible = pagers.size > 1

            rvPostList.run {
                val mLayoutManager = object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
                    override fun canScrollVertically(): Boolean = false
                }
                layoutManager = mLayoutManager
                adapter = pagerAdapter

                try {
                    PagerSnapHelper().attachToRecyclerView(this)
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }

                addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val position = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                        if (position != RecyclerView.NO_POSITION) {
                            itemView.pageControlShcPostPager.setCurrentIndicator(position)
                        }
                    }
                })
            }
        }

        if (pagerAdapter.itemCount <= 0) {
            pagerAdapter.setItems(pagers)
            pagerAdapter.notifyDataSetChanged()
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendPosListItemClickEvent(dataKey: String, title: String) {}

        fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {}

        fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {}

        fun sendPostListFilterClick(element: PostListWidgetUiModel) {}

        fun sendPostListEmptyStateCtaClickEvent(element: PostListWidgetUiModel) {}

        fun showPostFilter(element: PostListWidgetUiModel, adapterPosition: Int) {}
    }
}
