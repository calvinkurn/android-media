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
import com.tokopedia.unifycomponents.Label

class VpsPackageAdapter : RecyclerView.Adapter<VpsPackageAdapter.ViewHolder>() {

    private var onVpsPackageClicked: (VpsPackageUiModel) -> Unit = {}

    companion object {
        private const val EMPTY_QUOTA = 0
    }

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
            binding.tpgPackageName.text = vpsPackage.packageName
            handleShopBenefit(vpsPackage)
            handleRemainingQuota(vpsPackage)
            binding.root.setOnClickListener { onVpsPackageClicked(vpsPackage) }
        }

        private fun handleShopBenefit(vpsPackage: VpsPackageUiModel) {
            if (vpsPackage.isShopTierBenefit) {
                handleShopTierBenefit()
            } else {
                handleVpsPackageBenefit(vpsPackage)
            }

        }

        private fun handleShopTierBenefit() {
            binding.labelRemainingQuota.gone()
            binding.tpgPeriod.gone()
        }

        private fun handleVpsPackageBenefit(vpsPackage: VpsPackageUiModel) {
            val remainingQuota = String.format(
                binding.labelRemainingQuota.context.getString(R.string.sfs_placeholder_vps_quota_period),
                vpsPackage.packageStartTime.formatTo(DateConstant.DATE),
                vpsPackage.packageEndTime.formatTo(DateConstant.DATE)
            )
            binding.labelRemainingQuota.text = remainingQuota

            binding.labelRemainingQuota.visible()
            binding.tpgPeriod.visible()
        }

        private fun handleRemainingQuota(vpsPackage: VpsPackageUiModel) {
            if (vpsPackage.currentQuota == EMPTY_QUOTA) {
                binding.labelRemainingQuota.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                binding.labelRemainingQuota.text = binding.labelRemainingQuota.context.getString(R.string.sfs_empty_quota)
                binding.tpgPeriod.gone()
            } else {
                binding.labelRemainingQuota.setLabelType(Label.HIGHLIGHT_LIGHT_RED)
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