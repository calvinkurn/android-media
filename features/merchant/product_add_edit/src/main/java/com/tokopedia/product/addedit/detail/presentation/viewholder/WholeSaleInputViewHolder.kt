package com.tokopedia.product.addedit.detail.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.unifycomponents.TextFieldUnify
import java.text.NumberFormat
import java.util.*

class WholeSaleInputViewHolder(itemView: View,
                               private val deleteClickListener: OnDeleteButtonClickListener,
                               private val addClickListener: OnAddButtonClickListener,
                               private val textChangedListener: TextChangedListener)
    : RecyclerView.ViewHolder(itemView) {

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

        wholeSaleQuantityField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val wholeSaleQuantityInput = charSequence?.toString()
                    wholeSaleQuantityInput?.let { textChangedListener.onWholeSaleQuantityItemTextChanged(adapterPosition, it) }
                }
            }
        })

        wholeSalePriceField?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // clean any kind of number formatting here
                    val wholeSalePriceInput = charSequence?.toString()?.replace(".", "")
                    wholeSalePriceInput?.let {
                        // do the validation first
                        textChangedListener.onWholeSalePriceItemTextChanged(adapterPosition, it)
                        if (it.isNotEmpty()) {
                            // format the number
                            wholeSalePriceField?.textFieldInput?.removeTextChangedListener(this)
                            val formattedText: String = NumberFormat.getNumberInstance(Locale.US).format(it.toBigInteger()).toString().replace(",", ".")
                            wholeSalePriceField?.textFieldInput?.setText(formattedText)
                            wholeSalePriceField?.textFieldInput?.setSelection(formattedText.length)
                            wholeSalePriceField?.textFieldInput?.addTextChangedListener(this)
                        }
                    }
                }
            }
        })

        deleteButton?.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                deleteClickListener.onDeleteButtonClicked(adapterPosition)
            }
        }
    }

    fun bindData(inputModel: WholeSaleInputModel) {
        wholeSaleQuantityField?.textFieldInput?.setText(inputModel.quantity)
        wholeSaleQuantityField?.setError(false)
        wholeSaleQuantityField?.setMessage("")
        wholeSalePriceField?.textFieldInput?.setText(inputModel.price)
        wholeSalePriceField?.setError(false)
        wholeSalePriceField?.setMessage("")
        if (adapterPosition > 0) {
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