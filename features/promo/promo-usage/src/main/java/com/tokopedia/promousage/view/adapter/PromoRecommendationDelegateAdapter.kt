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
import com.tokopedia.unifycomponents.HtmlLinkHelper

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
            binding.btnRecommendationUseVoucher.setOnClickListener {
                startButtonAnimation {
                    startMessageAnimation(item.messageSelected) {
                        onClickUsePromoRecommendation()
                    }
                }
            }
            if (item.selectedCodes.containsAll(item.codes)) {
                binding.tpgRecommendationTitle.text =
                    HtmlLinkHelper(binding.tpgRecommendationTitle.context, item.messageSelected).spannedString
            } else {
                binding.tpgRecommendationTitle.text =
                    HtmlLinkHelper(binding.tpgRecommendationTitle.context, item.message).spannedString
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
                    HtmlLinkHelper(binding.tpgRecommendationTitle.context, newMessage).spannedString
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
