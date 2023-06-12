package com.tokopedia.wallet.ovoactivation.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.wallet.ovoactivation.view.model.PhoneActionModel

/**
 * Created by nabillasabbaha on 24/09/18.
 */
interface IntroOvoContract {

    interface View : CustomerView {

        fun setApplinkButton(helpAllink: String, tncApplink: String)

        fun directPageWithApplink(registeredApplink: String)

        fun directPageWithExtraApplink(
            unRegisteredApplink: String,
            registeredApplink: String,
            phoneNumber: String,
            changeMsisdnApplink: String
        )

        fun showSnackbarErrorMessage(message: String)

        fun getErrorMessage(e: Throwable): String

        fun showProgressBar()

        fun hideProgressBar()

        fun showDialogErrorPhoneNumber(phoneActionModel: PhoneActionModel?)

        fun removeTokoCashCache()
    }

    interface Presenter : CustomerPresenter<View> {

        fun checkPhoneNumber()

        fun getBalanceWallet()

        fun onDestroyView()
    }
}
