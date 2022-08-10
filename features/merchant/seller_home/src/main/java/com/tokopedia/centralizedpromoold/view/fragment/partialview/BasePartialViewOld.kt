package com.tokopedia.centralizedpromoold.view.fragment.partialview

import androidx.annotation.CallSuper
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.centralizedpromoold.view.fragment.CoachMarkListener
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.databinding.FragmentCentralizedPromoOldBinding

abstract class BasePartialViewOld<T : BaseUiModel>(
    private val coachMarkListener: CoachMarkListener,
    val binding: FragmentCentralizedPromoOldBinding,
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