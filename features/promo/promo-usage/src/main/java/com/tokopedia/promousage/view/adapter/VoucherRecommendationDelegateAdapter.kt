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
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherRecommendationBinding
import com.tokopedia.promousage.domain.entity.list.Voucher
import com.tokopedia.promousage.domain.entity.list.VoucherRecommendation
import com.tokopedia.promousage.util.composite.DelegateAdapter
import com.tokopedia.promousage.util.extension.applyPaddingToLastItem
import com.tokopedia.promousage.R

class VoucherRecommendationDelegateAdapter(
    private val onVoucherClick: (Voucher) -> Unit,
    private val onButtonUseRecommendedVoucherClick: () -> Unit
) : DelegateAdapter<VoucherRecommendation, VoucherRecommendationDelegateAdapter.ViewHolder>(VoucherRecommendation::class.java) {

    companion object {
        private const val PADDING_BOTTOM_DP = 16
        private const val ONE_VOUCHER = 1
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PromoUsageItemVoucherRecommendationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(item: VoucherRecommendation, viewHolder: ViewHolder) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: PromoUsageItemVoucherRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerView.applyPaddingToLastItem(PADDING_BOTTOM_DP)
            binding.btnRecommendationUseVoucher.setOnClickListener {
                onButtonUseRecommendedVoucherClick()
                binding.btnRecommendationUseVoucher.gone()
                binding.ivCheckmark.visible()
                binding.ivCheckmarkOutline.visible()
                val shrinkOutAnimation = AnimationUtils.loadAnimation(binding.ivCheckmarkOutline.context, R.anim.shrink_out)
                shrinkOutAnimation.setAnimationListener(object : AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {}

                    override fun onAnimationEnd(p0: Animation?) {
                        binding.ivCheckmarkOutline.gone()
                    }

                    override fun onAnimationRepeat(p0: Animation?) {}

                })
                binding.ivCheckmarkOutline.startAnimation(shrinkOutAnimation)


                val zoomInAnimation = AnimationUtils.loadAnimation(binding.recyclerView.context, R.anim.zoom_in)
                binding.recyclerView.startAnimation(zoomInAnimation)
            }
        }

        fun bind(recommendation: VoucherRecommendation) {
            val voucherAdapter = VoucherAdapter(onVoucherClick)

            binding.tpgRecommendationTitle.text = recommendation.title
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(binding.recyclerView.context)
                adapter = voucherAdapter
            }

            binding.btnRecommendationUseVoucher.isVisible = recommendation.vouchers.size > ONE_VOUCHER

            voucherAdapter.submit(recommendation.vouchers)
        }

    }
    inner class VoucherAdapter(private val onVoucherClick: (Voucher) -> Unit) : RecyclerView.Adapter<VoucherAdapter.ViewHolder>() {

        private val differCallback = object : DiffUtil.ItemCallback<Voucher>() {
            override fun areItemsTheSame(
                oldItem: Voucher,
                newItem: Voucher
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Voucher,
                newItem: Voucher
            ): Boolean {
                return oldItem == newItem
            }
        }

        private val differ = AsyncListDiffer(this, differCallback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = PromoUsageItemVoucherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(differ.currentList[position])
        }

        inner class ViewHolder(private val binding: PromoUsageItemVoucherBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(voucher: Voucher) {
                binding.voucherView.bind(voucher)
                binding.root.setOnClickListener {
                    onVoucherClick(voucher)
                }
            }

        }
        fun submit(newVouchers: List<Voucher>) {
            differ.submitList(newVouchers)
        }
    }

}
