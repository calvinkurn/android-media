package com.tokopedia.promousage.view.adapter

import android.animation.Animator
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.wrapper.MediaCacheStrategy
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.CompositeAdapter
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.composite.DelegatePayload
import com.tokopedia.promousage.util.extension.toSpannableHtmlString
import timber.log.Timber
import kotlin.math.ceil

internal class PromoRecommendationDelegateAdapter(
    private val onClickUsePromoRecommendation: () -> Unit,
    private val onClickPromo: (PromoItem) -> Unit,
    private val onImpressionPromo: (PromoItem) -> Unit,
    private val onRecommendationAnimationEnd: () -> Unit,
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

    override fun bindViewHolder(
        item: PromoRecommendationItem,
        viewHolder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        val payload = payloads
            .firstOrNull { it is DelegatePayload.UpdatePromoRecommendation }
            as? DelegatePayload.UpdatePromoRecommendation
        val isReload = payload?.isReload ?: true
        val isPromoStateUpdated = payload?.isPromoStateUpdated ?: true
        val isPromoStartAnimation = payload?.isPromoStartAnimating ?: false
        viewHolder.bind(
            item = item,
            isReload = isReload,
            isPromoStateUpdated = isPromoStateUpdated,
            isStartAnimating = isPromoStartAnimation
        )
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

        fun bind(
            item: PromoRecommendationItem,
            isReload: Boolean = true,
            isPromoStateUpdated: Boolean = true,
            isStartAnimating: Boolean = false
        ) {
            if (isReload) {
                setupRecyclerView()
                renderBackground(item)
            }
            if (isPromoStateUpdated) {
                submitItems(item)
                renderContent(item, isStartAnimating)
                setupListener(item)
            }
        }

        private fun setupRecyclerView() {
            binding.rvPromoRecommendation.layoutManager = layoutManager
            binding.rvPromoRecommendation.adapter = adapter
        }

        private fun submitItems(item: PromoRecommendationItem) {
            adapter.submit(item.promos)
        }

        private fun renderBackground(item: PromoRecommendationItem) {
            val colorHex = item.backgroundColor
            if (colorHex.isNotBlank()) {
                binding.clContainer.setBackgroundColor(
                    Color.parseColor(colorHex)
                )
            }
            setImageBackgroundMaxHeight(getScreenHeight())
            setImageBackground(item.backgroundUrl)
            binding.ivPromoRecommendationBackground.visible()
        }

        private fun renderContent(
            item: PromoRecommendationItem,
            isStartAnimating: Boolean = false
        ) {
            if (item.selectedCodes.containsAll(item.codes)) {
                binding.tpgRecommendationTitle.text =
                    item.messageSelected.toSpannableHtmlString(binding.tpgRecommendationTitle.context)
            } else {
                binding.tpgRecommendationTitle.text =
                    item.message.toSpannableHtmlString(binding.tpgRecommendationTitle.context)
            }
            if (item.codes.size > 1) {
                if (!item.showAnimation || isStartAnimating) {
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

        private fun setupListener(item: PromoRecommendationItem) {
            binding.btnRecommendationUseVoucher.setOnClickListener {
                if (!item.isCalculating) {
                    startButtonAnimation {
                        startMessageAnimation(item.messageSelected)
                    }
                    onClickUsePromoRecommendation()
                }
            }
            binding.btnBottomSheetHeaderClose.setOnClickListener {
                onClickClose()
            }
        }

        private fun setImageBackground(imageUrl: String) {
            try {
                binding.ivPromoRecommendationBackground.loadImage(imageUrl) {
                    setCacheStrategy(MediaCacheStrategy.ALL)
                    listener(
                        onSuccess = { bitmap, _ ->
                            bitmap?.let {
                                val resource = it.toDrawable(binding.ivPromoRecommendationBackground.resources)
                                val containerWidth = binding.ivPromoRecommendationBackground
                                    .measuredWidth.toFloat()
                                val containerHeight = binding.ivPromoRecommendationBackground
                                    .measuredHeight.toFloat()
                                val wScale = containerWidth.div(resource.intrinsicWidth.toFloat())
                                var hScale = containerHeight.div(resource.intrinsicHeight.toFloat())
                                hScale = hScale.coerceAtMost(wScale)

                                var maxHeight = ceil(resource.intrinsicHeight * hScale)
                                maxHeight = maxHeight.coerceAtMost(getScreenHeight().toFloat())
                                if (containerHeight > maxHeight) {
                                    setImageBackgroundMaxHeight(maxHeight.toInt())
                                }

                                binding.ivPromoRecommendationBackground.scaleType =
                                    ImageView.ScaleType.MATRIX
                                binding.ivPromoRecommendationBackground.imageMatrix =
                                    Matrix().apply {
                                        postScale(wScale, hScale)
                                    }
                            }
                        },
                        onError = {
                            setImageBackgroundDefault(imageUrl)
                        }
                    )
                }
            } catch (e: Exception) {
                Timber.e(e)
                setImageBackgroundDefault(imageUrl)
            }
        }

        private fun setImageBackgroundDefault(imageUrl: String) {
            setImageBackgroundMaxHeight(getScreenHeight())
            binding.ivPromoRecommendationBackground.gone()
        }

        private fun setImageBackgroundMaxHeight(maxHeight: Int) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.clContainer)
            constraintSet.constrainMaxHeight(
                R.id.ivPromoRecommendationBackground,
                maxHeight
            )
            constraintSet.applyTo(binding.clContainer)
        }

        private fun showPromoRecommendationAnimation(item: PromoRecommendationItem) {
            LottieCompositionFactory.fromUrl(binding.lottieAnimationView.context, item.animationUrl)
                .addListener { result ->
                    binding.lottieAnimationView.setComposition(result)
                    binding.lottieAnimationView.removeAllAnimatorListeners()
                    binding.lottieAnimationView.addAnimatorListener(object :
                            Animator.AnimatorListener {
                            override fun onAnimationStart(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationEnd(animator: Animator) {
                                onRecommendationAnimationEnd()
                            }

                            override fun onAnimationCancel(animator: Animator) {
                                // no-op
                            }

                            override fun onAnimationRepeat(animator: Animator) {
                                // no-op
                            }
                        })
                    setLottieScaling(result)
                    binding.lottieAnimationView.playAnimation()
                }
        }

        private fun setLottieScaling(composition: LottieComposition) {
            try {
                val containerWidth = binding.lottieAnimationView
                    .width.toFloat()
                val containerHeight = binding.lottieAnimationView
                    .height.toFloat()
                val wScale = containerWidth.div(composition.bounds.width().toFloat())
                var hScale = containerHeight.div(composition.bounds.height().toFloat())
                hScale = hScale.coerceAtMost(wScale)

                var maxHeight = ceil(composition.bounds.height() * hScale)
                maxHeight = maxHeight.coerceAtMost(getScreenHeight().toFloat())
                if (containerHeight > maxHeight) {
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(binding.clContainer)
                    constraintSet.constrainMaxHeight(R.id.lottieAnimationView, maxHeight.toInt())
                    constraintSet.applyTo(binding.clContainer)
                }

                binding.lottieAnimationView.scaleType =
                    ImageView.ScaleType.MATRIX
                binding.lottieAnimationView.imageMatrix =
                    Matrix().apply {
                        postScale(wScale, hScale)
                    }
                binding.lottieAnimationView.scaleX = wScale
                binding.lottieAnimationView.scaleY = wScale
            } catch (e: Exception) {
                Timber.e(e)
                setLottieScalingDefault()
            }
        }

        private fun setLottieScalingDefault() {
            binding.lottieAnimationView.scaleType = ImageView.ScaleType.CENTER_CROP
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
