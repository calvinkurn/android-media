package com.tokopedia.affiliate.feature.onboarding.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by milhamj on 10/4/18.
 */
interface UsernameInputContract {
    interface View : CustomerView {
        fun getContext(): Context?

        fun showLoading()

        fun hideLoading()

        fun onSuccessGetUsernameSuggestion(suggestions: List<String>)

        fun onSuggestionClicked(username: String)

        fun onSuccessRegisterUsername()

        fun onErrorRegisterUsername(message: String)

        fun onErrorInputRegisterUsername(message: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getUsernameSuggestion()

        fun registerUsername(username: String)
    }
}
