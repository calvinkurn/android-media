package com.tokopedia.oldproductbundle.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartBundleUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.oldproductbundle.common.usecase.GetBundleInfoUseCase
import com.tokopedia.oldproductbundle.common.util.ResourceProvider
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ProductBundleViewModelTestFixture {

    @get:Rule
    val instantTaskExcecutorRule = InstantTaskExecutorRule()

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

    protected val testDispatcher: TestCoroutineDispatcher by lazy {
        TestCoroutineDispatcher()
    }

    protected val viewModel: ProductBundleViewModel by lazy {
        spyk(
            ProductBundleViewModel(
            resourceProvider,
            CoroutineTestDispatchersProvider,
            getBundleInfoUseCase,
            addToCartBundleUseCase,
            chosenAddressHelper,
            userSession
        )
        )
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
}