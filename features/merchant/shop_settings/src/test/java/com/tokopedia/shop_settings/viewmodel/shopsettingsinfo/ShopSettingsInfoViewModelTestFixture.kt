package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsInfoViewModel
import com.tokopedia.shop_settings.viewmodel.TestDispatcherProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule


@ExperimentalCoroutinesApi
abstract class ShopSettingsInfoViewModelTestFixture  {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var checkOsMerchantUseCase: CheckOfficialStoreTypeUseCase

    @RelaxedMockK
    lateinit var getShopBasicDataUseCase: GetShopBasicDataUseCase

    @RelaxedMockK
    lateinit var getShopStatusUseCase: GetShopStatusUseCase

    @RelaxedMockK
    lateinit var updateShopScheduleUseCase: UpdateShopScheduleUseCase

    protected lateinit var shopSettingsInfoViewModel: ShopSettingsInfoViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopSettingsInfoViewModel = ShopSettingsInfoViewModel(
                checkOsMerchantUseCase,
                getShopBasicDataUseCase,
                getShopStatusUseCase,
                updateShopScheduleUseCase,
                TestDispatcherProvider()
        )
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        TestCase.assertEquals(expected, actual)
    }

}