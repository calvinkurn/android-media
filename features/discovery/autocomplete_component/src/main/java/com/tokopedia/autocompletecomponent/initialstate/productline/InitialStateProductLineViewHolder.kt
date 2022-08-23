package com.tokopedia.autocompletecomponent.initialstate.productline

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteProductListItemBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class InitialStateProductLineViewHolder(
    itemView: View,
    private val clickListener: ProductLineListener
) : RecyclerView.ViewHolder(itemView) {
    private var binding: LayoutAutocompleteProductListItemBinding? by viewBinding()

    fun bind(item: BaseItemInitialStateSearch) {
        setComponentHeight(item)
        setImage(item)
        setTitle(item)
        setTitleMargin(item)
        setLabelDiscountPercentage(item)
        setOriginalPrice(item)
        setPrice(item)
        setPriceMargin(item)
        setListener(item)
    }

    private fun setComponentHeight(item: BaseItemInitialStateSearch) {
        val autocompleteProductItem = binding?.autocompleteProductItem ?: return
        val layoutParams = autocompleteProductItem.layoutParams

        if (item.hasSlashedPrice()) layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_height)
        else layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_initial_state_product_double_line_height)

        autocompleteProductItem.layoutParams = layoutParams
    }

    private fun setImage(item: BaseItemInitialStateSearch) {
        setImageHeight()
        bindImage(item)
    }

    private fun setImageHeight() {
        val autocompleteProductImage = binding?.autocompleteProductImage ?: return
        val layoutParams = autocompleteProductImage.layoutParams
        val resources = itemView.context.resources

        layoutParams.height = resources.getDimensionPixelSize(R.dimen.autocomplete_product_initial_state_image_size)
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.autocomplete_product_initial_state_image_size)

        autocompleteProductImage.layoutParams = layoutParams
    }

    private fun bindImage(item: BaseItemInitialStateSearch) {
        val context = itemView.context
        binding?.autocompleteProductImage?.let {
            ImageHandler.loadImageRounded(context, it, item.imageUrl, context.resources.getDimension(R.dimen.autocomplete_product_initial_state_image_radius))
        }
    }

    private fun setTitle(item: BaseItemInitialStateSearch) {
        val autocompleteProductTitle = binding?.autocompleteProductTitle ?: return
        autocompleteProductTitle.setType(Typography.BODY_3)
        autocompleteProductTitle.setWeight(Typography.REGULAR)

        autocompleteProductTitle.setTextAndCheckShow(item.title)
    }

    private fun setTitleMargin(item: BaseItemInitialStateSearch) {
        val resources = itemView.context.resources

        val topMargin = if (item.hasSlashedPrice()) resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_title_margin_top)
        else resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_title_margin_top)

        binding?.autocompleteProductTitle?.setMargin(
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_title_margin_left),
                topMargin,
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_title_margin_right),
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_title_margin_bottom)
        )
    }

    private fun setLabelDiscountPercentage(item: BaseItemInitialStateSearch) {
        binding?.autocompleteProductLabelDiscountPercentage?.shouldShowWithAction(item.hasSlashedPrice()) {
            binding?.autocompleteProductLabelDiscountPercentage?.text = item.discountPercentage
        }
    }

    private fun setOriginalPrice(item: BaseItemInitialStateSearch) {
        val autocompleteProductOriginalPrice = binding?.autocompleteProductOriginalPrice ?: return
        autocompleteProductOriginalPrice.shouldShowWithAction(item.hasSlashedPrice()) {
            autocompleteProductOriginalPrice.setTextAndCheckShow(item.originalPrice)
            autocompleteProductOriginalPrice.paintFlags = autocompleteProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setPrice(item: BaseItemInitialStateSearch) {
        val autocompleteProductPrice = binding?.autocompleteProductPrice ?: return
        autocompleteProductPrice.setType(Typography.BODY_2)
        autocompleteProductPrice.setWeight(Typography.BOLD)

        autocompleteProductPrice.setTextAndCheckShow(item.subtitle)
    }

    private fun setPriceMargin(item: BaseItemInitialStateSearch) {
        val resources = itemView.context.resources

        val bottomMargin = if (item.hasSlashedPrice()) resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_price_margin_bottom)
        else resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_price_margin_bottom)

        binding?.autocompleteProductPrice?.setMargin(
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_price_margin_left),
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_price_margin_top),
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_price_margin_right),
                bottomMargin
        )
    }

    private fun setListener(item: BaseItemInitialStateSearch) {
        binding?.autocompleteProductItem?.setOnClickListener {
            clickListener.onProductLineClicked(item)
        }
    }
}