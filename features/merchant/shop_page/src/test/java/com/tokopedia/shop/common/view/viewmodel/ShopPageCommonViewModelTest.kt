package com.tokopedia.shop.common.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.util.LiveDataUtil.observeAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import org.junit.*

class ShopPageCommonViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val shopChangeProductGridSharedViewModel by lazy {
        ShopChangeProductGridSharedViewModel()
    }

    private val shopPageFollowingStatusSharedViewModel by lazy {
        ShopPageFollowingStatusSharedViewModel()
    }

    private val shopProductFilterParameterSharedViewModel by lazy {
        ShopProductFilterParameterSharedViewModel()
    }

    @Before
    @Throws(Exception::class)
    fun setup() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun  `When change shared product grid type should be successful`(){
        val mockData = ShopProductViewGridType.BIG_GRID

        shopChangeProductGridSharedViewModel.changeSharedProductGridType(mockData)

        shopChangeProductGridSharedViewModel.sharedProductGridType.observeAwaitValue()

        shopChangeProductGridSharedViewModel.sharedProductGridType.value?.let {
            verifySuccessResult(mockData, it)
        }
    }

    @Test
    fun  `When set page following status should be successful`(){
        val mockData = true

        shopPageFollowingStatusSharedViewModel.setShopPageFollowingStatus(mockData)

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.observeAwaitValue()

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.value?.let {
            verifySuccessResult(mockData, it)
        }
    }

    @Test
    fun  `When change shared sort data should be successful`(){
        val mockData = ShopProductFilterParameter()

        shopProductFilterParameterSharedViewModel.changeSharedSortData(mockData)

        shopProductFilterParameterSharedViewModel.sharedShopProductFilterParameter.observeAwaitValue()

        shopProductFilterParameterSharedViewModel.sharedShopProductFilterParameter.value?.let {
            verifySuccessResult(mockData, it)
        }
    }

    private fun verifySuccessResult(prevData: Any, currentData: Any) {
        Assert.assertEquals(prevData, currentData)
    }
}