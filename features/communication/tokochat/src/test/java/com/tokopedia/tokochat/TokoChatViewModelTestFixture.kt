package com.tokopedia.tokochat

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokochat.domain.usecase.*
import com.tokopedia.tokochat.view.viewmodel.TokoChatViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.After
import org.junit.Before
import org.junit.Rule

@FlowPreview
@ExperimentalCoroutinesApi
abstract class TokoChatViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    protected lateinit var getChannelUseCase: TokoChatChannelUseCase

    @RelaxedMockK
    protected lateinit var getChatHistoryUseCase: TokoChatGetChatHistoryUseCase

    @RelaxedMockK
    protected lateinit var markAsReadUseCase: TokoChatMarkAsReadUseCase

    @RelaxedMockK
    protected lateinit var registrationChannelUseCase: TokoChatRegistrationChannelUseCase

    @RelaxedMockK
    protected lateinit var sendMessageUseCase: TokoChatSendMessageUseCase

    @RelaxedMockK
    protected lateinit var getTypingUseCase: TokoChatGetTypingUseCase

    @RelaxedMockK
    protected lateinit var getTokoChatBackgroundUseCase: GetTokoChatBackgroundUseCase

    @RelaxedMockK
    protected lateinit var getTokoChatRoomTickerUseCase: GetTokoChatRoomTickerUseCase

    @RelaxedMockK
    protected lateinit var profileUseCase: TokoChatMutationProfileUseCase

    @RelaxedMockK
    protected lateinit var getTokoChatOrderProgressUseCase: TokoChatOrderProgressUseCase

    @RelaxedMockK
    protected lateinit var getImageUrlUseCase: TokoChatGetImageUseCase

    protected lateinit var viewModel: TokoChatViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoChatViewModel(
            getChannelUseCase,
            getChatHistoryUseCase,
            markAsReadUseCase,
            registrationChannelUseCase,
            sendMessageUseCase,
            getTypingUseCase,
            getTokoChatBackgroundUseCase,
            getTokoChatRoomTickerUseCase,
            profileUseCase,
            getTokoChatOrderProgressUseCase,
            getImageUrlUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }
}
