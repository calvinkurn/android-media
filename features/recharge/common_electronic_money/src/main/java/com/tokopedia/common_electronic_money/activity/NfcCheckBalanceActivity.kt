package com.tokopedia.common_electronic_money.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.common_electronic_money.R

open abstract class NfcCheckBalanceActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_nfc_common
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.nfc_checkout_toolbar
    }
}