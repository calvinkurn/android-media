package com.tokopedia.attachproduct.view.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.view.uimodel.NewAttachProductItemUiModel

/**
 * Created by Hendri on 13/02/18.
 */
class NewAttachProductListItemViewHolder
    (itemView: View,
     private val newCheckableInteractionListener: NewCheckableInteractionListenerWithPreCheckedAction,
     checkableInteractionListener: CheckableInteractionListener)
    : BaseCheckableViewHolder<NewAttachProductItemUiModel>(itemView, checkableInteractionListener),
        CompoundButton.OnCheckedChangeListener {
    private var imageView: ImageView = itemView.findViewById(R.id.attach_product_item_image)
    private var nameTextView: TextView = itemView.findViewById(R.id.attach_product_item_name)
    private var checkBox: CheckBox = itemView.findViewById(R.id.attach_product_item_checkbox)
    private var priceTextView: TextView = itemView.findViewById(R.id.attach_product_item_price)


    override fun getCheckable(): CompoundButton {
        return checkBox
    }

    override fun bind(element: NewAttachProductItemUiModel) {
        bindClickable()
        bindName(element)
        bindImage(element)
        bindPrice(element)
        bindToogle()
    }

    override fun setChecked(checked: Boolean) {
        if (newCheckableInteractionListener.shouldAllowCheckChange(adapterPosition, checked)) {
            itemView.isSelected = checked
            super.setChecked(checked)
        }
    }

    private fun bindClickable() {
        checkBox.isClickable = false
    }

    private fun bindName(element: NewAttachProductItemUiModel) {
        nameTextView.text = element.productName
    }

    private fun bindImage(element: NewAttachProductItemUiModel) {
        ImageHandler.loadImageRounded2(imageView.context, imageView, element.productImage)
    }

    private fun bindPrice(element: NewAttachProductItemUiModel) {
        priceTextView.text = element.productPrice
    }

    private fun bindToogle() {
        itemView.setOnClickListener {
            toggle()
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        super.onCheckedChanged(buttonView, isChecked)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_attach
    }
}
