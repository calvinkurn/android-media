package com.tokopedia.shop.flashsale.presentation.creation.information.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemVpsPackageBinding
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel

class VpsPackageAdapter : RecyclerView.Adapter<VpsPackageAdapter.ViewHolder>() {

    private var onVpsPackageClicked: (VpsPackageUiModel) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<VpsPackageUiModel>() {
        override fun areItemsTheSame(oldItem: VpsPackageUiModel, newItem: VpsPackageUiModel): Boolean {
            return oldItem.isSelected == newItem.isSelected
        }

        override fun areContentsTheSame(oldItem: VpsPackageUiModel, newItem: VpsPackageUiModel): Boolean {
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

    fun setOnVpsPackageClicked(onVpsPackageClicked: (VpsPackageUiModel) -> Unit) {
        this.onVpsPackageClicked = onVpsPackageClicked
    }

    inner class ViewHolder(
        private val binding: SsfsItemVpsPackageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vpsPackage: VpsPackageUiModel) {
            binding.radioButton.isSelected =  vpsPackage.isSelected

            binding.tpgProductName.text = vpsPackage.packageName

            binding.tpgRemainingQuota.setPackageRemainingQuota(vpsPackage)

            binding.tpgPeriod.setPackagePeriod(vpsPackage)
            binding.tpgPeriod.isVisible = !vpsPackage.isShopTierBenefit

            binding.root.setOnClickListener { onVpsPackageClicked(vpsPackage) }
        }

        private fun AppCompatTextView.setPackageRemainingQuota(vpsPackage: VpsPackageUiModel) {
            val quota = String.format(
                context.getString(R.string.sfs_placeholder_remaining_vps_quota),
                vpsPackage.currentQuota,
                vpsPackage.originalQuota
            )
            this.text = quota
        }

        private fun AppCompatTextView.setPackagePeriod(vpsPackage: VpsPackageUiModel) {
            val quota = String.format(
                context.getString(R.string.sfs_placeholder_vps_quota_period),
                vpsPackage.formattedPackageStartTime,
                vpsPackage.formattedPackageEndTime
            )
            this.text = quota
        }
    }

    fun submit(newGradients: List<VpsPackageUiModel>) {
        differ.submitList(newGradients)
    }

    fun snapshot(): List<VpsPackageUiModel> {
        return differ.currentList
    }
}