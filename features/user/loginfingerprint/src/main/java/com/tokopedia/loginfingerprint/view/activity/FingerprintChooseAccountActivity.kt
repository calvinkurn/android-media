package com.tokopedia.loginfingerprint.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginfingerprint.view.fragment.ChooseAccountFingerprintFragment
import com.tokopedia.loginphone.chooseaccount.view.activity.ChooseAccountActivity

class FingerprintChooseAccountActivity: ChooseAccountActivity() {
    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null)
            bundle.putAll(intent.extras)
        return ChooseAccountFingerprintFragment.createInstance(bundle)
    }
}