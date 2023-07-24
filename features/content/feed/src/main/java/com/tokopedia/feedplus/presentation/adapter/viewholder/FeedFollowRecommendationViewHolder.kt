package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
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
import com.tokopedia.feedplus.presentation.adapter.util.FeedFollowProfilePayloadHelper
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.kotlin.extensions.view.showWithCondition

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
        binding.rvFollowRecommendation.layoutManager = layoutManager
        binding.rvFollowRecommendation.addItemDecoration(FeedFollowProfileItemDecoration(itemView.context))
        snapHelper.attachToRecyclerView(binding.rvFollowRecommendation)
        binding.rvFollowRecommendation.adapter = profileAdapter
        binding.rvFollowRecommendation.itemAnimator = null

        binding.rvFollowRecommendation.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (profileAdapter.itemCount == 0) return
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return

                setupProfileList(selectedPosition = getSelectedPosition())
            }
        })
    }

    override fun bind(element: FeedFollowRecommendationModel?) {
        mData = element

        mData?.let { model ->
            setupHeader(model)
            setupProfileList(model, getSelectedPosition())
            setupEmptyLayout()
        }
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
            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_SELECTED)) {
                forcePlayVideo()
            }

            if (payloads.contains(FeedViewHolderPayloadActions.FEED_POST_NOT_SELECTED)) {
                forceStopVideo()
            }
        }
    }

    private fun setupHeader(model: FeedFollowRecommendationModel) {
        binding.tvTitle.text = model.title
        binding.tvDesc.text = model.description
    }

    private fun setupProfileList(
        model: FeedFollowRecommendationModel? = mData,
        selectedPosition: Int
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
                isSelected = idx == finalSelectedPosition,
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

        binding.clMain.showWithCondition(isDataAvailable)
        binding.feedNoContent.root.showWithCondition(!isDataAvailable)
    }

    private fun setupEmptyLayout() {
        binding.feedNoContent.btnShowOtherContent.setOnClickListener {
            listener.onClickViewOtherContent()
        }
    }

    private fun forcePlayVideo() {
        profileAdapter.notifyItemChanged(getSelectedPosition(), FeedFollowProfilePayloadHelper.createPayload(isPlayVideo = true))
    }

    private fun forceStopVideo() {
        profileAdapter.notifyItemChanged(getSelectedPosition(), FeedFollowProfilePayloadHelper.createPayload(isPlayVideo = false))
    }

    private fun getSelectedPosition(): Int {
        val snappedView = snapHelper.findSnapView(layoutManager) ?: return 0
        return binding.rvFollowRecommendation.getChildAdapterPosition(snappedView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_follow_recommendation
    }
}
