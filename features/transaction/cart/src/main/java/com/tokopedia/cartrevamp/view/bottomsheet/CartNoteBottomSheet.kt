package com.tokopedia.cartrevamp.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
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
            renderContent(it)
        } ?: dismiss()
    }

    private fun renderContent(data: CartNoteBottomSheetData) {
        setTitle(getString(com.tokopedia.cart.R.string.cart_label_add_note))
        binding?.apply {
            iuCartItem.loadImage(data.productImage)
            labelProductName.text = data.productName
            labelProductVariant.text = data.variant
            textAreaNote.setMessage(data.note)
            textAreaNote.isEnabled = data.note.isNotBlank()
        }
    }
}
