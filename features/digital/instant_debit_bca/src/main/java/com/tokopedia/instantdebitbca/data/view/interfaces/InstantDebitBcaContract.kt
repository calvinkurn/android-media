package com.tokopedia.instantdebitbca.data.view.interfaces

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * Created by nabillasabbaha on 25/03/19.
 */
interface InstantDebitBcaContract {

    interface View : CustomerView {
        fun openWidgetBca(accessToken: String)

        fun redirectPageAfterRegisterBca()

        fun showErrorMessage(throwable: Throwable)

        fun createAndSetBcaWidget()
    }

    interface Presenter : CustomerPresenter<View> {
        fun getAccessTokenBca()

        fun notifyDebitRegisterBca(debitData: String, deviceId: String)
        fun notifyDebitRegisterEditLimit(debitData: String, deviceId: String)
    }
}
