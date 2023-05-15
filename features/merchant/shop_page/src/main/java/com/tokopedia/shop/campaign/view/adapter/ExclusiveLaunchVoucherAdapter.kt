package com.tokopedia.shop.campaign.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.databinding.ItemExclusiveLaunchVoucherBinding
import com.tokopedia.shop.R

class ExclusiveLaunchVoucherAdapter :
    RecyclerView.Adapter<ExclusiveLaunchVoucherAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ExclusiveLaunchVoucher>() {
        override fun areItemsTheSame(
            oldItem: ExclusiveLaunchVoucher,
            newItem: ExclusiveLaunchVoucher
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExclusiveLaunchVoucher,
            newItem: ExclusiveLaunchVoucher
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
    private var onVoucherClick: (Int) -> Unit = {}
    private var onVoucherClaimClick: (Int) -> Unit = {}
    private var onUseVoucherClick: (Int) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemExclusiveLaunchVoucherBinding.inflate(
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

    inner class ViewHolder(private val binding: ItemExclusiveLaunchVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.elVoucher.setOnPrimaryCtaClick { onVoucherClaimClick(bindingAdapterPosition) }
            binding.root.setOnClickListener { onVoucherClick(bindingAdapterPosition) }
        }

        fun bind(voucher: ExclusiveLaunchVoucher) {
            binding.elVoucher.apply {
                setMinimumPurchase(voucher.minimumPurchase)
                setRemainingQuota(voucher.remainingQuota)
                setVoucherName(voucher.benefit, voucher.benefitMax)
                val ctaText = if (voucher.isClaimed) binding.root.context.getString(R.string.shop_page_use) else binding.root.context.getString(R.string.shop_page_claim)
                setPrimaryCta(ctaText, onClick = {
                    if (voucher.isClaimed) {
                        onUseVoucherClick(bindingAdapterPosition)
                    } else {
                        onVoucherClaimClick(bindingAdapterPosition)
                    }
                })
            }
        }

    }

    fun submit(newVouchers: List<ExclusiveLaunchVoucher>) {
        differ.submitList(newVouchers)
    }

    fun setOnVoucherClick(onVoucherClick: (Int) -> Unit) {
        this.onVoucherClick = onVoucherClick
    }

    fun setOnVoucherClaimClick(onVoucherClaimClick: (Int) -> Unit) {
        this.onVoucherClaimClick = onVoucherClaimClick
    }

    fun setOnUseVoucherClick(onUseVoucherClick: (Int) -> Unit) {
        this.onUseVoucherClick = onUseVoucherClick
    }

    fun snapshot(): List<ExclusiveLaunchVoucher> {
        return differ.currentList
    }
}
