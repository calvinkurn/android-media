package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemQuotaMonitoringBinding
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.Constant.DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID
import com.tokopedia.shop.flashsale.common.constant.DateConstant
import com.tokopedia.shop.flashsale.common.extension.epochToDate
import com.tokopedia.shop.flashsale.common.extension.formatTo
import com.tokopedia.shop.flashsale.common.extension.isZero
import com.tokopedia.shop.flashsale.domain.entity.VpsPackage

class QuotaMonitoringAdapter() :
    RecyclerView.Adapter<QuotaMonitoringAdapter.QuotaMonitoringListViewHolder>() {

    private var vpsPackages: MutableList<VpsPackage> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QuotaMonitoringListViewHolder {
        val binding =
            SsfsItemQuotaMonitoringBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return QuotaMonitoringListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return vpsPackages.size
    }

    override fun onBindViewHolder(
        holder: QuotaMonitoringListViewHolder,
        position: Int
    ) {
        val vpsPackage = vpsPackages[position]
        holder.bind(vpsPackage)
    }

    fun clearAll() {
        vpsPackages.clear()
        notifyItemRangeRemoved(Constant.FIRST_PAGE, vpsPackages.size)
    }

    fun submit(newPackages: List<VpsPackage>) {
        val oldItemsSize = vpsPackages.size
        vpsPackages.addAll(newPackages)
        notifyItemRangeChanged(oldItemsSize, vpsPackages.size)
    }

    class QuotaMonitoringListViewHolder(private val binding: SsfsItemQuotaMonitoringBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(vpsPackage: VpsPackage) {
            val remainingQuota = vpsPackage.remainingQuota
            val totalQuota = vpsPackage.originalQuota
            val expireDate =
                if (vpsPackage.packageEndTime.isZero() || vpsPackage.packageId == DEFAULT_SHOP_TIER_BENEFIT_PACKAGE_ID) {
                    itemView.context.getString(R.string.stfs_nothing_label)
                } else {
                    vpsPackage.packageEndTime.epochToDate().formatTo(DateConstant.DATE)
                }

            binding.run {
                tpPackageName.text = vpsPackage.packageName
                tpRemainingQuota.text = itemView.context.getString(
                    R.string.ssfs_remaining_and_total_quota_value_placeholder,
                    remainingQuota,
                    totalQuota
                )
                tpExpire.text = expireDate
            }
        }
    }
}