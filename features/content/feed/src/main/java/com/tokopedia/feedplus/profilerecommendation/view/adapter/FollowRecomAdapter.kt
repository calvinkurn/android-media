package com.tokopedia.feedplus.profilerecommendation.view.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.feedplus.profilerecommendation.data.AuthorType
import com.tokopedia.feedplus.profilerecommendation.view.state.FollowRecomAction
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardThumbnailViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomCardViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomLoadMoreViewModel
import com.tokopedia.feedplus.profilerecommendation.view.viewmodel.FollowRecomViewModel
import com.tokopedia.kotlin.extensions.view.*
import kotlin.math.max

/**
 * Created by jegul on 2019-09-11.
 */
class FollowRecomAdapter(
        list: List<FollowRecomViewModel>,
        private val listener: ActionListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val TYPE_CARD = 1
        const val TYPE_LOADING = 2
    }

    private val itemList: MutableList<FollowRecomViewModel> = list.toMutableList()

    private val cardList: List<FollowRecomCardViewModel>
        get() = itemList.filterIsInstance(FollowRecomCardViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CARD -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_follow_recommendation_card, parent, false)
                FollowRecommendationCardViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context).inflate(com.tokopedia.baselist.R.layout.loading_layout, parent, false)
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
            holder is FollowRecommendationCardViewHolder && item is FollowRecomCardViewModel -> holder.bind(item, position)
            holder is FollowRecommendationLoadMoreViewHolder && item is FollowRecomLoadMoreViewModel -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is FollowRecomCardViewModel -> TYPE_CARD
            is FollowRecomLoadMoreViewModel -> TYPE_LOADING
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

    fun addItems(list: List<FollowRecomCardViewModel>) {
        updateItems(cardList + list)
        itemList.addAll(list)
    }

    fun showLoading() {
        itemList.add(FollowRecomLoadMoreViewModel)
        notifyItemInserted(itemList.lastIndex)
    }

    fun hideLoading() {
        if (itemList.size <= 0) return
        for (index in max(itemList.size-1, 0) downTo 0) {
            if (itemList[index] is FollowRecomLoadMoreViewModel) {
                itemList.removeAt(index)
                notifyItemRemoved(index)
                return
            }
        }
    }

    fun getFollowedCount(): Int = cardList.count(FollowRecomCardViewModel::isFollowed)

    fun updateFollowState(id: String, action: FollowRecomAction) {
        val itemIndex = cardList.indexOfFirst { it.authorId == id }
        itemList[itemIndex] = (itemList[itemIndex] as FollowRecomCardViewModel).copy(
                isFollowed = action == FollowRecomAction.FOLLOW
        )
        notifyItemChanged(itemIndex, Unit)
        listener.onFollowStateChanged(getFollowedCount())
    }

    fun getItemByAuthorId(authorId: String): FollowRecomCardViewModel? = cardList.firstOrNull { it.authorId == authorId }

    private fun updateItems(newList: List<FollowRecomCardViewModel>) {
        val callback = FollowRecommendationDiffUtilCallback(itemList, newList)
        val result = DiffUtil.calculateDiff(callback)
        result.dispatchUpdatesTo(this)
    }

    inner class FollowRecommendationLoadMoreViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val progressBar = itemView.findViewById<ProgressBar>(R.id.progressBar)

        fun bind(element: FollowRecomLoadMoreViewModel) {

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
        private val layoutHolder: ConstraintLayout = itemView.findViewById(R.id.layouting)

        fun bind(element: FollowRecomCardViewModel, position: Int) {
            reset()
            initView(element, position)
            initViewListener(element)
        }

        fun onViewRecycled() {
            reset()
        }

        private fun reset() {
            ivImage1.clearImage()
            ivImage2.clearImage()
            ivImage3.clearImage()
            ivProfile.clearImage()

            ivImage1.setImageDrawable(null)
            ivImage2.setImageDrawable(null)
            ivImage3.setImageDrawable(null)
            resetListener()
        }

        private fun initView(element: FollowRecomCardViewModel, position: Int) {
            element.thumbnailList.take(3).forEachIndexed { index, model ->
                val imageView = when (index) {
                    0 -> ivImage1
                    1 -> ivImage2
                    else -> ivImage3
                }
                loadImageOrDefault(
                        imageView,
                        model.url
                )
                imageView.setOnClickListener { listener.onThumbnailClicked(model, position, element.authorType) }
            }
            tvName.text = element.title
            tvDescription.text = element.description
            tvHeader.text = element.header

            if (position != 0) tvHeader.gone() else tvHeader.visible()

            if (!TextUtils.isEmpty(element.avatar)) {
                ivProfile.loadImageCircle(element.avatar)
            } else {
                ivProfile.setImageDrawable(
                        MethodChecker.getDrawable(itemView.context, com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                )
            }

            setBadge(element.badgeUrl)
            setButtonFollow(element.isFollowed, element.textFollowTrue, element.textFollowFalse)
            layoutHolder.addOnImpressionListener(element.impressHolder) {
                listener.onFirstTimeCardShown(element, position)
            }
        }

        private fun initViewListener(element: FollowRecomCardViewModel) {
            btnFollow.setOnClickListener {
                listener.onFollowButtonClicked(
                        element.authorId,
                        element.isFollowed,
                        if (element.isFollowed) FollowRecomAction.UNFOLLOW else FollowRecomAction.FOLLOW
                )
            }

            ivProfile.setOnClickListener {
                listener.onNameOrAvatarClicked(element)
            }

            tvName.setOnClickListener {
                listener.onNameOrAvatarClicked(element)
            }
        }

        private fun resetListener() {
            ivImage1.setOnClickListener {  }
            ivImage2.setOnClickListener {  }
            ivImage3.setOnClickListener {  }
        }

        private fun setBadge(badgeUrl: String) {
            val layoutParams = tvName.layoutParams as ViewGroup.MarginLayoutParams
            if (!TextUtils.isEmpty(badgeUrl)) {
                ivBadge.show()
                ivBadge.loadImage(badgeUrl)
                layoutParams.leftMargin = itemView.context.resources.getDimension(R.dimen.dp_4).toInt()
            } else {
                ivBadge.hide()
                layoutParams.leftMargin = itemView.context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_0).toInt()
            }
        }

        private fun setButtonFollow(isFollowed: Boolean, actionTrue: String, actionFalse: String) {
            btnFollow.apply {
                if (isFollowed) {
                    text = actionFalse
                    buttonVariant = 2
                } else {
                    text = actionTrue
                    buttonVariant = 1
                }
            }
        }

        private fun loadImageOrDefault(imageView: ImageView, imageUrl: String) {
            if (!TextUtils.isEmpty(imageUrl)) {
                imageView.loadImage(imageUrl)
            } else {
                imageView.setBackgroundColor(
                        MethodChecker.getColor(imageView.context, com.tokopedia.unifyprinciples.R.color.Unify_N50)
                )
            }
        }
    }

    inner class FollowRecommendationDiffUtilCallback(
            private val oldList: List<FollowRecomViewModel>,
            private val newList: List<FollowRecomViewModel>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            val oldItem = oldList[oldPos]
            val newItem = newList[newPos]
            return if (oldItem is FollowRecomCardViewModel && newItem is FollowRecomCardViewModel) oldItem.authorId == newItem.authorId
            else oldItem is FollowRecomLoadMoreViewModel && newItem is FollowRecomLoadMoreViewModel
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

    interface ActionListener {
        fun onFollowButtonClicked(authorId: String, isFollowed: Boolean, actionToCall: FollowRecomAction)
        fun onFollowStateChanged(followCount: Int)
        fun onNameOrAvatarClicked(model: FollowRecomCardViewModel)
        fun onThumbnailClicked(model: FollowRecomCardThumbnailViewModel, itemPos: Int, authorType: AuthorType?)
        fun onFirstTimeCardShown(element: FollowRecomCardViewModel, adapterPosition: Int)
    }
}