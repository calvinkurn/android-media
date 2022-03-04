package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.settings.basicinfo.domain.SetShopOperationalHoursUseCase
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSetOperationalHoursViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopSetOperationalHoursViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var gqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase

    @RelaxedMockK
    lateinit var setShopOperationalHoursUseCase: SetShopOperationalHoursUseCase

    protected lateinit var shopSetOperationalHoursViewModel: ShopSetOperationalHoursViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopSetOperationalHoursViewModel = ShopSetOperationalHoursViewModel(
                shopOperationalHoursListUseCase = gqlGetShopOperationalHoursListUseCase,
                setShopOperationalHoursUseCase = setShopOperationalHoursUseCase,
                dispatchers = CoroutineTestDispatchersProvider
        )
    }
}