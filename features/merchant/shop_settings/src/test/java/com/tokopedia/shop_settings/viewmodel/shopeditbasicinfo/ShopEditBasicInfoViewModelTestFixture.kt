package com.tokopedia.shop_settings.viewmodel.shopeditbasicinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateDomainShopName
import com.tokopedia.shop.common.graphql.data.shopopen.ValidateShopDomainNameResult
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.UpdateShopBasicDataUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.GetShopDomainNameSuggestionUseCase
import com.tokopedia.shop.common.graphql.domain.usecase.shopopen.ValidateDomainShopNameUseCase
import com.tokopedia.shop.settings.basicinfo.data.AllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.domain.GetAllowShopNameDomainChanges
import com.tokopedia.shop.settings.basicinfo.view.viewmodel.ShopEditBasicInfoViewModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import java.lang.reflect.Field

@ExperimentalCoroutinesApi
abstract class ShopEditBasicInfoViewModelTestFixture {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @RelaxedMockK
    lateinit var getShopBasicDataUseCase: GetShopBasicDataUseCase

    @RelaxedMockK
    lateinit var updateShopBasicDataUseCase: UpdateShopBasicDataUseCase

    @RelaxedMockK
    lateinit var uploaderUseCase: UploaderUseCase

    @RelaxedMockK
    lateinit var getAllowShopNameDomainChangesUseCase: GetAllowShopNameDomainChanges

    @RelaxedMockK
    lateinit var getShopDomainNameSuggestionUseCase: GetShopDomainNameSuggestionUseCase

    @RelaxedMockK
    lateinit var validateDomainShopNameUseCase: ValidateDomainShopNameUseCase

    protected lateinit var shopEditBasicInfoViewModel: ShopEditBasicInfoViewModel
    protected lateinit var privateCurrentShopNameField: Field
    protected lateinit var privateCurrentShopField: Field

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopEditBasicInfoViewModel = ShopEditBasicInfoViewModel(
                getShopBasicDataUseCase,
                updateShopBasicDataUseCase,
                uploaderUseCase,
                getAllowShopNameDomainChangesUseCase,
                getShopDomainNameSuggestionUseCase,
                validateDomainShopNameUseCase,
                coroutineTestRule.dispatchers
        )

        privateCurrentShopNameField = shopEditBasicInfoViewModel::class.java.getDeclaredField("currentShopName").apply {
            isAccessible = true
        }

        privateCurrentShopField = shopEditBasicInfoViewModel::class.java.getDeclaredField("currentShop").apply {
            isAccessible = true
        }
    }

    protected fun onCheckAllowShopNameDomainChanges_thenReturnSuccess() {
        coEvery { getAllowShopNameDomainChangesUseCase.executeOnBackground() } returns AllowShopNameDomainChanges()
    }

    protected fun onCheckAllowShopNameDomainChanges_thenReturnFail() {
        coEvery { getAllowShopNameDomainChangesUseCase.executeOnBackground() } throws Exception()
    }

    protected fun onValidateShopDomainName_thenReturnSuccess() {
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult()
    }

    protected fun onValidateShopDomainName_thenReturnSuccessIsValid() {
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } returns ValidateShopDomainNameResult(ValidateDomainShopName(isValid = true))
    }

    protected fun onValidateShopDomainName_thenReturnException() {
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } throws Exception()
    }

    protected fun onValidateShopDomainName_thenReturnThrowable() {
        coEvery {
            validateDomainShopNameUseCase.executeOnBackground()
        } throws Throwable()
    }

    protected fun onGetShopDomainNameSuggestion_thenReturnSuccess() {
        coEvery {
            getShopDomainNameSuggestionUseCase.executeOnBackground()
        } returns ShopDomainSuggestionData()
    }

    protected fun onGetShopDomainNameSuggestion_thenReturnFail() {
        coEvery {
            getShopDomainNameSuggestionUseCase.executeOnBackground()
        } throws Exception()
    }

    protected fun _onUpdateShopBasicData_thenReturn() {
        coEvery {
            updateShopBasicDataUseCase.setParams(any())
            updateShopBasicDataUseCase.executeOnBackground()
        } returns ShopBasicDataMutation()
    }

    protected fun _onGetShopBasicData_thenReturnSuccess() {
        every {
            getShopBasicDataUseCase.getData(any())
        } returns ShopBasicDataModel()
    }

    protected fun _onGetShopBasicData_thenReturnFail() {
        every {
            getShopBasicDataUseCase.getData(any())
        } throws Exception()
    }

    protected fun _onUploadShopImage_thenReturnUploadResultSuccess() {
        coEvery {
            uploaderUseCase(any())
        } returns UploadResult.Success("1231")
    }

    protected fun _onUploadShopImage_thenReturnUploadResultError() {
        coEvery {
            uploaderUseCase(any())
        } returns UploadResult.Error("error")
    }

    protected fun _onUploadShopImage_thenReturnNull() {
        coEvery {
            uploaderUseCase(any())
        } returns mockk<UploadResult>()
    }


    protected fun _onUploadShopImage_thenReturnFail() {
        coEvery {
            uploaderUseCase(any())
        } throws Exception()
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

    protected fun verifyGetShopDomainNameSuggestionCalled() {
        coVerify { getShopDomainNameSuggestionUseCase.executeOnBackground() }
    }

    protected fun verifySuccessValidateShopNameRequestParamsCalled(shoName: String) {
        verify { ValidateDomainShopNameUseCase.createRequestParams(shoName) }
    }

    protected fun verifySuccessValidateDomainNameRequestParamsCalled(domainName: String) {
        verify { ValidateDomainShopNameUseCase.createRequestParam(domainName) }
    }
}