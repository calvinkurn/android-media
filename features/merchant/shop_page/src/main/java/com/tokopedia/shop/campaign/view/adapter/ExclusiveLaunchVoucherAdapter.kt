package com.tokopedia.shop.campaign.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.databinding.ItemExclusiveLaunchVoucherBinding

class ExclusiveLaunchVoucherAdapter :
    RecyclerView.Adapter<ExclusiveLaunchVoucherAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ExclusiveLaunchVoucher>() {
        override fun areItemsTheSame(
            oldItem: ExclusiveLaunchVoucher,
            newItem: ExclusiveLaunchVoucher
        ): Boolean {
            return oldItem.voucherId == newItem.voucherId
        }

        override fun areContentsTheSame(
            oldItem: ExclusiveLaunchVoucher,
            newItem: ExclusiveLaunchVoucher
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
    private var onVoucherClick: (ExclusiveLaunchVoucher) -> Unit = {}
    private var onVoucherClaimClick: (ExclusiveLaunchVoucher) -> Unit = {}

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
            /*   binding.tpgUpdateVariant.setOnClickListener { onCtaChangeVariantClick(bindingAdapterPosition) }
               binding.layoutVariant.setOnClickListener { onVariantClick(bindingAdapterPosition) }*/
        }

        fun bind(item: ExclusiveLaunchVoucher) {
            with(binding) {

            }
        }

    }

    fun submit(newVariants: List<ExclusiveLaunchVoucher>) {
        differ.submitList(newVariants)
    }

    fun setOnVoucherClick(onVoucherClick: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherClick = onVoucherClick
    }

    fun setOnVoucherClaimClick(onVoucherClaimClick: (ExclusiveLaunchVoucher) -> Unit) {
        this.onVoucherClaimClick = onVoucherClaimClick
    }

    fun snapshot(): List<ExclusiveLaunchVoucher> {
        return differ.currentList
    }
}
