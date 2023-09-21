package com.tokopedia.universal_sharing.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet

class UniversalSharingUniversalSharingPostPurchaseSharingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBottomSheet()
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        val bottomSheet = UniversalSharingPostPurchaseBottomSheet()
        bottomSheet.show(
            supportFragmentManager,
            ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
        )
    }
}
