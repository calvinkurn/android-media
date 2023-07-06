package com.tokopedia.feedplus.presentation.adapter.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.PagerSnapHelper
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
    private val listener: FeedFollowRecommendationListener
) : AbstractViewHolder<FeedFollowRecommendationModel>(binding.root) {

    private val profileAdapter = FeedFollowProfileAdapter(
        profileListener = object : FeedFollowProfileViewHolder.Profile.Listener {
            override fun onScrollProfile(position: Int) {
                binding.rvFollowRecommendation.smoothScrollToPosition(position)
            }
        },
        followRecommendationListener = listener
    )
    private val layoutManager = FeedFollowProfileLayoutManager(itemView.context)
    private val snapHelper = PagerSnapHelper()

    init {
        binding.rvFollowRecommendation.layoutManager = layoutManager
        binding.rvFollowRecommendation.addItemDecoration(FeedFollowProfileItemDecoration(itemView.context))
        snapHelper.attachToRecyclerView(binding.rvFollowRecommendation)
        binding.rvFollowRecommendation.adapter = profileAdapter
    }

    override fun bind(element: FeedFollowRecommendationModel?) {
        element?.let { model ->
            setupHeader(model)
            setupProfileList(model)
            setupEmptyLayout()
        }
    }

    private fun setupHeader(model: FeedFollowRecommendationModel) {
        binding.tvTitle.text = model.title
        binding.tvDesc.text = model.description
    }

    private fun setupProfileList(model: FeedFollowRecommendationModel) {

        val mappedList = model.data.map {
            FeedFollowProfileAdapter.Model.Profile(data = it)
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

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_feed_follow_recommendation
    }
}
