package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.unifyprinciples.UnifyMotion

class PromoRecommendationDelegateAdapter(
    private val onClickUsePromoRecommendation: () -> Unit
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

        fun bind(item: PromoRecommendationItem) {
            val selectedRecommendedPromoCount = item.selectedCodes.size
            val recommendedPromoCount = item.codes.size
            binding.btnRecommendationUseVoucher.setOnClickListener {
                hideMessage {
                    showMessage(item.messageSelected)
                }
                startButtonAnimation()
            }
            if (item.selectedCodes.containsAll(item.codes)) {
                binding.tpgRecommendationTitle.text = item.messageSelected
                binding.ivCheckmark.visible()
                binding.ivCheckmarkOutline.invisible()
            } else {
                binding.tpgRecommendationTitle.text = item.message
                binding.ivCheckmark.invisible()
                binding.ivCheckmarkOutline.invisible()
            }
            if (selectedRecommendedPromoCount < recommendedPromoCount) {
                binding.btnRecommendationUseVoucher.visible()
            } else {
                binding.btnRecommendationUseVoucher.invisible()
            }
        }

        private fun hideMessage(onCompleted: (() -> Unit)?) {
            val fadeOutAnimation = AlphaAnimation(1f, 0f)
            fadeOutAnimation.interpolator = UnifyMotion.EASE_IN_OUT
            fadeOutAnimation.duration = UnifyMotion.T2
            fadeOutAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // no-op
                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.tpgRecommendationTitle.invisible()
                    onCompleted?.invoke()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // no-op
                }
            })
            binding.tpgRecommendationTitle.startAnimation(fadeOutAnimation)
        }

        private fun showMessage(message: String) {
            val fadeInAnimation = AlphaAnimation(0f, 1f)
            fadeInAnimation.interpolator = UnifyMotion.EASE_IN_OUT
            fadeInAnimation.duration = UnifyMotion.T2
            fadeInAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // no-op
                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.tpgRecommendationTitle.text = message
                    binding.tpgRecommendationTitle.visible()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // no-op
                }
            })
            binding.tpgRecommendationTitle.startAnimation(fadeInAnimation)
        }

        private fun startButtonAnimation() {
            val shrinkLeftAnimation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.shrink_left)
            val zoomOutAnimation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.zoom_out)
            shrinkLeftAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    binding.btnRecommendationUseVoucher.visible()
                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.btnRecommendationUseVoucher.invisible()
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    // no-op
                }
            })
            zoomOutAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    binding.ivCheckmark.visible()
                    binding.ivCheckmarkOutline.visible()
                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.ivCheckmarkOutline.invisible()
                    onClickUsePromoRecommendation()
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    // no-op
                }
            })
            binding.btnRecommendationUseVoucher.startAnimation(shrinkLeftAnimation)
            binding.ivCheckmarkOutline.startAnimation(zoomOutAnimation)
        }
    }
}
