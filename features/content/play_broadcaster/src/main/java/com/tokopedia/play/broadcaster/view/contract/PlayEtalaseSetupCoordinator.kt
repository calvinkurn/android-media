package com.tokopedia.play.broadcaster.view.contract

import android.view.View

interface PlayEtalaseSetupCoordinator {

    fun openEtalaseDetail(
            etalaseId: String,
            sharedElements: List<View>
    )

    fun openSearchPage(
            keyword: String,
            sharedElements: List<View>
    )

    fun postponeEnterTransition()

    fun startPostponedEnterTransition()
}