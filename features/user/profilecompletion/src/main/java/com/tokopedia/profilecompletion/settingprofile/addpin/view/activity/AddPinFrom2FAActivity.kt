package com.tokopedia.profilecompletion.settingprofile.addpin.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.profilecompletion.settingprofile.addpin.view.fragment.AddPinFrom2FAFragment

class AddPinFrom2FAActivity : AddPinActivity() {
    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddPinFrom2FAFragment.createInstance(bundle)
    }
}
