package com.tokopedia.shop_settings.viewmodel.shopeditbasicinfo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.graphql.data.shopbasicdata.ShopBasicDataModel
import com.tokopedia.shop.common.graphql.data.shopbasicdata.gql.ShopBasicDataMutation
import com.tokopedia.shop.common.graphql.data.shopopen.ShopDomainSuggestionData
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
    lateinit var uploadShopImageUseCase: UploadShopImageUseCase

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
                uploadShopImageUseCase,
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

    protected fun onGetShopDomainNameSuggestion_thenReturn() {
        coEvery {
            getShopDomainNameSuggestionUseCase.executeOnBackground()
        } returns ShopDomainSuggestionData()
    }

    protected fun _onUpdateShopBasicData_thenReturn() {
        coEvery {
            updateShopBasicDataUseCase.setParams(any())
            updateShopBasicDataUseCase.executeOnBackground()
        } returns ShopBasicDataMutation()
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