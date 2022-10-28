package com.tokopedia.product_bundle.single.presentation.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.product_bundle.common.customview.DiscountPriceView
import com.tokopedia.product_bundle.common.customview.SpinnerView
import com.tokopedia.product_bundle.single.presentation.constant.SingleBundleInfoConstants.MIN_DISPLAYED_QTY
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleSelectedItem
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class SingleProductBundleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var layoutItem: ConstraintLayout = itemView.findViewById(R.id.layout_item)
    var radioItem: RadioButtonUnify = itemView.findViewById(R.id.radio_item)
    var ivItemImage: ImageUnify = itemView.findViewById(R.id.iv_item_image)
    var bundleName: Label = itemView.findViewById(R.id.label_item_bundle_name)
    var title: Typography = itemView.findViewById(R.id.tv_item_title)
    var spinnerItemVariant: SpinnerView = itemView.findViewById(R.id.spinner_item_variant)
    var discountViewItem: DiscountPriceView = itemView.findViewById(R.id.discountview_item)
    var tvVariantEmpty: View = itemView.findViewById(R.id.tv_variant_empty)

    fun bindData(
        item: SingleProductBundleItem,
        selectedItem: SingleProductBundleSelectedItem
    ) {
        title.text = item.productName
        ivItemImage.urlSrc = item.imageUrl
        bundleName.text = itemView.context.getString(R.string.bundle_item_title_prefix, item.quantity)
        bundleName.showWithCondition(item.quantity >= MIN_DISPLAYED_QTY)
        discountViewItem.price = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.discountedPrice, false)
        discountViewItem.discountAmount = "${item.discount}%"
        discountViewItem.slashPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(item.originalPrice, false)
        spinnerItemVariant.isVisible = item.hasVariant
        spinnerItemVariant.text = item.selectedVariantText

        radioItem.isChecked = selectedItem.isSelected
        tvVariantEmpty.isVisible = selectedItem.isVariantEmpty
    }
}