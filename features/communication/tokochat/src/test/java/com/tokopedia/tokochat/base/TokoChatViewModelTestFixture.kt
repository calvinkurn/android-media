package com.tokopedia.tokochat.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokochat.common.util.TokoChatCacheManager
import com.tokopedia.tokochat.domain.usecase.*
import com.tokopedia.tokochat.util.TokoChatViewUtil
import com.tokopedia.tokochat.view.chatroom.TokoChatViewModel
import com.tokopedia.tokochat.view.chatroom.uimodel.TokoChatImageAttachmentExtensionProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.usercomponents.userconsent.domain.collection.GetNeedConsentUseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
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

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

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
    protected lateinit var getTokoChatOrderProgressUseCase: TokoChatOrderProgressUseCase

    @RelaxedMockK
    protected lateinit var getImageUrlUseCase: TokoChatGetImageUseCase

    @RelaxedMockK
    protected lateinit var uploadImageUseCase: TokoChatUploadImageUseCase

    @RelaxedMockK
    protected lateinit var getNeedConsentUseCase: GetNeedConsentUseCase

    @RelaxedMockK
    protected lateinit var getTokopediaOrderIdUseCase: TokoChatGetTokopediaOrderIdUseCase

    @RelaxedMockK
    protected lateinit var imageAttachmentExtensionProvider: TokoChatImageAttachmentExtensionProvider

    @RelaxedMockK
    protected lateinit var cacheManager: TokoChatCacheManager

    @RelaxedMockK
    protected lateinit var viewUtil: TokoChatViewUtil

    protected lateinit var viewModel: TokoChatViewModel
    protected val throwableDummy = Throwable("Oops!")

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
        viewModel = spyk(
            TokoChatViewModel(
                getChannelUseCase,
                getChatHistoryUseCase,
                markAsReadUseCase,
                registrationChannelUseCase,
                sendMessageUseCase,
                getTypingUseCase,
                getTokoChatBackgroundUseCase,
                getTokoChatRoomTickerUseCase,
                getTokoChatOrderProgressUseCase,
                getImageUrlUseCase,
                uploadImageUseCase,
                getNeedConsentUseCase,
                getTokopediaOrderIdUseCase,
                viewUtil,
                imageAttachmentExtensionProvider,
                cacheManager,
                CoroutineTestDispatchersProvider
            )
        )
    }

    @After
    fun finish() {
        unmockkAll()
    }

    companion object {
        const val USER_ID_DUMMY = "9075737"
        const val GOJEK_ORDER_ID_DUMMY = "F-68720516436"
        const val TKPD_ORDER_ID_DUMMY = "52af8a53-86cc-40b7-bb98-cc3adde8e32a"
        const val CHANNEL_ID_DUMMY = "b61e429e-b11e-4310-bd47-6242d6ceef19"
        const val ORDER_TRACKING_OTW_DESTINATION = "json/orderprogress/order_progress_otw_destination.json"
        const val ORDER_TRACKING_SUCCESS = "json/orderprogress/order_progress_success.json"
    }
}
