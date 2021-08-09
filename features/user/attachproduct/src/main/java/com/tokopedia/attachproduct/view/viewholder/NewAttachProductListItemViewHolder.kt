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
class NewAttachProductListItemViewHolder(private val itemView: View, checkableInteractionListener: CheckableInteractionListener) : BaseCheckableViewHolder<NewAttachProductItemUiModel>(itemView, checkableInteractionListener), CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private var imageView: ImageView? = null
    private var nameTextView: TextView? = null
    private var checkBox: CheckBox? = null
    private var priceTextView: TextView? = null
    private val newCheckableInteractionListener: NewCheckableInteractionListenerWithPreCheckedAction
    private fun findAndAssignAllFields(itemView: View) {
        imageView = itemView.findViewById(R.id.attach_product_item_image)
        nameTextView = itemView.findViewById(R.id.attach_product_item_name)
        checkBox = itemView.findViewById(R.id.attach_product_item_checkbox)
        priceTextView = itemView.findViewById(R.id.attach_product_item_price)
    }

    override fun getCheckable(): CompoundButton {
        return checkBox!!
    }

    override fun bind(element: NewAttachProductItemUiModel) {
        ImageHandler.loadImageRounded2(imageView!!.context, imageView, element.productImage)
        nameTextView!!.text = element.productName
        priceTextView!!.text = element.productPrice
    }

    override fun setChecked(checked: Boolean) {
        if (newCheckableInteractionListener.shouldAllowCheckChange(adapterPosition, checked)) {
            itemView.isSelected = checked
            super.setChecked(checked)
        }
    }

    override fun onClick(view: View) {
        toggle()
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        super.onCheckedChanged(buttonView, isChecked)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_attach
    }

    init {
        findAndAssignAllFields(itemView)
        checkBox!!.isClickable = false
        itemView.setOnClickListener(this)
        this.newCheckableInteractionListener = checkableInteractionListener as NewCheckableInteractionListenerWithPreCheckedAction
    }
}