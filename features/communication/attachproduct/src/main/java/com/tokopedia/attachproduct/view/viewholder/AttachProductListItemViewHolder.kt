package com.tokopedia.attachproduct.view.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.attachproduct.R
import com.tokopedia.attachproduct.databinding.ItemProductAttachBinding
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by Hendri on 13/02/18.
 */
class AttachProductListItemViewHolder
    (itemView: View, private val newCheckableInteractionListener: CheckableInteractionListenerWithPreCheckedAction,
     checkableInteractionListener: CheckableInteractionListener)
    : BaseCheckableViewHolder<AttachProductItemUiModel>(itemView, checkableInteractionListener),
        CompoundButton.OnCheckedChangeListener {

    private val binding: ItemProductAttachBinding? by viewBinding()


    override fun getCheckable(): CompoundButton {
        return binding!!.attachProductItemCheckbox
    }

    override fun bind(element: AttachProductItemUiModel) {
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
        binding?.attachProductItemCheckbox?.isClickable = false
    }

    private fun bindName(element: AttachProductItemUiModel) {
        binding?.attachProductItemName?.text = element.productName
    }

    private fun bindImage(element: AttachProductItemUiModel) {
        ImageHandler.loadImageRounded2(binding?.attachProductItemImage?.context, binding?.attachProductItemImage, element.productImage)
    }

    private fun bindPrice(element: AttachProductItemUiModel) {
        binding?.attachProductItemPrice?.text = element.productPrice
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
