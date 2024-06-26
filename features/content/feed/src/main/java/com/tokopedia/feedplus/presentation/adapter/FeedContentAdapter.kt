package com.tokopedia.feedplus.presentation.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedErrorViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedFollowRecommendationViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedNoContentViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostImageViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostLiveViewHolder
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostVideoViewHolder
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardLivePreviewContentModel
import com.tokopedia.feedplus.presentation.model.FeedCardVideoContentModel
import com.tokopedia.feedplus.presentation.model.FeedFollowRecommendationModel
import com.tokopedia.feedplus.presentation.model.FeedNoContentModel

/**
 * Created by kenny.hadisaputra on 04/07/23
 */
class FeedContentAdapter(
    private val typeFactory: FeedAdapterTypeFactory,
    private val onLoadMore: () -> Unit
) : ListAdapter<FeedContentAdapter.Item, AbstractViewHolder<out Visitable<*>>>(
    object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return if (oldItem.data is FeedCardImageContentModel && newItem.data is FeedCardImageContentModel) {
                if (oldItem.data.isTopAds && newItem.data.isTopAds) {
                    oldItem.data.topAdsId == newItem.data.topAdsId
                } else {
                    oldItem.data.id == newItem.data.id
                }
            } else if (oldItem.data is FeedCardVideoContentModel && newItem.data is FeedCardVideoContentModel) {
                oldItem.data.id == newItem.data.id
            } else if (oldItem.data is FeedCardLivePreviewContentModel && newItem.data is FeedCardLivePreviewContentModel) {
                oldItem.data.id == newItem.data.id
            } else if (oldItem.data is FeedFollowRecommendationModel && newItem.data is FeedFollowRecommendationModel) {
                oldItem.data.id == newItem.data.id
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return if (oldItem.data is FeedCardImageContentModel && newItem.data is FeedCardImageContentModel) {
                oldItem == newItem
            } else if (oldItem.data is FeedCardVideoContentModel && newItem.data is FeedCardVideoContentModel) {
                oldItem == newItem
            } else if (oldItem.data is FeedCardLivePreviewContentModel && newItem.data is FeedCardLivePreviewContentModel) {
                oldItem == newItem
            } else if (oldItem.data is FeedFollowRecommendationModel && newItem.data is FeedFollowRecommendationModel) {
                oldItem == newItem
            } else {
                oldItem == newItem
            }
        }

        override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
            val payloads = mutableListOf<Int>()
            with(payloads) {
                if (oldItem.isSelected != newItem.isSelected) {
                    add(FeedViewHolderPayloadActions.FEED_POST_SELECTED_CHANGED)
                }
                if (oldItem.isScrolling != newItem.isScrolling) {
                    add(FeedViewHolderPayloadActions.FEED_POST_SCROLLING_CHANGED)
                }
            }

            if (oldItem.data is FeedCardImageContentModel && newItem.data is FeedCardImageContentModel) {
                if (oldItem.data.isTopAds && !oldItem.data.isFetched && newItem.data.isTopAds && newItem.data.isFetched) {
                    Unit
                } else {
                    with(payloads) {
                        if (oldItem.data.followers.isFollowed != newItem.data.followers.isFollowed) {
                            add(FeedViewHolderPayloadActions.FEED_POST_FOLLOW_CHANGED)
                        }
                        if (oldItem.data.like.isLiked != newItem.data.like.isLiked) {
                            add(FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED)
                        }
                        if (oldItem.data.comments.countFmt != newItem.data.comments.countFmt) {
                            add(FeedViewHolderPayloadActions.FEED_POST_COMMENT_COUNT)
                        }
                        if (oldItem.data.campaign.isReminderActive != newItem.data.campaign.isReminderActive) {
                            add(FeedViewHolderPayloadActions.FEED_POST_REMINDER_CHANGED)
                        }
                    }
                }
            } else if (oldItem.data is FeedCardVideoContentModel && newItem.data is FeedCardVideoContentModel) {
                with(payloads) {
                    if (oldItem.data.followers.isFollowed != newItem.data.followers.isFollowed) {
                        add(FeedViewHolderPayloadActions.FEED_POST_FOLLOW_CHANGED)
                    }
                    if (oldItem.data.like.isLiked != newItem.data.like.isLiked) {
                        add(FeedViewHolderPayloadActions.FEED_POST_LIKED_UNLIKED)
                    }
                    if (oldItem.data.comments.countFmt != newItem.data.comments.countFmt) {
                        add(FeedViewHolderPayloadActions.FEED_POST_COMMENT_COUNT)
                    }
                }
            }

            return if (payloads.isNotEmpty()) FeedViewHolderPayloads(payloads) else null
        }
    }
) {

    private val loadingMoreModel = Item(LoadingMoreModel(), isSelected = false)
    private val errorNetworkModel = Item(ErrorNetworkModel(), isSelected = false)

    private var mSelectedPosition = RecyclerView.NO_POSITION
    private var mIsScrolling = false

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return typeFactory.createViewHolder(parent, viewType)!!
    }

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int) {
        val item = getItem(position)
        when {
            holder is FeedPostImageViewHolder && item.data is FeedCardImageContentModel -> {
                holder.bind(item)
            }
            holder is FeedPostVideoViewHolder && item.data is FeedCardVideoContentModel -> {
                holder.bind(item)
            }
            holder is FeedPostLiveViewHolder && item.data is FeedCardLivePreviewContentModel -> {
                holder.bind(item)
            }
            holder is FeedNoContentViewHolder && item.data is FeedNoContentModel -> {
                holder.bind(item.data)
            }
            holder is FeedFollowRecommendationViewHolder && item.data is FeedFollowRecommendationModel -> {
                holder.bind(item)
            }
            holder is LoadingMoreViewHolder && item.data is LoadingMoreModel -> {
                holder.bind(item.data)
            }
            holder is FeedErrorViewHolder && item.data is ErrorNetworkModel -> {
                holder.bind(item.data)
            }
        }

        if (position >= itemCount - 6) onLoadMore()
    }

    override fun onBindViewHolder(
        holder: AbstractViewHolder<out Visitable<*>>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            val item = getItem(position)
            when {
                holder is FeedPostImageViewHolder && item.data is FeedCardImageContentModel -> {
                    holder.bind(item, payloads)
                }
                holder is FeedPostVideoViewHolder && item.data is FeedCardVideoContentModel -> {
                    holder.bind(item, payloads)
                }
                holder is FeedPostLiveViewHolder && item.data is FeedCardLivePreviewContentModel -> {
                    holder.bind(item, payloads)
                }
                holder is FeedFollowRecommendationViewHolder && item.data is FeedFollowRecommendationModel -> {
                    holder.bind(item, payloads)
                }
                holder is FeedNoContentViewHolder && item.data is FeedNoContentModel -> {
                    holder.bind(item.data, payloads)
                }
                holder is LoadingMoreViewHolder && item.data is LoadingMoreModel -> {
                    holder.bind(item.data, payloads)
                }
                holder is FeedErrorViewHolder && item.data is ErrorNetworkModel -> {
                    holder.bind(item.data, payloads)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position).data) {
            is FeedCardImageContentModel -> FeedPostImageViewHolder.LAYOUT
            is FeedCardVideoContentModel -> FeedPostVideoViewHolder.LAYOUT
            is FeedCardLivePreviewContentModel -> FeedPostLiveViewHolder.LAYOUT
            is FeedFollowRecommendationModel -> FeedFollowRecommendationViewHolder.LAYOUT
            is FeedNoContentModel -> FeedNoContentViewHolder.LAYOUT
            is LoadingMoreModel -> LoadingMoreViewHolder.LAYOUT
            is ErrorNetworkModel -> ErrorNetworkViewHolder.LAYOUT
            else -> error("Item $item is not supported")
        }
    }

    fun showClearView(position: Int) {
        if (itemCount <= position) return
        notifyItemChanged(position, FeedViewHolderPayloadActions.FEED_POST_CLEAR_MODE)
    }

    fun showLoading() {
        val currentList = this.currentList
        if (currentList.contains(loadingMoreModel)) return
        submitList(currentList + loadingMoreModel)
    }

    fun hideLoading() {
        val currentList = this.currentList
        submitList(currentList - loadingMoreModel)
    }

    fun showErrorNetwork() {
        val currentList = this.currentList
        if (currentList.filter { it.data is ErrorNetworkModel }.size >= 2) return
        submitList(currentList + errorNetworkModel)
    }

    fun removeErrorNetwork() {
        val currentList = this.currentList
        submitList(currentList - errorNetworkModel)
    }

    fun pauseFollowRecommendationVideo(position: Int) {
        notifyItemChanged(position, FeedViewHolderPayloadActions.FEED_FOLLOW_RECOM_PAUSE_VIDEO)
    }

    fun resumeFollowRecommendationVideo(position: Int) {
        notifyItemChanged(position, FeedViewHolderPayloadActions.FEED_FOLLOW_RECOM_RESUME_VIDEO)
    }

    fun pauseVideoProductIconAnimation(position: Int) {
        notifyItemChanged(position, FeedViewHolderPayloadActions.FEED_VIDEO_PRODUCT_ICON_ANIM_PAUSE)
    }

    fun resumeVideoProductIconAnimation(position: Int) {
        notifyItemChanged(position, FeedViewHolderPayloadActions.FEED_VIDEO_PRODUCT_ICON_ANIM_RESUME)
    }

    fun addElement(element: Any) {
        val currentList = this.currentList
        submitList(currentList + Item(element, isSelected = false))
    }

    fun select(position: Int) {
        mSelectedPosition = position
        val newList = currentList.mapIndexed { index, data ->
            if (index == position || data.isSelected) {
                data.copy(isSelected = index == position, isScrolling = false)
            } else {
                if (data.isScrolling) {
                    data.copy(isScrolling = false)
                } else {
                    data
                }
            }
        }
        mIsScrolling = false
        submitList(newList)
    }

    fun onScrolling() {
        val currPosition = mSelectedPosition
        val newList = currentList.mapIndexed { index, data ->
            if (index == currPosition) {
                data.copy(isScrolling = true)
            } else {
                if (data.isScrolling) {
                    data.copy(isScrolling = false)
                } else {
                    data
                }
            }
        }
        mIsScrolling = true
        submitList(newList)
    }

    fun stopScrolling() {
        val newList = currentList.map { data ->
            if (data.isScrolling) {
                data.copy(isScrolling = false)
            } else {
                data
            }
        }
        mIsScrolling = false
        submitList(newList)

        if (mSelectedPosition >= itemCount - 2) onLoadMore()
    }

    fun setList(elements: List<Any>, commitCallback: () -> Unit = {}) {
        if (mSelectedPosition > elements.size - 1) mSelectedPosition = RecyclerView.NO_POSITION
        if (mSelectedPosition == RecyclerView.NO_POSITION) {
            mSelectedPosition =
                elements.indexOfFirst { true }
        }

        submitList(
            elements.mapIndexed { index, element ->
                Item(
                    element,
                    index == mSelectedPosition,
                    if (mIsScrolling) index == mSelectedPosition else false
                )
            },
            commitCallback
        )
    }

    override fun onViewRecycled(holder: AbstractViewHolder<out Visitable<*>>) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    data class Item(
        val data: Any,
        val isSelected: Boolean,
        val isScrolling: Boolean = false
    )
}
