package com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantMultilocItemBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.tkpd.flashsale.domain.entity.ReservedProduct
import com.tokopedia.tkpd.flashsale.presentation.manageproduct.variant.singlelocation.adapter.item.ManageProductVariantItemMultiloc

class ManageProductVariantMultilocAdapter(
    private val listener: ManageProductVariantListener,
    private val product: ReservedProduct.Product
) :
    DelegateAdapter<ManageProductVariantItemMultiloc, ManageProductVariantMultilocAdapter.VariantMultilocViewHolder>(
        ManageProductVariantItemMultiloc::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutCampaignManageProductDetailVariantMultilocItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VariantMultilocViewHolder(binding, listener)
    }

    override fun bindViewHolder(
        item: ManageProductVariantItemMultiloc,
        viewHolder: ManageProductVariantMultilocAdapter.VariantMultilocViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class VariantMultilocViewHolder(
        private val binding: LayoutCampaignManageProductDetailVariantMultilocItemBinding,
        private val listener: ManageProductVariantListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ManageProductVariantItemMultiloc) {
            val discount = item.discountSetup
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
                    listener?.onDataInputChanged(
                        adapterPosition,
                        product, discount
                    )
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

        private fun getFilledWarehousesCount(item: ManageProductVariantItemMultiloc): Int {
            val filledWarehouses = item.warehouses.filter { warehouse -> warehouse.isToggleOn }
            return filledWarehouses.count()
        }
    }
}