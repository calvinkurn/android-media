package com.tokopedia.shop.flashsale.presentation.creation.information.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemShopTierBenefitBinding
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemVpsBenefitQuotaEmptyBinding
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemVpsPackageBinding
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.localFormatTo
import com.tokopedia.shop.flashsale.presentation.creation.information.uimodel.VpsPackageUiModel
import com.tokopedia.unifycomponents.Label

class VpsPackageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onVpsPackageClicked: (VpsPackageUiModel) -> Unit = {}

    companion object {
        private const val EMPTY_QUOTA = 0
        private const val SHOP_TIER_BENEFIT_VIEW_TYPE = 1
        private const val VPS_PACKAGE_VIEW_TYPE = 2
        private const val EMPTY_VPS_PACKAGE_VIEW_TYPE = 3
    }

    private val differCallback = object : DiffUtil.ItemCallback<VpsPackageUiModel>() {
        override fun areItemsTheSame(
            oldItem: VpsPackageUiModel,
            newItem: VpsPackageUiModel
        ): Boolean {
            return oldItem.packageId == newItem.packageId
        }

        override fun areContentsTheSame(
            oldItem: VpsPackageUiModel,
            newItem: VpsPackageUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SHOP_TIER_BENEFIT_VIEW_TYPE -> {
                val binding = SsfsItemShopTierBenefitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShopTierBenefitViewHolder(binding)
            }
            VPS_PACKAGE_VIEW_TYPE -> {
                val binding = SsfsItemVpsPackageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                VpsBenefitViewHolder(binding)
            }
            else -> {
                val binding = SsfsItemVpsBenefitQuotaEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyQuotaViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val benefitPackage = differ.currentList[position]
        when (getItemViewType(position)) {
            SHOP_TIER_BENEFIT_VIEW_TYPE -> {
                val shopTierViewHolder = viewHolder as? ShopTierBenefitViewHolder ?: return
                shopTierViewHolder.bind(benefitPackage)
            }
            VPS_PACKAGE_VIEW_TYPE -> {
                val vpsBenefitViewHolder = viewHolder as? VpsBenefitViewHolder ?: return
                vpsBenefitViewHolder.bind(benefitPackage)
            }
            else -> {
                val emptyQuotaViewHolder = viewHolder as? EmptyQuotaViewHolder ?: return
                emptyQuotaViewHolder.bind(benefitPackage)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        val benefitPackage = differ.currentList[position]
        return when {
            benefitPackage.isShopTierBenefit -> SHOP_TIER_BENEFIT_VIEW_TYPE
            benefitPackage.remainingQuota == EMPTY_QUOTA -> EMPTY_VPS_PACKAGE_VIEW_TYPE
            else -> VPS_PACKAGE_VIEW_TYPE
        }
    }


    fun setOnVpsPackageClicked(onVpsPackageClicked: (VpsPackageUiModel) -> Unit) {
        this.onVpsPackageClicked = onVpsPackageClicked
    }

    inner class EmptyQuotaViewHolder(private val binding: SsfsItemVpsBenefitQuotaEmptyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vpsPackage: VpsPackageUiModel) {
            binding.tpgPackageName.text = vpsPackage.packageName
            binding.radioButton.isChecked = false
            binding.root.isEnabled = false
            binding.radioButton.isEnabled = false
            binding.labelRemainingQuota.setLabelType(Label.HIGHLIGHT_LIGHT_RED)
            binding.labelRemainingQuota.text = binding.labelRemainingQuota.context.getString(R.string.sfs_empty_quota)
        }
    }

    inner class ShopTierBenefitViewHolder(private val binding: SsfsItemShopTierBenefitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vpsPackage: VpsPackageUiModel) {
            binding.radioButton.setOnCheckedChangeListener { _, _ -> onVpsPackageClicked(vpsPackage) }
            binding.radioButton.isChecked = vpsPackage.isSelected
            binding.tpgPackageName.text = vpsPackage.packageName
            binding.root.setOnClickListener { onVpsPackageClicked(vpsPackage) }
        }
    }

    inner class VpsBenefitViewHolder(
        private val binding: SsfsItemVpsPackageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(vpsPackage: VpsPackageUiModel) {
            binding.tpgPackageName.text = vpsPackage.packageName
            binding.radioButton.setOnCheckedChangeListener { _, _ -> onVpsPackageClicked(vpsPackage) }
            binding.radioButton.isChecked = vpsPackage.isSelected
            binding.root.isEnabled = !vpsPackage.disabled
            binding.root.setOnClickListener { onVpsPackageClicked(vpsPackage) }
            binding.labelRemainingQuota.setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
            binding.labelRemainingQuota.setRemainingQuota(vpsPackage)
            binding.tpgPeriod.setPeriod(vpsPackage)
        }

        private fun TextView.setRemainingQuota(vpsPackage: VpsPackageUiModel) {
            val remainingQuota = String.format(
                binding.labelRemainingQuota.context.getString(R.string.sfs_placeholder_remaining_vps_quota),
                vpsPackage.currentQuota,
                vpsPackage.originalQuota
            )

            this.text = remainingQuota
        }

        private fun TextView.setPeriod(vpsPackage: VpsPackageUiModel) {
            val period = String.format(
                binding.tpgPeriod.context.getString(R.string.sfs_placeholder_vps_quota_period),
                vpsPackage.packageStartTime.localFormatTo(DateConstant.DATE),
                vpsPackage.packageEndTime.localFormatTo(DateConstant.DATE)
            )
            this.text = period
        }
    }


    fun submit(newGradients: List<VpsPackageUiModel>) {
        differ.submitList(newGradients)
    }

    fun snapshot(): List<VpsPackageUiModel> {
        return differ.currentList
    }
}