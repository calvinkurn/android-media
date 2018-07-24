package com.tokopedia.product.edit.price.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import kotlinx.android.synthetic.main.partial_product_edit_name.view.*

class ProductEditNameViewHolder(view: View, listener: Listener){

    private lateinit var nameTextWatcher: TextWatcher

    interface Listener {
        fun onNameChanged(name: String)
    }

    init {
        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    view.textInputLayoutName.error = null
                } else {
                    view.textInputLayoutName.error = "Error Nama"
                }
                listener.onNameChanged(view.editTextName.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        view.editTextName.addTextChangedListener(nameTextWatcher)
    }
}