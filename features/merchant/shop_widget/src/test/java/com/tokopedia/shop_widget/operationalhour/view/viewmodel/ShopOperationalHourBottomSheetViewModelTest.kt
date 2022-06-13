package com.tokopedia.shop_widget.operationalhour.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopOperationalHourBottomSheetViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var shopOperationalHourBottomSheetViewModel : ShopOperationalHourBottomSheetViewModel


    @RelaxedMockK
    lateinit var getShopOperationalHoursListUseCase: Lazy<GqlGetShopOperationalHoursListUseCase>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopOperationalHourBottomSheetViewModel = ShopOperationalHourBottomSheetViewModel(
            getShopOperationalHoursListUseCase,
            testCoroutineDispatcherProvider
        )
    }

    @Test
    fun `check whether shopOperationalHoursListData value is success`() {
        val mockShopId = "123"
        coEvery {
            getShopOperationalHoursListUseCase.get().executeOnBackground()
        } returns ShopOperationalHoursListResponse()
        shopOperationalHourBottomSheetViewModel.getShopOperationalHoursList(mockShopId)
        assert(shopOperationalHourBottomSheetViewModel.shopOperationalHoursListData.value is Success)
    }

    @Test
    fun `check whether shopOperationalHoursListData value is fail if exception happened`() {
        val mockShopId = "123"
        coEvery {
            getShopOperationalHoursListUseCase.get().executeOnBackground()
        } throws Throwable()
        shopOperationalHourBottomSheetViewModel.getShopOperationalHoursList(mockShopId)
        assert(shopOperationalHourBottomSheetViewModel.shopOperationalHoursListData.value is Fail)
    }
}