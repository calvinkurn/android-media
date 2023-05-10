package com.tokopedia.shop.open.view.viewmodel

import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ShopOpenRevampSaveShipmentLocationUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.open.domain.ShopOpenRevampCreateShopUseCase
import com.tokopedia.shop.open.domain.ShopOpenRevampGetSurveyUseCase
import com.tokopedia.shop.open.domain.ShopOpenRevampSendSurveyUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Matchers.anyMap

@ExperimentalCoroutinesApi
class ShopOpenRevampViewModelTest: ShopOpenRevampViewModelTestFixtures()  {

    @Test
    fun `given success response when validate shop name is called `() {
        runTest {
            shopName = "tokoHape"

            mockkObject(ValidateDomainShopNameUseCase)

            everyValidateDomainShopNameUseCase(isSuccess = true)

            viewModel.checkShopName(shopName)

            advanceUntilIdle()

            verifyValidateShopNameUseCase(shopName)

            Assert.assertTrue((privateCurrentShopNameField).get(viewModel) == shopName)
            Assert.assertTrue(viewModel.checkShopNameResponse.value is Success)
        }
    }

    @Test
    fun `check shop name is return if given shop name is empty`() {
        shopName = ""

        viewModel.checkShopName(shopName)

        // check validateDomainShopNameUseCase not called because domainName is Empty
        Assert.assertTrue((privateCurrentShopNameField).get(viewModel) == "")
    }

    @Test
    fun `given success response when validate domain name is called `() {
        runTest {
            domainName = "tokoHape"

            mockkObject(ValidateDomainShopNameUseCase)

            everyValidateDomainShopNameUseCase(isSuccess = true)

            viewModel.checkDomainName(domainName)

            advanceUntilIdle()

            verifyValidateDomainNameUseCase(domainName)

            Assert.assertTrue((privateCurrentDomainNameField).get(viewModel) == domainName)
            Assert.assertTrue(viewModel.checkDomainNameResponse.value is Success)
        }
    }

    @Test
    fun `check domain name is return if given domain name is empty`() {
        shopName = ""

        viewModel.checkDomainName(domainName)

        // check validateDomainShopNameUseCase not called because domainName is Empty
        Assert.assertTrue((privateCurrentDomainNameField).get(viewModel) == "")
    }

    @Test
    fun `given shop name validation when shop name is not empty and gotten success result`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            everyValidateDomainShopNameUseCase(isSuccess = true)

            viewModel.validateShopName(shopName)

            advanceUntilIdle()

            verifyValidateShopNameUseCase(shopName)

