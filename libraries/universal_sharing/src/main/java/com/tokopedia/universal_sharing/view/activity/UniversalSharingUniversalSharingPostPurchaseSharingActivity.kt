package com.tokopedia.universal_sharing.view.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalSharingPostPurchaseBottomSheet

class UniversalSharingUniversalSharingPostPurchaseSharingActivity : BaseActivity() {

    private var bottomSheet: UniversalSharingPostPurchaseBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
        setBottomSheetListener()
        showBottomSheet()
    }

    @SuppressLint("DeprecatedMethod")
    private fun setupBottomSheet() {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                ApplinkConstInternalCommunication.PRODUCT_LIST_DATA,
                UniversalSharingPostPurchaseModel::class.java
            ) ?: UniversalSharingPostPurchaseModel()
        } else {
            intent.getParcelableExtra(ApplinkConstInternalCommunication.PRODUCT_LIST_DATA)
                ?: UniversalSharingPostPurchaseModel()
        }

        bottomSheet = UniversalSharingPostPurchaseBottomSheet.newInstance(data)
    }

    private fun showBottomSheet() {
        currentFocus?.hideKeyboard()
        bottomSheet?.show(
            supportFragmentManager,
            ::UniversalSharingUniversalSharingPostPurchaseSharingActivity.name
        )
    }

    private fun setBottomSheetListener() {
        bottomSheet?.setCloseClickListener {
            finish()
        }
        bottomSheet?.setOnDismissListener {
            finish()
        }
    }
}
