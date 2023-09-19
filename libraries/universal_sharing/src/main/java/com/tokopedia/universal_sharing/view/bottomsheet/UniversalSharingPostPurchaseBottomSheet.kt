package com.tokopedia.universal_sharing.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingBottomSheetListener

class UniversalSharingPostPurchaseBottomSheet: BottomSheetUnify() {

    private var listener: UniversalSharingBottomSheetListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomSheet()
    }

    fun setListener(listener: UniversalSharingBottomSheetListener) {
        this.listener = listener
    }

    private fun setupBottomSheet() {
        setTitle(getString(R.string.universal_sharing_post_purchase_bottomsheet_title))
        setBottomSheetView()
        setCloseClickListener {
            listener?.onCloseBottomSheet()
        }
    }
    private fun setBottomSheetView() {
        val view = View.inflate(
            activity, R.layout.universal_sharing_post_purchase_bottomsheet,
            null
        )
    }
}
