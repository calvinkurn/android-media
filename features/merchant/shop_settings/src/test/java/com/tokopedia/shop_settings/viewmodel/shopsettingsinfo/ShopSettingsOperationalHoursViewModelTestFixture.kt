package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.shop.common.di.GqlGetShopCloseDetailInfoQualifier
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetShopOperationalHoursListUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsOperationalHoursViewModel
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class ShopSettingsOperationalHoursViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    @GqlGetShopCloseDetailInfoQualifier
    lateinit var getShopCloseDetailInfoUseCase: Lazy<GQLGetShopInfoUseCase>

    @RelaxedMockK
    lateinit var gqlGetShopOperationalHoursListUseCase: GqlGetShopOperationalHoursListUseCase

    @RelaxedMockK
    lateinit var updateShopScheduleUseCase: UpdateShopScheduleUseCase

    protected lateinit var shopSettingsOperationalHoursViewModel: ShopSettingsOperationalHoursViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopSettingsOperationalHoursViewModel = ShopSettingsOperationalHoursViewModel(
                getShopCloseDetailInfoUseCase = getShopCloseDetailInfoUseCase,
                gqlGetShopOperationalHoursListUseCase = gqlGetShopOperationalHoursListUseCase,
                updateShopScheduleUseCase = updateShopScheduleUseCase,
                dispatchers = CoroutineDispatchersProvider
        )
    }
}