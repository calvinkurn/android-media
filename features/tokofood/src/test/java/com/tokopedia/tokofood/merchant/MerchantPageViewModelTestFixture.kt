package com.tokopedia.tokofood.merchant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.tokofood.common.util.ResourceProvider
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodHomeViewModel
import com.tokopedia.tokofood.feature.merchant.domain.usecase.GetMerchantDataUseCase
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.MerchantPageViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class MerchantPageViewModelTestFixture {

    @RelaxedMockK
    lateinit var resourceProvider: ResourceProvider
    @RelaxedMockK
    lateinit var getMerchantDataUseCase: GetMerchantDataUseCase
    @RelaxedMockK
    lateinit var getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    protected lateinit var viewModel: MerchantPageViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = MerchantPageViewModel(
                resourceProvider = resourceProvider,
                dispatchers = CoroutineTestDispatchersProvider,
                getMerchantDataUseCase = getMerchantDataUseCase,
                getChooseAddressWarehouseLocUseCase = getChooseAddressWarehouseLocUseCase
        )
    }
}
