package com.tokopedia.shop.flashsale.presentation.creation.information.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemVpsPackageBinding
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel

class VpsPackageAdapter : RecyclerView.Adapter<VpsPackageAdapter.ViewHolder>() {

    private var onVpsPackageClicked: (VpsPackageUiModel) -> Unit = {}

    private val differCallback = object : DiffUtil.ItemCallback<VpsPackageUiModel>() {
        override fun areItemsTheSame(oldItem: VpsPackageUiModel, newItem: VpsPackageUiModel): Boolean {
            return oldItem.packageId == newItem.packageId
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
            binding.radioButton.isChecked = vpsPackage.isSelected
            binding.tpgProductName.text = vpsPackage.packageName
            handleShopBenefit(vpsPackage)
            binding.root.setOnClickListener { onVpsPackageClicked(vpsPackage) }
        }

        private fun handleShopBenefit(vpsPackage: VpsPackageUiModel) {
            if (vpsPackage.isShopTierBenefit) {
                val startPeriod = vpsPackage.packageStartTime.formatTo(DateConstant.MONTH)
                val endPeriod = vpsPackage.packageEndTime.formatTo(DateConstant.MONTH_YEAR)
                val period = String.format(
                    binding.tpgRemainingQuota.context.getString(R.string.sfs_placeholder_shop_tier_vps_quota_period),
                    startPeriod,
                    endPeriod
                )
                binding.tpgRemainingQuota.text = period
                binding.tpgPeriod.gone()
            } else {
                val remainingQuota = String.format(
                    binding.tpgRemainingQuota.context.getString(R.string.sfs_placeholder_vps_quota_period),
                    vpsPackage.formattedPackageStartTime,
                    vpsPackage.formattedPackageEndTime
                )
                binding.tpgRemainingQuota.text = remainingQuota
                binding.tpgRemainingQuota.visible()
                binding.tpgPeriod.visible()
            }
        }
    }

    fun submit(newGradients: List<VpsPackageUiModel>) {
        differ.submitList(newGradients)
    }

    fun snapshot(): List<VpsPackageUiModel> {
        return differ.currentList
    }
}