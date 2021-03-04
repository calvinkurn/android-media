package com.tokopedia.homenav.base.diffutil

import com.tokopedia.homenav.base.datamodel.HomeNavGlobalErrorDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel

/**
 * Created by Lukas on 20/10/20.
 */

interface HomeNavTypeFactory{
    fun type(visitable: HomeNavMenuDataModel) : Int
    fun type(visitable: HomeNavTitleDataModel) : Int
    fun type(visitable: HomeNavGlobalErrorDataModel) : Int
    fun type(visitable: HomeNavTickerDataModel) : Int
}