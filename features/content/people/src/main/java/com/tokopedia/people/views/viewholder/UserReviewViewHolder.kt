package com.tokopedia.people.views.viewholder

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.people.R
import com.tokopedia.people.databinding.ItemUserReviewBinding
import com.tokopedia.people.databinding.ItemUserReviewShimmerBinding
import com.tokopedia.people.databinding.UpItemUserPostLoadingBinding
import com.tokopedia.people.utils.getBoldSpan
import com.tokopedia.people.utils.getClickableSpan
import com.tokopedia.people.utils.getGreenColorSpan
import com.tokopedia.people.views.adapter.UserReviewMediaAdapter
import com.tokopedia.people.views.itemdecoration.UserReviewMediaItemDecoration
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import kotlin.math.abs
import com.tokopedia.unifyprinciples.R as unifyR


/**
 * Created By : Jonathan Darwin on May 15, 2023
 */
class UserReviewViewHolder private constructor() {

    class Review(
        private val lifecycleOwner: LifecycleOwner,
        private val binding: ItemUserReviewBinding,
        private val listener: Listener
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter by lazyThreadSafetyNone {
            UserReviewMediaAdapter(
                lifecycleOwner,
                listener = object : UserReviewMediaAdapter.Listener {
                    override fun onMediaClick(
                        feedbackId: String,
                        productId: String,
                        attachment: UserReviewUiModel.Attachment
                    ) {
                        listener.onMediaClick(feedbackId, productId,  attachment)
                    }
                }
            )
        }
        init {
            if (binding.rvMedia.itemDecorationCount == 0)
                binding.rvMedia.addItemDecoration(UserReviewMediaItemDecoration(itemView.context))
            binding.rvMedia.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvMedia.adapter = adapter

            var preX = 0f
            var preY = 0f

            binding.rvMedia.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(
                    view: RecyclerView,
                    event: MotionEvent
                ): Boolean {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> binding.rvMedia.parent.requestDisallowInterceptTouchEvent(true)
                        MotionEvent.ACTION_MOVE -> {
                            if (abs(event.x - preX) > abs(event.y - preY)) {
                                binding.rvMedia.parent.requestDisallowInterceptTouchEvent(true)
                            } else if (abs(event.y - preY) > Y_SCROLL_BUFFER) {
                                binding.rvMedia.parent.requestDisallowInterceptTouchEvent(false)
                            }
                        }
                    }

                    preX = event.x
                    preY = event.y

                    return false
                }

                override fun onTouchEvent(view: RecyclerView, event: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }

        fun bind(item: UserReviewUiModel.Review) {
            binding.apply {
                listener.onImpressCard(item, layoutPosition + 1)

                imgProduct.setImageUrl(item.product.productImageURL)
                tvProduct.text = item.product.productName

                tvVariant.shouldShowWithAction(item.product.productVariant.variantName.isNotEmpty()) {
                    tvVariant.text = itemView.context.getString(
                        R.string.up_profile_user_review_product_variant_template,
                        item.product.productVariant.variantName
                    )
                }

                tvTime.text = item.reviewTime

                setupStar(icStar1, item.rating >= 1)
                setupStar(icStar2, item.rating >= 2)
                setupStar(icStar3, item.rating >= 3)
                setupStar(icStar4, item.rating >= 4)
                setupStar(icStar5, item.rating >= 5)

                setupReviewText(item)

                setupMedia(item)

                setupLike(item)

                binding.clProductInfo.setOnClickListener {
                    listener.onClickProductInfo(item)
                }
            }
        }

        private fun setupStar(icon: IconUnify, isFilled: Boolean) {
            val color = if (isFilled) {
                unifyR.color.Unify_YN300
            } else {
                unifyR.color.Unify_NN300
            }

            icon.setImage(
                IconUnify.STAR_FILLED,
                newLightEnable = MethodChecker.getColor(itemView.context, color),
                newDarkEnable = MethodChecker.getColor(itemView.context, color)
            )
        }

        private fun setupReviewText(review: UserReviewUiModel.Review) {
            val formattedReviewText = SpannableStringBuilder()

            if (review.isReviewTextExpanded || review.reviewText.length <= MAX_REVIEW_CHAR) {
                formattedReviewText.append(review.reviewText)
            } else {
                formattedReviewText.append(
                    itemView.context.getString(
                        R.string.up_profile_user_review_text_template,
                        review.reviewText.substring(0, MAX_REVIEW_CHAR)
                    )
                )
                val highlightedText = itemView.context.getString(R.string.up_link_see_more)
                formattedReviewText.setSpanOnText(
                    highlightedText,
                    getClickableSpan {
                        listener.onClickSeeMore(review)
                    },
                    getBoldSpan(),
                    getGreenColorSpan(itemView.context)
                )
            }

            binding.tvReview.text = formattedReviewText
            binding.tvReview.movementMethod = LinkMovementMethod.getInstance()
        }

        private fun setupMedia(item: UserReviewUiModel.Review) {
            adapter.setItemsAndAnimateChanges(
                item.attachments.map { attachment ->
                    when (attachment) {
                        is UserReviewUiModel.Attachment.Video -> {
                            UserReviewMediaAdapter.Model.Video(
                                feedbackId = item.feedbackID,
                                productId = item.product.productID,
                                attachment = attachment,
                            )
                        }
                        is UserReviewUiModel.Attachment.Image -> {
                            UserReviewMediaAdapter.Model.Image(
                                feedbackId = item.feedbackID,
                                productId = item.product.productID,
                                attachment = attachment,
                            )
                        }
                    }
                }
            )
        }

        private fun setupLike(item: UserReviewUiModel.Review) {
            binding.icLike.setImage(if (item.likeDislike.isLike) IconUnify.THUMB_FILLED else IconUnify.THUMB)
            binding.icLike.setOnClickListener {
                listener.onClickLike(item)
            }
            binding.tvLikeCounter.text = item.likeDislike.totalLike.toString()
            binding.tvLikeCounter.setOnClickListener {
                listener.onClickLike(item)
            }
        }

        companion object {

            private const val Y_SCROLL_BUFFER = 10
            private const val MAX_REVIEW_CHAR = 140
            fun create(
                lifecycleOwner: LifecycleOwner,
                parent: ViewGroup,
                listener: Listener
            ) = Review(
                lifecycleOwner,
                ItemUserReviewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }

        interface Listener {
            fun onImpressCard(review: UserReviewUiModel.Review, position: Int)
            fun onClickLike(review: UserReviewUiModel.Review)
            fun onClickSeeMore(review: UserReviewUiModel.Review)
            fun onClickProductInfo(review: UserReviewUiModel.Review)
            fun onMediaClick(
                feedbackId: String,
                productId: String,
                attachment: UserReviewUiModel.Attachment
            )
        }
    }

    class Shimmer(
        binding: ItemUserReviewShimmerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(
                parent: ViewGroup
            ) = Shimmer(
                ItemUserReviewShimmerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    class Loading(
        binding: UpItemUserPostLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(
                parent: ViewGroup
            ) = Loading(
                UpItemUserPostLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}
