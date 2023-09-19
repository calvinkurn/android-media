package com.tokopedia.universal_sharing.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.UniversalSharingBottomSheetListener

class UniversalSharingPostPurchaseSharingActivity:
    BaseActivity(),
    UniversalSharingBottomSheetListener {

    private var bottomSheet: UniversalSharingPostPurchaseBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomSheet()
        showBottomSheet()
    }

    private fun setBottomSheet() {
        bottomSheet = UniversalSharingPostPurchaseBottomSheet()
        bottomSheet?.setListener(this)
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        bottomSheet?.show(supportFragmentManager, ::UniversalSharingPostPurchaseSharingActivity.name)
    }

    override fun onCloseBottomSheet() {
        finish()
    }
}
