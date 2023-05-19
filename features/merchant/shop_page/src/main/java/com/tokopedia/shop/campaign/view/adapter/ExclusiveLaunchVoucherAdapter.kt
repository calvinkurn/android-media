package com.tokopedia.shop.campaign.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
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
    private var onVoucherUseClick: (Int) -> Unit = {}
    private var useDarkBackground = true

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
                setMinimumPurchase(
                    context.getString(
                        R.string.shop_page_placeholder_minimal_purchase,
                        voucher.minimumPurchase.getNumberFormatted()
                    )
                )
                setRemainingQuota(voucher.remainingQuota)
                setVoucherName(voucher.voucherName)

                val isMerchantCreatedVoucher = voucher.source == ExclusiveLaunchVoucher.VoucherSource.MerchantCreated
                val ctaText = getVoucherClaimStatus(context, voucher)
                setPrimaryCta(ctaText, onClick = {
                    if (isMerchantCreatedVoucher) {
                        onVoucherUseClick(bindingAdapterPosition)
                    } else {
                        onVoucherClaimClick(bindingAdapterPosition)
                    }
                })

                if (useDarkBackground) useDarkBackground() else useLightBackground()
            }
        }

    }

    private fun getVoucherClaimStatus(context: Context, voucher: ExclusiveLaunchVoucher): String {
        return when {
            voucher.source is ExclusiveLaunchVoucher.VoucherSource.MerchantCreated -> context.getString(R.string.shop_page_use)
            voucher.source is ExclusiveLaunchVoucher.VoucherSource.Promo && voucher.source.isClaimed -> context.getString(R.string.shop_page_claimed)
            voucher.source is ExclusiveLaunchVoucher.VoucherSource.Promo && !voucher.source.isClaimed -> context.getString(R.string.shop_page_claim)
            else -> context.getString(R.string.shop_page_use)
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

    fun setOnVoucherUseClick(onVoucherUseClick: (Int) -> Unit) {
        this.onVoucherUseClick = onVoucherUseClick
    }

    fun setUseDarkBackground(useDarkBackground: Boolean) {
        this.useDarkBackground = useDarkBackground
    }

    fun getItemAtOrNull(position: Int): ExclusiveLaunchVoucher? {
        return try {
            snapshot()[position]
        } catch (e: Exception) {
            null
        }
    }

    private fun snapshot(): List<ExclusiveLaunchVoucher> {
        return differ.currentList
    }
}
