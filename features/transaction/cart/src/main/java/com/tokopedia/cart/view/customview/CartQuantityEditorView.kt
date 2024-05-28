package com.tokopedia.cart.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.cart.databinding.LayoutCartQuantityEditorBinding
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class CartQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var binding: LayoutCartQuantityEditorBinding

    val anchorMinusButton: View

    var skipListener: Boolean = false

    private var qty: Int = 0
        set(value) {
            field = value
            binding.cartQtyTextField.setText("$value")
        }

    var onValueChanged: (Int) -> Unit = { value ->

    }

    var onFocusChanged: (View, Boolean) -> Unit = { v, hf ->

    }

    init {
        binding = LayoutCartQuantityEditorBinding.inflate(LayoutInflater.from(context), this)
        anchorMinusButton = binding.cartQtyMinusButton

        binding.cartQtyPlusButton.setOnClickListener {
            qty++
        }

        binding.cartQtyMinusButton.setOnClickListener {
            qty--
        }

        binding.cartQtyTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (!skipListener) {
                    onValueChanged.invoke(s.toString().toIntOrZero())
                }
                skipListener = false
            }
        })

        binding.cartQtyTextField.setOnFocusChangeListener { v, hasFocus ->
            onFocusChanged.invoke(v, hasFocus)
        }
    }

    fun setQuantity(qty: Int) {
        skipListener = true
        this.qty = qty
    }

    fun getQuantity(): Int {
        return this.qty
    }
}
