package com.tokopedia.atc_variant.view

import com.tokopedia.abstraction.base.view.listener.CustomerView

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

interface NormalCheckoutContract {

    interface View : CustomerView {

        fun showToasterError(message: String?)

        fun navigateCheckoutToThankYouPage(appLink: String)

    }

}