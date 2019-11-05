package com.tokopedia.tokopoints.view.contract

import android.content.res.Resources
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface TokopointAddPointContract {

    interface View : CustomerView {
        fun inflatePointsData(resources: Resources)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getRewardPoint(resources: Resources)
    }
}
