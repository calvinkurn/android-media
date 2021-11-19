package com.tokopedia.topchat.chatroom.viewmodel.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.seamless_login_common.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatroom.domain.usecase.*
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
    lateinit var addToCartUseCase: AddToCartUseCase

    @RelaxedMockK
    lateinit var seamlessLoginUsecase: SeamlessLoginUsecase

    @RelaxedMockK
    lateinit var getChatRoomSettingUseCase: GetChatRoomSettingUseCase

    @RelaxedMockK
    lateinit var orderProgressUseCase: OrderProgressUseCase

    @RelaxedMockK
    lateinit var chatToggleBlockChatUseCase: ChatToggleBlockChatUseCaseNew

    @RelaxedMockK
    lateinit var remoteConfig: RemoteConfig
    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider

    protected lateinit var viewModel: TopChatViewModel

    protected val testShopId = "123"
    protected val testUserId = "345"
    protected val source = "testSource"
    protected val expectedThrowable = Throwable("Oops!")
    protected val testMessageId = "123123"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = TopChatViewModel(
            getExistingMessageIdUseCase,
            getShopFollowingUseCase,
            toggleFavouriteShopUseCase,
            addToCartUseCase,
            seamlessLoginUsecase,
            getChatRoomSettingUseCase,
            orderProgressUseCase,
            chatToggleBlockChatUseCase,
            dispatchers,
            remoteConfig
        )
    }
}