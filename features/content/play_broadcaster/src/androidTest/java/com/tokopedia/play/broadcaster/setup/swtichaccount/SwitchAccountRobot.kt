package com.tokopedia.play.broadcaster.setup.swtichaccount

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistryOwner
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.onboarding.data.UGCOnboardingRepositoryImpl
import com.tokopedia.content.common.onboarding.view.bottomsheet.UserCompleteOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.bottomsheet.UserTnCOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.strategy.UGCCompleteOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.strategy.UGCTncOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.strategy.factory.UGCOnboardingStrategyFactory
import com.tokopedia.content.common.onboarding.view.viewmodel.UGCOnboardingViewModel
import com.tokopedia.content.common.onboarding.view.viewmodel.factory.UGCOnboardingViewModelFactory
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.factory.PlayBroTestFragmentFactory
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.parentBroViewModel
import com.tokopedia.play.broadcaster.setup.preparationBroViewModel
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPreparationFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalytic
import com.tokopedia.play.broadcaster.analytic.ugc.PlayBroadcastAccountAnalyticImpl
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk
import kotlin.LazyThreadSafetyMode.NONE
import com.tokopedia.content.common.R as contentR

/**
 * Created by fachrizalmrsln on 28/09/22
 */
class SwitchAccountRobot(
    dataStore: PlayBroadcastDataStore,
    hydraConfigStore: HydraConfigStore,
    userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers,
    repo: PlayBroadcastRepository,
    channelUseCase: GetChannelUseCase,
    addedChannelTagsUseCase: GetAddedChannelTagsUseCase,
    sharedPreferences: HydraSharedPreferences,
) {
    private val context = InstrumentationRegistry.getInstrumentation().context

    private val parentViewModel: PlayBroadcastViewModel by lazy {
        parentBroViewModel(
            dataStore = dataStore,
            hydraConfigStore = hydraConfigStore,
            userSession = userSessionInterface,
            dispatcher = dispatcher,
            repo = repo,
            getChannelUseCase = channelUseCase,
            getAddedChannelTagsUseCase = addedChannelTagsUseCase,
            sharedPref = sharedPreferences,
        ).apply {
            submitAction(PlayBroadcastAction.GetAccountList())
        }
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

    private val playAnalytic = PlayBroadcastAnalytic(
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
        shortsEntryPointAnalytic = mockk(relaxed = true),
    )

    private val ugcViewModelFactory = object : UGCOnboardingViewModelFactory.Creator {
        override fun create(
            owner: SavedStateRegistryOwner,
            onboardingStrategy: UGCOnboardingStrategy
        ): UGCOnboardingViewModelFactory {
            return UGCOnboardingViewModelFactory(
                owner,
                onboardingStrategy,
                UGCOnboardingViewModelFactory = object :
                    UGCOnboardingViewModel.Factory {
                    override fun create(
                        onboardingStrategy: UGCOnboardingStrategy
                    ): UGCOnboardingViewModel {
                        return UGCOnboardingViewModel(
                            onboardingStrategy
                        )
                    }

                })
        }
    }

    private val ugcRepository = UGCOnboardingRepositoryImpl(
        dispatcher = dispatcher,
        feedProfileAcceptTncUseCase = mockk(relaxed = true),
        feedProfileSubmitUseCase = mockk(relaxed = true),
        feedProfileValidateUsernameUseCase = mockk(relaxed = true)
    )

    private val onboardingStrategy = UGCOnboardingStrategyFactory(
        completeStrategy = UGCCompleteOnboardingStrategy(
            dispatcher = dispatcher,
            repo = ugcRepository,
        ),
        tncStrategy = UGCTncOnboardingStrategy(
            dispatcher = dispatcher,
            repo = ugcRepository
        )
    )

    private val fragmentFactory = PlayBroTestFragmentFactory(
        mapOf(
            PlayBroadcastPreparationFragment::class.java to {
                PlayBroadcastPreparationFragment(
                    parentViewModelFactoryCreator = parentViewModelFactoryCreator,
                    viewModelFactory = viewModelFactory,
                    analytic = playAnalytic,
                    analyticManager = mockk(relaxed = true),
                    userSession = mockk(relaxed = true),
                    coachMarkSharedPref = mockk(relaxed = true),
                )
            },
            UserCompleteOnboardingBottomSheet::class.java to {
                UserCompleteOnboardingBottomSheet(
                    viewModelFactoryCreator = ugcViewModelFactory,
                    strategyFactory = onboardingStrategy
                )
            },
            UserTnCOnboardingBottomSheet::class.java to {
                UserTnCOnboardingBottomSheet(
                    viewModelFactoryCreator = ugcViewModelFactory,
                    strategyFactory = onboardingStrategy
                )
            },
        )
    )

    private val scenario = launchFragmentInContainer<PlayBroadcastPreparationFragment>(
        factory = fragmentFactory,
        themeResId = R.style.AppTheme,
    )

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay(1000L)
    }

    fun entryPointWhenBothAccountLive() = chainable {
        Espresso.onView(withId(contentR.id.tv_warning_title))
            .check(matches(withText(context.getString(contentR.string.ugc_warning_both_account_live_title))))

        delay()
    }

    fun switchAccountSellerToBuyer() = chainable {
        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Shop")))
            .perform(click())

        delay()

        Espresso.onView(withId(contentR.id.rv_feed_account))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        delay()

        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Buyer")))

        delay()
    }

    fun switchAccountSellerToBuyerAndNotHaveUsername() = chainable {
        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Shop")))
            .perform(click())

        delay()

        Espresso.onView(withId(contentR.id.rv_feed_account))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        delay()

        Espresso.onView(withId(contentR.id.text_field_username))
            .check(matches(isCompletelyDisplayed()))

        delay()
    }

    fun switchAccountSellerToBuyerAndNotAcceptTnc() = chainable {
        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Shop")))
            .perform(click())

        delay()

        Espresso.onView(withId(contentR.id.rv_feed_account))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        delay()

        Espresso.onView(withId(contentR.id.img_onboarding))
            .check(matches(isCompletelyDisplayed()))

        delay()
    }

    fun switchAccountBuyerToSellerAndNotAllowed() = chainable {
        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Buyer")))
            .perform(click())

        delay()

        Espresso.onView(withId(contentR.id.rv_feed_account))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        delay()

        Espresso.onView(withId(contentR.id.rv_tnc_benefit))
            .check(matches(isCompletelyDisplayed()))

        delay()
    }

    fun switchAccountSellerHaveDraft() = chainable {
        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Shop")))
            .perform(click())

        delay()

        Espresso.onView(withId(contentR.id.rv_feed_account))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        delay()
        Espresso.onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary))
            .check(matches(isCompletelyDisplayed()))
    }

    fun switchAccountBuyerHaveDraft() = chainable {
        Espresso.onView(withId(contentR.id.text_com_toolbar_subtitle))
            .check(matches(withText("Buyer")))
            .perform(click())

        delay()

        Espresso.onView(withId(contentR.id.rv_feed_account))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1, click()
                )
            )

        delay()
        Espresso.onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary))
            .check(matches(isCompletelyDisplayed()))
    }

    fun onClickDropDownAccount() = chainable {
        Espresso.onView(
            withId(com.tokopedia.content.common.R.id.img_content_creator_expand)
        ).perform(click())
        delay(500L)
    }

    fun onClickAccountFromBottomSheet() = chainable {
        Espresso.onView(
            withId(com.tokopedia.content.common.R.id.rv_feed_account)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1, click()
            )
        )
    }

    fun onSwitchAccountConfirmationDialogShown() = chainable {
        delay()
        Espresso.onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary))
            .check(matches(isCompletelyDisplayed()))
    }

    fun onClickCancelSwitchWhenHavingDraft() = chainable {
        delay(500L)
        Espresso.onView(withId(com.tokopedia.dialog.R.id.dialog_btn_primary))
            .perform(click())
    }

    fun onClickConfirmSwitchWhenHavingDraft() = chainable {
        delay(500L)
        Espresso.onView(withId(com.tokopedia.dialog.R.id.dialog_btn_secondary_long))
            .perform(click())
    }

    private fun chainable(fn: () -> Unit): SwitchAccountRobot {
        fn()
        return this
    }
}
