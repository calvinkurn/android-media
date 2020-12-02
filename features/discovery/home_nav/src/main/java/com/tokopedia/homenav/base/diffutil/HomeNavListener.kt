package com.tokopedia.homenav.base.diffutil

import com.tokopedia.homenav.base.viewmodel.HomeNavMenuViewModel

/**
 * Created by Lukas on 21/10/20.
 */
interface HomeNavListener {
    fun onRefresh()
    fun onMenuClick(homeNavMenuViewModel: HomeNavMenuViewModel)
    fun onMenuImpression(homeNavMenuViewModel: HomeNavMenuViewModel)
    fun getUserId(): String
}