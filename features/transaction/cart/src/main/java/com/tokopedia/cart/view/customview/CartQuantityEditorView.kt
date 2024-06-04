package com.tokopedia.cart.view.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.LayoutCartQuantityEditorBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.toIntOrZero

class CartQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var binding: LayoutCartQuantityEditorBinding

    val anchorMinusButton: IconUnify

    var skipListener: Boolean = false

    private var qty: Int = 0
        set(value) {
            field = value
            binding.cartQtyTextField.setText("$value")
        }

    var minQty = 0
    var maxQty = 0

    var onValueChanged: (Int) -> Unit = { value ->

    }

    var onFocusChanged: (View, Boolean) -> Unit = { v, hf ->

    }

    var onDoneListener: () -> Unit = {}

    var onPlusClickListener: () -> Unit = {}

    var onMinusClickListener: () -> Boolean = { false }

    private val defaultBackground: Drawable?
    private val focusedBackground: Drawable?

    init {
        binding = LayoutCartQuantityEditorBinding.inflate(LayoutInflater.from(context), this)
        defaultBackground = AppCompatResources.getDrawable(context, R.drawable.bg_cart_quantity_editor)
        focusedBackground = AppCompatResources.getDrawable(context, R.drawable.bg_cart_quantity_editor_focused)
        binding.root.background = defaultBackground

        anchorMinusButton = binding.cartQtyMinusButton

        binding.cartQtyPlusButton.setOnClickListener {
            if (qty < maxQty) {
                onPlusClickListener.invoke()
                qty++
                onValueChanged.invoke(qty)
            }
        }

        binding.cartQtyMinusButton.setOnClickListener {
            if (onMinusClickListener.invoke()) {
                qty--
                onValueChanged.invoke(qty)
            }
        }

        binding.cartQtyTextField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
//                if (!skipListener) {
//                    onValueChanged.invoke(s.toString().toIntOrZero())
//                }
//                skipListener = false
                setupButtons(s.toString().toIntOrZero())
            }
        })

        binding.cartQtyTextField.setOnFocusChangeListener { v, hasFocus ->
            binding.root.background = if (hasFocus) focusedBackground else defaultBackground
            onFocusChanged.invoke(v, hasFocus)
        }

        binding.cartQtyTextField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                qty = binding.cartQtyTextField.text.toString().toIntOrZero()
                onDoneListener.invoke()
            }
            false
        }
    }

    private fun setupButtons(qty: Int) {
        binding.cartQtyPlusButton.isEnabled = qty < maxQty
        val newIconId = if (qty <= minQty) IconUnify.DELETE_SMALL else IconUnify.REMOVE_16
        if (binding.cartQtyMinusButton.iconId != newIconId) {
            binding.cartQtyMinusButton.setImage(newIconId)
        }
    }

    fun setQuantity(qty: Int) {
        skipListener = true
        this.qty = qty
        setupButtons(qty)
        onValueChanged.invoke(qty)
    }

    fun getQuantity(): Int {
        return this.qty
    }
}