            Assert.assertTrue((privateCurrentShopNameField).get(viewModel) == shopName)
            Assert.assertTrue(viewModel.checkShopNameResponse.value is Success)
        }
    }

    @Test
    fun `given shop name validation when shop name is not empty and gotten failed result`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            everyValidateDomainShopNameUseCase(isSuccess = false)

            viewModel.validateShopName(shopName)

            advanceUntilIdle()

            verifyValidateShopNameUseCase(shopName)

            Assert.assertTrue((privateCurrentShopNameField).get(viewModel) == shopName)
            Assert.assertTrue(viewModel.checkShopNameResponse.value is Fail)
        }
    }

    @Test
    fun `given shop name validation when shop name is not empty and the shop name do not equal to current shop name`() {
        runTest {
            shopName = "tera1717"

            privateCurrentShopNameField.set(viewModel, "erra2222")

            viewModel.validateShopName(shopName)

            advanceUntilIdle()

            Assert.assertTrue((privateCurrentShopNameField).get(viewModel) != shopName)
        }
    }

    @Test
    fun `given domain name validation when domain name is not empty and gotten success result`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            everyValidateDomainShopNameUseCase(isSuccess = true)

            viewModel.validateDomainName(domainName)

            advanceUntilIdle()

            verifyValidateDomainNameUseCase(domainName)

            Assert.assertTrue((privateCurrentDomainNameField).get(viewModel) == domainName)
            Assert.assertTrue(viewModel.checkDomainNameResponse.value is Success)
        }
    }

    @Test
    fun `given domain name validation when domain name is not empty and gotten failed result`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            everyValidateDomainShopNameUseCase(isSuccess = false)

            viewModel.validateDomainName(domainName)

            advanceUntilIdle()

            verifyValidateDomainNameUseCase(domainName)

            Assert.assertTrue((privateCurrentDomainNameField).get(viewModel) == domainName)
            Assert.assertTrue(viewModel.checkDomainNameResponse.value is Fail)
        }
    }

    @Test
    fun `given domain name validation when shop name is not empty and the domain name do not equal to current domain name`() {
        runTest {
            domainName = "tera5555"

            privateCurrentShopNameField.set(viewModel, "erra1818")

            viewModel.validateDomainName(domainName)

            advanceUntilIdle()

            Assert.assertTrue((privateCurrentDomainNameField).get(viewModel) != domainName)
        }
    }

    @Test
    fun `given domain name suggestion when shop name is provided and gotten success result`() {
        runTest {
            mockkObject(GetShopDomainNameSuggestionUseCase)

            everyGetShopDomainNameSuggestionUseCase(isSuccess = true)

            viewModel.getDomainShopNameSuggestions(domainName)

            advanceUntilIdle()

            verifyGetShopDomainNameSuggestionUseCase(domainName)

            Assert.assertTrue(viewModel.domainShopNameSuggestionsResponse.value is Success)
        }
    }

    @Test
    fun `given domain name suggestion when shop name is provided and gotten failed result`() {
        runTest {
            mockkObject(GetShopDomainNameSuggestionUseCase)

            everyGetShopDomainNameSuggestionUseCase(isSuccess = false)

            viewModel.getDomainShopNameSuggestions(domainName)

            advanceUntilIdle()

            verifyGetShopDomainNameSuggestionUseCase(domainName)

            Assert.assertTrue(viewModel.domainShopNameSuggestionsResponse.value is Fail)
        }
    }

    @Test
    fun `given shop id when valid shop name and valid domain name are provided and gotten success result`() {
        runTest {
            mockkObject(ShopOpenRevampCreateShopUseCase)

            everyCreateShopUseCase(isSuccess = true)

            viewModel.createShop(shopName, domainName)

            advanceUntilIdle()

            verifyCreateShopUseCase(shopName, domainName)

            Assert.assertTrue(viewModel.createShopOpenResponse.value is Success)
        }
    }

    @Test
    fun `given shop id when valid shop name and valid domain name are provided and gotten failed result`() {
        runTest {
            mockkObject(ShopOpenRevampCreateShopUseCase)

            everyCreateShopUseCase(isSuccess = false)

            viewModel.createShop(shopName, domainName)

            advanceUntilIdle()

            verifyCreateShopUseCase(shopName, domainName)

            Assert.assertTrue(viewModel.createShopOpenResponse.value is Fail)
        }
    }

    @Test
    fun `given success param when send survey data and gotten success result`() {
        runTest {
            mockkObject(ShopOpenRevampSendSurveyUseCase)

            everySendSurveyUseCase(isSuccess = true)

            viewModel.sendSurveyData(anyMap())

            advanceUntilIdle()

            verifySendSurveyUseCase(anyMap())

            Assert.assertTrue(viewModel.sendSurveyDataResponse.value is Success)
        }
    }

    @Test
    fun `given success param when send survey data and gotten failed result`() {
        runTest {
            mockkObject(ShopOpenRevampSendSurveyUseCase)

            everySendSurveyUseCase(isSuccess = false)

            viewModel.sendSurveyData(anyMap())

            advanceUntilIdle()

            verifySendSurveyUseCase(anyMap())

            Assert.assertTrue(viewModel.sendSurveyDataResponse.value is Fail)
        }
    }

    @Test
    fun `given quisionaire data when request is executed and gotten success result`() {
        runTest {
            mockkObject(ShopOpenRevampGetSurveyUseCase)

            everyGetSurveyUseCase(isSuccess = true)

            viewModel.getSurveyQuizionaireData()

            advanceUntilIdle()

            verifyGetSurveyUseCase()

            Assert.assertTrue(viewModel.getSurveyDataResponse.value is Success)
        }
    }

    @Test
    fun `given quisionaire data when request is executed and gotten failed result`() {
        runTest {
            mockkObject(ShopOpenRevampGetSurveyUseCase)

            everyGetSurveyUseCase(isSuccess = false)

            viewModel.getSurveyQuizionaireData()

            advanceUntilIdle()

            verifyGetSurveyUseCase()

            Assert.assertTrue(viewModel.getSurveyDataResponse.value is Fail)
        }
    }

    @Test
    fun `given success message when shopId, postCode, courierOrigin, addrStreet, lat, long are provided and gotten success result`() {
        runTest {
            val saveShippingData: MutableMap<String, Any> = viewModel.getSaveShopShippingLocationData(shopId, postCode, courierOrigin, addrStreet, lat, long)

            mockkObject(ShopOpenRevampSaveShipmentLocationUseCase)

            everySaveShopShipmentLocationUseCase(isSuccess = true)

            viewModel.saveShippingLocation(saveShippingData)

            advanceUntilIdle()

            verifySaveShopShipmentLocationUseCase(saveShippingData)

            Assert.assertTrue(viewModel.saveShopShipmentLocationResponse.value is Success)
        }
    }

    @Test
    fun `given success message when shopId, postCode, courierOrigin, addrStreet, lat, long are provided and gotten failed result`() {
        runTest {
            val saveShippingData: MutableMap<String, Any> = viewModel.getSaveShopShippingLocationData(shopId, postCode, courierOrigin, addrStreet, lat, long)

            mockkObject(ShopOpenRevampSaveShipmentLocationUseCase)

            everySaveShopShipmentLocationUseCase(isSuccess = false)

            viewModel.saveShippingLocation(saveShippingData)

            advanceUntilIdle()

            verifySaveShopShipmentLocationUseCase(saveShippingData)

            Assert.assertTrue(viewModel.saveShopShipmentLocationResponse.value is Fail)
        }
    }

    @Test
    fun `given survey payload when data survey is provided`() {
        val exampleQuestionID = 1
        val exampleQuestionID2 = 2
        val exampleChoices1 = mutableListOf(1, 2, 3)
        val exampleChoices2 = mutableListOf(3, 4, 5)

        val dataSurvey: MutableMap<Int, MutableList<Int>> = mutableMapOf()
        dataSurvey[exampleQuestionID] = exampleChoices1
        dataSurvey[exampleQuestionID2] = exampleChoices2

        val surveyPayload = viewModel.getDataSurveyInput(dataSurvey)

        Assert.assertTrue(surveyPayload.isNotEmpty())
    }
}
