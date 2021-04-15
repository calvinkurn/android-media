package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.shop.common.constant.ShopPageConstant.DISABLE_SHOP_PAGE_CACHE_INITIAL_PRODUCT_LIST
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestStatus
import com.tokopedia.shop.common.domain.interactor.*
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.domain.interactor.*
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateSuccessResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageP1DataUseCase
import com.tokopedia.shop.pageheader.domain.interactor.ShopModerateRequestStatusUseCase
import com.tokopedia.shop.pageheader.domain.interactor.ShopRequestUnmoderateUseCase
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import dagger.Lazy
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.io.File

class ShopPageViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var gqlGetShopFavoriteStatusUseCase: Lazy<GQLGetShopFavoriteStatusUseCase>

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>

    @RelaxedMockK
    lateinit var getBroadcasterShopConfigUseCase: Lazy<GetBroadcasterShopConfigUseCase>

    @RelaxedMockK
    lateinit var gqlGetShopInfobUseCaseCoreAndAssets: Lazy<GQLGetShopInfoUseCase>

    @RelaxedMockK
    lateinit var getShopReputationUseCase: Lazy<GetShopReputationUseCase>

    @RelaxedMockK
    lateinit var toggleFavouriteShopUseCase: Lazy<ToggleFavouriteShopUseCase>

    @RelaxedMockK
    lateinit var shopQuestGeneralTrackerUseCase: Lazy<ShopQuestGeneralTrackerUseCase>

    @RelaxedMockK
    lateinit var gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>

    @RelaxedMockK
    lateinit var getShopPageP1DataUseCase: Lazy<GetShopPageP1DataUseCase>

    @RelaxedMockK
    lateinit var getShopProductListUseCase: Lazy<GqlGetShopProductUseCase>

    @RelaxedMockK
    lateinit var shopModerateRequestStatusUseCase: Lazy<ShopModerateRequestStatusUseCase>

    @RelaxedMockK
    lateinit var shopRequestUnmoderateUseCase: Lazy<ShopRequestUnmoderateUseCase>

    @RelaxedMockK
    lateinit var getFollowStatusUseCase: Lazy<GetFollowStatusUseCase>

    @RelaxedMockK
    lateinit var updateFollowStatusUseCase: Lazy<UpdateFollowStatusUseCase>

    @RelaxedMockK
    lateinit var context: Context

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var shopPageViewModel : ShopPageViewModel

    private val SAMPLE_SHOP_ID = "123"

    private val addressWidgetData: LocalCacheModel = LocalCacheModel()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopPageViewModel = ShopPageViewModel(
                gqlGetShopFavoriteStatusUseCase,
                userSessionInterface,
                gqlGetShopInfoForHeaderUseCase,
                getBroadcasterShopConfigUseCase,
                gqlGetShopInfobUseCaseCoreAndAssets,
                getShopReputationUseCase,
                shopQuestGeneralTrackerUseCase,
                gqlGetShopOperationalHourStatusUseCase,
                getShopPageP1DataUseCase,
                getShopProductListUseCase,
                shopModerateRequestStatusUseCase,
                shopRequestUnmoderateUseCase,
                getFollowStatusUseCase,
                updateFollowStatusUseCase,
                testCoroutineDispatcherProvider
        )
    }

    @Test
    fun `check whether isMyShop return true if given same shop id as mocked user session shop id`() {
        every { userSessionInterface.shopId } returns SAMPLE_SHOP_ID
        assertTrue(shopPageViewModel.isMyShop(SAMPLE_SHOP_ID))
    }

    @Test
    fun `check whether isUserSessionActive return the same value as the mock value`() {
        every { userSessionInterface.isLoggedIn } returns true
        assertTrue(shopPageViewModel.isUserSessionActive)
    }

    @Test
    fun `check whether ownerShopName return the same value as the mock value`() {
        val shopNameMock = "mock shop"
        every { userSessionInterface.shopName } returns shopNameMock
        assertTrue(shopPageViewModel.ownerShopName == shopNameMock)
    }

    @Test
    fun `check whether shopPageP1Data value is Success`() {
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } returns ShopPageHeaderP1()
        coEvery { getShopProductListUseCase.get().executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(),ShopProduct())
        )
        shopPageViewModel.getShopPageTabData(
                SAMPLE_SHOP_ID.toIntOrZero(),
                "shop domain",
                1,
                10,
                ShopProductFilterParameter(),
                "",
                "",
                false,
                addressWidgetData
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageViewModel.shopPageP1Data.value is Success)
        assert(shopPageViewModel.productListData.data.size == 2)


    }

    @Test
    fun `check whether shopPageP1Data value is Success if isRefresh and cache remote config true`() {
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } returns ShopPageHeaderP1()
        coEvery { getShopProductListUseCase.get().executeOnBackground() } returns ShopProduct.GetShopProduct(
                data = listOf(ShopProduct(),ShopProduct())
        )
        shopPageViewModel.getShopPageTabData(
                SAMPLE_SHOP_ID.toIntOrZero(),
                "shop domain",
                1,
                10,
                ShopProductFilterParameter(),
                "",
                "",
                true,
                addressWidgetData
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageViewModel.shopPageP1Data.value is Success)
        assert(shopPageViewModel.productListData.data.size == 2)
    }

    @Test
    fun `check whether shopPageP1Data value is Fail`() {
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } throws Exception()
        shopPageViewModel.getShopPageTabData(
                SAMPLE_SHOP_ID.toIntOrZero(),
                "shop domain",
                1,
                10,
                ShopProductFilterParameter(),
                "",
                "",
                true,
                addressWidgetData
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageViewModel.shopPageP1Data.value is Fail)
    }

    @Test
    fun `check whether shopPageP1Data value is not null when shopId is 0 but shopDomain isn't empty`() {
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } returns ShopPageHeaderP1()
        shopPageViewModel.getShopPageTabData(
                0,
                "domain",
                1,
                10,
                ShopProductFilterParameter(),
                "",
                "",
                true,
                addressWidgetData
        )
        assertTrue(shopPageViewModel.shopPageP1Data.value != null)
    }

    @Test
    fun `check whether update follow status is success`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { updateFollowStatusUseCase.get().executeOnBackground() } returns FollowShopResponse(null)
        shopPageViewModel.updateFollowStatus(SAMPLE_SHOP_ID, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        coVerify { updateFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.followShopData.value is Success)
    }

    @Test
    fun `check whether update follow status is fail`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { updateFollowStatusUseCase.get().executeOnBackground() } throws Throwable()
        shopPageViewModel.updateFollowStatus(SAMPLE_SHOP_ID, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        coVerify { updateFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.followShopData.value is Fail)
    }

    @Test
    fun `check whether update follow status is error when user not login`() {
        every { userSessionInterface.isLoggedIn } returns false
        shopPageViewModel.updateFollowStatus(SAMPLE_SHOP_ID, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        assert(shopPageViewModel.followShopData.value is Fail)
    }

    @Test
    fun `check whether get follow status is success`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { getFollowStatusUseCase.get().executeOnBackground() } returns FollowStatusResponse(null)
        shopPageViewModel.getFollowStatus(SAMPLE_SHOP_ID)
        coVerify { getFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.followStatusData.value is Success)
    }

    @Test
    fun `check whether get follow status is fail`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { getFollowStatusUseCase.get().executeOnBackground() } throws Throwable()
        shopPageViewModel.getFollowStatus(SAMPLE_SHOP_ID)
        coVerify { getFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.followStatusData.value is Fail)
    }

    @Test
    fun `check whether getShopIdFromDomain post shopIdFromDomainData success value`() {
        coEvery { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() } returns ShopInfo()
        shopPageViewModel.getShopIdFromDomain("Mock domain")
        coVerify { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() }
        assert(shopPageViewModel.shopIdFromDomainData.value is Success)
    }

    @Test
    fun `check whether getShopIdFromDomain post shopIdFromDomainData fail value`() {
        coEvery { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() } throws Throwable()
        shopPageViewModel.getShopIdFromDomain("Mock domain")
        coVerify { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() }
        assert(shopPageViewModel.shopIdFromDomainData.value is Fail)
    }

    @Test
    fun `check whether getShopPageHeaderContentData post shopPageHeaderContentData success value`() {
        every { userSessionInterface.shopId } returns SAMPLE_SHOP_ID
        coEvery { gqlGetShopInfoForHeaderUseCase.get().executeOnBackground() } returns ShopInfo()
        coEvery { getShopReputationUseCase.get().executeOnBackground() } returns ShopBadge()
        coEvery { gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground() } returns ShopOperationalHourStatus()
        coEvery { gqlGetShopFavoriteStatusUseCase.get().executeOnBackground() } returns ShopInfo()
        coEvery { getBroadcasterShopConfigUseCase.get().executeOnBackground() } returns Broadcaster.Config()
        shopPageViewModel.getShopPageHeaderContentData(
                SAMPLE_SHOP_ID,
                "domain",
                true
        )
        coVerify { gqlGetShopInfoForHeaderUseCase.get().executeOnBackground() }
        coVerify { getShopReputationUseCase.get().executeOnBackground() }
        coVerify { gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground() }
        coVerify { gqlGetShopFavoriteStatusUseCase.get().executeOnBackground() }
        coVerify { getBroadcasterShopConfigUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.shopPageHeaderContentData.value is Success)

        shopPageViewModel.getShopPageHeaderContentData(
                "",
                "domain",
                true
        )
        assert(shopPageViewModel.shopPageHeaderContentData.value is Success)

        coEvery { gqlGetShopFavoriteStatusUseCase.get().executeOnBackground() } throws Exception()
        coEvery { getBroadcasterShopConfigUseCase.get().executeOnBackground() } throws Exception()
        shopPageViewModel.getShopPageHeaderContentData(
                SAMPLE_SHOP_ID,
                "",
                false
        )
        assert(shopPageViewModel.shopPageHeaderContentData.value is Success)
    }

    @Test
    fun `check whether shopImagePath value is set when call saveShopImageToPhoneStorage`() {
        val mockBitmap = mockk<Bitmap>()
        val mockTransition = mockk<Transition<in Bitmap>>()

        mockkStatic(ImageHandler::class)
        every { ImageHandler.loadImageWithTarget(any(), any(), any()) } answers {
            (thirdArg() as CustomTarget<Bitmap>).onResourceReady(mockBitmap, mockTransition)
        }

        mockkStatic(ImageProcessingUtil::class)
        every {
            ImageProcessingUtil.writeImageToTkpdPath(
                    mockBitmap,
                    Bitmap.CompressFormat.PNG)
        } returns File("path")
        shopPageViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageViewModel.shopImagePath.value.orEmpty().isNotEmpty())
    }

    @Test
    fun `check whether userShopId should return same value as mocked userSessionInterface shopId`() {
        val mockUserShopId = "123"
        every {
            userSessionInterface.shopId
        } returns mockUserShopId
        assert(shopPageViewModel.userShopId == mockUserShopId)
    }

    @Test
    fun `check whether userId should return same value as mocked userId`() {
        val mockUserId = "123"
        every {
            userSessionInterface.userId
        } returns mockUserId
        assert(shopPageViewModel.userId == mockUserId)
    }

    @Test
    fun `check whether checkShopRequestModerateStatus post shopModerateRequestStatus success value`() {
        coEvery {
            shopModerateRequestStatusUseCase.get().executeOnBackground()
        } returns ShopModerateRequestData(ShopModerateRequestStatus())
        shopPageViewModel.checkShopRequestModerateStatus()
        assert(shopPageViewModel.shopModerateRequestStatus.value is Success)
    }

    @Test
    fun `check whether checkShopRequestModerateStatus post shopModerateRequestStatus fail value`() {
        coEvery {
            shopModerateRequestStatusUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageViewModel.checkShopRequestModerateStatus()
        assert(shopPageViewModel.shopModerateRequestStatus.value is Fail)
    }

    @Test
    fun `check whether sendRequestUnmoderateShop post shopUnmoderateData success value`() {
        val mockShopId = 123.0
        val mockOptionValue = "optionValue"
        coEvery {
            shopRequestUnmoderateUseCase.get().executeOnBackground()
        } returns ShopRequestUnmoderateSuccessResponse()
        shopPageViewModel.sendRequestUnmoderateShop(mockShopId, mockOptionValue)
        assert(shopPageViewModel.shopUnmoderateData.value is Success)
    }

    @Test
    fun `check whether sendRequestUnmoderateShop post shopUnmoderateData fail value`() {
        val mockShopId = 123.0
        val mockOptionValue = "optionValue"
        coEvery {
            shopRequestUnmoderateUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageViewModel.sendRequestUnmoderateShop(mockShopId, mockOptionValue)
        assert(shopPageViewModel.shopUnmoderateData.value is Fail)
    }

    @Test
    fun `check whether sendShopShareTracker post shopShareTracker success value`() {
        val mockShopId = "123"
        val mockChannel = "channel"
        coEvery {
            shopQuestGeneralTrackerUseCase.get().executeOnBackground()
        } returns ShopQuestGeneralTracker()
        shopPageViewModel.sendShopShareTracker(mockShopId, mockChannel)
        assert(shopPageViewModel.shopShareTracker.value is Success)
    }

    @Test
    fun `check whether sendShopShareTracker post shopShareTracker fail value`() {
        val mockShopId = "123"
        val mockChannel = "channel"
        coEvery {
            shopQuestGeneralTrackerUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageViewModel.sendShopShareTracker(mockShopId, mockChannel)
        assert(shopPageViewModel.shopShareTracker.value is Fail)
    }



}