package com.tokopedia.cartrevamp.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.addTextChangedListener
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.LayoutBottomsheetCartNoteBinding
import com.tokopedia.cartrevamp.view.uimodel.CartNoteBottomSheetData
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CartNoteBottomSheet : BottomSheetUnify() {

    init {
        isDragable = false
        isHideable = true
        showCloseIcon = true
        showHeader = true
    }

    private var binding by autoClearedNullable<LayoutBottomsheetCartNoteBinding>()
    private var listener: ((note: String) -> Unit)? = null
    private var data: CartNoteBottomSheetData? = null

    companion object {
        const val TAG = "CartNoteBottomSheet"
        private const val DATA = "data"

        fun newInstance(data: CartNoteBottomSheetData): CartNoteBottomSheet {
            return CartNoteBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutBottomsheetCartNoteBinding
            .inflate(LayoutInflater.from(context), null, false)
        setChild(binding?.root)

        data = arguments?.getParcelable(DATA)
        data?.let {
            adjustViewConstraint(it)
            renderContent(it)
        } ?: dismiss()
    }

    private fun adjustViewConstraint(data: CartNoteBottomSheetData) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding?.clNoteBottomsheetContainer)
        constraintSet.apply {
            if (data.variant.isNotBlank()) {
                connect(
                    R.id.label_product_name,
                    ConstraintSet.TOP,
                    R.id.iu_cart_item,
                    ConstraintSet.TOP
                )
                clear(R.id.label_product_name)
            } else {
                connect(
                    R.id.label_product_name,
                    ConstraintSet.TOP,
                    R.id.iu_cart_item,
                    ConstraintSet.TOP
                )
                connect(
                    R.id.label_product_name,
                    ConstraintSet.BOTTOM,
                    R.id.iu_cart_item,
                    ConstraintSet.BOTTOM
                )
            }
            applyTo(binding?.clNoteBottomsheetContainer)
        }
    }

    private fun renderContent(data: CartNoteBottomSheetData) {
        setTitle(getString(R.string.cart_label_add_note))
        binding?.apply {
            iuCartItem.loadImage(data.productImage)
            labelProductName.text = data.productName
            labelProductVariant.text = data.variant
            textAreaNote.editText.setText(data.note)
            textAreaNote.editText.addTextChangedListener {
                btnSave.isEnabled = it.toString() != data.note
            }
            btnSave.isEnabled = data.note.isNotBlank() && textAreaNote.editText.text.toString() != data.note
            btnSave.setOnClickListener {
                listener?.invoke(textAreaNote.editText.text.toString().trim())
                dismiss()
            }
        }
    }

    fun setListener(listener: (note: String) -> Unit) {
        this.listener = listener
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}
