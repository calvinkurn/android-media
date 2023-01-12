package com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.extensions.getColorChecker
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

    private fun setUI(element: VariantOptionWithAttribute) = with(binding) {
        variantTitle.text = element.variantName
        variantThumbnail.loadImage(element.image100, properties = {
            centerCrop()
        })
        renderFlashSale(element = element)
    }

    private fun setEvent(element: VariantOptionWithAttribute) = with(binding) {
        // trigger to adapter for scroll smooth to position
        if (element.currentState == VariantConstant.STATE_SELECTED) {
            listener.onSelectionChanged(
                itemView,
                bindingAdapterPosition
            )
        }

        variantCard.setOnClickListener {
            listener.onThumbnailVariantSelected(element)
        }
    }

    private fun setState(element: VariantOptionWithAttribute, firstLoad: Boolean) = with(binding) {
        when (element.currentState) {
            VariantConstant.STATE_SELECTED -> setSelectedState(firstLoad)
            VariantConstant.STATE_SELECTED_EMPTY -> setSelectedStockEmptyState(firstLoad)
            VariantConstant.STATE_UNSELECTED -> setUnselectedState()
            else -> setDisableState()
        }
    }

    private fun setUnselectedState() {
        setVariantTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_NN600)
        binding.variantCard.cardType = CardUnify2.TYPE_BORDER
    }

    private fun setSelectedState(firstLoad: Boolean) = with(binding) {
        setVariantTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        variantCard.cardType = if (firstLoad) {
            CardUnify2.TYPE_BORDER
        } else {
            CardUnify2.TYPE_BORDER_ACTIVE
        }
    }

    private fun setSelectedStockEmptyState(firstLoad: Boolean) {
        if (firstLoad) {
            setDisableState()
        } else {
            setSelectedState(firstLoad = false)
            setThumbGrayscale()
        }
    }

    private fun setDisableState() {
        setVariantTitleColor(com.tokopedia.unifyprinciples.R.color.Unify_NN400)
        binding.variantCard.cardType = CardUnify2.TYPE_BORDER_DISABLED
        setThumbGrayscale()
    }

    private fun setThumbGrayscale() {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val filter = ColorMatrixColorFilter(colorMatrix)
        binding.variantThumbnail.colorFilter = filter
    }

    private fun setVariantTitleColor(resId: Int) {
        binding.variantTitle.setTextColor(getColor(resId))
    }

    private fun getColor(resId: Int): Int {
        return binding.root.context.getColorChecker(resId)
    }

    private fun renderFlashSale(element: VariantOptionWithAttribute) {
        val shouldShowFlashSale = element.currentState != VariantConstant.STATE_EMPTY &&
            element.currentState != VariantConstant.STATE_SELECTED_EMPTY

        binding.variantPromoIcon.isVisible = shouldShowFlashSale && element.flashSale
    }
}
