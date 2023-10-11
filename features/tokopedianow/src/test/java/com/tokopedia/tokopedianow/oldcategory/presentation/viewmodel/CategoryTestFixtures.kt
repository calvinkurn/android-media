package com.tokopedia.tokopedianow.oldcategory.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.common.domain.usecase.SetUserPreferenceUseCase
import com.tokopedia.tokopedianow.common.service.NowAffiliateService
import com.tokopedia.tokopedianow.oldcategory.domain.model.CategoryModel
import com.tokopedia.tokopedianow.searchcategory.cartservice.CartService
import com.tokopedia.tokopedianow.searchcategory.domain.usecase.GetFilterUseCase
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.tokopedianow.util.AddressMapperTestUtils.mapToWarehouses
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import com.tokopedia.tokopedianow.util.TestUtils.mockSuperClassField
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule

open class CategoryTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    protected val defaultCategoryL1 = "123"
    protected val defaultCategoryL2 = ""
    protected val defaultExternalServiceType = ""
    protected val defaultQueryParamMap = mapOf<String, String>()
    protected val getCategoryFirstPageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getCategoryLoadMorePageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getFilterUseCase = mockk<GetFilterUseCase>(relaxed = true)
    protected val getProductCountUseCase = mockk<UseCase<String>>(relaxed = true)
    protected val getMiniCartListSimplifiedUseCase = mockk<GetMiniCartListSimplifiedUseCase>(relaxed = true)
    protected val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    protected val updateCartUseCase = mockk<UpdateCartUseCase>(relaxed = true)
    protected val deleteCartUseCase = mockk<DeleteCartUseCase>(relaxed = true)
    protected val getWarehouseUseCase = mockk<GetChosenAddressWarehouseLocUseCase>(relaxed = true)
    protected val getRecommendationUseCase = mockk<GetRecommendationUseCase>(relaxed = true)
    protected val getCategoryListUseCase = mockk<GetCategoryListUseCase>(relaxed = true)
    protected val setUserPreferenceUseCase = mockk<SetUserPreferenceUseCase>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)
    protected val affiliateService = mockk<NowAffiliateService>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true).also {
        every { it.isLoggedIn } returns true
    }
    protected val cartService = CartService(
        addToCartUseCase,
        updateCartUseCase,
        deleteCartUseCase,
        userSession
    )
    protected lateinit var tokoNowCategoryViewModel: TokoNowCategoryViewModel

    @Before
    open fun setUp() {
        `Given choose address data`()
        `Given category view model`()
    }

    protected open fun `Given choose address data`(
        chooseAddressData: LocalCacheModel = dummyChooseAddressData
    ) {
        every {
            chooseAddressWrapper.getChooseAddressData()
        } returns chooseAddressData
    }

    protected open fun `Given category view model`(
        categoryL1: String = defaultCategoryL1,
        categoryL2: String = defaultCategoryL2,
        externalServiceType: String = defaultExternalServiceType,
        queryParamMap: Map<String, String> = defaultQueryParamMap
    ) {
        tokoNowCategoryViewModel = TokoNowCategoryViewModel(
            CoroutineTestDispatchersProvider,
            categoryL1,
            categoryL2,
            externalServiceType,
            queryParamMap,
            getCategoryFirstPageUseCase,
            getCategoryLoadMorePageUseCase,
            getFilterUseCase,
            getProductCountUseCase,
            getMiniCartListSimplifiedUseCase,
            cartService,
            getWarehouseUseCase,
            getCategoryListUseCase,
            setUserPreferenceUseCase,
            chooseAddressWrapper,
            affiliateService,
            userSession
        )
    }

    protected fun createMandatoryTokonowQueryParams(
        chooseAddressData: LocalCacheModel = dummyChooseAddressData
    ) = mutableMapOf(
        SearchApiConst.NAVSOURCE to TOKONOW_DIRECTORY,
        SearchApiConst.SOURCE to TOKONOW_DIRECTORY,
        SearchApiConst.DEVICE to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE,
        SearchApiConst.SRP_PAGE_ID to defaultCategoryL1,
        SearchApiConst.USER_CITY_ID to chooseAddressData.city_id,
        SearchApiConst.USER_ADDRESS_ID to chooseAddressData.address_id,
        SearchApiConst.USER_DISTRICT_ID to chooseAddressData.district_id,
        SearchApiConst.USER_LAT to chooseAddressData.lat,
        SearchApiConst.USER_LONG to chooseAddressData.long,
        SearchApiConst.USER_POST_CODE to chooseAddressData.postal_code
    ).apply {
        if (chooseAddressData.warehouses.isNotEmpty()) {
            SearchApiConst.WAREHOUSES to mapToWarehouses(chooseAddressData)
        }
    }

    protected fun `Given get category first page use case will be successful`(
        categoryModel: CategoryModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        every {
            getCategoryFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(CategoryModel) -> Unit>().invoke(categoryModel)
        }
    }

    protected fun `Given address data null`() {
        tokoNowCategoryViewModel.mockSuperClassField("chooseAddressData", null)
    }

    protected fun `Given view already created`() {
        tokoNowCategoryViewModel.onViewCreated()
    }

    protected fun `When view created`() {
        tokoNowCategoryViewModel.onViewCreated()
    }

    protected fun `When view reload page`() {
        tokoNowCategoryViewModel.onViewReloadPage()
    }
}
