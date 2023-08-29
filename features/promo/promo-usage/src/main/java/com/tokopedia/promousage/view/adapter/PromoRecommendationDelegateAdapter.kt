package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                startButtonAnimation(item)
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

        private fun startButtonAnimation(item: PromoRecommendationItem) {
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
                    startMessageAnimation(item)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // no-op
                }
            })
            binding.btnRecommendationUseVoucher.startAnimation(shrinkLeftAnimation)
            binding.ivCheckmarkOutline.startAnimation(zoomOutAnimation)
        }

        private fun startMessageAnimation(item: PromoRecommendationItem) {
            binding.tpgRecommendationTitle.animateGone {
                binding.tpgRecommendationTitle.text = item.messageSelected
                binding.tpgRecommendationTitle.animateShow {
                    onClickUsePromoRecommendation()
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
