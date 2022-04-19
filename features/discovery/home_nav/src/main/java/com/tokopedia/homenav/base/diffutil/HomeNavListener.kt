package com.tokopedia.homenav.base.diffutil

import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel

/**
 * Created by Lukas on 21/10/20.
 */
interface HomeNavListener {
    fun onRefresh()
    fun onMenuClick(homeNavMenuDataModel: HomeNavMenuDataModel)
    fun onMenuImpression(homeNavMenuDataModel: HomeNavMenuDataModel)
    fun getUserId(): String
    fun getReviewCounterAbIsUnify(): Boolean
}