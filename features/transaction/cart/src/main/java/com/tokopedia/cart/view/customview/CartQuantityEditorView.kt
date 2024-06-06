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

open class CartQuantityEditorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var binding: LayoutCartQuantityEditorBinding

    val anchorMinusButton: IconUnify

    private var qty: Int = 0

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

        anchorMinusButton = binding.quantityEditorMin

        binding.quantityEditorPlus.setOnClickListener {
            if (qty < maxQty) {
                onPlusClickListener.invoke()
                qty++
                binding.quantityEditorQty.setText("$qty")
                setupButtons(qty)
                onValueChanged.invoke(qty)
            }
        }

        binding.quantityEditorMin.setOnClickListener {
            if (onMinusClickListener.invoke()) {
                qty--
                binding.quantityEditorQty.setText("$qty")
                setupButtons(qty)
                onValueChanged.invoke(qty)
            }
        }

        binding.quantityEditorQty.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val value = s.toString().toIntOrZero()
                setupButtons(value)
                qty = value
            }
        })

        binding.quantityEditorQty.setOnFocusChangeListener { v, hasFocus ->
            binding.root.background = if (hasFocus) focusedBackground else defaultBackground
            onFocusChanged.invoke(v, hasFocus)
        }

        binding.quantityEditorQty.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onDoneListener.invoke()
            }
            false
        }
    }

    private fun setupButtons(qty: Int) {
        binding.quantityEditorPlus.isEnabled = qty < maxQty
        val newIconId = if (qty <= minQty) IconUnify.DELETE_SMALL else IconUnify.REMOVE_16
        if (binding.quantityEditorMin.iconId != newIconId) {
            binding.quantityEditorMin.setImage(newIconId)
        }
    }

    fun setQuantity(qty: Int) {
        this.qty = qty
        setupButtons(qty)
        binding.quantityEditorQty.setText("$qty")
    }

    fun getQuantity(): Int {
        return this.qty
    }
}
