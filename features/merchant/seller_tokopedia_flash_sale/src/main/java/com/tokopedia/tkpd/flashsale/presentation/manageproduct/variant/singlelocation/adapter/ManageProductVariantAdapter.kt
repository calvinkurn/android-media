package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantItemBinding
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantMultilocItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.viewHolder.VariantMultilocViewHolder
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.viewHolder.VariantViewHolder

class ManageProductVariantAdapter(
    private val listener: ManageProductVariantListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SINGLE_WAREHOUSE = 0
        private const val VIEW_TYPE_MULTI_WAREHOUSE = 1
    }

    private var data: ReservedProduct.Product? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val bindingSingleWarehouse = LayoutCampaignManageProductDetailVariantItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val bindingMultiWarehouse =
            LayoutCampaignManageProductDetailVariantMultilocItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        return when (viewType) {
            VIEW_TYPE_MULTI_WAREHOUSE -> VariantMultilocViewHolder(bindingMultiWarehouse, listener)
            else -> VariantViewHolder(bindingSingleWarehouse, listener)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        data?.let {
            it.childProducts.getOrNull(position)?.let { selectedChildProduct ->
                when (holder) {
                    is VariantViewHolder -> holder.bind(it, selectedChildProduct)
                    is VariantMultilocViewHolder -> holder.bind(it, selectedChildProduct)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data?.childProducts?.get(position)?.isMultiwarehouse) {
            true -> VIEW_TYPE_MULTI_WAREHOUSE
            else -> VIEW_TYPE_SINGLE_WAREHOUSE
        }
    }

    override fun getItemCount(): Int = data?.childProducts?.size.orZero()

    fun getDataList() = data?.childProducts.orEmpty()

    fun setDataList(newData: ReservedProduct.Product) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.childProducts.size)
    }
}
