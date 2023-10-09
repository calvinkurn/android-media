package com.tokopedia.catalog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.catalog.domain.GetProductListFromSearchUseCase
import com.tokopedia.catalog.ui.viewmodel.CatalogProductListViewModel
import com.tokopedia.oldcatalog.usecase.listing.CatalogDynamicFilterUseCase
import com.tokopedia.oldcatalog.usecase.listing.CatalogQuickFilterUseCase
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

class CatalogProductListViewModelTest {

    // Initialization scope
    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var quickFilterUseCase: CatalogQuickFilterUseCase
    @RelaxedMockK
    lateinit var dynamicFilterUseCase: CatalogDynamicFilterUseCase
    @RelaxedMockK
    lateinit var addToCartUseCase: AddToCartUseCase
    @RelaxedMockK
    lateinit var getNotificationUseCase: GetNotificationUseCase
    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListFromSearchUseCase
    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    val viewModel by lazy {
        spyk(
            CatalogProductListViewModel(
                CoroutineTestDispatchersProvider,
                quickFilterUseCase,
                dynamicFilterUseCase,
                addToCartUseCase,
                getNotificationUseCase,
                getProductListUseCase,
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

    }
}
