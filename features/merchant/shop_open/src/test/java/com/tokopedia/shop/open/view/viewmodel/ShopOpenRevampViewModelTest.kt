package com.tokopedia.shop.open.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.open.data.model.*
import com.tokopedia.shop.open.domain.*
import com.tokopedia.shop.open.presentation.viewmodel.ShopOpenRevampViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyMap
import org.mockito.Matchers.anyString
import org.mockito.Matchers.anyInt


@ExperimentalCoroutinesApi
class ShopOpenRevampViewModelTest  {

    @RelaxedMockK
    lateinit var validateDomainShopNameUseCase: ValidateDomainShopNameUseCase

    @RelaxedMockK
    lateinit var getDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase

    @RelaxedMockK
    lateinit var getSurveyUseCase: ShopOpenRevampGetSurveyUseCase

    @RelaxedMockK
    lateinit var sendSurveyUseCase: ShopOpenRevampSendSurveyUseCase

    @RelaxedMockK
    lateinit var createShopUseCase: ShopOpenRevampCreateShopUseCase

    @RelaxedMockK
    lateinit var saveShopShipmentLocationUseCase: ShopOpenRevampSaveShipmentLocationUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private val viewModel by lazy {
        ShopOpenRevampViewModel(
                validateDomainShopNameUseCase,
                getDomainNameSuggestionUseCase,
                getSurveyUseCase,
                sendSurveyUseCase,
                createShopUseCase,
                saveShopShipmentLocationUseCase,
                TestDispatcherProvider()
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given shop name validation when shop name is not empty`() {
        mockkObject(ValidateDomainShopNameUseCase)
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
        val shopName: String = anyString()
        viewModel.validateShopName(shopName)
        Thread.sleep(1000)

        verify {
            ValidateDomainShopNameUseCase.createRequestParams(shopName)
        }

        Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())
        coVerify {
            validateDomainShopNameUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.checkShopNameResponse.value is Success)
    }

    @Test
    fun `given domain name validation when domain name is not empty`() {
        mockkObject(ValidateDomainShopNameUseCase)
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
        val domainName: String = anyString()
        viewModel.validateDomainName(domainName)
        Thread.sleep(1000)

        verify {
            ValidateDomainShopNameUseCase.createRequestParam(domainName)
        }

        Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())
        coVerify {
            validateDomainShopNameUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.checkDomainNameResponse.value is Success)
    }

    @Test
    fun `given domain name suggestion when shop name is provided`() {
        mockkObject(GetShopDomainNameSuggestionUseCase)
        coEvery {
            getDomainNameSuggestionUseCase.executeOnBackground()
        } returns ShopDomainSuggestionData()
        viewModel.getDomainShopNameSuggestions(anyString())
        verify {
            GetShopDomainNameSuggestionUseCase
                    .createRequestParams(anyString())
        }
        Assert.assertTrue(getDomainNameSuggestionUseCase.params.parameters.isNotEmpty())
        coVerify {
            getDomainNameSuggestionUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.domainShopNameSuggestionsResponse.value is Success)
    }

    @Test
    fun `given shop id when valid shop name and valid domain name are provided`() {
        mockkObject(ShopOpenRevampCreateShopUseCase)
        coEvery {
            createShopUseCase.executeOnBackground()
        } returns CreateShop()
        viewModel.createShop(anyString(), anyString())
        verify {
            ShopOpenRevampCreateShopUseCase.createRequestParams(anyString(), anyString())
        }
        Assert.assertTrue(createShopUseCase.params.parameters.isNotEmpty())
        coVerify {
            createShopUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.createShopOpenResponse.value is Success)
    }

    @Test
    fun `given success param when send survey data`() {
        mockkObject(ShopOpenRevampSendSurveyUseCase)
        coEvery {
            sendSurveyUseCase.executeOnBackground()
        } returns SendSurveyData()
        val anyMap: MutableMap<String, Any> = anyMap()
        viewModel.sendSurveyData(anyMap)
        verify {
            ShopOpenRevampSendSurveyUseCase.createRequestParams(anyMap)
        }
        Assert.assertTrue(sendSurveyUseCase.params.parameters.isNotEmpty())
        coVerify {
            sendSurveyUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.sendSurveyDataResponse.value is Success)
    }

    @Test
    fun `given success message when shopId, postCode, courierOrigin, addrStreet, lat, long are provided`() {
        val shopId: Int = 1111
        val postCode: String = "2222"
        val courierOrigin: Int = 3333
        val addrStreet: String = "ABC Street"
        val lat: String = "12345.67890"
        val long: String = "09876.54321"

        mockkObject(ShopOpenRevampSaveShipmentLocationUseCase)
        coEvery {
            saveShopShipmentLocationUseCase.executeOnBackground()
        } returns SaveShipmentLocation()
        val saveShippingData: MutableMap<String, Any> = viewModel.getSaveShopShippingLocationData(shopId, postCode, courierOrigin, addrStreet, lat, long)
        viewModel.saveShippingLocation(saveShippingData)
        Thread.sleep(1000)

        verify {
            ShopOpenRevampSaveShipmentLocationUseCase.createRequestParams(saveShippingData)
        }
        Assert.assertTrue(saveShopShipmentLocationUseCase.params.parameters.isNotEmpty())
        coVerify {
            saveShopShipmentLocationUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.saveShopShipmentLocationResponse.value is Success)
    }

    @Test
    fun `given success response when validate shop name is called`() {
        mockkObject(ValidateDomainShopNameUseCase)
        val shopName: String = "tokohape"
        viewModel.checkShopName(shopName)

        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
        Thread.sleep(1000)

        verify {
            ValidateDomainShopNameUseCase.createRequestParams(shopName)
        }

        Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())
        coVerify {
            validateDomainShopNameUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.checkShopNameResponse.value is Success)
    }

    @Test
    fun `given success response when validate domain name is called`() {
        mockkObject(ValidateDomainShopNameUseCase)
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()

        val domainName: String = "tokohapee"
        viewModel.checkDomainName(domainName)
        Thread.sleep(1000)

        verify {
            ValidateDomainShopNameUseCase.createRequestParam(domainName)
        }

        Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())
        coVerify {
            validateDomainShopNameUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.checkDomainNameResponse.value is Success)
    }

    @Test
    fun `given survey payload when data survey is provided`() {
        val dataSurvey: MutableMap<Int, MutableList<Int>> = anyMap()
        viewModel.getDataSurveyInput(dataSurvey)

        Assert.assertTrue(viewModel.getDataSurveyInput(dataSurvey) is MutableMap<String, Any>)
    }

    @Test
    fun `given quisionaire data when request is executed`() {
        mockkObject(ShopOpenRevampGetSurveyUseCase)
        coEvery {
            getSurveyUseCase.executeOnBackground()
        } returns GetSurveyData()
        viewModel.getSurveyQuizionaireData()
        coVerify {
            getSurveyUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.getSurveyDataResponse.value is Success)
    }
}