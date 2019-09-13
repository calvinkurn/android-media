package com.tokopedia.feedplus.profilerecommendation.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecommendationAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecommendationCardViewModel
import com.tokopedia.kotlin.extensions.view.*

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecommendationAdapter(
        list: List<FollowRecommendationCardViewModel>,
        private val listener: ActionListener)
    : RecyclerView.Adapter<FollowRecommendationAdapter.FollowRecommendationCardViewHolder>() {

    interface ActionListener {
        fun onFollowButtonClicked(authorId: String, isFollowed: Boolean, actionToCall: FollowRecommendationAction)
        fun onFollowStateChanged(followCount: Int)
    }

    private val itemList: MutableList<FollowRecommendationCardViewModel> = list.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowRecommendationCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_recommendation_card, parent, false)
        return FollowRecommendationCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FollowRecommendationCardViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun onViewRecycled(holder: FollowRecommendationCardViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    fun addItems(list: List<FollowRecommendationCardViewModel>) {
        updateItems(list)
        itemList.addAll(list)
    }

    fun clearItems() {
        itemList.clear()
    }

    fun setItems(newList: List<FollowRecommendationCardViewModel>) {
        updateItems(newList)
        itemList.apply {
            clear()
            addAll(newList)
        }
    }

    fun getFollowedCount(): Int = itemList.count(FollowRecommendationCardViewModel::isFollowed)

    fun updateFollowState(id: String, action: FollowRecommendationAction) {
        val itemIndex = itemList.indexOfFirst { it.authorId == id }
        itemList[itemIndex] = itemList[itemIndex].copy(
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

        fun bind(element: FollowRecommendationCardViewModel) {
            initView(element)
            initViewListener(element)
        }

        fun onViewRecycled() {
            ivImage1.clearImage()
            ivImage2.clearImage()
            ivImage3.clearImage()
            ivProfile.clearImage()
        }

        private fun initView(element: FollowRecommendationCardViewModel) {
            loadImageOrDefault(ivImage1, element.image1Url)
            loadImageOrDefault(ivImage2, element.image2Url)
            loadImageOrDefault(ivImage3, element.image3Url)
            tvName.text = element.title
            tvDescription.text = element.description
            tvHeader.text = element.header

            if (element.header == null) tvHeader.gone() else tvHeader.visible()

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
            private val oldList: List<FollowRecommendationCardViewModel>,
            private val newList: List<FollowRecommendationCardViewModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos].authorId == newList[newPos].authorId
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