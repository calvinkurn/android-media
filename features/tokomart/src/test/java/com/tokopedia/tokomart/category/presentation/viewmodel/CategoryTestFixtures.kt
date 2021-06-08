package com.tokopedia.tokomart.category.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokomart.category.domain.model.CategoryModel
import com.tokopedia.tokomart.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokomart.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.HARDCODED_WAREHOUSE_ID_PLEASE_DELETE
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW_DIRECTORY
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule

open class CategoryTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val defaultCategoryId = 123
    protected val defaultQueryParamMap = mapOf<String, String>()
    protected val getCategoryFirstPageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getCategoryLoadMorePageUseCase = mockk<UseCase<CategoryModel>>(relaxed = true)
    protected val getFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val getProductCountUseCase = mockk<UseCase<String>>(relaxed = true)
    protected val getMiniCartListSimplifiedUseCase = mockk<GetMiniCartListSimplifiedUseCase>(relaxed = true)
    protected val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)
    protected val abTestPlatformWrapper = mockk<ABTestPlatformWrapper>(relaxed = true)
    protected val dummyChooseAddressData =
            LocalCacheModel(
                    address_id = "12257",
                    city_id = "12345",
                    district_id = "2274",
                    lat = "1.1000",
                    long = "37.002",
                    postal_code = "15123",
            )
    protected lateinit var categoryViewModel: CategoryViewModel

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
            categoryId: Int = defaultCategoryId,
            queryParamMap: Map<String, String> = defaultQueryParamMap,
    ) {
        categoryViewModel = CategoryViewModel(
                CoroutineTestDispatchersProvider,
                categoryId,
                queryParamMap,
                getCategoryFirstPageUseCase,
                getCategoryLoadMorePageUseCase,
                getFilterUseCase,
                getProductCountUseCase,
                getMiniCartListSimplifiedUseCase,
                addToCartUseCase,
                chooseAddressWrapper,
                abTestPlatformWrapper,
        )
    }

    protected fun createMandatoryTokonowQueryParams(
            chooseAddressData: LocalCacheModel = dummyChooseAddressData
    ) = mapOf(
            SearchApiConst.NAVSOURCE to TOKONOW_DIRECTORY,
            SearchApiConst.SOURCE to TOKONOW_DIRECTORY,
            SearchApiConst.DEVICE to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE,
            SearchApiConst.SRP_PAGE_ID to defaultCategoryId.toString(),
            SearchApiConst.USER_WAREHOUSE_ID to HARDCODED_WAREHOUSE_ID_PLEASE_DELETE,
            SearchApiConst.USER_CITY_ID to chooseAddressData.city_id,
            SearchApiConst.USER_ADDRESS_ID to chooseAddressData.address_id,
            SearchApiConst.USER_DISTRICT_ID to chooseAddressData.district_id,
            SearchApiConst.USER_LAT to chooseAddressData.lat,
            SearchApiConst.USER_LONG to chooseAddressData.long,
            SearchApiConst.USER_POST_CODE to chooseAddressData.postal_code,
        )

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

    protected fun `Given view already created`() {
        categoryViewModel.onViewCreated()
    }

    protected fun `When view created`() {
        categoryViewModel.onViewCreated()
    }
}