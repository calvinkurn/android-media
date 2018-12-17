package com.tokopedia.browse

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.browse.common.di.DigitalBrowseComponent
import com.tokopedia.browse.common.di.DaggerDigitalBrowseComponent

/**
 * @author by furqan on 19/09/18.
 */

object DigitalBrowseComponentInstance {
    private var digitalBrowseComponent: DigitalBrowseComponent? = null

    fun getDigitalBrowseComponent(application: Application): DigitalBrowseComponent {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DaggerDigitalBrowseComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }

        return digitalBrowseComponent as DigitalBrowseComponent
    }
}
