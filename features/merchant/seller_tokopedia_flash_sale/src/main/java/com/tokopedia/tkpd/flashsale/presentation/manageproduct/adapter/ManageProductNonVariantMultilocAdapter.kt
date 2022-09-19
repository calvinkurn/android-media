package com.tokopedia.tkpd.flashsale.presentation.manageproduct.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailLocationItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct

class ManageProductNonVariantMultilocAdapter: RecyclerView.Adapter<ManageProductNonVariantMultilocAdapter.CriteriaViewHolder>() {

    private var product: ReservedProduct.Product? = null
    private var listener: ManageProductNonVariantAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        val binding = LayoutCampaignManageProductDetailLocationItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return CriteriaViewHolder(binding, listener)
    }

    override fun getItemCount() = product?.warehouses?.size.orZero()

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        product?.let {
            it.warehouses.getOrNull(position)?.let { selectedWarehouse ->
                holder.bind(it, selectedWarehouse)
            }
        }
    }

    fun setDataList(newData: ReservedProduct.Product) {
        product = newData
        notifyItemRangeChanged(Int.ZERO, newData.warehouses.size)
    }

    fun setListener(listener: ManageProductNonVariantAdapterListener) {
        this.listener = listener
    }

    inner class CriteriaViewHolder(
        private val binding: LayoutCampaignManageProductDetailLocationItemBinding,
        private val listener: ManageProductNonVariantAdapterListener?
    ) : ManageProductNonVariantBaseViewHolder(binding.root, listener) {
        fun bind(
            product: ReservedProduct.Product,
            selectedWarehouse: ReservedProduct.Product.Warehouse,
        ) {
            binding.containerLayoutProductParent.apply {
                textParentErrorMessage.gone()
                imageParentError.gone()
                textParentTitle.text = selectedWarehouse.name
                textParentOriginalPrice.text = selectedWarehouse.price.getCurrencyFormatted()
                textParentTotalStock.text = root.context.getString(R.string.manageproductnonvar_stock_total_format, selectedWarehouse.stock)
                switcherToggleParent.isChecked = selectedWarehouse.isToggleOn
                switcherToggleParent.setOnClickListener {
                    selectedWarehouse.isToggleOn = switcherToggleParent.isChecked
                    binding.containerProductChild.isVisible = selectedWarehouse.isToggleOn
                }
            }
            binding.containerProductChild.isVisible = selectedWarehouse.isToggleOn
            binding.containerLayoutProductInformation.apply {
                val discount = selectedWarehouse.discountSetup
                val criteria = product.productCriteria
                setupInputField(criteria, discount)
                setupListener(criteria, discount)
            }
        }
    }
}