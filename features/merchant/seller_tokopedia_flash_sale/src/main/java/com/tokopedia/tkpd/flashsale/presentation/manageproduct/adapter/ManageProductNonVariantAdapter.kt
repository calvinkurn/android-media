package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsItemManageProductNonvariantBinding
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

class ManageProductNonVariantAdapter: RecyclerView.Adapter<ManageProductNonVariantAdapter.CriteriaViewHolder>() {

    private var data: List<ReservedProduct.Product> = emptyList()
    private var listener: ManageProductNonVariantAdapterListener? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = StfsItemManageProductNonvariantBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, listener)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        data.getOrNull(position)?.let { menu ->
            holder.bind(menu)
        }
    }

    fun setDataList(newData: List<ReservedProduct.Product>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setListener(listener: ManageProductNonVariantAdapterListener) {
        this.listener = listener
    }

    inner class CriteriaViewHolder(
        private val binding: StfsItemManageProductNonvariantBinding,
        private val listener: ManageProductNonVariantAdapterListener?
    ) : ManageProductNonVariantBaseViewHolder(binding.root, listener) {
        fun bind(item: ReservedProduct.Product) {
            binding.mainLayout.apply {
                val discount = item.warehouses.firstOrNull()?.discountSetup
                val criteria = item.productCriteria
                setupInputField(criteria, discount)
                setupListener(criteria, discount)
            }
        }
    }
}