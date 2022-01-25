package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.databinding.ShcPostListCardWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostListPagerAdapter
import com.tokopedia.sellerhomecommon.utils.Utils
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.toggleWidgetHeight
import com.tokopedia.unifycomponents.NotificationUnify
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
    }

    private val binding by lazy { ShcPostListCardWidgetBinding.bind(itemView) }
    private val errorStateBinding by lazy {
        binding.shcPostListErrorView
    }
    private val commonErrorStateBinding by lazy {
        errorStateBinding.shcPostListCommonErrorView
    }
    private val loadingStateBinding by lazy {
        binding.shcPostListLoadingView
    }

    private var pagerAdapter: PostListPagerAdapter? = null

    override fun bind(element: PostListWidgetUiModel) {
        if (!listener.getIsShouldRemoveWidget()) {
            itemView.toggleWidgetHeight(true)
        }
        binding.shcPostListSuccessView.rvPostList.isNestedScrollingEnabled = false
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
        with(errorStateBinding) {
            tvPostListTitleOnError.text = cardTitle
            commonErrorStateBinding.imgWidgetOnError.loadImage(
                com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
            )
            showErrorLayout()
        }
    }

    private fun onSuccessLoadData(postListWidgetUiModel: PostListWidgetUiModel) {
        val isEmpty = postListWidgetUiModel.data?.isWidgetEmpty().orFalse()
        when {
            isEmpty && !postListWidgetUiModel.isShowEmpty -> {
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(adapterPosition, postListWidgetUiModel)
                } else {
                    listener.onRemoveWidget(adapterPosition)
                    itemView.toggleWidgetHeight(false)
                }
                setupLastUpdated(postListWidgetUiModel)
            }
            else -> showSuccessState(postListWidgetUiModel)
        }
    }

    private fun setupLastUpdated(element: PostListWidgetUiModel) {
        with(binding.shcPostListSuccessView) {
            element.data?.lastUpdated?.let { lastUpdated ->
                tvShcPostLastUpdated.isVisible = true
                tvShcPostLastUpdated.text = Utils.LastUpdated
                    .getCopy(root.context, lastUpdated)
            }
            icShcRefreshPost.isVisible = element.isFromCache
            icShcRefreshPost.setOnClickListener {
                listener.reloadPostListWidget(element)
            }
        }
    }

    private fun showEmptyState(element: PostListWidgetUiModel) {
        with(binding.shcPostListSuccessView) {
            rvPostList.gone()
            tvPostListSeeDetails.gone()
            imgShcPostEmpty.visible()
            tvShcPostEmptyTitle.run {
                text = element.emptyState.title.takeIf { it.isNotBlank() }
                    ?: getString(R.string.shc_empty_state_title)
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

            val imageUrl = element.emptyState.imageUrl
                .takeIf { it.isNotBlank() } ?: SellerHomeUrl.IMG_EMPTY_STATE
            imgShcPostEmpty.loadImage(imageUrl)
        }
    }

    private fun showLoadingState() {
        hideErrorLayout()
        hideListLayout()
        showShimmeringLayout()
    }

    private fun showSuccessState(element: PostListWidgetUiModel) {
        with(binding.shcPostListSuccessView) {
            tvShcPostEmptyTitle.gone()
            tvShcPostEmptyDescription.gone()
            btnShcPostEmpty.gone()
            imgShcPostEmpty.gone()
            rvPostList.visible()
        }

        element.data?.run {
            initPagerAdapter(element)
            hideErrorLayout()
            hideShimmeringLayout()
            setupTooltip(element.tooltip)
            binding.shcPostListSuccessView.tvPostListTitle.text = element.title
            setTagNotification(element.tag)
            setupPostFilter(element)
            showCtaButtonIfNeeded(element)
            showListLayout()
            addImpressionTracker(element)
            setupLastUpdated(element)

            if (isWidgetEmpty()) {
                showEmptyState(element)
            } else {
                setupPostPager(postPagers)
            }
        }
    }

    private fun initPagerAdapter(element: PostListWidgetUiModel) {
        pagerAdapter = PostListPagerAdapter {
            if (RouteManager.route(itemView.context, it.appLink)) {
                listener.sendPosListItemClickEvent(element, it)
            }
        }
    }

    private fun setTagNotification(tag: String) {
        val isTagVisible = tag.isNotBlank()
        with(binding.shcPostListSuccessView) {
            notifTagPostList.showWithCondition(isTagVisible)
            if (isTagVisible) {
                notifTagPostList.setNotification(
                    tag,
                    NotificationUnify.TEXT_TYPE,
                    NotificationUnify.COLOR_TEXT_TYPE
                )
            }
        }
    }

    private fun setupPostFilter(element: PostListWidgetUiModel) {
        with(binding.shcPostListSuccessView) {
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
        itemView.addOnImpressionListener(element.impressHolder) {
            listener.sendPostListImpressionEvent(element)
        }
    }

    private fun showShimmeringLayout() {
        loadingStateBinding.sahPostListOnLoadingLayout.visible()
    }

    private fun hideShimmeringLayout() {
        loadingStateBinding.sahPostListOnLoadingLayout.gone()
    }

    private fun showListLayout() {
        binding.shcPostListSuccessView.sahPostListOnSuccessLayout.visible()
    }

    private fun hideListLayout() {
        binding.shcPostListSuccessView.sahPostListOnSuccessLayout.gone()
    }

    private fun hideErrorLayout() {
        errorStateBinding.sahPostListOnErrorLayout.gone()
    }

    private fun showErrorLayout() {
        errorStateBinding.sahPostListOnErrorLayout.visible()
    }

    private fun setupTooltip(tooltip: TooltipUiModel?) = with(binding.shcPostListSuccessView) {
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
        with(binding.shcPostListSuccessView) {
            tvPostListSeeDetails.text = ctaText
            tvPostListSeeDetails.setOnClickListener {
                goToDetails(
                    element,
                    appLink
                )
            }

            val iconColor = root.context.getResColor(
                com.tokopedia.unifyprinciples.R.color.Unify_G400
            )
            val iconWidth = root.context.resources.getDimension(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
            )
            val iconHeight = root.context.resources.getDimension(
                com.tokopedia.unifyprinciples.R.dimen.layout_lvl3
            )
            tvPostListSeeDetails.setUnifyDrawableEnd(
                IconUnify.CHEVRON_RIGHT,
                iconColor,
                iconWidth,
                iconHeight
            )
        }
    }

    private fun toggleCtaButtonVisibility(isShow: Boolean) {
        binding.shcPostListSuccessView.tvPostListSeeDetails.isVisible = isShow
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
        with(binding.shcPostListSuccessView) {
            pageControlShcPostPager.setIndicator(pagers.size)
            pageControlShcPostPager.isVisible = pagers.size > 1

            rvPostList.run {
                val mLayoutManager =
                    object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
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
                            binding.shcPostListSuccessView.pageControlShcPostPager.setCurrentIndicator(
                                position
                            )
                        }
                    }
                })
            }
        }

        if (pagers != pagerAdapter?.pagers) {
            pagerAdapter?.pagers = pagers
        }
    }

    interface Listener : BaseViewHolderListener {

        fun sendPosListItemClickEvent(element: PostListWidgetUiModel, post: PostItemUiModel) {}

        fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {}

        fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {}

        fun sendPostListFilterClick(element: PostListWidgetUiModel) {}

        fun sendPostListEmptyStateCtaClickEvent(element: PostListWidgetUiModel) {}

        fun showPostFilter(element: PostListWidgetUiModel, adapterPosition: Int) {}

        fun reloadPostListWidget(element: PostListWidgetUiModel) {}
    }
}
