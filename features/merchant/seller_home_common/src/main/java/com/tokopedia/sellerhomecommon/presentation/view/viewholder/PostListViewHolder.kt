package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.DismissibleState
import com.tokopedia.sellerhomecommon.common.const.SellerHomeUrl
import com.tokopedia.sellerhomecommon.databinding.ShcPostListCardWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListPagerUiModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostListPagerAdapter
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
        private const val DIMEN_22DP = 22
    }

    private val binding by lazy { ShcPostListCardWidgetBinding.bind(itemView) }
    private val errorStateBinding by lazy {
        binding.shcPostListErrorView
    }
    private val loadingStateBinding by lazy {
        binding.shcPostListLoadingView
    }

    private var pagerAdapter: PostListPagerAdapter? = null
    private val mLayoutManager = object : LinearLayoutManager(itemView.context, HORIZONTAL, false) {
        override fun canScrollVertically(): Boolean = false
    }

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
            data == null || postListWidgetUiModel.showLoadingState -> showLoadingState()
            data.error.isNotEmpty() -> {
                onError(postListWidgetUiModel)
                listener.setOnErrorWidget(absoluteAdapterPosition, postListWidgetUiModel, data.error)
            }
            else -> onSuccessLoadData(postListWidgetUiModel)
        }
    }

    private fun onError(element: PostListWidgetUiModel) {
        hideListLayout()
        hideShimmeringLayout()
        with(errorStateBinding) {
            tvPostListTitleOnError.text = element.title
            shcPostListCommonErrorView.setOnReloadClicked {
                listener.onReloadWidget(element)
            }
            showErrorLayout()
        }
    }

    private fun onSuccessLoadData(postListWidgetUiModel: PostListWidgetUiModel) {
        val isEmpty = postListWidgetUiModel.data?.isWidgetEmpty().orFalse()
        when {
            isEmpty && !postListWidgetUiModel.isShowEmpty -> {
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(absoluteAdapterPosition, postListWidgetUiModel)
                } else {
                    listener.onRemoveWidget(absoluteAdapterPosition)
                    itemView.toggleWidgetHeight(false)
                }
                setupLastUpdated(postListWidgetUiModel)
            }
            else -> showSuccessState(postListWidgetUiModel)
        }

        with(binding.shcPostListSuccessView) {
            horLineShcPostListBtm.isVisible = luvShcPost.isVisible || tvPostListSeeDetails.isVisible
        }
    }

    private fun setupLastUpdated(element: PostListWidgetUiModel) {
        with(binding.shcPostListSuccessView.luvShcPost) {
            element.data?.lastUpdated?.let { lastUpdated ->
                isVisible = lastUpdated.isEnabled && !element.isCheckingMode
                setLastUpdated(lastUpdated.lastUpdatedInMillis)
                setRefreshButtonVisibility(lastUpdated.needToUpdated)
                setRefreshButtonClickListener {
                    refreshWidget(element)
                }
            }
        }
    }

    private fun refreshWidget(element: PostListWidgetUiModel) {
        listener.onReloadWidget(element)
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
        setupCheckingMode(element)

        val data = element.data
        if (data?.isWidgetEmpty().orTrue()) {
            showEmptyState(element)
        } else {
            val postList = if (element.shouldShowDismissalTimer) {
                data?.postPagers?.flatMap { it.postList }?.filter { !it.isChecked }
            } else {
                data?.postPagers?.flatMap { it.postList }
            }
            val pagers = getPostPagers(postList.orEmpty(), element.maxDisplay)
            setupPostPager(pagers)
        }
    }

    private fun getPostPagers(
        postList: List<PostItemUiModel>, maxDisplay: Int
    ): List<PostListPagerUiModel> {
        return postList.chunked(maxDisplay).map {
            val items = it.mapIndexed { index, postItemUiModel ->
                if (postItemUiModel is PostItemUiModel.PostTextEmphasizedUiModel) {
                    val isLastItem = index != it.size.minus(Int.ONE)
                    postItemUiModel.shouldShowUnderLine = isLastItem
                }
                return@mapIndexed postItemUiModel
            }
            return@map PostListPagerUiModel(items)
        }
    }

    private fun setupCheckingMode(element: PostListWidgetUiModel) {
        binding.shcPostListSuccessView.run {
            setMoreOptionVisibility(element)

            moreShcPostWidget.setOnMoreClicked {
                listener.showPostWidgetMoreOption(element)
            }
            moreShcPostWidget.setOnCancelClicked {
                setOnCancelClicked(element)
            }
            moreShcPostWidget.showCheckingMode(element.isCheckingMode)

            groupShcPostRemoveItem.isVisible = element.isCheckingMode

            if (element.isCheckingMode) {
                tvPostListSeeDetails.gone()
                luvShcPost.gone()
                setOnCheckedListener(element)
                filterShcPostList.gone()
            } else {
                setupLastUpdated(element)
                setupCtaButton(element)
                setupPostFilter(element)
            }
            btnShcPostRemoveItem.setOnClickListener {
                listener.setOnPostWidgetRemoveItemClicked(element)
            }
        }
    }

    private fun setMoreOptionVisibility(element: PostListWidgetUiModel): Boolean {
        val shouldMoreOption = !element.shouldShowDismissalTimer
            && element.isDismissible && (element.dismissibleState == DismissibleState.ALWAYS)

        binding.shcPostListSuccessView.run {
            if (shouldMoreOption) {
                moreShcPostWidget.visible()
            } else {
                moreShcPostWidget.gone()
            }
        }

        return shouldMoreOption
    }

    private fun setOnCancelClicked(element: PostListWidgetUiModel) {
        resetCheckList(element)
        element.isCheckingMode = !element.isCheckingMode
        setupCheckingMode(element)
        val postList = element.data?.postPagers?.flatMap { it.postList }.orEmpty()
        val pagers = getPostPagers(postList, element.maxDisplay)
        setPagers(pagers)
        pagerAdapter?.setCheckingMode(element.isCheckingMode)
        notifyPostAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun notifyPostAdapter() {
        pagerAdapter?.notifyDataSetChanged()
    }

    private fun resetCheckList(element: PostListWidgetUiModel) {
        element.data?.postPagers?.flatMap { it.postList }?.forEach {
            it.isChecked = false
        }
    }

    private fun initPagerAdapter(element: PostListWidgetUiModel) {
        pagerAdapter = PostListPagerAdapter(object : PostListPagerAdapter.Listener {
            override fun onItemClicked(model: PostItemUiModel) {
                if (RouteManager.route(itemView.context, model.appLink)) {
                    listener.sendPosListItemClickEvent(element, model)
                }
            }

            override fun onCheckedListener(isChecked: Boolean) {
                setOnCheckedListener(element)
            }

            override fun onTimerFinished() {
                updatePostPager(element)
                element.shouldShowDismissalTimer = false
                setMoreOptionVisibility(element)
            }

            override fun onCancelDismissalClicked() {
                listener.setOnWidgetCancelDismissal(element)
                removeTimerItem(element)
                element.shouldShowDismissalTimer = false
                setMoreOptionVisibility(element)
            }
        })
        pagerAdapter?.setCheckingMode(element.isCheckingMode)
    }

    private fun updatePostPager(element: PostListWidgetUiModel) {
        val postList = getPostsWithoutTimerItem(element).filter { !it.isChecked }
        val pagers = getPostPagers(postList, element.maxDisplay)
        setPagers(pagers)
        notifyPostAdapter()

        setDataPagers(element, pagers)
    }

    private fun removeTimerItem(element: PostListWidgetUiModel) {
        val postList = getPostsWithoutTimerItem(element).map {
            it.isChecked = false
            return@map it
        }

        val pagers = getPostPagers(postList, element.maxDisplay)
        setPagers(pagers)
        notifyPostAdapter()

        setDataPagers(element, pagers)
    }

    private fun setDataPagers(element: PostListWidgetUiModel, pagers: List<PostListPagerUiModel>) {
        element.data = element.data?.copy(
            postPagers = pagers
        )
        if (element.isEmpty()) {
            if (element.isShowEmpty) {
                showEmptyState(element)
            } else {
                if (listener.getIsShouldRemoveWidget()) {
                    listener.removeWidget(absoluteAdapterPosition, element)
                } else {
                    listener.onRemoveWidget(absoluteAdapterPosition)
                    itemView.toggleWidgetHeight(false)
                }
            }
        }
    }

    private fun getPostsWithoutTimerItem(element: PostListWidgetUiModel): List<PostItemUiModel> {
        val postList = element.data?.postPagers?.flatMap { it.postList }
            .orEmpty().toMutableList()
        postList.removeFirst { it is PostItemUiModel.PostTimerDismissalUiModel }
        return postList
    }

    private fun setOnCheckedListener(element: PostListWidgetUiModel) {
        val isItemChecked = element.data?.postPagers?.any {
            it.postList.any { item -> item.isChecked }
        }.orFalse()
        binding.shcPostListSuccessView.btnShcPostRemoveItem.isEnabled = isItemChecked
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
                filterShcPostList.setUnifyDrawableEnd(
                    iconId = IconUnify.CHEVRON_DOWN,
                    width = root.context.dpToPx(DIMEN_22DP)
                )
                filterShcPostList.setOnClickListener {
                    listener.showPostFilter(element)
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
        binding.shcPostListSuccessView.root.visible()
    }

    private fun hideListLayout() {
        binding.shcPostListSuccessView.root.gone()
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
                com.tokopedia.unifyprinciples.R.color.Unify_GN500
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
            rvPostList.run {
                layoutManager = mLayoutManager
                adapter = pagerAdapter

                try {
                    PagerSnapHelper().attachToRecyclerView(this)
                } catch (e: IllegalStateException) {
                    Timber.e(e)
                }

                clearOnScrollListeners()
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

        setPagers(pagers)
    }

    private fun updatePageIndicator(pagers: List<PostListPagerUiModel>) {
        with(binding.shcPostListSuccessView) {
            pageControlShcPostPager.setIndicator(pagers.size)
            pageControlShcPostPager.isVisible = pagers.size > Int.ONE
        }
    }

    private fun setPagers(pagers: List<PostListPagerUiModel>) {
        pagerAdapter?.setPagers(pagers)
        updatePageIndicator(pagers)
    }

    interface Listener : BaseViewHolderListener, WidgetDismissalListener {

        fun sendPosListItemClickEvent(element: PostListWidgetUiModel, post: PostItemUiModel) {}

        fun sendPostListCtaClickEvent(element: PostListWidgetUiModel) {}

        fun sendPostListImpressionEvent(element: PostListWidgetUiModel) {}

        fun sendPostListFilterClick(element: PostListWidgetUiModel) {}

        fun sendPostListEmptyStateCtaClickEvent(element: PostListWidgetUiModel) {}

        fun showPostFilter(element: PostListWidgetUiModel) {}

        fun showPostWidgetMoreOption(element: PostListWidgetUiModel) {}

        fun setOnPostWidgetRemoveItemClicked(element: PostListWidgetUiModel) {}
    }
}
