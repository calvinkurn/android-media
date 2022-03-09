package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gm.common.domain.interactor.GetPMStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetIsShopOsUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopScheduleViewModel
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsInfoViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopSettingsInfoViewModelTestFixture  {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var checkOsMerchantUseCase: GqlGetIsShopOsUseCase

    @RelaxedMockK
    lateinit var getShopBasicDataUseCase: GetShopBasicDataUseCase

    @RelaxedMockK
    lateinit var getShopStatusUseCase: GetPMStatusUseCase

    @RelaxedMockK
    lateinit var shopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase

    @RelaxedMockK
    lateinit var updateShopScheduleUseCase: UpdateShopScheduleUseCase

    @RelaxedMockK
    lateinit var getShopInfoUseCase: GQLGetShopInfoUseCase

    protected lateinit var shopSettingsInfoViewModel: ShopSettingsInfoViewModel
    protected lateinit var shopScheduleViewModel: ShopScheduleViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopSettingsInfoViewModel = ShopSettingsInfoViewModel(
                checkOsMerchantUseCase,
                getShopBasicDataUseCase,
                getShopStatusUseCase,
                shopOperationalHoursListUseCase,
                updateShopScheduleUseCase,
                getShopInfoUseCase,
                CoroutineTestDispatchersProvider
        )

        shopScheduleViewModel = ShopScheduleViewModel(
                updateShopScheduleUseCase,
                getShopBasicDataUseCase,
                CoroutineTestDispatchersProvider
        )
    }
}