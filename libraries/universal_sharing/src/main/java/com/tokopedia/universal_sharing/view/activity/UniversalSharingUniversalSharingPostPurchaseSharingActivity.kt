package com.tokopedia.universal_sharing.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet

class UniversalSharingUniversalSharingPostPurchaseSharingActivity : BaseActivity() {

    private var bottomSheet = UniversalSharingPostPurchaseBottomSheet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomSheetListener()
        showBottomSheet()
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        bottomSheet.show(
            supportFragmentManager,
            ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
        )
    }

    private fun setBottomSheetListener() {
        bottomSheet.setCloseClickListener {
            finish()
        }

        bottomSheet.setOnDismissListener {
            finish()
        }
    }
}
