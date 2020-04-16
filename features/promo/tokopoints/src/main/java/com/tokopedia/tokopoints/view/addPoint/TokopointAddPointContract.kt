package com.tokopedia.tokopoints.view.addPoint

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.tokopoints.view.model.addpointsection.SheetHowToGetV2

interface TokopointAddPointContract {

    interface View : CustomerView {
        fun inflatePointsData(item: SheetHowToGetV2)
        fun inflateContainerLayout(success: Boolean)
    }

    interface Presenter {
        fun getRewardPoint()
    }
}

