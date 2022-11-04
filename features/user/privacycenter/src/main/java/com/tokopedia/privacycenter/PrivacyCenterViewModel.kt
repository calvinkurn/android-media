package com.tokopedia.privacycenter

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.domain.DarkModeUseCase
import javax.inject.Inject

class PrivacyCenterViewModel @Inject constructor(
    private val darkModeUseCase: DarkModeUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    var isUsingDarkMode = false
        private set

    init {
        isUsingDarkMode()
    }

    private fun isUsingDarkMode() {
        isUsingDarkMode = darkModeUseCase.isDarkModeActivated()
    }

}
