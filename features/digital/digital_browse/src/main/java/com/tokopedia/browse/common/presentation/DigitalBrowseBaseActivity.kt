package com.tokopedia.browse.common.presentation

import android.os.Bundle
import android.os.PersistableBundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.browse.common.di.DigitalBrowseComponent
import com.tokopedia.browse.common.di.utils.DigitalBrowseComponentUtils

/**
 * @author by furqan on 30/08/18.
 */

abstract class DigitalBrowseBaseActivity : BaseSimpleActivity() {

    private var digitalBrowseComponent: DigitalBrowseComponent? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        initInjector()
    }

    private fun initInjector() {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DigitalBrowseComponentUtils.getDigitalBrowseComponent(application)
        }
        digitalBrowseComponent!!.inject(this)
    }
}
