package com.tokopedia.shop_settings.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop_settings.viewmodel.shopeditbasicinfo.ShopEditBasicInfoViewModelTestFixture
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import io.mockk.*
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Matchers
import rx.Observable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ShopEditBasicInfoViewModelTest : ShopEditBasicInfoViewModelTestFixture() {

    @Test
    fun `when detach view should unsubscribe use case`() {
        shopEditBasicInfoViewModel.detachView()
        verifyUnsubscribeUseCase()
    }


    @Test
    fun `when get shop basic data should return success`() {
        runBlocking {
//            every {
//                getShopBasicDataUseCase.getData(any())
//            } returns ShopBasicDataModel()

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
            shopEditBasicInfoViewModel.allowShopNameDomainChanges.verifyValueEquals(expectedResult)
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

//            every {
//                uploadShopImageUseCase.getData(any())
//            } returns UploadShopEditImageModel()

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

//            every {
//                updateShopBasicDataUseCase.getData(any())
//            } returns "test string response"

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
    fun onSuccessCancelValidateShopNameJob() {
        shopEditBasicInfoViewModel.cancelValidateShopName()

        val job = shopEditBasicInfoViewModel.validateShopNameJob
        runBlocking {
            job?.cancel()
        }

        assertTrue(shopEditBasicInfoViewModel.validateShopNameJob == null)
    }

    @Test
    fun onSuccessCancelValidateShopDomainNameJob() {
        shopEditBasicInfoViewModel.cancelValidateShopDomain()

        val job = shopEditBasicInfoViewModel.validateShopDomainJob
        runBlocking {
            job?.cancel()
        }

        assertTrue(shopEditBasicInfoViewModel.validateShopDomainJob == null)
    }

    private fun <T> LiveData<T>.observeAwaitValue(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)
        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }
        observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }

}