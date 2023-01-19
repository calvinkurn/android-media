package com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.databinding.ItemThumbnailVariantBinding

/**
 * Created by yovi.putra on 10/01/23"
 * Project name: android-tokopedia-core
 **/

class ThumbnailVariantViewHolder(
    val view: View,
    val listener: AtcVariantListener
) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_thumbnail_variant
    }

    private val binding by lazyThreadSafetyNone {
        ItemThumbnailVariantBinding.bind(view)
    }

    private val card get() = binding.pdpThumbnailVariantView

    fun bind(element: VariantOptionWithAttribute) = with(binding) {
        setUI(element = element)
        setEvent(element = element)
        setState(element = element)
    }

    private fun setUI(element: VariantOptionWithAttribute) = with(card) {
        val shouldRenderPromoIcon = shouldRenderFlashSale(element = element)
        setData(
            title = element.variantName,
            thumbnailUrl = element.image100,
            showPromoIcon = shouldRenderPromoIcon
        )
    }

    private fun setEvent(element: VariantOptionWithAttribute) {
        // trigger to adapter for scroll smooth to position
        if (element.currentState == VariantConstant.STATE_SELECTED) {
            listener.onSelectionChanged(
                itemView,
                bindingAdapterPosition
            )
        }

        card.onClickListener {
            listener.onThumbnailVariantSelected(
                variantId = element.variantId,
                categoryKey = element.variantCategoryKey
            )
        }
    }

    private fun setState(element: VariantOptionWithAttribute): Unit = with(card) {
        when (element.currentState) {
            VariantConstant.STATE_SELECTED -> setSelectedState()
            VariantConstant.STATE_SELECTED_EMPTY -> setSelectedStockEmptyState()
            VariantConstant.STATE_UNSELECTED -> setUnselectedState()
            else -> setDisableState()
        }
    }

    private fun shouldRenderFlashSale(element: VariantOptionWithAttribute): Boolean {
        return element.currentState != VariantConstant.STATE_EMPTY &&
            element.currentState != VariantConstant.STATE_SELECTED_EMPTY &&
            element.flashSale
    }
}
