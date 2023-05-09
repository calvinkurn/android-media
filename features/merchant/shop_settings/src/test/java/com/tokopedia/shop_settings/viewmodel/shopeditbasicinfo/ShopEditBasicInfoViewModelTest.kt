package com.tokopedia.shop_settings.viewmodel.shopeditbasicinfo

import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockkObject
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import junit.framework.TestCase.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class ShopEditBasicInfoViewModelTest : ShopEditBasicInfoViewModelTestFixture() {

    @Test
    fun `when get shop basic data should return success`() {
        runTest {
            _onGetShopBasicData_thenReturnSuccess()

            shopEditBasicInfoViewModel.getShopBasicData()

            advanceTimeBy(1000)

            val isGetShopBasicDataSubscribe = shopEditBasicInfoViewModel.shopBasicData.value

            assertTrue(isGetShopBasicDataSubscribe is Success)
        }
    }

    @Test
    fun `when get shop basic data should return fail`() {
        runTest {
            _onGetShopBasicData_thenReturnFail()

            shopEditBasicInfoViewModel.getShopBasicData()

            advanceTimeBy(1000)

            val isGetShopBasicDataSubscribe = shopEditBasicInfoViewModel.shopBasicData.value

            assertTrue(isGetShopBasicDataSubscribe is Fail)
        }
    }

    @Test
    fun `when get status allow shop name domain changes should return success`() {
        runTest {
            mockkObject(GetAllowShopNameDomainChanges)
            onCheckAllowShopNameDomainChanges_thenReturnSuccess()

            shopEditBasicInfoViewModel.getAllowShopNameDomainChanges()

            advanceTimeBy(1000)

            verifySuccessGetAllowShopNameDomainChangesCalled()

            val allowShopNameDomainChanges = AllowShopNameDomainChanges().data
            val expectedResult = Success(allowShopNameDomainChanges)

            assertTrue(shopEditBasicInfoViewModel.allowShopNameDomainChanges.value is Success)
            shopEditBasicInfoViewModel.allowShopNameDomainChanges.verifySuccessEquals(expectedResult)
        }
    }

    @Test
    fun `when get status allow shop name domain changes should return fail`() {
        runTest {
            mockkObject(GetAllowShopNameDomainChanges)
            onCheckAllowShopNameDomainChanges_thenReturnFail()

            shopEditBasicInfoViewModel.getAllowShopNameDomainChanges()

            advanceTimeBy(1000)

            verifySuccessGetAllowShopNameDomainChangesCalled()

            assertTrue(shopEditBasicInfoViewModel.allowShopNameDomainChanges.value is Fail)
        }
    }

    @Test
    fun `when upload shop image should return upload result success`() {
        runTest {
            val imagePath: String = "imagePath"
            val name: String = "name"
            val domain: String = "domain"
            val tagline: String = "tagline"
            val description: String = "description"

            _onUploadShopImage_thenReturnUploadResultSuccess()

            shopEditBasicInfoViewModel.uploadShopImage(
                imagePath,
                name,
                domain,
                tagline,
                description
            )

            advanceTimeBy(1000)

            val isSuccessSubscribe = shopEditBasicInfoViewModel.uploadShopImage.value

            assertTrue(isSuccessSubscribe is Success)
        }
    }

    @Test
    fun `when upload shop image should return upload result error`() {
        runTest {
            val imagePath: String = "imagePath"
            val name: String = "name"
            val domain: String = "domain"
            val tagline: String = "tagline"
            val description: String = "description"

            _onUploadShopImage_thenReturnUploadResultError()

            shopEditBasicInfoViewModel.uploadShopImage(
                imagePath,
                name,
                domain,
                tagline,
                description
            )

            advanceTimeBy(1000)

            val isSuccessSubscribe = shopEditBasicInfoViewModel.uploadShopImage.value

            assertTrue(isSuccessSubscribe is Fail)
        }
    }

    @Test
    fun `when upload shop image should return null`() {
        runBlocking {
            val imagePath: String = "imagePath"
            val name: String = "name"
            val domain: String = "domain"
            val tagline: String = "tagline"
            val description: String = "description"

            _onUploadShopImage_thenReturnNull()

            shopEditBasicInfoViewModel.uploadShopImage(
                imagePath,
                name,
                domain,
                tagline,
                description
            )

            val subscribe = shopEditBasicInfoViewModel.uploadShopImage.observeAwaitValue()

            assertTrue(subscribe == null)
        }
    }

    @Test
    fun `when upload shop image should return fail`() {
        runTest {
            val imagePath: String = "imagePath"
            val name: String = "name"
            val domain: String = "domain"
            val tagline: String = "tagline"
            val description: String = "description"

            _onUploadShopImage_thenReturnFail()

            shopEditBasicInfoViewModel.uploadShopImage(
                imagePath,
                name,
                domain,
                tagline,
                description
            )

            advanceTimeBy(1000)

            val isSuccessSubscribe = shopEditBasicInfoViewModel.uploadShopImage.value

            assertTrue(isSuccessSubscribe is Fail)
        }
    }

    @Test
    fun `when update shop basic data should return success`() {
        runTest {
            val shopName = "shop"
            val shopDomain = "domain"
            val shopTagLine = "tagline"
            val shopDescription = "description"

            _onUpdateShopBasicData_thenReturn()

            shopEditBasicInfoViewModel.updateShopBasicData(
                shopName,
                shopDomain,
                shopTagLine,
                shopDescription
            )

            advanceTimeBy(1000)

            val isSuccessSubscribe = shopEditBasicInfoViewModel.updateShopBasicData.observeAwaitValue()

            assertTrue(isSuccessSubscribe is Success)
        }
    }

    @Test
    fun `when validate shop name should return success`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnSuccess()

            val shopName: String = "shopname"
            shopEditBasicInfoViewModel.validateShopName(shopName = shopName)
            advanceTimeBy(1000)

            verifySuccessValidateShopNameRequestParamsCalled(shopName)

            verifySuccessValidateShopNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopName.value is Success)
            Assert.assertNotNull(shopEditBasicInfoViewModel.validateShopName.value)
        }
    }

    @Test
    fun `when validate shop name should return fail and success`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnException()

            val shopName: String = "shopname"
            shopEditBasicInfoViewModel.validateShopName(shopName = shopName)
            advanceTimeBy(1000)

            verifySuccessValidateShopNameRequestParamsCalled(shopName)

            verifySuccessValidateShopNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopName.value is Fail)

            onValidateShopDomainName_thenReturnSuccess()

            shopEditBasicInfoViewModel.validateShopName(shopName = shopName)
            advanceTimeBy(1000)

            verifySuccessValidateShopNameRequestParamsCalled(shopName)

            verifySuccessValidateShopNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopName.value is Success)
        }
    }

    @Test
    fun `when validate shop domain should return fail and success`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnException()

            val shopDomain: String = "domain"
            shopEditBasicInfoViewModel.validateShopDomain(shopDomain = shopDomain)
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameRequestParamsCalled(shopDomain)

            verifySuccessValidateDomainNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Fail)

            onValidateShopDomainName_thenReturnSuccess()

            shopEditBasicInfoViewModel.validateShopDomain(shopDomain = shopDomain)
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameRequestParamsCalled(shopDomain)

            verifySuccessValidateDomainNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
        }
    }

    @Test
    fun `when validate shop name should return fail`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnThrowable()

            val shopName: String = "shopname"
            shopEditBasicInfoViewModel.validateShopName(shopName = shopName)
            advanceTimeBy(1000)

            verifySuccessValidateShopNameRequestParamsCalled(shopName)

            verifySuccessValidateShopNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopName.value is Fail)
        }
    }

    @Test
    fun `when validate shop domain should return fail`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnThrowable()

            val shopDomain: String = "domain"
            shopEditBasicInfoViewModel.validateShopDomain(shopDomain = shopDomain)
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameRequestParamsCalled(shopDomain)

            verifySuccessValidateDomainNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Fail)
        }
    }

    @Test
    fun `when validate domain name should return success`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnSuccess()

            val domainName: String = "domain"
            shopEditBasicInfoViewModel.validateShopDomain(domainName)
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameRequestParamsCalled(domainName)

            verifySuccessValidateDomainNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
            Assert.assertNotNull(shopEditBasicInfoViewModel.validateShopDomain.value)
        }
    }

    @Test
    fun `when validate domain name should return success and it is valid`() {
        runTest {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopDomainName_thenReturnSuccessIsValid()

            val domainName: String = "domain"
            shopEditBasicInfoViewModel.validateShopDomain(domainName)
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameRequestParamsCalled(domainName)

            verifySuccessValidateDomainNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
            Assert.assertNotNull(shopEditBasicInfoViewModel.validateShopDomain.value)
        }
    }

    @Test
    fun `when validate domain name should return success and get domain suggestion should return success`() {
        runTest {
            onValidateShopDomainName_thenReturnSuccess()
            privateCurrentShopNameField.set(shopEditBasicInfoViewModel, "shop")

            onGetShopDomainNameSuggestion_thenReturnSuccess()
            shopEditBasicInfoViewModel.validateShopDomain("domain")
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameCalled()
            verifyGetShopDomainNameSuggestionCalled()

            assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
            assertNotNull(shopEditBasicInfoViewModel.validateShopDomain.value)
            assertTrue(shopEditBasicInfoViewModel.shopDomainSuggestion.observeAwaitValue() is Success)
        }
    }

    @Test
    fun `when validate domain name should return success and get domain suggestion should return fail`() {
        runTest {
            onValidateShopDomainName_thenReturnSuccess()
            privateCurrentShopNameField.set(shopEditBasicInfoViewModel, "shop")

            onGetShopDomainNameSuggestion_thenReturnFail()
            shopEditBasicInfoViewModel.validateShopDomain("domain")
            advanceTimeBy(1000)

            verifySuccessValidateDomainNameCalled()
            verifyGetShopDomainNameSuggestionCalled()

            assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
            assertNotNull(shopEditBasicInfoViewModel.validateShopDomain.value)
            assertTrue(shopEditBasicInfoViewModel.shopDomainSuggestion.observeAwaitValue() is Fail)
        }
    }

    @Test
    fun `when set shop data and shop name should be the same`() {
        val shopBasicDataModel = ShopBasicDataModel().apply {
            name = "shop"
        }

        val shopName = "shop"
        shopEditBasicInfoViewModel.setShopName(shopName)
        shopEditBasicInfoViewModel.setCurrentShopData(shopBasicDataModel)

        assertTrue((privateCurrentShopNameField).get(shopEditBasicInfoViewModel) == shopName)
        assertTrue((privateCurrentShopField.get(shopEditBasicInfoViewModel) as ShopBasicDataModel).name == shopName)
    }

    @Test
    fun `when update shop basic data and current shop name is the same as previous name`() {
        val shopName = "shop"
        val shopDomain = "domain"
        val shopBasicDataModel = ShopBasicDataModel().apply {
            name = "shop"
            domain = "domain"
        }
        privateCurrentShopField.set(shopEditBasicInfoViewModel, shopBasicDataModel)

        shopEditBasicInfoViewModel.updateShopBasicData(shopName, shopDomain, "", "")

        assertTrue((privateCurrentShopField.get(shopEditBasicInfoViewModel) as ShopBasicDataModel).name == shopName)
        assertTrue((privateCurrentShopField.get(shopEditBasicInfoViewModel) as ShopBasicDataModel).domain == shopDomain)
    }
}
