package com.tokopedia.product_bundle.single.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem
import com.tokopedia.product_service_common.R

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
            val selectedProductId = selectedData.getOrNull(position)?.productId
            listener.onVariantSpinnerClicked(selectedVariant, selectedProductId)
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
        listener.onDataChanged(getSelectedData(), getSelectedProductVariant())
        notifyDataSetChanged()
    }

    fun setSelectedVariant(selectedProductId: String, variantText: String) {
        selectedData.forEachIndexed { index, selectedItem ->
            if (selectedItem.isSelected) {
                selectedItem.productId = selectedProductId
                data.getOrNull(index)?.apply {
                    val variant = getVariantChildFromProductId(selectedProductId)
                    val variantQuantity = variant?.stock?.minimumOrder.toIntSafely()
                    selectedItem.quantity = variantQuantity
                    quantity = variantQuantity
                    discount = variant?.campaign?.discountedPercentage.toIntSafely()
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

    fun getSelectedBundleId(): String? {
        return selectedData.find { it.isSelected }?.bundleId
    }

    fun getSelectedProductId(): String? {
        // should not return empty product id, empty productId will happened when variant not selected yet
        return selectedData.find { it.isSelected && it.productId.isNotEmpty() }?.productId
    }
}