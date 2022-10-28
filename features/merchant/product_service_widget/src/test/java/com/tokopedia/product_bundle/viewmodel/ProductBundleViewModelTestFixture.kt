package com.tokopedia.product_bundle.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product_bundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.product_bundle.common.util.ResourceProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ProductBundleViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getBundleInfoUseCase: GetBundleInfoUseCase

    @RelaxedMockK
    lateinit var addToCartBundleUseCase: AddToCartBundleUseCase

    @RelaxedMockK
    lateinit var chosenAddressHelper: ChosenAddressRequestHelper

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider

    lateinit var viewModel: ProductBundleViewModel
    lateinit var nonSpykViewModel2: ProductBundleViewModel

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = spyk(ProductBundleViewModel(
            resourceProvider,
            CoroutineTestDispatchersProvider,
            getBundleInfoUseCase,
            addToCartBundleUseCase,
            chosenAddressHelper,
            userSession
        ))

        nonSpykViewModel2 = ProductBundleViewModel(
            resourceProvider,
            CoroutineTestDispatchersProvider,
            getBundleInfoUseCase,
            addToCartBundleUseCase,
            chosenAddressHelper,
            userSession
        )
    }
}