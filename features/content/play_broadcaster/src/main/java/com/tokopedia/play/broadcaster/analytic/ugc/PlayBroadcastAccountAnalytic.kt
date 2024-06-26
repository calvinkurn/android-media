package com.tokopedia.play.broadcaster.analytic.ugc

/**
 * Created by fachrizalmrsln on 22/09/22
 */
interface PlayBroadcastAccountAnalytic {
    fun onClickAccountDropdown()
    fun onClickAccount()
    fun onClickAccountAndHaveDraft()
    fun onClickCancelSwitchAccount()
    fun onClickConfirmSwitchAccount()
    fun onClickCloseOnboardingUGC()
    fun onClickUsernameFieldCompleteOnboardingUGC()
    fun onClickCheckBoxCompleteOnboardingUGC()
    fun onClickNextOnboardingUGC()
    fun onClickCloseTNCSGC()
    fun onClickOkButtonTNCSGC()
}
