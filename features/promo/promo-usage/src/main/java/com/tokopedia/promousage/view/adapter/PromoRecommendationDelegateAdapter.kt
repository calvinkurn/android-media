package com.tokopedia.promousage.view.adapter

import android.animation.Animator
import android.graphics.Color
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.toSpannableHtmlString


internal class PromoRecommendationDelegateAdapter(
    private val onClickUsePromoRecommendation: () -> Unit,
    private val onClickPromo: (PromoItem) -> Unit,
    private val onImpressionPromo: (PromoItem) -> Unit,
    private val onClickClose: () -> Unit
) : DelegateAdapter<PromoRecommendationItem, PromoRecommendationDelegateAdapter.ViewHolder>(
    PromoRecommendationItem::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherRecommendationBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: PromoRecommendationItem, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(
        private val binding: PromoUsageItemVoucherRecommendationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val adapter: CompositeAdapter by lazy {
            CompositeAdapter.Builder()
                .add(PromoAccordionItemDelegateAdapter(onClickPromo, onImpressionPromo))
                .build()
        }
        private val layoutManager by lazy { LinearLayoutManager(itemView.context) }

        fun bind(item: PromoRecommendationItem) {
            binding.rvPromoRecommendation.layoutManager = layoutManager
            binding.rvPromoRecommendation.adapter = adapter
            adapter.submit(item.promos)
            val colorHex = item.backgroundColor
            if (colorHex.isNotBlank()) {
                binding.clContainer.setBackgroundColor(
                    Color.parseColor(colorHex)
                )
            }
            binding.ivPromoRecommendationBackground.loadImage(
                url = item.backgroundUrl,
                properties = {
                    isAnimate(false)
                    setPlaceHolder(-1)
                }
            )
            binding.ivPromoRecommendationBackground.imageMatrix = Matrix().apply {
                val screenWidth = getScreenWidth().toFloat()
                val containerHeight = binding.clContainer.height.toFloat()

                val viewHeight = if (containerHeight > screenWidth) {
                    screenWidth
                } else {
                    containerHeight
                }
                val widthScale = screenWidth / screenWidth
                val heightScale = viewHeight / screenWidth
                val scale = widthScale.coerceAtLeast(heightScale)
                postScale(scale, scale)
            }
            binding.ivPromoRecommendationBackground.visible()
            binding.btnRecommendationUseVoucher.setOnClickListener {
                if (!item.isCalculating) {
                    startButtonAnimation {
                        startMessageAnimation(item.messageSelected) {
                            onClickUsePromoRecommendation()
                        }
                    }
                }
            }
            if (item.selectedCodes.containsAll(item.codes)) {
                binding.tpgRecommendationTitle.text =
                    item.messageSelected.toSpannableHtmlString(binding.tpgRecommendationTitle.context)
            } else {
                binding.tpgRecommendationTitle.text =
                    item.message.toSpannableHtmlString(binding.tpgRecommendationTitle.context)
            }
            if (item.codes.size > 1) {
                if (item.selectedCodes.containsAll(item.codes)) {
                    binding.ivCheckmark.visible()
                    binding.ivCheckmarkOutline.invisible()
                } else {
                    binding.ivCheckmark.invisible()
                    binding.ivCheckmarkOutline.invisible()
                }
                if (item.selectedCodes.containsAll(item.codes)) {
                    binding.btnRecommendationUseVoucher.invisible()
                } else {
                    binding.btnRecommendationUseVoucher.visible()
                }
            } else {
                binding.ivCheckmark.invisible()
                binding.ivCheckmarkOutline.invisible()
                binding.btnRecommendationUseVoucher.invisible()
                if (item.selectedCodes.containsAll(item.codes)) {
                    startMessageAnimation(item.messageSelected)
                }
            }
            if (item.showAnimation) {
                showPromoRecommendationAnimation(item)
            }
        }

        private fun showPromoRecommendationAnimation(item: PromoRecommendationItem) {
            LottieCompositionFactory.fromUrl(binding.lottieAnimationView.context, item.animationUrl)
                .addListener { result ->
                    binding.lottieAnimationView.setComposition(result)
                    binding.lottieAnimationView.addAnimatorListener(object :
                        Animator.AnimatorListener {
                        override fun onAnimationStart(animator: Animator) {
                            // no-op
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            // no-op
                        }

                        override fun onAnimationCancel(animator: Animator) {
                            // no-op
                        }

                        override fun onAnimationRepeat(animator: Animator) {
                            // no-op
                        }
                    })
                    binding.lottieAnimationView.playAnimation()
                }
        }

        private fun startButtonAnimation(onCompleted: (() -> Unit)? = null) {
            val shrinkLeftAnimation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.shrink_left)
            val zoomOutAnimation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.zoom_out)
            shrinkLeftAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    binding.btnRecommendationUseVoucher.visible()
                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.btnRecommendationUseVoucher.invisible()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // no-op
                }
            })
            zoomOutAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    binding.ivCheckmark.visible()
                    binding.ivCheckmarkOutline.visible()
                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.ivCheckmarkOutline.invisible()
                    onCompleted?.invoke()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // no-op
                }
            })
            binding.btnRecommendationUseVoucher.startAnimation(shrinkLeftAnimation)
            binding.ivCheckmarkOutline.startAnimation(zoomOutAnimation)
        }

        private fun startMessageAnimation(
            newMessage: String,
            onCompleted: (() -> Unit)? = null
        ) {
            binding.tpgRecommendationTitle.animateGone {
                binding.tpgRecommendationTitle.text =
                    newMessage.toSpannableHtmlString(binding.tpgRecommendationTitle.context)
                binding.tpgRecommendationTitle.animateShow {
                    onCompleted?.invoke()
                }
            }
        }

        private fun View.animateGone(
            duration: Long = 200L,
            onCompleted: (() -> Unit)? = null
        ) {
            animate()
                .alpha(0f)
                .setDuration(duration)
                .withEndAction {
                    onCompleted?.invoke()
                }
        }

        private fun View.animateShow(
            duration: Long = 200L,
            onCompleted: (() -> Unit)? = null
        ) {
            alpha = 0f
            visibility = View.VISIBLE
            animate().alpha(1f)
                .setDuration(duration)
                .withEndAction {
                    onCompleted?.invoke()
                }
        }
    }
}
