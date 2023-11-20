package com.tokopedia.tokofood.merchant

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.feature.merchant.domain.usecase.CheckDeliveryCoverageUseCase
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.ManageLocationViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

abstract class ManageLocationViewModelTestFixture {

    @RelaxedMockK
    lateinit var keroEditAddressUseCase: KeroEditAddressUseCase

    @RelaxedMockK
    lateinit var getChooseAddressWarehouseLocUseCase: GetChosenAddressWarehouseLocUseCase

    @RelaxedMockK
    lateinit var checkDeliveryCoverageUseCase: CheckDeliveryCoverageUseCase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: ManageLocationViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ManageLocationViewModel(
            keroEditAddressUseCase,
            getChooseAddressWarehouseLocUseCase,
            checkDeliveryCoverageUseCase,
            CoroutineTestDispatchersProvider
        )
    }
}
