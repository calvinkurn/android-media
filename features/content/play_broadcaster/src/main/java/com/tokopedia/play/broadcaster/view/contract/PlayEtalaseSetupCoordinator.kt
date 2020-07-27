package com.tokopedia.play.broadcaster.view.contract

import android.view.View
import androidx.fragment.app.Fragment

interface PlayEtalaseSetupCoordinator {

    fun openEtalaseDetail(
            etalaseId: String,
            sharedElements: List<View>
    )

    fun getParent(): Fragment

    fun openSearchPage(keyword: String)

    fun openProductSearchPage(keyword: String)

    fun postponeEnterTransition()

    fun startPostponedEnterTransition()

    fun goBack(clazz: Class<out Fragment>)

    fun showBottomAction(shouldShow: Boolean)
}