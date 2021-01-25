package com.tokopedia.homenav.base.diffutil

import android.view.View
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel

/**
 * Created by Lukas on 21/10/20.
 */
interface HomeNavListener {
    fun onRefresh()
    fun onMenuClick(homeNavMenuDataModel: HomeNavMenuDataModel)
    fun onMenuImpression(homeNavMenuDataModel: HomeNavMenuDataModel)
    fun getUserId(): String
    fun onMenuBind(itemView: View, model: HomeNavMenuDataModel)
    fun onHeaderBind(itemView: View)
}