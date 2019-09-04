package com.tokopedia.affiliate.feature.dashboard.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * Created by jegul on 2019-09-04.
 */
interface AffiliateProductBoughtContract {

    interface View : CustomerView {

    }

    interface Presenter : CustomerPresenter<View> {

        fun loadProductBoughtByType(type: Int, cursor: String)
    }
}