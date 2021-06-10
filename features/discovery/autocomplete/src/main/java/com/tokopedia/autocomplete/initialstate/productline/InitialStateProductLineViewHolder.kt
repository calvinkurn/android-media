package com.tokopedia.autocomplete.initialstate.productline

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_product_list_item.view.*

class InitialStateProductLineViewHolder(
        itemView: View,
        private val clickListener: InitialStateItemClickListener
) : RecyclerView.ViewHolder(itemView) {

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
        val layoutParams = itemView.autocompleteProductItem.layoutParams

        if (item.hasSlashedPrice()) layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_height)
        else layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.autocomplete_initial_state_product_double_line_height)

        itemView.autocompleteProductItem.layoutParams = layoutParams
    }

    private fun setImage(item: BaseItemInitialStateSearch) {
        setImageHeight()
        bindImage(item)
    }

    private fun setImageHeight() {
        val layoutParams = itemView.autocompleteProductImage.layoutParams
        val resources = itemView.context.resources

        layoutParams.height = resources.getDimensionPixelSize(R.dimen.autocomplete_product_initial_state_image_size)
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.autocomplete_product_initial_state_image_size)

        itemView.autocompleteProductImage.layoutParams = layoutParams
    }

    private fun bindImage(item: BaseItemInitialStateSearch) {
        val context = itemView.context
        itemView.autocompleteProductImage?.let {
            ImageHandler.loadImageRounded(context, it, item.imageUrl, context.resources.getDimension(R.dimen.autocomplete_product_initial_state_image_radius))
        }
    }

    private fun setTitle(item: BaseItemInitialStateSearch) {
        itemView.autocompleteProductTitle?.setType(Typography.BODY_3)
        itemView.autocompleteProductTitle?.setWeight(Typography.REGULAR)

        itemView.autocompleteProductTitle?.setTextAndCheckShow(item.title)
    }

    private fun setTitleMargin(item: BaseItemInitialStateSearch) {
        val resources = itemView.context.resources

        val topMargin = if (item.hasSlashedPrice()) resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_title_margin_top)
        else resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_title_margin_top)

        itemView.autocompleteProductTitle?.setMargin(
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_title_margin_left),
                topMargin,
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_title_margin_right),
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_title_margin_bottom)
        )
    }

    private fun setLabelDiscountPercentage(item: BaseItemInitialStateSearch) {
        itemView.autocompleteProductLabelDiscountPercentage?.shouldShowWithAction(item.hasSlashedPrice()) {
            itemView.autocompleteProductLabelDiscountPercentage?.text = item.discountPercentage
        }
    }

    private fun setOriginalPrice(item: BaseItemInitialStateSearch) {
        itemView.autocompleteProductOriginalPrice?.shouldShowWithAction(item.hasSlashedPrice()) {
            itemView.autocompleteProductOriginalPrice?.setTextAndCheckShow(item.originalPrice)
            itemView.autocompleteProductOriginalPrice?.paintFlags = itemView.autocompleteProductOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private fun setPrice(item: BaseItemInitialStateSearch) {
        itemView.autocompleteProductPrice?.setType(Typography.BODY_2)
        itemView.autocompleteProductPrice?.setWeight(Typography.BOLD)

        itemView.autocompleteProductPrice?.setTextAndCheckShow(item.subtitle)
    }

    private fun setPriceMargin(item: BaseItemInitialStateSearch) {
        val resources = itemView.context.resources

        val bottomMargin = if (item.hasSlashedPrice()) resources.getDimensionPixelSize(R.dimen.autocomplete_product_triple_line_price_margin_bottom)
        else resources.getDimensionPixelSize(R.dimen.autocomplete_product_double_line_price_margin_bottom)

        itemView.autocompleteProductPrice?.setMargin(
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_price_margin_left),
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_price_margin_top),
                resources.getDimensionPixelSize(R.dimen.autocomplete_product_line_price_margin_right),
                bottomMargin
        )
    }

    private fun setListener(item: BaseItemInitialStateSearch) {
        itemView.autocompleteProductItem?.setOnClickListener {
            clickListener.onItemClicked(item.applink, item.url)
        }
    }
}