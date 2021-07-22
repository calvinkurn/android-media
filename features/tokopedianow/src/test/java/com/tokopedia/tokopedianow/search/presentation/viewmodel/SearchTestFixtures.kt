package com.tokopedia.tokopedianow.search.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.cartcommon.domain.usecase.DeleteCartUseCase
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.usecase.GetChosenAddressWarehouseLocUseCase
import com.tokopedia.minicart.common.domain.usecase.GetMiniCartListSimplifiedUseCase
import com.tokopedia.tokopedianow.search.domain.model.SearchModel
import com.tokopedia.tokopedianow.searchcategory.utils.ABTestPlatformWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.ChooseAddressWrapper
import com.tokopedia.tokopedianow.searchcategory.utils.TOKONOW
import com.tokopedia.tokopedianow.util.SearchCategoryDummyUtils.dummyChooseAddressData
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.Before
import org.junit.Rule

open class SearchTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    protected val defaultKeyword = "samsung"
    protected val defaultQueryParamMap = mapOf(SearchApiConst.Q to defaultKeyword)
    protected val getSearchFirstPageUseCase = mockk<UseCase<SearchModel>>(relaxed = true)
    protected val getSearchLoadMorePageUseCase = mockk<UseCase<SearchModel>>(relaxed = true)
    protected val getFilterUseCase = mockk<UseCase<DynamicFilterModel>>(relaxed = true)
    protected val getProductCountUseCase = mockk<UseCase<String>>(relaxed = true)
    protected val getMiniCartListSimplifiedUseCase = mockk<GetMiniCartListSimplifiedUseCase>(relaxed = true)
    protected val addToCartUseCase = mockk<AddToCartUseCase>(relaxed = true)
    protected val updateCartUseCase = mockk<UpdateCartUseCase>(relaxed = true)
    protected val deleteCartUseCase = mockk<DeleteCartUseCase>(relaxed = true)
    protected val getWarehouseUseCase = mockk<GetChosenAddressWarehouseLocUseCase>(relaxed = true)
    protected val chooseAddressWrapper = mockk<ChooseAddressWrapper>(relaxed = true)
    protected val abTestPlatformWrapper = mockk<ABTestPlatformWrapper>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true).also {
        every { it.isLoggedIn } returns true
    }
    protected lateinit var tokoNowSearchViewModel: TokoNowSearchViewModel

    @Before
    open fun setUp() {
        `Given choose address data`()
        `Given search view model`()
    }

    protected fun `Given choose address data`(
            chooseAddressData: LocalCacheModel = dummyChooseAddressData
    ) {
        every {
            chooseAddressWrapper.getChooseAddressData()
        } returns chooseAddressData
    }

    protected fun `Given search view model`(queryParamMap: Map<String, String> = defaultQueryParamMap) {
        tokoNowSearchViewModel = TokoNowSearchViewModel(
                CoroutineTestDispatchersProvider,
                queryParamMap,
                getSearchFirstPageUseCase,
                getSearchLoadMorePageUseCase,
                getFilterUseCase,
                getProductCountUseCase,
                getMiniCartListSimplifiedUseCase,
                addToCartUseCase,
                updateCartUseCase,
                deleteCartUseCase,
                getWarehouseUseCase,
                chooseAddressWrapper,
                abTestPlatformWrapper,
                userSession,
        )
    }

    protected fun `Given get search first page use case will be successful`(
            searchModel: SearchModel,
            requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        every {
            getSearchFirstPageUseCase.execute(any(), any(), capture(requestParamsSlot))
        } answers {
            firstArg<(SearchModel) -> Unit>().invoke(searchModel)
        }
    }

    protected fun createMandatoryTokonowQueryParams(
            chooseAddressData: LocalCacheModel = dummyChooseAddressData
    ) = mapOf(
            SearchApiConst.SOURCE to TOKONOW,
            SearchApiConst.DEVICE to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE,
            SearchApiConst.USER_WAREHOUSE_ID to chooseAddressData.warehouse_id,
            SearchApiConst.USER_CITY_ID to chooseAddressData.city_id,
            SearchApiConst.USER_ADDRESS_ID to chooseAddressData.address_id,
            SearchApiConst.USER_DISTRICT_ID to chooseAddressData.district_id,
            SearchApiConst.USER_LAT to chooseAddressData.lat,
            SearchApiConst.USER_LONG to chooseAddressData.long,
            SearchApiConst.USER_POST_CODE to chooseAddressData.postal_code,
    )

    protected fun `Given view already created`() {
        tokoNowSearchViewModel.onViewCreated()
    }

    protected fun `When view created`() {
        tokoNowSearchViewModel.onViewCreated()
    }
}