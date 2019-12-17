package com.tokopedia.instantloan.view.contractor

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.instantloan.data.model.response.GqlFilterData

interface OnlineLoanContractor {

    interface View : CustomerView {
        fun setFilterDataForOnlineLoan(gqlFilterData: GqlFilterData)
        fun isViewAttached(): Boolean
    }

    interface Presenter : CustomerPresenter<View> {
        fun isViewAttached(): Boolean
        fun getView(): View
    }
}