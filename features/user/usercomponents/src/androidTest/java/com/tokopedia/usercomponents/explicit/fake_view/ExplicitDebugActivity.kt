package com.tokopedia.usercomponents.explicit.fake_view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.usercomponents.explicit.view.ExplicitData
import com.tokopedia.usercomponents.explicit.view.interactor.ExplicitViewContract

class ExplicitDebugActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment =
        ExplicitDebugFragment()
}