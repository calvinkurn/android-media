package com.tokopedia.inbox.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetAllCounterUseCase
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetWidgetMetaUseCase
import com.tokopedia.inbox.universalinbox.view.UniversalInboxMenuMapper
import com.tokopedia.inbox.universalinbox.view.UniversalInboxViewModel
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class UniversalInboxViewModelTestFixture {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    @RelaxedMockK
    protected lateinit var getAllCounterUseCase: UniversalInboxGetAllCounterUseCase

    @RelaxedMockK
    protected lateinit var getWidgetMetaUseCase: UniversalInboxGetWidgetMetaUseCase

    @RelaxedMockK
    protected lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    protected lateinit var addWishListV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    protected lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    protected lateinit var inboxMenuMapper: UniversalInboxMenuMapper

    @RelaxedMockK
    protected lateinit var userSession: UserSessionInterface

    protected lateinit var viewModel: UniversalInboxViewModel
    protected val dummyThrowable = Throwable("Oops!")

    @Before
    open fun setup() {
        MockKAnnotations.init(this)
        viewModel = UniversalInboxViewModel(
            getAllCounterUseCase,
            getWidgetMetaUseCase,
            getRecommendationUseCase,
            addWishListV2UseCase,
            deleteWishlistV2UseCase,
            inboxMenuMapper,
            userSession,
            CoroutineTestDispatchersProvider
        )
    }

    @After
    open fun finish() {
        unmockkAll()
    }
}
