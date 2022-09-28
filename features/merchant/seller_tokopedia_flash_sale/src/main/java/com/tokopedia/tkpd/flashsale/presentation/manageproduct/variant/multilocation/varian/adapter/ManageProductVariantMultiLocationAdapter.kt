package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.multilocation.varian.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailLocationItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ANNOUNCEMENT

class ManageProductVariantMultiLocationAdapter :
    RecyclerView.Adapter<ManageProductVariantMultiLocationAdapter.CriteriaViewHolder>() {

    private var productVariant: ReservedProduct.Product.ChildProduct? = null
    private var listener: ManageProductVariantAdapterListener? = null
    private var binding: LayoutCampaignManageProductDetailLocationItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriteriaViewHolder {
        binding = LayoutCampaignManageProductDetailLocationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CriteriaViewHolder(binding!!, listener)
    }

    override fun getItemCount() = productVariant?.warehouses?.size.orZero()

    override fun onBindViewHolder(holder: CriteriaViewHolder, position: Int) {
        productVariant?.let {
            it.warehouses.getOrNull(position)?.let { selectedWarehouse ->
                holder.bind(it, selectedWarehouse)
            }
        }
    }

    fun setDataList(newData: ReservedProduct.Product.ChildProduct) {
        productVariant = newData
        notifyItemRangeChanged(Int.ZERO, newData.warehouses.size)
    }

    fun setDataList(position: Int, warehouse: ReservedProduct.Product.Warehouse) {
        notifyItemChanged(position, warehouse)
    }

    fun getDataList() = productVariant?.warehouses.orEmpty()

    fun setListener(listener: ManageProductVariantAdapterListener?) {
        this.listener = listener
    }

    inner class CriteriaViewHolder(
        private val binding: LayoutCampaignManageProductDetailLocationItemBinding,
        private val listener: ManageProductVariantAdapterListener?
    ) : ManageProductVariantBaseViewHolder(binding.root, listener) {
        fun bind(
            product: ReservedProduct.Product.ChildProduct,
            selectedWarehouse: ReservedProduct.Product.Warehouse,
        ) {
            val discount = selectedWarehouse.discountSetup
            val criteria = product.productCriteria
            binding.containerLayoutProductParent.apply {
                textParentErrorMessage.gone()
                imageParentError.gone()
                textParentTitle.text = selectedWarehouse.name
                textParentOriginalPrice.text = selectedWarehouse.price.getCurrencyFormatted()
                textParentTotalStock.text = root.context.getString(
                    R.string.manageproductnonvar_stock_total_format,
                    selectedWarehouse.stock
                )
                switcherToggleParent.isChecked = selectedWarehouse.isToggleOn
                switcherToggleParent.setOnClickListener {
                    selectedWarehouse.isToggleOn = switcherToggleParent.isChecked
                    binding.containerProductChild.isVisible = selectedWarehouse.isToggleOn
                    listener?.onDataInputChanged(adapterPosition, criteria, discount)
                }
                iconTkpd.isVisible = selectedWarehouse.isDilayaniTokopedia
            }
            binding.containerProductChild.isVisible = selectedWarehouse.isToggleOn
            binding.containerLayoutProductInformation.apply {
                clearListener()
                setupInputField(criteria, discount)
                setupListener(criteria, discount)
                if (selectedWarehouse.isDilayaniTokopedia) {
                    tickerPriceError.visible()
                    tickerPriceError.tickerType = TYPE_ANNOUNCEMENT
                    tickerPriceError.setTextDescription(
                        String.format(
                            binding.root.context.getString(
                                R.string.stfs_text_ticker_warning
                            )
                        )
                    )
                }
            }
        }
    }
}