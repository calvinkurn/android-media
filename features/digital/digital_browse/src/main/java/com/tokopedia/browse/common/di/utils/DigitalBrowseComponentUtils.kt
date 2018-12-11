package com.tokopedia.browse.common.di.utils

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.browse.common.di.DigitalBrowseComponent
import com.tokopedia.browse.common.di.DaggerDigitalBrowseComponent

/**
 * @author by furqan on 30/08/18.
 */

object DigitalBrowseComponentUtils {
    private var digitalBrowseComponent: DigitalBrowseComponent? = null

    fun getDigitalBrowseComponent(application: Application): DigitalBrowseComponent {
        if (digitalBrowseComponent == null) {
            digitalBrowseComponent = DaggerDigitalBrowseComponent.builder().baseAppComponent(
                    (application as BaseMainApplication).baseAppComponent).build()
        }
        return digitalBrowseComponent as DigitalBrowseComponent
    }
}
