package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemManageProductNonvariantBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

class ManageProductNonVariantAdapter: RecyclerView.Adapter<ManageProductNonVariantAdapter.CriteriaViewHolder>() {

    private var productCriteria: ReservedProduct.Product.ProductCriteria? = null
    private var warehouses: List<ReservedProduct.Product.Warehouse> = emptyList()
    private var listener: ManageProductNonVariantAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemManageProductNonvariantBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, listener)
    }

    override fun getItemCount() = warehouses.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        warehouses.getOrNull(position)?.let {
            holder.bind(productCriteria ?: return, it)
        }
    }

    fun setDataList(newData: ReservedProduct.Product) {
        warehouses = newData.warehouses
        productCriteria = newData.productCriteria
        notifyItemRangeChanged(Int.ZERO, newData.warehouses.size)
    }

    fun getWarehouses() = warehouses

    fun setListener(listener: ManageProductNonVariantAdapterListener) {
        this.listener = listener
    }

    inner class CriteriaViewHolder(
        private val binding: StfsItemManageProductNonvariantBinding,
        private val listener: ManageProductNonVariantAdapterListener?
    ) : ManageProductNonVariantBaseViewHolder(binding.root, listener) {
        fun bind(criteria: ReservedProduct.Product.ProductCriteria, warehouse: ReservedProduct.Product.Warehouse) {
            warehouse.isToggleOn = true
            binding.mainLayout.apply {
                val discount = warehouse.discountSetup
                setupInputField(criteria, discount)
                setupInputListener(criteria, discount)
            }
        }
    }
}
