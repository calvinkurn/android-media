package com.tokopedia.developer_options.presentation.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.coachmark.CoachMark2

/**
 * Created by yfsx on 6/7/21.
 */
class CoachmarkDisableActivity : BaseSimpleActivity(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        CoachMark2.isCoachmmarkShowAllowed = false
        finish()
    }

    override fun getNewFragment(): Fragment? {
        return null
    }
}