package com.tokopedia.logisticaddaddress.features.autocomplete

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class AutoCompleteActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return AutoCompleteFragment.newInstance()
    }

    override fun isShowCloseButton(): Boolean = true

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0,0)
    }

}