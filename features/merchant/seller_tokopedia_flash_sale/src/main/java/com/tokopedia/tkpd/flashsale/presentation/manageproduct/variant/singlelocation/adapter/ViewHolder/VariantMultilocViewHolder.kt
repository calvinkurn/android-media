package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ViewHolder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantMultilocItemBinding
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.ManageProductVariantListener

class VariantMultilocViewHolder(
    private val binding: LayoutCampaignManageProductDetailVariantMultilocItemBinding,
    private val listener: ManageProductVariantListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        product: ReservedProduct.Product,
        selectedChildProduct: ReservedProduct.Product.ChildProduct
    ) {
        val discount = selectedChildProduct.warehouses.firstOrNull()?.discountSetup
        binding.containerLayoutProductParent.apply {
            textParentErrorMessage.gone()
            imageParentError.gone()
            textParentTitle.text = selectedChildProduct.name
            textParentOriginalPrice.text = root.context.getString(
                R.string.stfs_avp_price_range_placeholder,
                selectedChildProduct.price.lowerPrice.getCurrencyFormatted(),
                selectedChildProduct.price.upperPrice.getCurrencyFormatted()
            )
            textParentTotalStock.text = root.context.getString(
                R.string.stfs_avp_stock_placeholder,
                selectedChildProduct.stock,
                selectedChildProduct.warehouses.count()
            )

            switcherToggleParent.apply {
                isChecked = selectedChildProduct.isToggleOn
                setOnClickListener {
                    selectedChildProduct.isToggleOn = switcherToggleParent.isChecked
                    binding.containerProductChild.isVisible = selectedChildProduct.isToggleOn
                    listener?.onToggleSwitch(adapterPosition, switcherToggleParent.isChecked)
                    discount?.let { disc ->
                        listener?.onDataInputChanged(
                            adapterPosition,
                            product, disc
                        )
                    }
                }
            }
            binding.containerProductChild.isVisible = selectedChildProduct.isToggleOn
        }

        binding.containerLayoutProductInformationLocation.apply {
            if (getFilledWarehousesCount(selectedChildProduct).isMoreThanZero()) {
                textInformationLocationSubTitle.text = root.context.getString(
                    R.string.stfs_avp_location_placeholder,
                    getFilledWarehousesCount(selectedChildProduct)
                )
            }
            container.setOnClickListener {
                listener?.onMultiWarehouseClicked(adapterPosition)
            }
        }
        triggerListener(product, selectedChildProduct, discount)
    }

    private fun triggerListener(
        product: ReservedProduct.Product,
        selectedChildProduct: ReservedProduct.Product.ChildProduct,
        discount: ReservedProduct.Product.Warehouse.DiscountSetup?
    ) {
        discount?.let { disc ->
            selectedChildProduct.warehouses
                .filter { warehouse -> warehouse.isToggleOn }
                .forEach { _ ->
                    listener?.onDataInputChanged(adapterPosition, product, disc)
                }
        }
    }

    private fun getFilledWarehousesCount(item: ReservedProduct.Product.ChildProduct): Int {
        val filledWarehouses = item.warehouses.filter { warehouse -> warehouse.isToggleOn }
        return filledWarehouses.count()
    }
}