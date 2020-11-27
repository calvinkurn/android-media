package com.tokopedia.shop_showcase.viewmodel.shopshowcaseproductadd

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.viewmodel.ShowcaseProductAddViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ShowcaseProductAddViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getProductListUseCase: GetProductListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface


    protected lateinit var showcaseProductAddViewModel: ShowcaseProductAddViewModel

    private val testDispatcher by lazy {
        CoroutineTestDispatchersProvider
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        showcaseProductAddViewModel = ShowcaseProductAddViewModel(
                userSession,
                getProductListUseCase,
                testDispatcher
        )
    }
}