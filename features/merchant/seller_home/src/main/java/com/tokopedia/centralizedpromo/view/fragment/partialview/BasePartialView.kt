package com.tokopedia.centralizedpromo.view.fragment.partialview

import android.view.View
import androidx.annotation.CallSuper
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.model.ImpressHolder

abstract class BasePartialView<T : BaseUiModel>(
        private val coachMarkListener: CoachMarkListener,
        val view: View,
        var showCoachMark: Boolean
) {
    var readyToShowCoachMark: Boolean = false

    var impressionEventSent: Boolean = false

    val impressHolder: ImpressHolder = ImpressHolder()

    abstract fun bindSuccessData(data: T)
    abstract fun renderLoading()
    abstract fun getCoachMarkItem(): CoachMarkItem?
    abstract fun shouldShowCoachMark(): Boolean

    @CallSuper
    open fun renderError(cause: Throwable) {
        shouldNotifyFragmentForCoachMark()
    }

    open fun renderSuccess(data: T) {
        bindSuccessData(data)
        shouldNotifyFragmentForCoachMark()
    }

    protected fun shouldNotifyFragmentForCoachMark() {
        if (showCoachMark) {
            readyToShowCoachMark = true
            coachMarkListener.onViewReadyForCoachMark()
        }
    }
}