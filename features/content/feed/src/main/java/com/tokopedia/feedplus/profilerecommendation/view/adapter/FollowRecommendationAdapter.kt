package com.tokopedia.feedplus.profilerecommendation.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecommendationAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationLoadMoreViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlin.math.max

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationAdapter(
        list: List<FollowRecommendationViewModel>,
        private val listener: ActionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val TYPE_CARD = 1
        const val TYPE_LOADING = 2
    }

    interface ActionListener {
        fun onFollowButtonClicked(authorId: String, isFollowed: Boolean, actionToCall: FollowRecommendationAction)
        fun onFollowStateChanged(followCount: Int)
    }

    private val itemList: MutableList<FollowRecommendationViewModel> = list.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_recommendation_card, parent, false)
                FollowRecommendationCardViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_layout, parent, false)
                FollowRecommendationLoadMoreViewHolder(view)
            }
            else -> throw IllegalStateException("No such type")
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when {
            holder is FollowRecommendationCardViewHolder && item is FollowRecommendationCardViewModel -> holder.bind(item, position)
            holder is FollowRecommendationLoadMoreViewHolder && item is FollowRecommendationLoadMoreViewModel -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is FollowRecommendationCardViewModel -> TYPE_CARD
            is FollowRecommendationLoadMoreViewModel -> TYPE_LOADING
            else -> -1
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        when (holder) {
            is FollowRecommendationCardViewHolder -> holder.onViewRecycled()
            is FollowRecommendationLoadMoreViewHolder -> holder.onViewRecycled()
        }
    }

    fun addItems(list: List<FollowRecommendationCardViewModel>) {
        updateItems(list)
        itemList.addAll(list)
    }

    fun clearItems() {
        itemList.clear()
    }

    fun showLoading() {
        itemList.add(FollowRecommendationLoadMoreViewModel)
        notifyItemInserted(itemList.lastIndex)
    }

    fun hideLoading() {
        if (itemList.size <= 0) return
        for (index in max(itemList.size-1, 0) downTo 0) {
            if (itemList[index] is FollowRecommendationLoadMoreViewModel) {
                itemList.removeAt(index)
                notifyItemRemoved(index)
                return
            }
        }
    }

    fun getFollowedCount(): Int = itemList.filterIsInstance(FollowRecommendationCardViewModel::class.java).count(FollowRecommendationCardViewModel::isFollowed)

    fun updateFollowState(id: String, action: FollowRecommendationAction) {
        val itemIndex = itemList.filterIsInstance(FollowRecommendationCardViewModel::class.java).indexOfFirst { it.authorId == id }
        itemList[itemIndex] = (itemList[itemIndex] as FollowRecommendationCardViewModel).copy(
                isFollowed = action == FollowRecommendationAction.FOLLOW
        )
        notifyItemChanged(itemIndex)
        listener.onFollowStateChanged(getFollowedCount())
    }

    private fun updateItems(newList: List<FollowRecommendationCardViewModel>) {
        val callback = FollowRecommendationDiffUtilCallback(itemList, newList)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
    }

    inner class FollowRecommendationLoadMoreViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        fun bind(element: FollowRecommendationLoadMoreViewModel) {

        }

        fun onViewRecycled() {
        }
    }

    inner class FollowRecommendationCardViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        private val ivImage1 = itemView.findViewById<ImageView>(R.id.iv_image1)
        private val ivImage2 = itemView.findViewById<ImageView>(R.id.iv_image2)
        private val ivImage3 = itemView.findViewById<ImageView>(R.id.iv_image3)
        private val ivProfile = itemView.findViewById<ImageView>(R.id.iv_profile)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tv_description)
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)
        private val btnFollow = itemView.findViewById<UnifyButton>(R.id.btn_follow)
        private val ivBadge = itemView.findViewById<ImageView>(R.id.iv_badge)
        private val tvHeader = itemView.findViewById<TextView>(R.id.tv_header)

        fun bind(element: FollowRecommendationCardViewModel, position: Int) {
            initView(element, position)
            initViewListener(element)
        }

        fun onViewRecycled() {
            ivImage1.clearImage()
            ivImage2.clearImage()
            ivImage3.clearImage()
            ivProfile.clearImage()
        }

        private fun initView(element: FollowRecommendationCardViewModel, position: Int) {
            loadImageOrDefault(ivImage1, element.image1Url)
            loadImageOrDefault(ivImage2, element.image2Url)
            loadImageOrDefault(ivImage3, element.image3Url)
            tvName.text = element.title
            tvDescription.text = element.description
            tvHeader.text = element.header

            if (position != 0) tvHeader.gone() else tvHeader.visible()

            if (!TextUtils.isEmpty(element.avatar)) {
                ivProfile.loadImageCircle(element.avatar)
            } else {
                ivProfile.setImageDrawable(
                        MethodChecker.getDrawable(itemView.context, R.drawable.error_drawable)
                )
            }

            setBadge(element.badgeUrl)
            setButtonFollow(element.isFollowed)
        }

        private fun initViewListener(element: FollowRecommendationCardViewModel) {
            btnFollow.setOnClickListener {
                listener.onFollowButtonClicked(
                        element.authorId,
                        element.isFollowed,
                        if (element.isFollowed) FollowRecommendationAction.UNFOLLOW else FollowRecommendationAction.FOLLOW
                )
            }
        }

        private fun setBadge(badgeUrl: String) {
            val layoutParams = tvName.layoutParams as ViewGroup.MarginLayoutParams
            if (!TextUtils.isEmpty(badgeUrl)) {
                ivBadge.show()
                ivBadge.loadImage(badgeUrl)
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_4).toInt()
            } else {
                ivBadge.hide()
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_0).toInt()
            }
        }

        private fun setButtonFollow(isFollowed: Boolean) {
            btnFollow.apply {
                if (isFollowed) {
                    text = itemView.context.getString(R.string.following)
                    buttonVariant = 2
                } else {
                    text = itemView.context.getString(R.string.action_follow_english)
                    buttonVariant = 1
                }
            }
        }

        private fun loadImageOrDefault(imageView: ImageView, imageUrl: String) {
            if (!TextUtils.isEmpty(imageUrl)) {
                imageView.loadImage(imageUrl)
            } else {
                imageView.setBackgroundColor(
                        MethodChecker.getColor(imageView.context, R.color.feed_image_default)
                )
            }
        }
    }

    inner class FollowRecommendationDiffUtilCallback(
            private val oldList: List<FollowRecommendationViewModel>,
            private val newList: List<FollowRecommendationViewModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            val oldItem = oldList[oldPos]
            val newItem = newList[newPos]
            return if (oldItem is FollowRecommendationCardViewModel && newItem is FollowRecommendationCardViewModel) oldItem.authorId == newItem.authorId
            else oldItem is FollowRecommendationLoadMoreViewModel && newItem is FollowRecommendationLoadMoreViewModel
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos] == newList[newPos]
        }
    }
}