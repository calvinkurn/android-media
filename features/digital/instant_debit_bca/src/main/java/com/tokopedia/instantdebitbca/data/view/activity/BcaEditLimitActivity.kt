package com.tokopedia.instantdebitbca.data.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.instantdebitbca.data.view.fragment.EditLimitFragment

class BcaEditLimitActivity : InstantDebitBcaActivity() {

    companion object {
        const val XCOID = "xcoid"
    }

    override fun getNewFragment(): Fragment {
        var callback = ""
        intent?.extras?.getString(CALLBACK_URL)?.let {
            callback = it
        }
        var xcoid = ""
        intent?.extras?.getString(XCOID)?.let {
            xcoid = it
        }
        return EditLimitFragment.newInstance(callback, xcoid)
    }
}