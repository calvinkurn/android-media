package com.tokopedia.home_account.explicitprofile.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ExplicitProfileActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        return ExplicitProfileFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
}