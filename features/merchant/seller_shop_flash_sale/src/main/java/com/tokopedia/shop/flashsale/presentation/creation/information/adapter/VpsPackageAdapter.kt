package com.tokopedia.shop.flashsale.presentation.creation.information.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemVpsPackageBinding
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage
import com.tokopedia.seller_shop_flash_sale.R

class VpsPackageAdapter : RecyclerView.Adapter<VpsPackageAdapter.ViewHolder>() {

    private var onVpsPackageClicked: (VpsPackage) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<VpsPackage>() {
        override fun areItemsTheSame(oldItem: VpsPackage, newItem: VpsPackage): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItem: VpsPackage, newItem: VpsPackage): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SsfsItemVpsPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun setOnVpsPackageClicked(onVpsPackageClicked: (VpsPackage) -> Unit) {
        this.onVpsPackageClicked = onVpsPackageClicked
    }

    inner class ViewHolder(
        private val binding: SsfsItemVpsPackageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vpsPackage: VpsPackage) {
            binding.radioButton.isSelected =  vpsPackage.isSelected
            binding.tpgProductName.text = vpsPackage.packageName
            binding.tpgRemainingQuota.setRemainingQuota(vpsPackage)
            binding.root.setOnClickListener { onVpsPackageClicked(vpsPackage) }
        }

        private fun AppCompatTextView.setRemainingQuota(vpsPackage: VpsPackage) {
            val quota = String.format(
                context.getString(R.string.sfs_placeholder_remaining_vps_quota),
                vpsPackage.currentQuota,
                vpsPackage.originalQuota
            )
            this.text = quota
        }
    }

    fun submit(newGradients: List<VpsPackage>) {
        differ.submitList(newGradients)
    }

    fun snapshot(): List<VpsPackage> {
        return differ.currentList
    }
}