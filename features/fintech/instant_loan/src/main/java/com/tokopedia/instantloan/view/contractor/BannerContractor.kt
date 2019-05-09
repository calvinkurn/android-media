package com.tokopedia.instantloan.view.contractor

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.instantloan.data.model.response.GqlLendingDataResponse

interface BannerContractor {

    interface View : CustomerView {

        fun renderLendingData(gqlLendingDataResponse: GqlLendingDataResponse)

        fun nextBanner()

        fun previousBanner()
    }

    interface Presenter : CustomerPresenter<View> {
        fun getLendingData()

        fun deAttachView()
    }
}
