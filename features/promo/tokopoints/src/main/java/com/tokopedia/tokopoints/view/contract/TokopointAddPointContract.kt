package com.tokopedia.tokopoints.view.contract

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.tokopoints.view.model.addpointsection.SheetHowToGetV2

interface TokopointAddPointContract {

    interface View : CustomerView {
        fun inflatePointsData(item: SheetHowToGetV2)
        fun inflateContainerLayout(success: Boolean)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getRewardPoint(resources: Resources)
    }
}

