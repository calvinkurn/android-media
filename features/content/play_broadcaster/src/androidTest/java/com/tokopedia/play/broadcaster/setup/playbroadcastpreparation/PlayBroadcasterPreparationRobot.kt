
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.R as contentR
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
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
import com.tokopedia.play.test.espresso.delay
import io.mockk.mockk
import kotlin.LazyThreadSafetyMode.NONE

/**
 * Created by fachrizalmrsln on 28/09/22
 */
class PlayBroadcasterPreparationRobot(
    dataStore: PlayBroadcastDataStore,
    hydraConfigStore: HydraConfigStore,
    dispatcher: CoroutineDispatchers,
    repo: PlayBroadcastRepository,
    channelUseCase: GetChannelUseCase,
    addedChannelTagsUseCase: GetAddedChannelTagsUseCase,
) {
    private val context = InstrumentationRegistry.getInstrumentation().context

    private val parentViewModel: PlayBroadcastViewModel by lazy {
        parentBroViewModel(
            dataStore = dataStore,
            hydraConfigStore = hydraConfigStore,
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
                accountAnalytic = mockk(relaxed = true),
            ),
            analyticManager = mockk(relaxed = true)
        )
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun onEnterPreparationWhenBothAccountLive() = chainable {
        Espresso.onView(withId(contentR.id.tv_warning_title))
            .check(matches(withText(context.getString(contentR.string.ugc_warning_both_account_live_title))))

        delay()
    }

    private fun chainable(fn: () -> Unit): PlayBroadcasterPreparationRobot {
        fn()
        return this
    }
}