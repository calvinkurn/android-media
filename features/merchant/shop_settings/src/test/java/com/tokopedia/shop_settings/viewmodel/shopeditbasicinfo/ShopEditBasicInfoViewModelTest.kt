package com.tokopedia.shop_settings.viewmodel.shopeditbasicinfo

import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop_settings.common.util.LiveDataUtil.observeAwaitValue
import com.tokopedia.shop_settings.common.util.LiveDataUtil.verifySuccessEquals
import com.tokopedia.usecase.coroutines.Success
import io.mockk.mockkObject
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopEditBasicInfoViewModelTest : ShopEditBasicInfoViewModelTestFixture() {

    @Test
    fun `when detach view should unsubscribe use case`() {
        shopEditBasicInfoViewModel.detachView()
        verifyUnsubscribeUseCase()
    }

    @Test
    fun `when get shop basic data should return success`() {
        runBlocking {
            _onGetShopBasicData_thenReturn()

            shopEditBasicInfoViewModel.getShopBasicData()

            val isGetShopBasicDataSubscribe = shopEditBasicInfoViewModel.shopBasicData.observeAwaitValue()

            assertTrue(isGetShopBasicDataSubscribe is Success)
        }
    }


    @Test
    fun `when get status allow shop name domain changes should return success`() {
        runBlocking {
            mockkObject(GetAllowShopNameDomainChanges)
            onCheckAllowShopNameDomainChanges_thenReturn()

            shopEditBasicInfoViewModel.getAllowShopNameDomainChanges()

            verifySuccessGetAllowShopNameDomainChangesCalled()

            val allowShopNameDomainChanges = AllowShopNameDomainChanges().data
            val expectedResult = Success(allowShopNameDomainChanges)

            assertTrue(shopEditBasicInfoViewModel.allowShopNameDomainChanges.value is Success)
            shopEditBasicInfoViewModel.allowShopNameDomainChanges.verifySuccessEquals(expectedResult)
        }
    }


    @Test
    fun `when upload shop image should return success`() {
        runBlocking {
            val imagePath: String = "imagePath"
            val name: String = "name"
            val domain: String = "domain"
            val tagline: String = "tagline"
            val description: String = "description"

            _onUploadShopImage_thenReturn()

            shopEditBasicInfoViewModel.uploadShopImage(
                    imagePath, name, domain, tagline, description)

            val isSuccessSubscribe = shopEditBasicInfoViewModel.uploadShopImage.observeAwaitValue()

            assertTrue(isSuccessSubscribe is Success)
        }
    }

    @Test
    fun `when update shop basic data should return success`() {
        runBlocking {
            val shopName = "shop"
            val shopDomain = "domain"
            val shopTagLine = "tagline"
            val shopDescription = "description"

            _onUpdateShopBasicData_thenReturn()

            shopEditBasicInfoViewModel.updateShopBasicData(
                    shopName, shopDomain, shopTagLine, shopDescription)

            val isSuccessSubscribe = shopEditBasicInfoViewModel.updateShopBasicData.observeAwaitValue()

            assertTrue(isSuccessSubscribe is Success)
        }
    }

    @Test
    fun `when validate shop name should return success`() {
        runBlocking {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateShopName_thenReturn()

            val shopName: String = "shopname"
            shopEditBasicInfoViewModel.validateShopName(shopName = shopName)
            Thread.sleep(1000)

            verifySuccessValidateShopNameRequestParamsCalled(shopName)

            Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())

            verifySuccessValidateShopNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopName.value is Success)
            Assert.assertNotNull(shopEditBasicInfoViewModel.validateShopName.value)
        }
    }

    @Test
    fun `when validate domain name should return success`() {
        runBlocking {
            mockkObject(ValidateDomainShopNameUseCase)

            onValidateDomainName_thenReturn()

            val domainName: String = "domain"
            shopEditBasicInfoViewModel.validateShopDomain(domain = domainName)
            Thread.sleep(2000)

            verifySuccessValidateDomainNameRequestParamsCalled(domainName)

            Assert.assertTrue(validateDomainShopNameUseCase.params.parameters.isNotEmpty())

            verifySuccessValidateDomainNameCalled()

            Assert.assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
            Assert.assertNotNull(shopEditBasicInfoViewModel.validateShopDomain.value)
        }
    }

    @Test
    fun `when validate domain name should return success and get domain suggestion should return success`() {
        runBlocking {
            onValidateDomainName_thenReturn()
            privateCurrentShopNameField.set(shopEditBasicInfoViewModel, "shop")

            onGetShopDomainNameSuggestion_thenReturn()
            shopEditBasicInfoViewModel.validateShopDomain(domain = "domain")
            delay(2000)

            verifySuccessValidateDomainNameCalled()
            verifyGetShopDomainNameSuggestionCalled()

            assertTrue(shopEditBasicInfoViewModel.validateShopDomain.value is Success)
            assertNotNull(shopEditBasicInfoViewModel.validateShopDomain.value)
            assertTrue(shopEditBasicInfoViewModel.shopDomainSuggestion.observeAwaitValue() is Success)
        }
    }

    @Test
    fun `when validate shop name but must return cause shop name is the same as current shop name`() {
        val shopBasicDataModel = ShopBasicDataModel().apply {
            name = "shop"
        }
        val shopName = "shop"
        privateCurrentShopField.set(shopEditBasicInfoViewModel, shopBasicDataModel)
        shopEditBasicInfoViewModel.validateShopName(shopName)
        assertTrue((privateCurrentShopField.get(shopEditBasicInfoViewModel) as ShopBasicDataModel).name == shopName)
    }

    @Test
    fun `when validate domain name but must return cause domain name is the same as current domain name`() {
        val shopBasicDataModel = ShopBasicDataModel().apply {
            domain = "domain"
        }
        val shopDomain = "domain"
        privateCurrentShopField.set(shopEditBasicInfoViewModel, shopBasicDataModel)
        shopEditBasicInfoViewModel.validateShopDomain(shopDomain)
        assertTrue((privateCurrentShopField.get(shopEditBasicInfoViewModel) as ShopBasicDataModel).domain == shopDomain)
    }

    @Test
    fun `when set current shop data should have the same data`() {
        val shopBasicDataModel = ShopBasicDataModel().apply {
            name = "shopName"
        }
        privateCurrentShopField.set(shopEditBasicInfoViewModel, shopBasicDataModel)
        shopBasicDataModel.name = "shop"
        shopEditBasicInfoViewModel.setCurrentShopData(shopBasicDataModel)
        assertTrue((privateCurrentShopField.get(shopEditBasicInfoViewModel) as ShopBasicDataModel) == shopBasicDataModel)
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

    @Test
    fun `when validate shop name should activate job then call validation again to cancel job after completion validation it will be active again`() {
        shopEditBasicInfoViewModel.validateShopName("shop")
        shopEditBasicInfoViewModel.validateShopName("shopName")
        assertTrue(shopEditBasicInfoViewModel.validateShopNameJob?.isActive == true)
    }

    @Test
    fun `when validate shop domain should activate job then call validation again to cancel job after completion validation it will be active again`() {
        shopEditBasicInfoViewModel.validateShopDomain("domain")
        shopEditBasicInfoViewModel.validateShopDomain("shopDomain")
        assertTrue(shopEditBasicInfoViewModel.validateShopDomainJob?.isActive == true)
    }
}