package com.tokopedia.play.broadcaster.view.contract

import android.view.View
import androidx.fragment.app.Fragment

interface PlayEtalaseSetupCoordinator {

    fun openEtalaseDetail(
            etalaseId: String,
            sharedElements: List<View>,
            source: Class<out Fragment>
    )

    fun openSearchPage(keyword: String, source: Class<out Fragment>)

    fun openProductSearchPage(keyword: String, source: Class<out Fragment>)

    fun postponeEnterTransition()

    fun startPostponedEnterTransition()

    fun goBack(clazz: Class<out Fragment>)

    fun showBottomAction(shouldShow: Boolean)
}