package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.StringValidationUtil.filterDigit
import com.tokopedia.product.addedit.common.util.setModeToNumberInput
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.unifycomponents.TextFieldUnify

class WholeSaleInputViewHolder(itemView: View,
                               private val deleteClickListener: OnDeleteButtonClickListener,
                               private val addClickListener: OnAddButtonClickListener,
                               private val textChangedListener: TextChangedListener)
    : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val MIN_CHAR_QTY = 6
    }

    interface OnDeleteButtonClickListener {
        fun onDeleteButtonClicked(position: Int)
    }

    interface OnAddButtonClickListener {
        fun getValidationCurrentWholeSaleQuantity(quantity: String, position: Int) : String
        fun getValidationCurrentWholeSalePrice(price: String, position: Int): String
    }

    interface TextChangedListener {
        fun onWholeSaleQuantityItemTextChanged(position: Int, input: String)
        fun onWholeSalePriceItemTextChanged(position: Int, input: String)
    }

    private var wholeSaleQuantityField: TextFieldUnify? = null
    private var wholeSalePriceField: TextFieldUnify? = null
    private var deleteButton: AppCompatImageView? = null

    init {
        wholeSaleQuantityField = itemView.findViewById(R.id.tfu_wholesale_quantity)
        wholeSalePriceField = itemView.findViewById(R.id.tfu_wholesale_price)
        deleteButton = itemView.findViewById(R.id.iv_delete_button)

        wholeSaleQuantityField.setModeToNumberInput(MIN_CHAR_QTY)
        wholeSalePriceField.setModeToNumberInput()

        wholeSaleQuantityField?.textFieldInput?.doOnTextChanged { text, _, _, _ ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val wholeSaleQuantityInput = text?.toString()?.filterDigit()
                wholeSaleQuantityInput?.let {
                    textChangedListener.onWholeSaleQuantityItemTextChanged(adapterPosition, it)
                }
            }
        }

        wholeSalePriceField?.textFieldInput?.doOnTextChanged { text, _, _, _ ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                // clean any kind of number formatting here
                val wholeSalePriceInput = text?.toString()?.filterDigit()
                wholeSalePriceInput?.let {
                    // do the validation first
                    textChangedListener.onWholeSalePriceItemTextChanged(adapterPosition, it)
                }
            }
        }

        deleteButton?.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteClickListener.onDeleteButtonClicked(adapterPosition)
            }
        }
    }

    fun bindData(inputModel: WholeSaleInputModel) {
        if (inputModel.quantity.length < MIN_CHAR_QTY) wholeSaleQuantityField?.textFieldInput?.setText(inputModel.quantity)
        wholeSaleQuantityField?.setError(false)
        wholeSaleQuantityField?.setMessage("")
        wholeSalePriceField?.textFieldInput?.setText(inputModel.price)
        wholeSalePriceField?.setError(false)
        wholeSalePriceField?.setMessage("")
        if (adapterPosition.isMoreThanZero()) {
            val quantityValidation = addClickListener.getValidationCurrentWholeSaleQuantity(inputModel.quantity, adapterPosition)
            val priceValidation = addClickListener.getValidationCurrentWholeSalePrice(inputModel.price, adapterPosition)
            if (quantityValidation.isNotEmpty()) {
                wholeSaleQuantityField?.setError(true)
                wholeSaleQuantityField?.setMessage(quantityValidation)
            }
            if (priceValidation.isNotEmpty()) {
                wholeSalePriceField?.setError(true)
                wholeSalePriceField?.setMessage(priceValidation)
            }
        }
    }
}