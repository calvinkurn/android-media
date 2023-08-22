package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
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
                binding.btnRecommendationUseVoucher.invisible()
                binding.ivCheckmark.visible()
                binding.ivCheckmarkOutline.visible()
                onClickUsePromoRecommendation()
                startButtonUseVoucherAnimation()
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

        private fun startButtonUseVoucherAnimation() {
            val shrinkOutAnimation =
                AnimationUtils.loadAnimation(binding.ivCheckmarkOutline.context, R.anim.shrink_out)
            shrinkOutAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {
                    // no-op
                }

                override fun onAnimationEnd(p0: Animation?) {
                    binding.ivCheckmarkOutline.invisible()
                }

                override fun onAnimationRepeat(p0: Animation?) {
                    // no-op
                }
            })
            binding.ivCheckmarkOutline.startAnimation(shrinkOutAnimation)
        }
    }
}
