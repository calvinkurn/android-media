package com.tokopedia.cartrevamp.view.bottomsheet

import android.os.Bundle
import com.tokopedia.cartrevamp.view.uimodel.CartNoteBottomSheetData
import com.tokopedia.unifycomponents.BottomSheetUnify

class CartNoteBottomSheet : BottomSheetUnify() {

    init {
        isDragable = false
        isHideable = true
        showCloseIcon = true
        showHeader = true
    }

    companion object {
        private const val TAG = "CartNoteBottomSheet"
        private const val DATA = "data"

        fun newInstance(data: CartNoteBottomSheetData): CartNoteBottomSheet {
            return CartNoteBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(DATA, data)
                }
            }
        }
    }
}
