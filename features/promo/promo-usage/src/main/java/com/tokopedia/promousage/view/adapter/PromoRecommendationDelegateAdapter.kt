package com.tokopedia.promousage.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.PromoUsageItemPromoBinding
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.domain.entity.list.PromoRecommendationItem
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.applyPaddingToLastItem

class PromoRecommendationDelegateAdapter(
    private val onVoucherClick: (PromoItem) -> Unit,
    private val onButtonUseRecommendedVoucherClick: (PromoRecommendationItem) -> Unit
) : DelegateAdapter<PromoRecommendationItem, PromoRecommendationDelegateAdapter.ViewHolder>(
    PromoRecommendationItem::class.java
) {

    companion object {
        private const val PADDING_BOTTOM_DP = 16
    }

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

//        init {
//            binding.rvPromoRecommendation.applyPaddingToLastItem(PADDING_BOTTOM_DP)
//        }

        fun bind(item: PromoRecommendationItem) {
//            val voucherAdapter = VoucherAdapter(onVoucherClick)
            val selectedRecommendedPromoCount = item.promos.count { it.state is PromoItemState.Selected }
            val recommendedPromoCount = item.promos.size
            binding.tpgRecommendationTitle.text = item.title
            binding.btnRecommendationUseVoucher.setOnClickListener {
                onButtonUseRecommendedVoucherClick(item)
                binding.btnRecommendationUseVoucher.gone()
                binding.ivCheckmark.visible()
                binding.ivCheckmarkOutline.visible()
                startButtonUseVoucherAnimation()
            }
//            binding.rvPromoRecommendation.apply {
//                layoutManager = LinearLayoutManager(binding.rvPromoRecommendation.context)
//                adapter = voucherAdapter
//            }
            binding.btnRecommendationUseVoucher.isVisible = selectedRecommendedPromoCount < recommendedPromoCount
            binding.ivCheckmark.isVisible = selectedRecommendedPromoCount == recommendedPromoCount
            binding.ivCheckmarkOutline.isVisible = selectedRecommendedPromoCount == recommendedPromoCount
//            voucherAdapter.submit(item.promos)
        }

        private fun startButtonUseVoucherAnimation() {
            val shrinkOutAnimation =
                AnimationUtils.loadAnimation(binding.ivCheckmarkOutline.context, R.anim.shrink_out)
            shrinkOutAnimation.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    binding.ivCheckmarkOutline.gone()
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
            binding.ivCheckmarkOutline.startAnimation(shrinkOutAnimation)

//            val zoomInAnimation =
//                AnimationUtils.loadAnimation(binding.rvPromoRecommendation.context, R.anim.zoom_in)
//            binding.rvPromoRecommendation.startAnimation(zoomInAnimation)
        }
    }

    inner class VoucherAdapter(
        private val onVoucherClick: (PromoItem) -> Unit
    ) : RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

        private val differCallback = object : DiffUtil.ItemCallback<PromoItem>() {
            override fun areItemsTheSame(
                oldItem: PromoItem,
                newItem: PromoItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PromoItem,
                newItem: PromoItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        private val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = PromoUsageItemPromoBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(differ.currentList[position])
        }

        inner class ViewHolder(
            private val binding: PromoUsageItemPromoBinding
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(item: PromoItem) {
                binding.vcvPromo.bind(item)
                binding.root.setOnClickListener { onVoucherClick(item) }
            }
        }

        fun submit(promos: List<PromoItem>) {
            differ.submitList(promos)
        }
    }
}
