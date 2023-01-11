package com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.databinding.ItemThumbnailVariantBinding
import com.tokopedia.unifycomponents.CardUnify2

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

    fun bind(element: VariantOptionWithAttribute, firstLoad: Boolean) = with(binding) {
        setUI(element = element)
        setEvent(element = element)
        setState(element = element, firstLoad = firstLoad)
    }

    fun bind(element: VariantOptionWithAttribute, payloads: MutableList<Any>, firstLoad: Boolean) {
        if (payloads.isNotEmpty()) {
            setState(element = element, firstLoad = firstLoad)
        }
    }

    private fun setUI(element: VariantOptionWithAttribute) = with(binding) {
        variantTitle.text = element.variantName
        variantThumbnail.loadImage(element.image100, properties = {
            centerCrop()
        })
        renderFlashSale(element = element)
    }

    private fun setEvent(element: VariantOptionWithAttribute) = with(binding) {
        root.setOnClickListener {
            listener.onVariantClicked(element, VariantConstant.STATE_SELECTED)
        }
    }

    private fun setState(element: VariantOptionWithAttribute, firstLoad: Boolean) = with(binding) {
       /* if (element.currentState == VariantConstant.STATE_SELECTED) listener.onSelectionChanged(
            itemView,
            bindingAdapterPosition
        )*/

        when (element.currentState) {
            VariantConstant.STATE_EMPTY, VariantConstant.STATE_SELECTED_EMPTY -> {
                variantCard.cardType = CardUnify2.TYPE_BORDER
            }
            VariantConstant.STATE_SELECTED -> {
                variantCard.cardType = if (firstLoad) CardUnify2.TYPE_BORDER
                else CardUnify2.TYPE_BORDER_ACTIVE
            }
            VariantConstant.STATE_UNSELECTED -> {
                variantCard.cardType = CardUnify2.TYPE_BORDER
            }
            else -> {
                variantCard.cardType = CardUnify2.TYPE_BORDER_DISABLED
            }
        }
    }

    private fun renderFlashSale(element: VariantOptionWithAttribute) {
        val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY &&
            element.currentState != VariantConstant.STATE_SELECTED_EMPTY

        binding.variantPromoIcon.isVisible = shouldShowFlashSale && element.flashSale
    }
}
