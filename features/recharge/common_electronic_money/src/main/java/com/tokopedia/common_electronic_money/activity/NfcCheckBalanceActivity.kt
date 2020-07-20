package com.tokopedia.common_electronic_money.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.common_electronic_money.R

open abstract class NfcCheckBalanceActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.emoney_toolbar_title_etoll_check_balance)
    }

    override fun getCloseButton(): Int {
        return com.tokopedia.resources.common.R.drawable.ic_system_close_default
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }
}