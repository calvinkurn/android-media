package com.tokopedia.feedcomponent.view.adapter.post

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXCard
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXMedia
import com.tokopedia.feedcomponent.util.util.doOnLayout
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.widget.PostTagView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created by kenny.hadisaputra on 24/06/22
 */
internal class FeedPostCarouselAdapter(
    dataSource: ViewHolder.DataSource,
    listener: ViewHolder.Listener,
) : BaseDiffUtilAdapter<FeedXMedia>() {

    init {
        delegatesManager.addDelegate(Delegate(dataSource, listener))
    }

    override fun areItemsTheSame(oldItem: FeedXMedia, newItem: FeedXMedia): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FeedXMedia,
        newItem: FeedXMedia
    ): Boolean {
        return oldItem == newItem
    }

    private class Delegate(
        private val dataSource: ViewHolder.DataSource,
        private val listener: ViewHolder.Listener,
    ) : TypedAdapterDelegate<FeedXMedia, FeedXMedia, ViewHolder>(
        R.layout.item_post_image_new
    ) {
        override fun onBindViewHolder(item: FeedXMedia, holder: ViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return ViewHolder.create(parent, dataSource, listener)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    internal class ViewHolder(
        itemView: View,
        private val dataSource: DataSource,
        private val listener: Listener,
    ) : BaseViewHolder(itemView) {

        private val postImage = itemView.findViewById<ImageUnify>(R.id.post_image)
        private val postImageLayout = itemView.findViewById<ConstraintLayout>(R.id.post_image_layout)
        private val llLihatProduct = itemView.findViewById<LinearLayout>(R.id.ll_lihat_product)
        private val tvLihatProduct = itemView.findViewById<TextView>(R.id.tv_lihat_product)
        private val likeAnim = itemView.findViewById<ImageUnify>(R.id.like_anim)

        private val topAdsCard = itemView.findViewById<ConstraintLayout>(R.id.top_ads_detail_card)
        private val topAdsProductName = itemView.findViewById<Typography>(R.id.top_ads_product_name)
        private val topAdsChevron = topAdsCard.findViewById<IconUnify>(R.id.chevron)

        private val animationLike = AnimationUtils.loadAnimation(
            itemView.context,
            android.R.anim.fade_in
        )

        private val postGestureDetector = GestureDetector(
            itemView.context,
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    changeTopAdsColorToGreen()
                    listener.onImageClicked(this@ViewHolder)

                    onPostTagViews {
                        it.showExpandedView()
                    }
                    animateLihatProduct(!tvLihatProduct.isVisible)

                    return true
                }

                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    onPostTagViews {
                        it.hideExpandedViewIfShown()
                    }
                    animateLihatProduct(false)

                    if (!dataSource.getFeedXCard().isTopAds) {
                        likeAnim.startAnimation(animationLike)
                    }

                    return true
                }

                override fun onLongPress(e: MotionEvent?) {
                    changeTopAdsColorToGreen()
                }
            }
        )

        init {
            postImage.setOnTouchListener { _, event ->
                postGestureDetector.onTouchEvent(event)
                true
            }

            likeAnim.setImageDrawable(
                MethodChecker.getDrawable(
                    itemView.context,
                    R.drawable.ic_thumb_filled
                )
            )

            animationLike.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    likeAnim.visible()
                    listener.onLiked(this@ViewHolder)
                }

                override fun onAnimationEnd(animation: Animation) {
                    likeAnim.gone()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }

        override fun onViewRecycled() {
            super.onViewRecycled()
            removeExistingPostTags()
        }

        fun bind(item: FeedXMedia) {
            val card = dataSource.getFeedXCard()

            postImage.setImageUrl(item.mediaUrl)
            llLihatProduct.showWithCondition(item.tagProducts.isNotEmpty())

            setupTopAds(item)
            topAdsProductName.text = if (card.totalProducts > 1) {
                itemView.context.getString(R.string.feeds_check_x_products, card.totalProducts)
            } else itemView.context.getString(R.string.feeds_cek_sekarang)

            if (card.products.isNotEmpty()) {
                itemView.doOnLayout {
                    item.tagging.forEach { tagging ->
                        val tagView = PostTagView(itemView.context, tagging)
                        tagView.bindData(
                            dynamicPostListener = dataSource.getDynamicPostListener(),
                            products = card.products,
                            width = it.width,
                            height = it.height,
                            positionInFeed = dataSource.getPositionInFeed(),
                            bitmap = postImage?.drawable?.toBitmap(),
                        )
                        postImageLayout.addView(tagView)
                    }
                }
            }

            llLihatProduct.setOnClickListener {
                onPostTagViews {
                    it.showExpandedView()
                }
                animateLihatProduct(!tvLihatProduct.isVisible)

                listener.onLihatProductClicked(this, item)
            }

            itemView.addOnImpressionListener(item.impressHolder) {
                listener.onImpressed(this)
            }
        }

        private fun setupTopAds(media: FeedXMedia) {
            topAdsCard.setOnClickListener {
                changeTopAdsColorToGreen()
                listener.onTopAdsCardClicked(this, media)
            }
            changeTopAdsColorToWhite()
        }

        private fun changeTopAdsColorToGreen() {
            changeTopAdsColor(
                primaryColor = MethodChecker.getColor(
                    itemView.context,
                    unifyR.color.Unify_G500
                ),
                secondaryColor = MethodChecker.getColor(
                    itemView.context,
                    unifyR.color.Unify_N0
                ),
            )
        }

        private fun changeTopAdsColorToWhite() {
            changeTopAdsColor(
                primaryColor = MethodChecker.getColor(
                    itemView.context,
                    unifyR.color.Unify_NN50
                ),
                secondaryColor = MethodChecker.getColor(
                    itemView.context,
                    unifyR.color.Unify_NN600
                ),
            )
        }

        private fun changeTopAdsColor(
            primaryColor: Int,
            secondaryColor: Int,
        ) {
            topAdsProductName.setTextColor(secondaryColor)
            topAdsChevron.setColorFilter(secondaryColor)
            topAdsCard.setBackgroundColor(primaryColor)
        }

        private fun removeExistingPostTags() {
            (0 until postImageLayout.childCount).mapNotNull {
                postImageLayout.getChildAt(it) as? PostTagView
            }.forEach { postImageLayout.removeView(it) }

            tvLihatProduct.gone()
        }

        private fun onPostTagViews(onTag: (PostTagView) -> Unit) {
            (0 until postImageLayout.childCount).forEach {
                val view = postImageLayout.getChildAt(it)
                if (view is PostTagView) onTag(view)
            }
        }

        private fun animateLihatProduct(shouldShow: Boolean) {
            TransitionManager.beginDelayedTransition(
                llLihatProduct,
                AutoTransition()
                    .setDuration(ANIMATION_LIHAT_PRODUCT_DURATION)
            )

            tvLihatProduct.showWithCondition(shouldShow)
        }

        companion object {
            private const val ANIMATION_LIHAT_PRODUCT_DURATION = 250L

            fun create(
                parent: ViewGroup,
                dataSource: DataSource,
                listener: Listener,
            ) = ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.item_post_image_new,
                        parent,
                        false,
                    ),
                dataSource,
                listener,
            )
        }

        interface Listener {
            fun onTopAdsCardClicked(viewHolder: ViewHolder, media: FeedXMedia)
            fun onImageClicked(viewHolder: ViewHolder)
            fun onLiked(viewHolder: ViewHolder)
            fun onImpressed(viewHolder: ViewHolder)
            fun onLihatProductClicked(viewHolder: ViewHolder, media: FeedXMedia)
        }

        interface DataSource {
            fun getFeedXCard(): FeedXCard
            fun getDynamicPostListener(): DynamicPostViewHolder.DynamicPostListener?
            fun getPositionInFeed(): Int
        }
    }
}
