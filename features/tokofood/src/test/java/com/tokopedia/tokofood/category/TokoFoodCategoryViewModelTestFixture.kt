package com.tokopedia.tokofood.category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodMerchantListResponse
import com.tokopedia.tokofood.feature.home.domain.usecase.TokoFoodMerchantListUseCase
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodErrorStateUiModel
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodListUiModel
import com.tokopedia.tokofood.feature.home.presentation.viewmodel.TokoFoodCategoryViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    private val privatePageKey by lazy {
        viewModel.getPrivateField<String>("pageKey")
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = TokoFoodCategoryViewModel(
            tokoFoodMerchantListUseCase,
            CoroutineTestDispatchersProvider
        )
    }

    protected fun verifyGetCategoryLayoutResponseSuccess(expectedResponse: TokoFoodListUiModel) {
        val actualResponse = viewModel.layoutList.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyGetCategoryLayoutResponseFail() {
        val actualResponse = viewModel.layoutList.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun verifyLoadMoreLayoutResponseSuccess(expectedResponse: TokoFoodListUiModel) {
        val actualResponse = viewModel.loadMore.value
        Assert.assertEquals(expectedResponse, (actualResponse as Success).data)
    }

    protected fun verifyLoadMoreLayoutResponseFail() {
        val actualResponse = viewModel.loadMore.value
        Assert.assertTrue(actualResponse is Fail)
    }

    protected fun onGetMerchantList_thenReturn(
        layoutResponse: TokoFoodMerchantListResponse,
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        option: Int = 0,
        sortBy: Int = 0,
        cuisine: String = "",
        pageKey: String = "0"
    ) {
        coEvery { tokoFoodMerchantListUseCase.execute(
            localCacheModel = localCacheModel,
            pageKey = pageKey,
            option = option,
            sortBy = sortBy,
            cuisine = cuisine
        ) } returns layoutResponse
    }

    protected fun onGetMerchantList_thenReturn(
        error: Throwable,
        localCacheModel: LocalCacheModel = LocalCacheModel(),
        option: Int = 0,
        sortBy: Int = 0,
        cuisine: String = "",
        pageKey: String = "0"
    ) {
        coEvery { tokoFoodMerchantListUseCase.execute(
            localCacheModel = localCacheModel,
            pageKey = pageKey,
            option = option,
            sortBy = sortBy,
            cuisine = cuisine) } throws error
    }

    protected fun verifyGetErrorLayoutShown() {
        val homeLayoutList = viewModel.layoutList.value
        val actualResponse = (homeLayoutList as Success).data.items.find { it is TokoFoodErrorStateUiModel }
        Assert.assertNotNull(actualResponse)
    }

    protected fun verifyPageKey(expectedPageKey: String){
        Assert.assertEquals(privatePageKey, expectedPageKey)
    }

    inline fun <reified T>Any.getPrivateField(name: String): T {
        return this::class.java.getDeclaredField(name).let {
            it.isAccessible = true
            return@let it.get(this) as T
        }
    }
}