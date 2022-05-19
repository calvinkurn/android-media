package com.tokopedia.creation.presentation.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.creation.presentation.fragment.CreationFragment

class CreationActivity: BaseSimpleActivity() {

    override fun getNewFragment() = CreationFragment()

}