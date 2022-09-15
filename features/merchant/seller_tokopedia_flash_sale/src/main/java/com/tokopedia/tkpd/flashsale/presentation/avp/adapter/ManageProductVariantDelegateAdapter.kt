package com.tokopedia.tkpd.flashsale.presentation.avp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.components.adapter.DelegateAdapter
import com.tokopedia.campaign.databinding.LayoutCampaignManageProductDetailVariantItemBinding
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.tkpd.flashsale.presentation.avp.adapter.item.ManageProductVariantItem

class ManageProductVariantDelegateAdapter(
    private val onToggleSwitched: (Int, Boolean) -> Unit
) :
    DelegateAdapter<ManageProductVariantItem, ManageProductVariantDelegateAdapter.ViewHolder>(
        ManageProductVariantItem::class.java
    ) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = LayoutCampaignManageProductDetailVariantItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun bindViewHolder(
        item: ManageProductVariantItem,
        viewHolder: ManageProductVariantDelegateAdapter.ViewHolder
    ) {
        viewHolder.bind(item)
    }

    inner class ViewHolder(private val binding: LayoutCampaignManageProductDetailVariantItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ManageProductVariantItem) {
            binding.run {
                containerLayoutProductParent.run {
                    textParentTitle.text = item.name
                    textParentOriginalPrice.text = item.price.price.toLong().getCurrencyFormatted()
                    textParentErrorMessage.gone()
                    textParentTotalStock.text = "${item.stock} produk"

                    switcherToggleParent.run {
                        isChecked = item.isToggleOn
                        setOnCheckedChangeListener { _, isChecked ->
                            onToggleSwitched(adapterPosition, isChecked)
                        }
                    }
                }
                containerProductChild.isVisible = item.isToggleOn
                containerLayoutProductInformation.run {
                    periodSection.gone()
                    textFieldPriceDiscountNominal.setMessage(getPriceRange(item))
                    quantityEditor.setValue(item.stock)
                    textQuantityEditorSubTitle.text = "stock wajib ${item.stock}"
                }
            }
        }

        private fun getPriceRange(item: ManageProductVariantItem): String {
            return "${
                item.price.lowerPrice.toLong().getCurrencyFormatted()
            } - ${item.price.upperPrice.toLong().getCurrencyFormatted()}"
        }
    }
}