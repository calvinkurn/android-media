package com.tokopedia.shop.open.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.open.shop_open_revamp.data.model.*
import com.tokopedia.shop.open.shop_open_revamp.domain.*
import com.tokopedia.shop.open.shop_open_revamp.presentation.viewmodel.ShopOpenRevampViewModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Matchers.anyMap
import org.mockito.Matchers.anyString
import org.mockito.Matchers.anyInt


@ExperimentalCoroutinesApi
class ShopOpenRevampViewModelTest  {

    @RelaxedMockK
    lateinit var validateDomainShopNameUseCase: ShopOpenRevampValidateDomainShopNameUseCase

    @RelaxedMockK
    lateinit var getDomainNameSuggestionUseCase: ShopOpenRevampGetDomainNameSuggestionUseCase

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

    private val dispatchers by lazy {
        Dispatchers.Unconfined
    }

    private val viewModel by lazy {
        ShopOpenRevampViewModel(
                validateDomainShopNameUseCase,
                getDomainNameSuggestionUseCase,
                getSurveyUseCase,
                sendSurveyUseCase,
                createShopUseCase,
                saveShopShipmentLocationUseCase,
                dispatchers
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun testValidateShopName() {
        mockkObject(ShopOpenRevampValidateDomainShopNameUseCase)
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
        viewModel.checkShopName(anyString())
        verify {
            ShopOpenRevampValidateDomainShopNameUseCase
                    .createRequestParams(anyString())
        }
        Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())
        coVerify {
            validateDomainShopNameUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.checkShopNameResponse.value is Success)
    }

    @Test
    fun testValidateDomainAndShopName() {
        mockkObject(ShopOpenRevampValidateDomainShopNameUseCase)
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
        viewModel.checkDomainAndShopName(anyString(), anyString())
        verify {
            ShopOpenRevampValidateDomainShopNameUseCase
                    .createRequestParams(anyString(), anyString())
        }
        Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())
        coVerify {
            validateDomainShopNameUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.checkDomainAndShopNameResponse.value is Success)
    }

    @Test
    fun testGetDomainNameSuggestions() {
        mockkObject(ShopOpenRevampGetDomainNameSuggestionUseCase)
        coEvery {
            getDomainNameSuggestionUseCase.executeOnBackground()
        } returns ShopDomainSuggestionResult()
        viewModel.getDomainShopNameSuggestions(anyString())
        verify {
            ShopOpenRevampGetDomainNameSuggestionUseCase
                    .createRequestParams(anyString())
        }
        Assert.assertTrue(getDomainNameSuggestionUseCase.params.parameters.isNotEmpty())
        coVerify {
            getDomainNameSuggestionUseCase.executeOnBackground()
        }
        Assert.assertTrue(viewModel.domainShopNameSuggestionsResponse.value is Success)
    }

    @Test
    fun testCreateShop() {
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
    fun testSendSurveyData() {
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
    fun testSaveShopShipmentLocation() {
        mockkObject(ShopOpenRevampSaveShipmentLocationUseCase)
        coEvery {
            saveShopShipmentLocationUseCase.executeOnBackground()
        } returns SaveShipmentLocation()
        viewModel.saveShippingLocation(
                anyInt(), anyString(), anyInt(), anyString(), anyString(), anyString()
        )
    }

    @Test
    fun testGetSurveyData() {
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