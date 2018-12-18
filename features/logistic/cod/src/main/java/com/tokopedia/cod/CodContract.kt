package com.tokopedia.cod

/**
 * Created by fajarnuha on 17/12/18.
 */
interface CodContract {

    interface View {
        fun loadInformation()
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun submitConfirmation()
    }

}