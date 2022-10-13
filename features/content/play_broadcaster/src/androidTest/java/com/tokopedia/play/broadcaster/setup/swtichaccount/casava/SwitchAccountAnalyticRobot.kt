package com.tokopedia.play.broadcaster.setup.swtichaccount.casava

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalyticImpl
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.parentBroViewModel
import com.tokopedia.play.broadcaster.setup.preparationBroViewModel
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPreparationFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlin.LazyThreadSafetyMode.NONE

/**
 * Created by fachrizalmrsln on 28/09/22
 */
class SwitchAccountAnalyticRobot(
    dataStore: PlayBroadcastDataStore,
    hydraConfigStore: HydraConfigStore,
    userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers,
    repo: PlayBroadcastRepository,
    channelUseCase: GetChannelUseCase,
    addedChannelTagsUseCase: GetAddedChannelTagsUseCase,
) {

    private val parentViewModel: PlayBroadcastViewModel by lazy {
        parentBroViewModel(
            dataStore = dataStore,
            hydraConfigStore = hydraConfigStore,
            userSession = userSessionInterface,
            dispatcher = dispatcher,
            repo = repo,
            getChannelUseCase = channelUseCase,
            getAddedChannelTagsUseCase = addedChannelTagsUseCase,
        )
    }

    private val preparationViewModel: PlayBroadcastPrepareViewModel by lazy(NONE) {
        preparationBroViewModel(dispatcher = dispatcher)
    }

    private val parentViewModelFactoryCreator = object : PlayBroadcastViewModelFactory.Creator {
        override fun create(activity: FragmentActivity): PlayBroadcastViewModelFactory {
            return PlayBroadcastViewModelFactory(
                activity = activity,
                playBroViewModelFactory = object : PlayBroadcastViewModel.Factory {
                    override fun create(handle: SavedStateHandle): PlayBroadcastViewModel {
                        return parentViewModel
                    }
                }
            )
        }
    }

    private val viewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return preparationViewModel as T
        }
    }

    private val scenario = launchFragmentInContainer {
        parentViewModel.submitAction(PlayBroadcastAction.GetAccountList)

        PlayBroadcastPreparationFragment(
            parentViewModelFactoryCreator = parentViewModelFactoryCreator,
            viewModelFactory = viewModelFactory,
            analytic = PlayBroadcastAnalytic(
                userSession = analyticUserSession,
                interactiveAnalytic = mockk(relaxed = true),
                setupMenuAnalytic = mockk(relaxed = true),
                setupTitleAnalytic = mockk(relaxed = true),
                setupCoverAnalytic = mockk(relaxed = true),
                setupProductAnalytic = mockk(relaxed = true),
                summaryAnalytic = mockk(relaxed = true),
                scheduleAnalytic = mockk(relaxed = true),
                pinProductAnalytic = mockk(relaxed = true),
                accountAnalytic = PlayBroadcastAccountAnalyticImpl(analyticUserSession, hydraConfigStore),
            ),
            analyticManager = mockk(relaxed = true)
        )
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun onClickDropDownAccount() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.img_content_creator_expand)
        ).perform(ViewActions.click())
        delay(500L)
    }

    fun onClickAccountFromBottomSheet() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.rv_feed_account)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1, ViewActions.click()
            )
        )
    }

    fun onSwitchAccountConfirmationDialogShown() = chainable {
        delay(500L)
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.dialog.R.id.dialog_btn_primary)
        ).check(matches(ViewMatchers.isCompletelyDisplayed()))
    }

    fun onClickCancelSwitchWhenHavingDraft() = chainable {
        delay(500L)
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.dialog.R.id.dialog_btn_primary)
        ).perform(ViewActions.click())
    }

    fun onClickConfirmSwitchWhenHavingDraft() = chainable {
        delay(500L)
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.dialog.R.id.dialog_btn_secondary_long)
        ).perform(ViewActions.click())
    }

    private fun chainable(fn: () -> Unit): SwitchAccountAnalyticRobot {
        fn()
        return this
    }
}
