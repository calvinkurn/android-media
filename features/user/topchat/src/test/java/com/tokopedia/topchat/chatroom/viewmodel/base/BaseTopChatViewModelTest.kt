package com.tokopedia.topchat.chatroom.viewmodel.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.CloseReminderTicker
import com.tokopedia.topchat.chatroom.domain.usecase.GetExistingMessageIdUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetReminderTickerUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.GetShopFollowingUseCase
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class BaseTopChatViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getExistingMessageIdUseCase: GetExistingMessageIdUseCase

    @RelaxedMockK
    lateinit var getShopFollowingUseCase: GetShopFollowingUseCase

    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: ToggleFavouriteShopUseCase

    @RelaxedMockK
    lateinit var reminderTickerUseCase: GetReminderTickerUseCase

    @RelaxedMockK
    lateinit var closeReminderTicker: CloseReminderTicker

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: TopChatViewModel

    protected val testShopId = "123"
    protected val testUserId = "345"
    protected val source = "testSource"
    protected val expectedThrowable = Throwable("Oops!")

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = TopChatViewModel(
            getExistingMessageIdUseCase,
            getShopFollowingUseCase,
            toggleFavouriteShopUseCase,
            reminderTickerUseCase,
            closeReminderTicker,
            dispatchers,
            remoteConfig
        )
    }
}