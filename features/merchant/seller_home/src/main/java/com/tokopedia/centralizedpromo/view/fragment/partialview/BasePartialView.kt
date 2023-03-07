package com.tokopedia.centralizedpromo.view.fragment.partialview

import androidx.annotation.CallSuper
import com.tokopedia.centralizedpromo.view.LoadingType
import com.tokopedia.centralizedpromo.view.fragment.CoachMarkListener
import com.tokopedia.centralizedpromo.view.model.BaseUiModel
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhome.databinding.FragmentCentralizedPromoBinding

abstract class BasePartialView<T : BaseUiModel>(
    private val coachMarkListener: CoachMarkListener,
    val binding: FragmentCentralizedPromoBinding,
    var showCoachMark: Boolean
) {
    var readyToShowCoachMark: Boolean = false

    var impressionEventSent: Boolean = false

    val impressHolder: ImpressHolder = ImpressHolder()

    abstract fun bindSuccessData(data: T)
    abstract fun renderLoading(loadingType: LoadingType = LoadingType.ALL)
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