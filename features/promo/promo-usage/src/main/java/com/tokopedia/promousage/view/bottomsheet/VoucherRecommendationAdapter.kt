package com.tokopedia.promousage.view.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.databinding.PromoUsageItemVoucherBinding

class VoucherRecommendationAdapter : RecyclerView.Adapter<VoucherRecommendationAdapter.ViewHolder>() {

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
    private var onVoucherClick: (Int) -> Unit = {}

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

        init {
            binding.root.setOnClickListener { onVoucherClick(bindingAdapterPosition) }
        }

        fun bind(voucher: Voucher) {
            binding.voucherView.bind(voucher)
        }

    }

    fun getItemAtOrNull(position: Int): Voucher? {
        return try {
            snapshot()[position]
        } catch (e: Exception) {
            null
        }
    }

    fun snapshot(): List<Voucher> {
        return differ.currentList
    }
    fun submit(newVouchers: List<Voucher>) {
        differ.submitList(newVouchers)
    }

    fun setOnVoucherClick(onVoucherClick: (Int) -> Unit) {
        this.onVoucherClick = onVoucherClick
    }

}
