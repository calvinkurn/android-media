package com.tokopedia.shop.open.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.data.shopopen.SaveShipmentLocation
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ShopOpenRevampSaveShipmentLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.open.data.model.CreateShop
import com.tokopedia.shop.open.data.model.GetSurveyData
import com.tokopedia.shop.open.data.model.SendSurveyData
import com.tokopedia.shop.open.domain.ShopOpenRevampCreateShopUseCase
import com.tokopedia.shop.open.domain.ShopOpenRevampGetSurveyUseCase
import com.tokopedia.shop.open.domain.ShopOpenRevampSendSurveyUseCase
import com.tokopedia.shop.open.presentation.viewmodel.ShopOpenRevampViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.ArgumentMatchers.anyString
import java.lang.reflect.Field

open class ShopOpenRevampViewModelTestFixtures {

    @RelaxedMockK
    protected lateinit var validateDomainShopNameUseCase: ValidateDomainShopNameUseCase

    @RelaxedMockK
    protected lateinit var getDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase

    @RelaxedMockK
    protected lateinit var getSurveyUseCase: ShopOpenRevampGetSurveyUseCase

    @RelaxedMockK
    protected lateinit var sendSurveyUseCase: ShopOpenRevampSendSurveyUseCase

    @RelaxedMockK
    protected lateinit var createShopUseCase: ShopOpenRevampCreateShopUseCase

    @RelaxedMockK
    protected lateinit var saveShopShipmentLocationUseCase: ShopOpenRevampSaveShipmentLocationUseCase

    protected lateinit var privateCurrentShopNameField: Field

    protected lateinit var privateCurrentDomainNameField: Field

    protected lateinit var viewModel: ShopOpenRevampViewModel

    protected val shopId = 1111
    protected val postCode = "2222"
    protected val courierOrigin = 3333
    protected val addrStreet = "ABC Street"
    protected val lat = "12345.67890"
    protected val long = "09876.54321"

    protected var shopName: String = anyString()
    protected var domainName: String = anyString()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = ShopOpenRevampViewModel(
            validateDomainShopNameUseCase,
            getDomainNameSuggestionUseCase,
            getSurveyUseCase,
            sendSurveyUseCase,
            createShopUseCase,
            saveShopShipmentLocationUseCase,
            coroutineTestRule.dispatchers
        )

        privateCurrentShopNameField = viewModel::class.java.getDeclaredField("currentShopName").apply {
            isAccessible = true
        }

        privateCurrentDomainNameField = viewModel::class.java.getDeclaredField("currentShopDomain").apply {
            isAccessible = true
        }
    }

    protected fun verifyValidateShopNameUseCase(name: String) {
        coVerify {
            ValidateDomainShopNameUseCase.createRequestParams(name)
            validateDomainShopNameUseCase.executeOnBackground()
        }
    }

    protected fun verifyValidateDomainNameUseCase(name: String) {
        coVerify {
            ValidateDomainShopNameUseCase.createRequestParam(name)
            validateDomainShopNameUseCase.executeOnBackground()
        }
    }

    protected fun verifyGetShopDomainNameSuggestionUseCase(name: String) {
        coVerify {
            GetShopDomainNameSuggestionUseCase.createRequestParams(name)
            getDomainNameSuggestionUseCase.executeOnBackground()
        }
    }

    protected fun verifyCreateShopUseCase(shopName: String, domainName: String) {
        coVerify {
            ShopOpenRevampCreateShopUseCase.createRequestParams(shopName, domainName)
            createShopUseCase.executeOnBackground()
        }
    }

    protected fun verifySendSurveyUseCase(anyMap: Map<String, Any>) {
        coVerify {
            ShopOpenRevampSendSurveyUseCase.createRequestParams(anyMap)
            sendSurveyUseCase.executeOnBackground()
        }
    }

    protected fun verifyGetSurveyUseCase() {
        coVerify {
            getSurveyUseCase.executeOnBackground()
        }
    }

    protected fun verifySaveShopShipmentLocationUseCase(shippingData: MutableMap<String, Any>) {
        coVerify {
            ShopOpenRevampSaveShipmentLocationUseCase.createRequestParams(shippingData)
            saveShopShipmentLocationUseCase.executeOnBackground()
        }
    }

    protected fun everyValidateDomainShopNameUseCase(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery {
                validateDomainShopNameUseCase.executeOnBackground()
            } returns ValidateShopDomainNameResult()
        } else {
            coEvery {
                validateDomainShopNameUseCase.executeOnBackground()
            } throws Exception()
        }
    }

    protected fun everyGetShopDomainNameSuggestionUseCase(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery {
                getDomainNameSuggestionUseCase.executeOnBackground()
            } returns ShopDomainSuggestionData()
        } else {
            coEvery {
                getDomainNameSuggestionUseCase.executeOnBackground()
            } throws Exception()
        }
    }

    protected fun everyCreateShopUseCase(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery {
                createShopUseCase.executeOnBackground()
            } returns CreateShop()
        } else {
            coEvery {
                createShopUseCase.executeOnBackground()
            } throws Exception()
        }
    }

    protected fun everySendSurveyUseCase(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery {
                sendSurveyUseCase.executeOnBackground()
            } returns SendSurveyData()
        } else {
            coEvery {
                sendSurveyUseCase.executeOnBackground()
            } throws Exception()
        }
    }

    protected fun everyGetSurveyUseCase(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery {
                getSurveyUseCase.executeOnBackground()
            } returns GetSurveyData()
        } else {
            coEvery {
                getSurveyUseCase.executeOnBackground()
            } throws Exception()
        }
    }

    protected fun everySaveShopShipmentLocationUseCase(isSuccess: Boolean) {
        if (isSuccess) {
            coEvery {
                saveShopShipmentLocationUseCase.executeOnBackground()
            } returns SaveShipmentLocation()
        } else {
            coEvery {
                saveShopShipmentLocationUseCase.executeOnBackground()
            } throws Exception()
        }
    }
}