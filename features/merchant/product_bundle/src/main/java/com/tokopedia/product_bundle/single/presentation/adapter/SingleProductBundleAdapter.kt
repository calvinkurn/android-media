package com.tokopedia.product_bundle.single.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem

class SingleProductBundleAdapter(
    private var listener: BundleItemListener
) : RecyclerView.Adapter<SingleProductBundleViewHolder>() {

    private var data: List<SingleProductBundleItem> = listOf()
    private var selectedData: List<SingleProductBundleSelectedItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleProductBundleViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.item_single_product_bundle, parent, false)
        val viewHolder = SingleProductBundleViewHolder(rootView)
        setupViewHolderOnClick(viewHolder)
        return viewHolder
    }

    private fun setupViewHolderOnClick(viewHolder: SingleProductBundleViewHolder) {
        viewHolder.radioItem.setOnClickListener {
            viewHolder.layoutItem.performClick()
        }
        viewHolder.layoutItem.setOnClickListener {
            selectedData.forEachIndexed { index, selectedItem ->
                selectedItem.isSelected = (index == viewHolder.adapterPosition)
            }
            data.getOrNull(viewHolder.adapterPosition)?.apply {
                listener.onBundleItemSelected(
                    originalPrice,
                    discountedPrice,
                    quantity,
                    preorderDurationWording
                )
            }
            notifyDataSetChanged()
        }
        viewHolder.spinnerItemVariant.setOnClickListener {
            val position = viewHolder.adapterPosition
            val selectedVariant = data.getOrNull(position)?.productVariant
            listener.onVariantSpinnerClicked(selectedVariant)
            viewHolder.layoutItem.performClick()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SingleProductBundleViewHolder, position: Int) {
        holder.bindData(data[position], selectedData[position])
    }

    fun setData(data: List<SingleProductBundleItem>,
                selectedData: List<SingleProductBundleSelectedItem>) {
        this.data = data
        this.selectedData = selectedData
        notifyDataSetChanged()
    }

    fun setSelectedVariant(selectedProductId: String, variantText: String) {
        selectedData.forEachIndexed { index, selectedItem ->
            if (selectedItem.isSelected) {
                data.getOrNull(index)?.apply {
                    val variant = getVariantChildFromProductId(selectedProductId)
                    discount = variant?.campaign?.discountedPercentage?.toInt().orZero()
                    originalPrice = variant?.finalMainPrice.orZero()
                    discountedPrice = variant?.finalPrice.orZero()
                    selectedVariantText = variantText
                    listener.onBundleItemSelected(
                        originalPrice,
                        discountedPrice,
                        quantity,
                        preorderDurationWording
                    )
                }

                selectedItem.productId = selectedProductId
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

    fun getSelectedData(): List<SingleProductBundleSelectedItem> {
        return selectedData
    }

    fun getSelectedProductVariant(): ProductVariant? {
        val index = selectedData.indexOfFirst { it.isSelected }
        return data.getOrNull(index)?.productVariant
    }
}