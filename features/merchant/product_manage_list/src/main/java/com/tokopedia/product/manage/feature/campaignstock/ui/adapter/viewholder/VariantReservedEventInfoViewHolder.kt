package com.tokopedia.product.manage.feature.campaignstock.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.databinding.ItemCampaignStockReservedInfoVariantBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.bottomsheet.VariantReservedEventInfoBottomSheet
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedEventInfoUiModel
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.VariantReservedEventInfoUiModel
import com.tokopedia.utils.view.binding.viewBinding

class VariantReservedEventInfoViewHolder(itemView: View?) :
    AbstractViewHolder<VariantReservedEventInfoUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_campaign_stock_reserved_info_variant
    }

    private val binding by viewBinding<ItemCampaignStockReservedInfoVariantBinding>()

    override fun bind(element: VariantReservedEventInfoUiModel) {
        binding?.run {
            tvCampaignReservedInfoVariantName.text = element.variantName
            tvCampaignReservedInfoVariantCount.run {
                text = itemView.context.getString(
                    com.tokopedia.product.manage.common.R.string.product_manage_campaign_count,
                    element.totalCampaign.orZero()
                )
                setOnClickListener {
                    showVariantReservedEventInfoBottomSheet(
                        element.variantName,
                        element.reservedEventInfos
                    )
                }
            }
            tvCampaignReservedInfoVariantStockCount.text = element.totalStock.toString()
        }
    }

    private fun showVariantReservedEventInfoBottomSheet(
        variantName: String,
        reservedEventInfos: MutableList<ReservedEventInfoUiModel>
    ) {
        VariantReservedEventInfoBottomSheet.createInstance(
            itemView.context,
            variantName,
            ArrayList(reservedEventInfos)
        ).run {
            show(childFragmentManager)
        }
    }

}