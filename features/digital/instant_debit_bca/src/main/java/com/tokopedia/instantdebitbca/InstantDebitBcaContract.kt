package com.tokopedia.instantdebitbca

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * Created by nabillasabbaha on 21/03/19.
 */
interface InstantDebitBcaContract {

    interface View : CustomerView {
        fun openWidgetBca(accessToken : String)

        fun showErrorMessage(throwable: Throwable)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getAccessTokenInstantDebitBca()

        fun onDestroy()
    }
}