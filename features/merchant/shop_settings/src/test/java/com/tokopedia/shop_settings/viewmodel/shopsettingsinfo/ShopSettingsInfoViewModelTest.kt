package com.tokopedia.shop_settings.viewmodel.shopsettingsinfo

import com.tokopedia.shop.settings.basicinfo.data.CheckShopIsOfficialModel
import com.tokopedia.shop.settings.basicinfo.domain.CheckOfficialStoreTypeUseCase
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class ShopSettingsInfoViewModelTest : ShopSettingsInfoViewModelTestFixture() {

    @Test
    fun `when_validate_os_merchant_type_with_provided_shopId_should_return_success`() {
        runBlocking {
            mockkObject(CheckOfficialStoreTypeUseCase)
            onCheckOsMerchantType_thenReturn()

            val shopId: Int = 123456
            shopSettingsInfoViewModel.validateOsMerchantType(shopId)

            verifySuccessCheckOsMerchantTypeCalled(shopId)

            val expectedValue = Success(CheckShopIsOfficialModel())
            assertTrue(shopSettingsInfoViewModel.checkOsMerchantTypeData.value is Success)
            shopSettingsInfoViewModel.checkOsMerchantTypeData.verifyValueEquals(expectedValue)
        }
    }

    private fun onCheckOsMerchantType_thenReturn() {
        coEvery { checkOsMerchantUseCase.executeOnBackground() } returns CheckShopIsOfficialModel()
    }

    private fun verifySuccessCheckOsMerchantTypeCalled(shopId: Int) {
        verify { CheckOfficialStoreTypeUseCase.createRequestParam(shopId) }
        coVerify { checkOsMerchantUseCase.executeOnBackground() }
    }

}