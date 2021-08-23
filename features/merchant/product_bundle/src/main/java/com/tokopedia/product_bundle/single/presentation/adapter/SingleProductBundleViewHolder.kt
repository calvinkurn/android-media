package com.tokopedia.product_bundle.single.presentation.adapter

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.customview.DiscountPriceView
import com.tokopedia.product_bundle.common.customview.SpinnerView
import com.tokopedia.product_bundle.single.presentation.model.SingleProductBundleItem
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class SingleProductBundleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var layoutItem: ConstraintLayout = itemView.findViewById(R.id.layout_item)
    var radioItem: RadioButtonUnify = itemView.findViewById(R.id.radio_item)
    var ivItemImage: ImageUnify = itemView.findViewById(R.id.iv_item_image)
    var bundleName: Label = itemView.findViewById(R.id.label_item_bundle_name)
    var title: Typography = itemView.findViewById(R.id.tv_item_title)
    var spinnerItemVariant: SpinnerView = itemView.findViewById(R.id.spinner_item_variant)
    var discountViewItem: DiscountPriceView = itemView.findViewById(R.id.discountview_item)

    fun bindData(item: SingleProductBundleItem, isChecked: Boolean) {
        title.text = item.productName
        radioItem.isChecked = isChecked
        ivItemImage.urlSrc = item.imageUrl
        bundleName.text = item.bundleName
        discountViewItem.price = item.price
        discountViewItem.discountAmount = "${item.discount}%"
        discountViewItem.slashPrice = item.slashPrice
    }
}