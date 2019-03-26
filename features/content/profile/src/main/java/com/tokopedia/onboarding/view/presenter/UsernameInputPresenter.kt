package com.tokopedia.onboarding.view.presenter


import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.onboarding.domain.usecase.GetUsernameSuggestionUseCase
import com.tokopedia.onboarding.domain.usecase.RegisterUsernameUseCase
import com.tokopedia.onboarding.view.listener.UsernameInputContract
import com.tokopedia.onboarding.view.subscriber.GetUsernameSuggestionSubscriber
import com.tokopedia.onboarding.view.subscriber.RegisterUsernameSubscriber

import javax.inject.Inject

/**
 * @author by milhamj on 10/4/18.
 */
class UsernameInputPresenter @Inject constructor(
        private val getUsernameSuggestionUseCase: GetUsernameSuggestionUseCase,
        private val registerUsernameUseCase: RegisterUsernameUseCase)
    : BaseDaggerPresenter<UsernameInputContract.View>(), UsernameInputContract.Presenter {

    override fun detachView() {
        super.detachView()
        getUsernameSuggestionUseCase.unsubscribe()
        registerUsernameUseCase.unsubscribe()
    }

    override fun getUsernameSuggestion() {
        view?.showLoading()
        getUsernameSuggestionUseCase.execute(GetUsernameSuggestionSubscriber(view))
    }

    override fun registerUsername(username: String) {
        view?.showLoading()
        registerUsernameUseCase.execute(
                RegisterUsernameUseCase.createRequestParams(username),
                RegisterUsernameSubscriber(view)
        )
    }
}
