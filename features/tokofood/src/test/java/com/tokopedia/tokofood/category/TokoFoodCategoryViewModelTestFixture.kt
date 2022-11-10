package com.tokopedia.tokofood.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addErrorState
import com.tokopedia.tokofood.feature.home.domain.mapper.TokoFoodCategoryMapper.addProgressBar
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodCategoryViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

abstract class TokoFoodCategoryViewModelTestFixture {

    @RelaxedMockK
    lateinit var tokoFoodMerchantListUseCase: TokoFoodMerchantListUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    protected lateinit var viewModel: TokoFoodCategoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoFoodCategoryViewModel(
            tokoFoodMerchantListUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyCategoryIsShowingErrorState(actualResponse: Boolean) {
        Assert.assertTrue(actualResponse)
    }

    protected fun verifyCategoryIsNotShowingErrorState(actualResponse: Boolean) {
        Assert.assertFalse(actualResponse)
    }

    protected fun onGetMerchantList_thenReturn(
        layoutResponse: TokoFoodMerchantListResponse,
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        option: Int = 0,
        sortBy: Int = 0,
        cuisine: String = "",
        pageKey: String = "0",
        brandUId: String = ""
    ) {
        coEvery { tokoFoodMerchantListUseCase.execute(
            localCacheModel = localCacheModel,
            pageKey = pageKey,
            option = option,
            sortBy = sortBy,
            cuisine = cuisine,
            brandUId = brandUId
        ) } returns layoutResponse
    }

    protected fun onGetMerchantList_thenReturn(
        error: Throwable,
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        option: Int = 0,
        sortBy: Int = 0,
        cuisine: String = "",
        pageKey: String = "0",
        brandUId: String = ""
    ) {
        coEvery { tokoFoodMerchantListUseCase.execute(
            localCacheModel = localCacheModel,
            pageKey = pageKey,
            option = option,
            sortBy = sortBy,
            cuisine = cuisine,
            brandUId = brandUId
        ) } throws error
    }

    fun mockProgressBar() {
        viewModel.categoryLayoutItemList.addProgressBar()
    }

    fun mockErrorState(throwable: Throwable) {
        viewModel.categoryLayoutItemList.addErrorState(throwable)
    }
}