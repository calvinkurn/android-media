package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.catalog.ui.viewmodel.CatalogDetailPageViewModel
import com.tokopedia.oldcatalog.usecase.detail.CatalogDetailUseCase
import com.tokopedia.searchbar.navigation_component.domain.GetNotificationUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach

class CatalogDetailPageViewModelTest {

    // Initialization scope
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var catalogDetailUseCase: CatalogDetailUseCase
    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    val viewModel by lazy {
        spyk(
            CatalogDetailPageViewModel(
                CoroutineTestDispatchersProvider,
                catalogDetailUseCase,
                getNotificationUseCase,
                userSession
            )
        )
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    // Testing scope
    @Test
    fun `this is dummy test`() {
        viewModel.refreshNotification()
    }
}
