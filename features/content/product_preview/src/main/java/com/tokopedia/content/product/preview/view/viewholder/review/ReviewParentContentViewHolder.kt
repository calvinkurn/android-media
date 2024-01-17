package com.tokopedia.content.product.preview.view.viewholder.review

import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.databinding.ItemReviewParentContentBinding
import com.tokopedia.content.product.preview.view.components.LikeDanceAnim
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.DescriptionUiModel
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImageCircle

class ReviewParentContentViewHolder(
    private val binding: ItemReviewParentContentBinding,
    private val listener: Listener,
) : ViewHolder(binding.root) {

    private val danceAnim by lazyThreadSafetyNone {
        LikeDanceAnim(binding.ivDanceLike)
    }

    fun bind(item: ReviewUiModel) {
        bindAuthor(item.author)
        bindDescription(item.description)
        bindLike(item.likeState)
        setupTap(item)
    }

    private fun bindAuthor(author: AuthorUiModel) = with(binding.layoutAuthorReview) {
        tvAuthorName.text = author.name
        ivAuthor.loadImageCircle(url = author.avatarUrl)
        lblAuthorStats.setLabel(author.type)
        lblAuthorStats.showWithCondition(author.type.isNotBlank())

        lblAuthorStats.setOnClickListener {
            listener.onReviewCredibilityClicked(author)
        }
        tvAuthorName.setOnClickListener {
            listener.onReviewCredibilityClicked(author)
        }
    }

    private fun bindDescription(description: DescriptionUiModel) = with(binding) {
        val divider = root.context.getString(R.string.circle_dot_divider)
        tvReviewDetails.text = buildString {
            append(description.stars)
            append(" $divider ")
            append(description.productType)
            append(" $divider ")
            append(description.timestamp)
        }
        tvReviewDescription.text = description.description
    }

    fun bindLike(state: LikeUiState) = with(binding.layoutLikeReview) {
        val icon = when (state.state) {
            LikeUiState.LikeStatus.Reset -> IconUnify.THUMB
            else -> IconUnify.THUMB_FILLED
        }
        ivReviewLike.setImage(newIconId = icon)
        tvLikeCount.text = state.count.toString()
        root.setOnClickListener {
            listener.onLike(state.copy(withAnimation = false))
        }

        if (!state.withAnimation) return@with
        danceAnim.setEnabled(isEnabled = true)
        danceAnim.setIsLiked(state.state == LikeUiState.LikeStatus.Like)
        danceAnim.playLikeAnimation()
    }

    private fun setupTap(item: ReviewUiModel) {
        binding.ivReviewMenu.setOnClickListener {
            listener.onMenuClicked(item.menus)
        }

        val gesture = GestureDetector(
            binding.root.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    listener.onLike(item.likeState.copy(withAnimation = true))
                    return true
                }
            })
        binding.root.setOnTouchListener { _, motionEvent ->
            gesture.onTouchEvent(motionEvent)
            true
        }
    }

    interface Listener {
        fun onReviewCredibilityClicked(author: AuthorUiModel)
        fun onMenuClicked(menu: MenuStatus)
        fun onLike(status: LikeUiState)
    }

    companion object {
        fun create(binding: ItemReviewParentContentBinding, listener: Listener) =
            ReviewParentContentViewHolder(binding, listener)
    }
}
