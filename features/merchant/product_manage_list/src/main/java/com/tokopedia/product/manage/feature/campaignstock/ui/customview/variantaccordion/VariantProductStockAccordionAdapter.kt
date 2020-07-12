package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import kotlinx.android.synthetic.main.item_campaign_stock_variant_name.view.*

class VariantProductStockAccordionAdapter(private val variantProductList: List<VariantProductStockNameUiModel>): RecyclerView.Adapter<VariantProductStockAccordionAdapter.VariantProductStockAccordionViewHolder>() {

    inner class VariantProductStockAccordionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariantProductStockAccordionViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_campaign_stock_variant_name, parent, false)
        return VariantProductStockAccordionViewHolder(view)
    }

    override fun getItemCount(): Int = variantProductList.size

    override fun onBindViewHolder(holder: VariantProductStockAccordionViewHolder, position: Int) {
        variantProductList[position].let { model ->
            holder.itemView.run {
                tv_campaign_stock_variant_name?.text = model.productName
                tv_campaign_stock_variant_count?.text = model.productStock
            }
        }
    }
}