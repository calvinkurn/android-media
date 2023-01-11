package com.tokopedia.shop.common.view.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.shop.common.constant.CATEGORY_PARAM_KEY
import com.tokopedia.shop.common.constant.IS_FULFILLMENT_KEY
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShop
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.view.model.ShopPageFabConfig
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.util.LiveDataUtil.observeAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.mockk
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

    private val shopPageFeedTabSharedViewModel by lazy {
        ShopPageFeedTabSharedViewModel()
    }

    private val shopPageMiniCartSharedViewModel by lazy {
        ShopPageMiniCartSharedViewModel()
    }

    private val context = mockk<Context>(relaxed = true)

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
    fun `When change shared product grid type should be successful`() {
        val mockData = ShopProductViewGridType.BIG_GRID

        shopChangeProductGridSharedViewModel.changeSharedProductGridType(mockData)

        shopChangeProductGridSharedViewModel.sharedProductGridType.observeAwaitValue()

        shopChangeProductGridSharedViewModel.sharedProductGridType.value?.let {
            verifySuccessResult(mockData, it)
        }
    }

    @Test
    fun `When set page following status and isFollowing is true`() {
        val mockData = FollowShop(null, null, null, true, null, null)

        shopPageFollowingStatusSharedViewModel.setShopPageFollowingStatus(mockData, context)

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.observeAwaitValue()

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.value?.let {
            verifySuccessResult(mockData, it)
        }
    }

    @Test
    fun `When set page following status as null`() {
        shopPageFollowingStatusSharedViewModel.setShopPageFollowingStatus(null, context)

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.observeAwaitValue()

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.value?.let {
            verifySuccessResult(null, it)
        }
    }

    @Test
    fun `When set page following status and isFollowing is false`() {
        val mockData = FollowShop(null, null, null, false, null, null)

        shopPageFollowingStatusSharedViewModel.setShopPageFollowingStatus(mockData, context)

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.observeAwaitValue()

        shopPageFollowingStatusSharedViewModel.shopPageFollowingStatusLiveData.value?.let {
            verifyNotSameResult(mockData, it)
        }
    }

    @Test
    fun `When change shared sort data should be successful`() {
        val mockData = ShopProductFilterParameter()

        shopProductFilterParameterSharedViewModel.changeSharedSortData(mockData)

        shopProductFilterParameterSharedViewModel.sharedShopProductFilterParameter.observeAwaitValue()

        shopProductFilterParameterSharedViewModel.sharedShopProductFilterParameter.value?.let {
            verifySuccessResult(mockData, it)
        }
    }

    @Test
    fun `When change fulfillment filter active status should be true`() {
        val mapParameter = mapOf(IS_FULFILLMENT_KEY to "true")
        shopProductFilterParameterSharedViewModel.setFulfillmentFilterActiveStatus(mapParameter)

        Assert.assertEquals(mapParameter.containsKey(IS_FULFILLMENT_KEY), true)
        Assert.assertEquals(mapParameter[IS_FULFILLMENT_KEY], "true")
        Assert.assertEquals(shopProductFilterParameterSharedViewModel.isFulfillmentFilterActive, true)
    }

    @Test
    fun `When change fulfillment filter active status should be false`() {
        val mapParameter = mapOf(CATEGORY_PARAM_KEY to "1")
        shopProductFilterParameterSharedViewModel.setFulfillmentFilterActiveStatus(mapParameter)

        Assert.assertEquals(mapParameter.containsKey(IS_FULFILLMENT_KEY), false)
        Assert.assertEquals(shopProductFilterParameterSharedViewModel.isFulfillmentFilterActive, false)
    }

    @Test
    fun `Trigger shop feed tab clear cache should be success`() {
        shopPageFeedTabSharedViewModel.clearCache()
        shopPageFeedTabSharedViewModel.feedTabClearCache.observeAwaitValue()
        shopPageFeedTabSharedViewModel.feedTabClearCache.value?.let {
            verifySuccessResult(true, it)
        }
    }

    @Test
    fun `Hide shop page seller migration bottomsheet should be success`() {
        shopPageFeedTabSharedViewModel.hideSellerMigrationBottomSheet()
        shopPageFeedTabSharedViewModel.sellerMigrationBottomSheet.observeAwaitValue()
        shopPageFeedTabSharedViewModel.sellerMigrationBottomSheet.value?.let {
            verifySuccessResult(false, it)
        }
    }

    @Test
    fun `Show shop page seller migration bottomsheet should be success`() {
        shopPageFeedTabSharedViewModel.showSellerMigrationBottomSheet()
        shopPageFeedTabSharedViewModel.sellerMigrationBottomSheet.observeAwaitValue()
        shopPageFeedTabSharedViewModel.sellerMigrationBottomSheet.value?.let {
            verifySuccessResult(true, it)
        }
    }

    @Test
    fun `Setup shop page fab should be success`() {
        val mockShopPageFabConfig = ShopPageFabConfig()
        shopPageFeedTabSharedViewModel.setupShopPageFab(mockShopPageFabConfig)
        assert(shopPageFeedTabSharedViewModel.shopPageFabConfig == mockShopPageFabConfig)
        shopPageFeedTabSharedViewModel.shopPageFab.observeAwaitValue()
        shopPageFeedTabSharedViewModel.shopPageFab.value?.let {
            verifySuccessResult(ShopPageFeedTabSharedViewModel.FAB_ACTION_SETUP, it)
        }
    }

    @Test
    fun `Show shop page fab should be success`() {
        shopPageFeedTabSharedViewModel.showShopPageFab()
        shopPageFeedTabSharedViewModel.shopPageFab.observeAwaitValue()
        shopPageFeedTabSharedViewModel.shopPageFab.value?.let {
            verifySuccessResult(ShopPageFeedTabSharedViewModel.FAB_ACTION_SHOW, it)
        }
    }

    @Test
    fun `Hide shop page fab should be success`() {
        shopPageFeedTabSharedViewModel.hideShopPageFab()
        shopPageFeedTabSharedViewModel.shopPageFab.observeAwaitValue()
        shopPageFeedTabSharedViewModel.shopPageFab.value?.let {
            verifySuccessResult(ShopPageFeedTabSharedViewModel.FAB_ACTION_HIDE, it)
        }
    }

    @Test
    fun `When assign shopPageFabConfig, then get shopPageFabConfig should return mocked data`() {
        val mockShopPageFabConfig = ShopPageFabConfig()
        shopPageFeedTabSharedViewModel.shopPageFabConfig = mockShopPageFabConfig
        assert(shopPageFeedTabSharedViewModel.shopPageFabConfig == mockShopPageFabConfig)
    }

    private fun verifySuccessResult(prevData: Any?, currentData: Any) {
        Assert.assertEquals(prevData, currentData)
    }

    private fun verifyNotSameResult(prevData: Any, currentData: Any) {
        Assert.assertNotEquals(prevData, currentData)
    }

    @Test
    fun `When updateSharedMiniCartData, then miniCartSimplifiedData data should be the same as mocked data`() {
        val mockMiniCartSimplifiedData = MiniCartSimplifiedData()
        shopPageMiniCartSharedViewModel.updateSharedMiniCartData(mockMiniCartSimplifiedData)
        val resultMiniCartSimplifiedData =
            shopPageMiniCartSharedViewModel.miniCartSimplifiedData.value
        assert(resultMiniCartSimplifiedData == mockMiniCartSimplifiedData)
    }
}
