package com.tokopedia.product_bundle.single.presentation.adapter

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
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
    var tvItemPrice: Typography = itemView.findViewById(R.id.tv_item_price)
    var labelItemDiscount: Label = itemView.findViewById(R.id.label_item_discount)
    var tvItemSlashPrice: Typography = itemView.findViewById(R.id.tv_item_slash_price)

    fun bindData(item: SingleProductBundleItem, isChecked: Boolean) {
        title.text = item.productName
        radioItem.isChecked = isChecked
        ivItemImage.urlSrc = item.imageUrl
        bundleName.text = item.bundleName
        tvItemPrice.text = item.price
        labelItemDiscount.text = "${item.discount}%"
        tvItemSlashPrice.text = item.slashPrice
        tvItemSlashPrice.paintFlags = tvItemSlashPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}