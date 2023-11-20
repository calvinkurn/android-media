package com.tokopedia.shop.campaign.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
    private var onCtaClick: (Int) -> Unit = {}
    private var useDarkBackground = true
    private var onVoucherImpression: (ExclusiveLaunchVoucher) -> Unit = {}

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
            binding.elVoucher.setOnClickListener { onVoucherClick(bindingAdapterPosition) }
        }

        fun bind(voucher: ExclusiveLaunchVoucher) {
            binding.elVoucher.apply {
                addOnImpressionListener(voucher) { onVoucherImpression(voucher) }
                setMinimumPurchase(
                    context.getString(
                        R.string.shop_page_placeholder_minimal_purchase,
                        voucher.minimumPurchase.getNumberFormatted()
                    )
                )

                setRemainingQuota(voucher.remainingQuota)
                setVoucherName(voucher.voucherName)

                setPrimaryCta(voucherCode = voucher.couponCode, isDisabledButton = voucher.isDisabledButton)

                setOnPrimaryCtaClick {
                    val isVoucherClaimed = voucher.couponCode.isNotEmpty()
                    if (!isVoucherClaimed) {
                        onCtaClick(bindingAdapterPosition)
                    }
                }

                val isClaimCtaDisabled = voucher.isDisabledButton
                if (useDarkBackground) useDarkBackground(isClaimCtaDisabled) else useLightBackground(isClaimCtaDisabled)
            }
        }

    }


    fun submit(newVouchers: List<ExclusiveLaunchVoucher>) {
        differ.submitList(newVouchers)
    }

    fun setOnVoucherClick(onVoucherClick: (Int) -> Unit) {
        this.onVoucherClick = onVoucherClick
    }

    fun setOnPrimaryCtaClick(onCtaClick : (Int) -> Unit) {
        this.onCtaClick = onCtaClick
    }
    fun setOnVoucherImpression(onVoucherImpression: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherImpression = onVoucherImpression
    }

    fun setUseDarkBackground(useDarkBackground: Boolean) {
        this.useDarkBackground = useDarkBackground
    }

    fun getItemAtOrNull(position: Int): ExclusiveLaunchVoucher? {
        return try {
            snapshot()[position]
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    private fun snapshot(): List<ExclusiveLaunchVoucher> {
        return differ.currentList
    }
}
