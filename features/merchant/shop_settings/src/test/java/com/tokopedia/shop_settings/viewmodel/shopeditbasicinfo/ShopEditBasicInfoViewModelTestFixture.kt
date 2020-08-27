package com.tokopedia.shop_settings.viewmodel.shopeditbasicinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.data.UploadShopEditImageModel
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.UploadShopImageUseCase
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopEditBasicInfoViewModel
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
abstract class ShopEditBasicInfoViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var getShopBasicDataUseCase: GetShopBasicDataUseCase

    @RelaxedMockK
    lateinit var updateShopBasicDataUseCase: UpdateShopBasicDataUseCase

    @RelaxedMockK
    lateinit var uploadShopImageUseCase: UploadShopImageUseCase

    @RelaxedMockK
    lateinit var getAllowShopNameDomainChangesUseCase: GetAllowShopNameDomainChanges

    @RelaxedMockK
    lateinit var getShopDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase

    @RelaxedMockK
    lateinit var validateDomainShopNameUseCase: ValidateDomainShopNameUseCase

    protected lateinit var shopEditBasicInfoViewModel: ShopEditBasicInfoViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopEditBasicInfoViewModel = ShopEditBasicInfoViewModel(
                getShopBasicDataUseCase,
                updateShopBasicDataUseCase,
                uploadShopImageUseCase,
                getAllowShopNameDomainChangesUseCase,
                getShopDomainNameSuggestionUseCase,
                validateDomainShopNameUseCase,
                TestDispatcherProvider()
        )
    }

    protected fun LiveData<*>.verifyValueEquals(expected: Any) {
        val actual = value
        TestCase.assertEquals(expected, actual)
    }

    protected fun verifyUnsubscribeUseCase() {
        coVerify { getShopBasicDataUseCase.unsubscribe() }
        coVerify { updateShopBasicDataUseCase.unsubscribe() }
        coVerify { uploadShopImageUseCase.unsubscribe() }
    }

    protected fun onCheckAllowShopNameDomainChanges_thenReturn() {
        coEvery { getAllowShopNameDomainChangesUseCase.executeOnBackground() } returns AllowShopNameDomainChanges()
    }

    protected fun onValidateShopName_thenReturn() {
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
    }

    protected fun onValidateDomainName_thenReturn() {
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
    }

    internal fun<T: Any> LiveData<Result<T>>.verifySuccessEquals(expected: Success<Any>?) {
        val expectedResult = expected?.data
        val actualResult = (value as? Success<T>)?.data
        TestCase.assertEquals(expectedResult, actualResult)
    }

    protected fun _onUpdateShopBasicData_thenReturn() {
        every {
            updateShopBasicDataUseCase.getData(any())
        } returns "test string response"
    }

    protected fun _onGetShopBasicData_thenReturn() {
        every {
            getShopBasicDataUseCase.getData(any())
        } returns ShopBasicDataModel()
    }

    protected fun _onUploadShopImage_thenReturn() {
        every {
            uploadShopImageUseCase.getData(any())
        } returns UploadShopEditImageModel()
    }


    protected fun verifySuccessGetAllowShopNameDomainChangesCalled() {
        coVerify { getAllowShopNameDomainChangesUseCase.executeOnBackground() }
    }

    protected fun verifySuccessValidateShopNameCalled() {
        coVerify { validateDomainShopNameUseCase.executeOnBackground() }
    }

    protected fun verifySuccessValidateDomainNameCalled() {
        coVerify { validateDomainShopNameUseCase.executeOnBackground() }
    }

    protected fun verifySuccessValidateShopNameRequestParamsCalled(shoName: String) {
        verify { ValidateDomainShopNameUseCase.createRequestParams(shoName) }
    }

    protected fun verifySuccessValidateDomainNameRequestParamsCalled(domainName: String) {
        verify { ValidateDomainShopNameUseCase.createRequestParam(domainName) }
    }
}