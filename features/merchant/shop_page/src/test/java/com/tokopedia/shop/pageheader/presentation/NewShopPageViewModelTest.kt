package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestStatus
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.domain.interactor.*
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateSuccessResponse
import com.tokopedia.shop.pageheader.domain.interactor.*
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
import java.io.File
import com.tokopedia.shop.common.graphql.data.shopoperationalhourslist.ShopOperationalHoursListResponse
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType

class NewShopPageViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>

    @RelaxedMockK
    lateinit var getBroadcasterShopConfigUseCase: Lazy<GetBroadcasterShopConfigUseCase>

    @RelaxedMockK
    lateinit var gqlGetShopInfobUseCaseCoreAndAssets: Lazy<GQLGetShopInfoUseCase>

    @RelaxedMockK
    lateinit var shopQuestGeneralTrackerUseCase: Lazy<ShopQuestGeneralTrackerUseCase>

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
    lateinit var getShopPageHeaderLayoutUseCase: Lazy<GetShopPageHeaderLayoutUseCase>

    @RelaxedMockK
    lateinit var getShopOperationalHoursListUseCase: Lazy<GqlGetShopOperationalHoursListUseCase>


    @RelaxedMockK
    lateinit var context: Context

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var shopPageViewModel : NewShopPageViewModel

    private val SAMPLE_SHOP_ID = "123"
    private val SAMPLE_BUTTON_FOLLOW_VARIANT_TYPE = "follow_green_small"

    private val addressWidgetData: LocalCacheModel = LocalCacheModel()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopPageViewModel = NewShopPageViewModel(
                userSessionInterface,
                gqlGetShopInfoForHeaderUseCase,
                getBroadcasterShopConfigUseCase,
                gqlGetShopInfobUseCaseCoreAndAssets,
                shopQuestGeneralTrackerUseCase,
                getShopPageP1DataUseCase,
                getShopProductListUseCase,
                shopModerateRequestStatusUseCase,
                shopRequestUnmoderateUseCase,
                getShopPageHeaderLayoutUseCase,
                getFollowStatusUseCase,
                updateFollowStatusUseCase,
                getShopOperationalHoursListUseCase,
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
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } returns ShopPageHeaderP1(
                shopInfoHomeTypeData = ShopPageGetHomeType(
                        homeLayoutData = ShopPageGetHomeType.HomeLayoutData(
                                widgetIdList = listOf(ShopPageGetHomeType.HomeLayoutData.WidgetIdList())
                        )
                )
        )
        coEvery { getShopPageHeaderLayoutUseCase.get().executeOnBackground() } returns ShopPageHeaderLayoutResponse()
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
        assert(shopPageViewModel.homeWidgetLayoutData.widgetIdList.isNotEmpty())
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
        shopPageViewModel.getFollowStatusData(SAMPLE_SHOP_ID, SAMPLE_BUTTON_FOLLOW_VARIANT_TYPE)
        coVerify { getFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.followStatusData.value is Success)
    }

    @Test
    fun `check whether get follow status is fail`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { getFollowStatusUseCase.get().executeOnBackground() } throws Throwable()
        shopPageViewModel.getFollowStatusData(SAMPLE_SHOP_ID, SAMPLE_BUTTON_FOLLOW_VARIANT_TYPE)
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

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and streamAllowed is false`() {
        val mockShopId = "123"
        shopPageViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.streamAllowed == false)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and streamAllowed is true`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterShopConfigUseCase.get().executeOnBackground()
        } returns Broadcaster.Config(true)
        shopPageViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.streamAllowed == true)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post Success value if get network data error`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterShopConfigUseCase.get().executeOnBackground()
        } throws Throwable()
        shopPageViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
    }

    @Test
    fun `check whether shopPageTickerData and shopPageShopShareData post success value`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } returns ShopInfo()
        shopPageViewModel.getShopInfoData(mockShopId, mockShopDomain, false)
        assert(shopPageViewModel.shopPageTickerData.value is Success)
        assert(shopPageViewModel.shopPageShopShareData.value is Success)

        shopPageViewModel.getShopInfoData("0", mockShopDomain, true)
        assert(shopPageViewModel.shopPageTickerData.value is Success)
        assert(shopPageViewModel.shopPageShopShareData.value is Success)
    }

    @Test
    fun `check whether shopPageTickerData and shopPageShopShareData value is null if error when get data`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } throws Throwable()
        shopPageViewModel.getShopInfoData(mockShopId, mockShopDomain, false)
        assert(shopPageViewModel.shopPageTickerData.value == null)
        assert(shopPageViewModel.shopPageShopShareData.value == null)
    }

    @Test
    fun `check whether shopOperationalHoursListData value is success`() {
        val mockShopId = "123"
        coEvery {
            getShopOperationalHoursListUseCase.get().executeOnBackground()
        } returns ShopOperationalHoursListResponse()
        shopPageViewModel.getShopOperationalHoursList(mockShopId)
        assert(shopPageViewModel.shopOperationalHoursListData.value is Success)
    }

    @Test
    fun `check whether shopOperationalHoursListData value is fail if exception happened`() {
        val mockShopId = "123"
        coEvery {
            getShopOperationalHoursListUseCase.get().executeOnBackground()
        } throws Throwable()
        shopPageViewModel.getShopOperationalHoursList(mockShopId)
        assert(shopPageViewModel.shopOperationalHoursListData.value is Fail)
    }
}