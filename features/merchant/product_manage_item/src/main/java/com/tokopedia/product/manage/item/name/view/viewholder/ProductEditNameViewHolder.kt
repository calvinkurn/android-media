package com.tokopedia.product.manage.item.name.view.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.name.view.model.ProductName
import kotlinx.android.synthetic.main.partial_product_edit_name.view.*

class ProductEditNameViewHolder(var view: View, listener: Listener){

    private val nameTextWatcher: TextWatcher
    private var productName: ProductName = ProductName()

    interface Listener {
        fun onNameChanged(productName: ProductName)
    }

    init {
        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                productName.name = view.editTextName.text.toString()
                validateForm()
                listener.onNameChanged(productName)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        view.editTextName.addTextChangedListener(nameTextWatcher)
    }

    fun setName(string: String){
        view.editTextName.setText(string)
    }

    fun setEditableName(isEditableName: Boolean){
        view.editTextName.isEnabled = isEditableName
    }

    fun validateForm(){
        view.textInputLayoutName.error = if (productName.name.isNotEmpty()) null else view.context.getString(R.string.product_error_product_name_empty)
    }
}