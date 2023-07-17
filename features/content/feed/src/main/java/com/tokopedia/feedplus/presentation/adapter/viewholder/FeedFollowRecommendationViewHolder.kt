package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.ItemFeedFollowRecommendationBinding
import com.tokopedia.feedplus.presentation.adapter.FeedFollowProfileAdapter
import com.tokopedia.feedplus.presentation.adapter.itemdecoration.FeedFollowProfileItemDecoration
import com.tokopedia.feedplus.presentation.adapter.layoutmanager.FeedFollowProfileLayoutManager
import com.tokopedia.feedplus.presentation.adapter.listener.FeedFollowRecommendationListener
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

    private fun setupHeader(model: FeedFollowRecommendationModel) {
        binding.tvTitle.text = model.title
        binding.tvDesc.text = model.description
    }

    private fun setupProfileList(
        model: FeedFollowRecommendationModel? = mData,
        selectedPosition: Int
    ) {
        if (model == null) return

        val mappedList = model.data.mapIndexed { idx, item ->
            FeedFollowProfileAdapter.Model.Profile(
                data = item,
                isSelected = idx == selectedPosition,
            )
        }

        val finalList = if (model.hasNext)
            mappedList + listOf(FeedFollowProfileAdapter.Model.Loading)
        else
            mappedList

        val isDataAvailable = finalList.isNotEmpty()

        profileAdapter.setItemsAndAnimateChanges(finalList)

        binding.clMain.showWithCondition(isDataAvailable)
        binding.feedNoContent.root.showWithCondition(!isDataAvailable)
    }

    private fun setupEmptyLayout() {
        binding.feedNoContent.btnShowOtherContent.setOnClickListener {
            listener.onClickViewOtherContent()
        }
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
