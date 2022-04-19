package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.manage.databinding.ItemCampaignStockVariantNameBinding
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockProductModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.convertCheckMaximumStockLimit

class VariantProductStockAccordionAdapter(private val variantProductList: List<ReservedStockProductModel>) :
    RecyclerView.Adapter<VariantProductStockAccordionAdapter.VariantProductStockAccordionViewHolder>() {

    inner class VariantProductStockAccordionViewHolder(val binding: ItemCampaignStockVariantNameBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VariantProductStockAccordionViewHolder {
        val binding = ItemCampaignStockVariantNameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VariantProductStockAccordionViewHolder(binding)
    }

    override fun getItemCount(): Int = variantProductList.size

    override fun onBindViewHolder(holder: VariantProductStockAccordionViewHolder, position: Int) {
        variantProductList[position].let { model ->
            holder.binding.run {
                tvCampaignStockVariantName.text = model.productName
                tvCampaignStockVariantCount.text =
                    model.stock.convertCheckMaximumStockLimit(holder.itemView.context)
                model.description.let { desc ->
                    if (desc.isNotEmpty()) {
                        tvCampaignStockVariantDescription.run {
                            text = desc
                            visible()
                        }
                    } else {
                        tvCampaignStockVariantDescription.gone()
                    }
                }
            }
        }
    }
}