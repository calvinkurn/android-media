package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.ReservedStockProductModel
import com.tokopedia.product.manage.feature.campaignstock.ui.util.convertCheckMaximumStockLimit
import kotlinx.android.synthetic.main.item_campaign_stock_variant_name.view.*

class VariantProductStockAccordionAdapter(private val variantProductList: List<ReservedStockProductModel>): RecyclerView.Adapter<VariantProductStockAccordionAdapter.VariantProductStockAccordionViewHolder>() {

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
                tv_campaign_stock_variant_count?.text = model.stock.convertCheckMaximumStockLimit(context)
                model.description.let { desc ->
                    if (desc.isNotEmpty()) {
                        tv_campaign_stock_variant_description?.run {
                            text = desc
                            visible()
                        }
                    } else {
                        tv_campaign_stock_variant_description?.gone()
                    }
                }
            }
        }
    }
}