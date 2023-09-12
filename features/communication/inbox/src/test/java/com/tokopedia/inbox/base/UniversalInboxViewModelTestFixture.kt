package com.tokopedia.inbox.base

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMenuMapper
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxMiscMapper
import com.tokopedia.inbox.universalinbox.domain.mapper.UniversalInboxWidgetMetaMapper
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllCounterUseCase
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetInboxMenuAndWidgetMetaUseCase
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetProductRecommendationUseCase
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProvider
import com.tokopedia.inbox.universalinbox.util.UniversalInboxResourceProviderImpl
import com.tokopedia.inbox.universalinbox.view.UniversalInboxViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
abstract class UniversalInboxViewModelTestFixture {

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    protected lateinit var getWidgetMetaUseCase: UniversalInboxGetInboxMenuAndWidgetMetaUseCase

    @RelaxedMockK
    protected lateinit var getAllCounterUseCase: UniversalInboxGetAllCounterUseCase

    @RelaxedMockK
    protected lateinit var getRecommendationUseCase: UniversalInboxGetProductRecommendationUseCase

    @RelaxedMockK
    protected lateinit var addWishListV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    protected lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    protected lateinit var getDriverChatCounterUseCase: UniversalInboxGetAllDriverChannelsUseCase

    @RelaxedMockK
    protected lateinit var userSession: UserSessionInterface

    private lateinit var context: Context
    private lateinit var resourceProvider: UniversalInboxResourceProvider
    protected lateinit var inboxMenuMapper: UniversalInboxMenuMapper
    private var inboxMiscMapper = UniversalInboxMiscMapper()
    protected var inboxWidgetMetaMapper = UniversalInboxWidgetMetaMapper()

    private lateinit var mockLifecycleOwner: LifecycleOwner
    protected lateinit var lifecycle: LifecycleRegistry
    protected lateinit var viewModel: UniversalInboxViewModel
    protected val dummyThrowable = Throwable("Oops!")

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
        setTestClass()
        mockLifecycleOwner = Mockito.mock(LifecycleOwner::class.java)
        lifecycle = LifecycleRegistry(mockLifecycleOwner)
        viewModel = UniversalInboxViewModel(
            getAllCounterUseCase,
            getWidgetMetaUseCase,
            getRecommendationUseCase,
            addWishListV2UseCase,
            deleteWishlistV2UseCase,
            getDriverChatCounterUseCase,
            inboxMenuMapper,
            inboxMiscMapper,
            inboxWidgetMetaMapper,
            userSession,
            CoroutineTestDispatchersProvider
        )
        earlyMock()
    }

    private fun setTestClass() {
        context = mockk()
        resourceProvider = UniversalInboxResourceProviderImpl(context)
        inboxMenuMapper = spyk(UniversalInboxMenuMapper(resourceProvider))
    }

    private fun earlyMock() {
        mockkStatic(Uri::class)
        every {
            Uri.parse(any()).buildUpon().appendQueryParameter(any(), any()).build()
        } returns mockk()
    }

    @After
    open fun finish() {
        unmockkAll()
    }

    protected fun getDummyConversationsChannel(
        unreadCount: Int,
        expiredAt: Long = System.currentTimeMillis() + 100000
    ) = ConversationsChannel(
        "",
        "",
        "",
        "",
        "",
        unreadCount,
        null,
        null,
        listOf(),
        false,
        0,
        expiredAt,
        null,
        null,
        0
    )
}
