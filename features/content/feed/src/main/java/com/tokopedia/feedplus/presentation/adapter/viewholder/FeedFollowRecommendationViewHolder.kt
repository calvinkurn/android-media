package com.tokopedia.feedplus.presentation.adapter.viewholder

import android.os.Build
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedFollowRecommendationBinding
import com.tokopedia.feedplus.presentation.adapter.FeedContentAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloadActions
import com.tokopedia.feedplus.presentation.adapter.FeedViewHolderPayloads
import com.tokopedia.feedplus.presentation.adapter.itemdecoration.FeedFollowProfileItemDecoration
import com.tokopedia.feedplus.presentation.adapter.layoutmanager.FeedFollowProfileLayoutManager
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
class FeedFollowRecommendationViewHolder(
    private val binding: ItemFeedFollowRecommendationBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val listener: FeedFollowRecommendationListener
) : AbstractViewHolder<FeedFollowRecommendationModel>(binding.root) {

    private val profileAdapter = FeedFollowProfileAdapter(
        lifecycleOwner = lifecycleOwner,
        profileListener = object : FeedFollowProfileViewHolder.Profile.Listener {
            override fun onScrollProfile(position: Int) {
                binding.rvFollowRecommendation.smoothScrollToPosition(position)
            }
        },
        followRecommendationListener = listener
    )
    private val layoutManager = FeedFollowProfileLayoutManager(itemView.context)
    private val snapHelper = PagerSnapHelper()

    private var mData: FeedFollowRecommendationModel? = null

    init {
        binding.rvFollowRecommendation.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        binding.rvFollowRecommendation.layoutManager = layoutManager
        binding.rvFollowRecommendation.addItemDecoration(FeedFollowProfileItemDecoration(itemView.context))
        snapHelper.attachToRecyclerView(binding.rvFollowRecommendation)
        binding.rvFollowRecommendation.adapter = profileAdapter

        binding.rvFollowRecommendation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (profileAdapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                setupProfileList(selectedPosition = getSelectedPosition(), isViewHolderSelected = true)
            }
        })
    }

    fun bind(item: FeedContentAdapter.Item) {
        val element = item.data as FeedFollowRecommendationModel

        bind(element, selectedPosition = getSelectedPosition(), isViewHolderSelected = item.isSelected)
    }

    fun bind(item: FeedContentAdapter.Item, payloads: MutableList<Any>) {
        val selectedPayload = if (item.isSelected) FeedViewHolderPayloadActions.FEED_POST_SELECTED else FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED
        val feedPayloads = payloads.firstOrNull { it is FeedViewHolderPayloads } as? FeedViewHolderPayloads
        val newPayloads = if (feedPayloads != null && feedPayloads.payloads.contains(
                FeedViewHolderPayloadActions.FEED_POST_SELECTED_CHANGED
            )) {
            payloads.toMutableList().also { it.add(selectedPayload) }
        } else {
            payloads
        }
        bind(item.data as FeedFollowRecommendationModel, newPayloads)
    }

    override fun bind(element: FeedFollowRecommendationModel?, payloads: MutableList<Any>) {
        element?.let {
            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED) ||
                payloads.contains(FeedViewHolderPayloadActions.FEED_FOLLOW_RECOM_PAUSE_VIDEO)) {
                bind(it, selectedPosition = getSelectedPosition(), isViewHolderSelected = false)
            }

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_SELECTED) ||
                payloads.contains(FeedViewHolderPayloadActions.FEED_FOLLOW_RECOM_RESUME_VIDEO)) {
                bind(it, selectedPosition = getSelectedPosition(), isViewHolderSelected = true)
            }

            payloads.forEach { payload ->
                if (payload is FeedViewHolderPayloads) {
                    bind(
                        element,
                        payload.payloads.toMutableList()
                    )
                }
            }
        }
    }

    private fun bind(
        element: FeedFollowRecommendationModel,
        selectedPosition: Int,
        isViewHolderSelected: Boolean
    ) {
        mData = element

        mData?.let { model ->
            setupHeader(model)
            setupLayout(model, selectedPosition, isViewHolderSelected)
        }
    }

    private fun setupHeader(model: FeedFollowRecommendationModel) {
        binding.tvTitle.text = model.title
        binding.tvDesc.text = model.description
    }

    private fun setupLayout(
        model: FeedFollowRecommendationModel,
        selectedPosition: Int,
        isViewHolderSelected: Boolean
    ) {
        when (model.status) {
            FeedFollowRecommendationModel.Status.Loading -> {
                if (model.data.isEmpty()) {
                    freezeScrolling(true)
                    profileAdapter.setItemsAndAnimateChanges(List(5) { FeedFollowProfileAdapter.Model.Loading })
                }

                binding.clMain.showWithCondition(true)
                binding.feedNoContent.root.showWithCondition(false)
            }
            FeedFollowRecommendationModel.Status.Success -> {
                freezeScrolling(false)
                setupProfileList(model, selectedPosition, isViewHolderSelected)
            }
            FeedFollowRecommendationModel.Status.Error -> {
                binding.feedNoContent.iconFeedNoContent.setImage(IconUnify.RELOAD)
                binding.feedNoContent.tyFeedNoContentTitle.text =
                    binding.root.context.getString(R.string.feed_load_follow_recommendation_error)
                binding.feedNoContent.tyFeedNoContentSubtitle.hide()
                binding.feedNoContent.btnShowOtherContent.text =
                    binding.root.context.getString(R.string.feed_label_error_fetch_button)
                binding.feedNoContent.btnShowOtherContent.setOnClickListener {
                    listener.reloadProfileRecommendation()
                }

                binding.clMain.showWithCondition(false)
                binding.feedNoContent.root.showWithCondition(true)
            }
            FeedFollowRecommendationModel.Status.NoInternet -> {
                binding.feedNoContent.iconFeedNoContent.setImage(IconUnify.SIGNAL_INACTIVE)
                binding.feedNoContent.tyFeedNoContentTitle.text =
                    binding.root.context.getString(R.string.feed_label_error_fetch_title)
                binding.feedNoContent.tyFeedNoContentSubtitle.show()
                binding.feedNoContent.tyFeedNoContentSubtitle.text =
                    binding.root.context.getString(R.string.feed_label_error_fetch_subtitle)
                binding.feedNoContent.btnShowOtherContent.text =
                    binding.root.context.getString(R.string.feed_label_error_fetch_button)
                binding.feedNoContent.btnShowOtherContent.setOnClickListener {
                    listener.reloadProfileRecommendation()
                }

                binding.clMain.showWithCondition(false)
                binding.feedNoContent.root.showWithCondition(true)
            }
            else -> {}
        }
    }

    private fun setupProfileList(
        model: FeedFollowRecommendationModel? = mData,
        selectedPosition: Int,
        isViewHolderSelected: Boolean,
    ) {
        if (model == null) return

        var isNeedForceScroll = false
        val finalSelectedPosition = if (selectedPosition >= model.data.size) {
            isNeedForceScroll = true
            (model.data.size - 1).coerceAtLeast(0)
        } else {
            selectedPosition
        }

        val mappedList = model.data.mapIndexed { idx, item ->
            FeedFollowProfileAdapter.Model.Profile(
                data = item,
                isSelected = if (isViewHolderSelected) idx == finalSelectedPosition else false,
            )
        }

        val finalList = if (model.hasNext)
            mappedList + listOf(FeedFollowProfileAdapter.Model.Loading)
        else
            mappedList

        val isDataAvailable = finalList.isNotEmpty()

        profileAdapter.setItemsAndAnimateChanges(finalList)

        if (isNeedForceScroll)
            binding.rvFollowRecommendation.smoothScrollToPosition(finalSelectedPosition)

        binding.feedNoContent.btnShowOtherContent.setOnClickListener {
            listener.onClickViewOtherContent()
        }

        binding.clMain.showWithCondition(isDataAvailable)
        binding.feedNoContent.root.showWithCondition(!isDataAvailable)
    }

    private fun getSelectedPosition(): Int {
        val snappedView = snapHelper.findSnapView(layoutManager) ?: return 0
        return binding.rvFollowRecommendation.getChildAdapterPosition(snappedView)
    }

    private fun freezeScrolling(isFreeze: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.rvFollowRecommendation.suppressLayout(isFreeze)
        } else {
            binding.rvFollowRecommendation.isLayoutFrozen = isFreeze
        }
    }

    override fun bind(element: FeedFollowRecommendationModel?) {
        /** mandatory to be overriden but not used */
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_follow_recommendation
    }
}
