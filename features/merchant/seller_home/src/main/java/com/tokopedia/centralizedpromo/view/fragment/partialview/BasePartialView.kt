package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.annotation.CallSuper
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.BaseUiModel

abstract class BasePartialView<T : BaseUiModel>(
        private val coachMarkListener: CoachMarkListener,
        val view: View,
        var shouldWaitForCoachMark: Boolean
) {
    var impressionEventSent: Boolean = false

    abstract fun bindSuccessData(data: T)
    abstract fun renderLoading()
    abstract fun getSuccessView(): View?

    @CallSuper
    open fun renderError(cause: Throwable) {
        shouldNotifyFragmentForCoachMark()
    }

    open fun renderSuccess(data: T) {
        bindSuccessData(data)
        shouldNotifyFragmentForCoachMark()
    }

    protected fun shouldNotifyFragmentForCoachMark() {
        if (shouldWaitForCoachMark) {
            shouldWaitForCoachMark = false
            coachMarkListener.onViewReadyForCoachMark()
        }
    }
}