package com.tokopedia.usercomponents.explicit.fake_view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

class ExplicitDebugActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment =
        ExplicitDebugFragment()
}