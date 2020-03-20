package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.unifycomponents.TextFieldUnify

class WholeSaleInputViewHolder(itemView: View, private val clickListener: OnDeleteButtonClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClicked(position: Int)
    }

    private var wholeSaleQuantityField: TextFieldUnify? = null
    private var wholeSalePriceField: TextFieldUnify? = null
    private var deleteButton: AppCompatImageView? = null

    init {
        wholeSaleQuantityField = itemView.findViewById(R.id.tfu_wholesale_quantity)
        wholeSalePriceField = itemView.findViewById(R.id.tfu_wholesale_price)
        deleteButton = itemView.findViewById(R.id.iv_delete_button)

        deleteButton?.setOnClickListener {
            clickListener.onDeleteButtonClicked(adapterPosition)
        }
    }

    fun bindData(inputModel: WholeSaleInputModel) {
        wholeSaleQuantityField?.textFieldInput?.setText(inputModel.quantity)
        wholeSalePriceField?.textFieldInput?.setText(inputModel.price)
    }
}