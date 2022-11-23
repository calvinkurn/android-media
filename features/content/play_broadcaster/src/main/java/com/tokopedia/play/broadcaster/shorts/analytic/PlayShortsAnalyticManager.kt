package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.navigation.shorts.PlayShorts
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.util.orUnknown
import com.tokopedia.play.broadcaster.shorts.view.fragment.PlayShortsPreparationFragment
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 23, 2022
 */
class PlayShortsAnalyticManager @Inject constructor(
    private val analytic: PlayShortsAnalytic,
    private val dispatchers: CoroutineDispatchers,
) {

    private var mDataSource: DataSource? = null

    private val selectedAccount: ContentAccountUiModel
        get() = mDataSource?.getSelectedAccount().orUnknown()

    fun setDataSource(dataSource: DataSource?) {
        mDataSource = dataSource
    }

    fun observe(
        scope: CoroutineScope,
        event: EventBus<out Any>,
    ) {
        scope.launch(dispatchers.computation) {
            event.subscribe().collect {
                when(it) {
                    is PlayShortsPreparationFragment.Event.ClickBack ->
                        analytic.clickBackOnPreparationPage(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickCloseCoachMark ->
                        analytic.clickCloseCoachOnPreparationPage(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickSwitchAccount ->
                        analytic.clickSwitchAccount(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickCloseSwitchAccount ->
                        analytic.clickCloseSwitchAccount(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickUserAccount ->
                        analytic.clickUserAccount(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickShopAccount ->
                        analytic.clickShopAccount(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ViewSwitchAccountToShopConfirmation ->
                        analytic.viewSwitchAccountToShopConfirmation(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickCancelSwitchAccountToShop ->
                        analytic.clickCancelSwitchAccountToShop(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ViewSwitchAccountToUserConfirmation ->
                        analytic.viewSwitchAccountToUserConfirmation(selectedAccount)

                    is PlayShortsPreparationFragment.Event.ClickCancelSwitchAccountToUser ->
                        analytic.clickCancelSwitchAccountToUser(selectedAccount)
                }
            }
        }
    }

    interface DataSource {
        fun getSelectedAccount(): ContentAccountUiModel
    }
}
