package com.tokopedia.csat_rating.di

import android.app.Application
import android.content.Context

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.csat_rating.di.general.CsatComponentCommon
import com.tokopedia.csat_rating.di.general.CsatModuleCommon
import com.tokopedia.csat_rating.di.general.DaggerCsatComponentCommon

/**
 * Created by nakama on 11/12/17.
 */

class CsatRatingComponentHelper {

    fun getComponent(
        application: Application,
        activityContext: Context
    ): CsatComponentCommon = DaggerCsatComponentCommon.builder().baseAppComponent(
        (application as BaseMainApplication).baseAppComponent
    ).csatModuleCommon(CsatModuleCommon(activityContext)).build()

}
