package com.tokopedia.activation.ui

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ActivationPageActivity : BaseSimpleActivity() {


    override fun getNewFragment(): Fragment? {
        return ActivationPageFragment()
    }


}