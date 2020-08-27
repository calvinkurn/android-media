package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.gm.common.domain.interactor.GetShopStatusUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopScheduleUseCase
import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopSettingsInfoViewModel
import com.tokopedia.shop_settings.viewmodel.TestDispatcherProvider
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import rx.Observable


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

    protected fun onCheckOsMerchantType_thenReturn() {
        coEvery { checkOsMerchantUseCase.executeOnBackground() } returns CheckShopIsOfficialModel()
    }

    protected fun verifySuccessCheckOsMerchantTypeCalled(shopId: Int) {
        verify { CheckOfficialStoreTypeUseCase.createRequestParam(shopId) }
        coVerify { checkOsMerchantUseCase.executeOnBackground() }
    }

    protected fun verifyUnsubscribeUseCase() {
        coVerify { getShopBasicDataUseCase.unsubscribe() }
        coVerify { getShopStatusUseCase.unsubscribe() }
        coVerify { updateShopScheduleUseCase.unsubscribe() }
    }

    internal fun<T: Any> LiveData<Result<T>>.verifySuccessEquals(expected: Success<Any>?) {
        val expectedResult = expected?.data
        val actualResult = (value as? Success<T>)?.data
        TestCase.assertEquals(expectedResult, actualResult)
    }

}