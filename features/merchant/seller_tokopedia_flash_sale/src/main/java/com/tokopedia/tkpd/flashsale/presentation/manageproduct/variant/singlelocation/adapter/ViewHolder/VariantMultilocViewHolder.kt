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
        item: ReservedProduct.Product.ChildProduct
    ) {
        val discount = item.warehouses.firstOrNull()?.discountSetup
        binding.containerLayoutProductParent.apply {
            textParentErrorMessage.gone()
            imageParentError.gone()

            textParentTitle.text = item.name
            textParentOriginalPrice.text = root.context.getString(
                R.string.stfs_avp_price_range_placeholder,
                item.price.lowerPrice.getCurrencyFormatted(),
                item.price.upperPrice.getCurrencyFormatted()
            )

            textParentTotalStock.text = root.context.getString(
                R.string.stfs_avp_stock_placeholder,
                item.stock,
                item.warehouses.count()
            )

            switcherToggleParent.setOnClickListener {
                item.isToggleOn = switcherToggleParent.isChecked
                binding.containerProductChild.isVisible = item.isToggleOn
                listener?.onToggleSwitch(adapterPosition, switcherToggleParent.isChecked)
                discount?.let { it ->
                    listener?.onDataInputChanged(
                        adapterPosition,
                        product, it
                    )
                }
            }
        }

        binding.containerLayoutProductInformationLocation.apply {
            if (getFilledWarehousesCount(item).isMoreThanZero()) {
                textInformationLocationSubTitle.text = root.context.getString(
                    R.string.stfs_avp_location_placeholder,
                    getFilledWarehousesCount(item)
                )
            }
        }
    }

    private fun getFilledWarehousesCount(item: ReservedProduct.Product.ChildProduct): Int {
        val filledWarehouses = item.warehouses.filter { warehouse -> warehouse.isToggleOn }
        return filledWarehouses.count()
    }
}