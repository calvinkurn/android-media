package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.common_sdk_affiliate_toko.utils.AffiliateCookieHelper
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.media.loader.utils.MediaBitmapEmptyTarget
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.shop.common.data.model.HomeLayoutData
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.data.model.ShopQuestGeneralTracker
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestStatus
import com.tokopedia.shop.common.data.source.cloud.model.followshop.FollowShopResponse
import com.tokopedia.shop.common.data.source.cloud.model.followstatus.FollowStatusResponse
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopOperationalHourStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GetFollowStatusUseCase
import com.tokopedia.shop.common.domain.interactor.ShopQuestGeneralTrackerUseCase
import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.NewShopPageHeaderP1
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderLayoutResponse
import com.tokopedia.shop.pageheader.data.model.ShopRequestUnmoderateSuccessResponse
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterAuthorConfig
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageHeaderLayoutUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageP1DataUseCase
import com.tokopedia.shop.pageheader.domain.interactor.ShopModerateRequestStatusUseCase
import com.tokopedia.shop.pageheader.domain.interactor.ShopRequestUnmoderateUseCase
import com.tokopedia.shop.pageheader.util.ShopPageHeaderMapper
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.universal_sharing.view.model.AffiliateInput
import com.tokopedia.universal_sharing.view.model.GenerateAffiliateLinkEligibility
import com.tokopedia.universal_sharing.view.usecase.AffiliateEligibilityCheckUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.image.ImageProcessingUtil
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class ShopPageHeaderViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var gqlGetShopInfoForHeaderUseCase: Lazy<GQLGetShopInfoUseCase>

    @RelaxedMockK
    lateinit var getBroadcasterAuthorConfig: Lazy<GetBroadcasterAuthorConfig>

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
    lateinit var gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>

    @RelaxedMockK
    lateinit var eligibilityCheckUseCase: Lazy<AffiliateEligibilityCheckUseCase>

    @RelaxedMockK
    lateinit var sharedPreferences: SharedPreferences

    @RelaxedMockK
    lateinit var affiliateCookieHelper: AffiliateCookieHelper

    @RelaxedMockK
    lateinit var playShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig

    @RelaxedMockK
    lateinit var context: Context

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var shopPageHeaderViewModel: ShopPageHeaderViewModel

    private val SAMPLE_SHOP_ID = "123"
    private val mockShopHomeTabName = "HomeTab"
    private val mockExtParam = "fs_widget%3D23600"

    private val addressWidgetData: LocalCacheModel = LocalCacheModel()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        shopPageHeaderViewModel = ShopPageHeaderViewModel(
            userSessionInterface,
            gqlGetShopInfoForHeaderUseCase,
            getBroadcasterAuthorConfig,
            gqlGetShopInfobUseCaseCoreAndAssets,
            shopQuestGeneralTrackerUseCase,
            getShopPageP1DataUseCase,
            getShopProductListUseCase,
            shopModerateRequestStatusUseCase,
            shopRequestUnmoderateUseCase,
            getShopPageHeaderLayoutUseCase,
            getFollowStatusUseCase,
            updateFollowStatusUseCase,
            gqlGetShopOperationalHourStatusUseCase,
            eligibilityCheckUseCase,
            sharedPreferences,
            testCoroutineDispatcherProvider,
            playShortsEntryPointRemoteConfig
        )
    }

    @Test
    fun `check whether isMyShop return true if given same shop id as mocked user session shop id`() {
        every { userSessionInterface.shopId } returns SAMPLE_SHOP_ID
        assertTrue(shopPageHeaderViewModel.isMyShop(SAMPLE_SHOP_ID))
    }

    @Test
    fun `check whether isUserSessionActive return the same value as the mock value`() {
        every { userSessionInterface.isLoggedIn } returns true
        assertTrue(shopPageHeaderViewModel.isUserSessionActive)
    }

    @Test
    fun `check whether ownerShopName return the same value as the mock value`() {
        val shopNameMock = "mock shop"
        every { userSessionInterface.shopName } returns shopNameMock
        assertTrue(shopPageHeaderViewModel.ownerShopName == shopNameMock)
    }

    @Test
    fun `check whether new shopPageP1Data value is Success`() {
        coEvery {
            getShopPageP1DataUseCase.get().executeOnBackground()
        } returns NewShopPageHeaderP1(
            shopPageGetDynamicTabResponse = ShopPageGetDynamicTabResponse(
                ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                    listOf(
                        ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData()
                    )
                )
            )
        )
        coEvery { getShopPageHeaderLayoutUseCase.get().executeOnBackground() } returns ShopPageHeaderLayoutResponse()
        coEvery { getShopProductListUseCase.get().executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct(), ShopProduct())
        )
        shopPageHeaderViewModel.getNewShopPageTabData(
            SAMPLE_SHOP_ID,
            "shop domain",
            1,
            10,
            ShopProductFilterParameter(),
            "",
            "",
            false,
            addressWidgetData,
            mockExtParam,
            mockShopHomeTabName
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageHeaderViewModel.shopPageP1Data.value is Success)
        assert(shopPageHeaderViewModel.productListData.data.size == 2)
    }

    @Test
    fun `check whether new shopPageP1Data value is success when shopId same as user session shopId`() {
        coEvery { userSessionInterface.shopId } returns SAMPLE_SHOP_ID
        coEvery {
            getShopPageP1DataUseCase.get().executeOnBackground()
        } returns NewShopPageHeaderP1(
            shopPageGetDynamicTabResponse = ShopPageGetDynamicTabResponse(
                ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                    listOf(
                        ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData()
                    )
                )
            )
        )
        coEvery { getShopPageHeaderLayoutUseCase.get().executeOnBackground() } returns ShopPageHeaderLayoutResponse()
        coEvery { getShopProductListUseCase.get().executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct(), ShopProduct())
        )
        shopPageHeaderViewModel.getNewShopPageTabData(
            shopId = SAMPLE_SHOP_ID,
            shopDomain = "shop domain",
            page = 1,
            itemPerPage = 10,
            shopProductFilterParameter = ShopProductFilterParameter(),
            keyword = "",
            etalaseId = "",
            isRefresh = false,
            widgetUserAddressLocalData = addressWidgetData,
            extParam = mockExtParam,
            tabName = mockShopHomeTabName
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageHeaderViewModel.shopPageP1Data.value is Success)
        assert(shopPageHeaderViewModel.productListData.data.size == 2)
    }

    @Test
    fun `check whether new shopPageP1Data value is Fail is mapper throw exception`() {
        coEvery {
            getShopPageP1DataUseCase.get().executeOnBackground()
        } returns NewShopPageHeaderP1(
            shopPageGetDynamicTabResponse = ShopPageGetDynamicTabResponse(
                ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab(
                    listOf(
                        ShopPageGetDynamicTabResponse.ShopPageGetDynamicTab.TabData()
                    )
                )
            )
        )
        coEvery { getShopPageHeaderLayoutUseCase.get().executeOnBackground() } returns ShopPageHeaderLayoutResponse()
        coEvery { getShopProductListUseCase.get().executeOnBackground() } returns ShopProduct.GetShopProduct(
            data = listOf(ShopProduct(), ShopProduct())
        )
        mockkObject(ShopPageHeaderMapper)
        every {
            ShopPageHeaderMapper.mapToNewShopPageP1HeaderData(
                shopInfoCoreData = any(),
                shopPageGetDynamicTabResponse = any(),
                feedWhitelistData = any(),
                shopPageHeaderLayoutData = any()
            )
        } throws Exception()
        shopPageHeaderViewModel.getNewShopPageTabData(
            SAMPLE_SHOP_ID,
            "shop domain",
            1,
            10,
            ShopProductFilterParameter(),
            "",
            "",
            false,
            addressWidgetData,
            mockExtParam,
            mockShopHomeTabName
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageHeaderViewModel.shopPageP1Data.value is Fail)
    }

    @Test
    fun `check whether new shopPageP1Data value is Fail`() {
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } throws Exception()
        shopPageHeaderViewModel.getNewShopPageTabData(
            SAMPLE_SHOP_ID,
            "shop domain",
            1,
            10,
            ShopProductFilterParameter(),
            "",
            "",
            true,
            addressWidgetData,
            mockExtParam,
            mockShopHomeTabName
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageHeaderViewModel.shopPageP1Data.value is Fail)
    }

    @Test
    fun `check whether new shopPageP1Data value is not null when shopId is 0 but shopDomain isn't empty`() {
        coEvery { getShopPageP1DataUseCase.get().executeOnBackground() } returns NewShopPageHeaderP1()
        shopPageHeaderViewModel.getNewShopPageTabData(
            "0",
            "domain",
            1,
            10,
            ShopProductFilterParameter(),
            "",
            "",
            true,
            addressWidgetData,
            mockExtParam,
            mockShopHomeTabName
        )
        assertTrue(shopPageHeaderViewModel.shopPageP1Data.value != null)
    }

    @Test
    fun `check whether update follow status is success`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { updateFollowStatusUseCase.get().executeOnBackground() } returns FollowShopResponse(null)
        shopPageHeaderViewModel.updateFollowStatus(SAMPLE_SHOP_ID, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        coVerify { updateFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.followShopData.value is Success)
    }

    @Test
    fun `check whether update follow status is fail`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { updateFollowStatusUseCase.get().executeOnBackground() } throws Throwable()
        shopPageHeaderViewModel.updateFollowStatus(SAMPLE_SHOP_ID, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        coVerify { updateFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.followShopData.value is Fail)
    }

    @Test
    fun `check whether update follow status is error when user not login`() {
        every { userSessionInterface.isLoggedIn } returns false
        shopPageHeaderViewModel.updateFollowStatus(SAMPLE_SHOP_ID, UpdateFollowStatusUseCase.ACTION_FOLLOW)
        assert(shopPageHeaderViewModel.followShopData.value is Fail)
    }

    @Test
    fun `check whether get follow status is success`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { getFollowStatusUseCase.get().executeOnBackground() } returns FollowStatusResponse(null)
        shopPageHeaderViewModel.getFollowStatusData(SAMPLE_SHOP_ID, RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_SMALL)
        coVerify { getFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.followStatusData.value is Success)

        shopPageHeaderViewModel.getFollowStatusData(SAMPLE_SHOP_ID, RollenceKey.AB_TEST_SHOP_FOLLOW_BUTTON_VARIANT_BIG)
        coVerify { getFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.followStatusData.value is Success)
    }

    @Test
    fun `check whether get follow status is fail`() {
        every { userSessionInterface.isLoggedIn } returns true
        coEvery { getFollowStatusUseCase.get().executeOnBackground() } throws Throwable()
        shopPageHeaderViewModel.getFollowStatusData(SAMPLE_SHOP_ID, "mock_key")
        coVerify { getFollowStatusUseCase.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.followStatusData.value is Fail)
    }

    @Test
    fun `check whether getShopIdFromDomain post shopIdFromDomainData success value`() {
        coEvery { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() } returns ShopInfo()
        shopPageHeaderViewModel.getShopIdFromDomain("Mock domain")
        coVerify { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.shopIdFromDomainData.value is Success)
    }

    @Test
    fun `check whether getShopIdFromDomain post shopIdFromDomainData fail value`() {
        coEvery { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() } throws Throwable()
        shopPageHeaderViewModel.getShopIdFromDomain("Mock domain")
        coVerify { gqlGetShopInfobUseCaseCoreAndAssets.get().executeOnBackground() }
        assert(shopPageHeaderViewModel.shopIdFromDomainData.value is Fail)
    }

    @Test
    fun `when homeWidgetLayoutData value is changed, then should return the new value`() {
        val mockHomeLayoutData = HomeLayoutData()
        shopPageHeaderViewModel.homeWidgetLayoutData = mockHomeLayoutData
        assert(shopPageHeaderViewModel.homeWidgetLayoutData == mockHomeLayoutData)
    }

    @Test
    fun `check whether shopImagePath value is set when call saveShopImageToPhoneStorage`() {
        val mockBitmap = mockk<Bitmap>()
        val mockTransition = mockk<Transition<in Bitmap>>()
        mockkObject(ShopUtil)
        every { ShopUtil.loadImageWithEmptyTarget(any(), any(), any(), any()) } answers {
            (lastArg() as MediaBitmapEmptyTarget<Bitmap>).onResourceReady(mockBitmap, mockTransition)
        }

        mockkStatic(ImageProcessingUtil::class)
        coEvery {
            ImageProcessingUtil.writeImageToTkpdPath(
                mockBitmap,
                Bitmap.CompressFormat.PNG
            )
        } returns File("path")
        shopPageHeaderViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageHeaderViewModel.shopImagePath.value.orEmpty().isNotEmpty())
    }

    @Test
    fun `check whether shopImagePath value is null when callback is not called`() {
        unmockkAll()
        shopPageHeaderViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageHeaderViewModel.shopImagePath.value == null)
    }

    @Test
    fun `check whether shopImagePath value is null when if context is null and callback is not called`() {
        shopPageHeaderViewModel.saveShopImageToPhoneStorage(null, "")
        assert(shopPageHeaderViewModel.shopImagePath.value == null)
    }

    @Test
    fun `check whether shopImagePath value is null when savedFile is null`() {
        val mockBitmap = mockk<Bitmap>()
        val mockTransition = mockk<Transition<in Bitmap>>()
        mockkObject(ShopUtil)
        every { ShopUtil.loadImageWithEmptyTarget(any(), any(), any(), any()) } answers {
            (lastArg() as MediaBitmapEmptyTarget<Bitmap>).onResourceReady(mockBitmap, mockTransition)
        }

        mockkStatic(ImageProcessingUtil::class)
        coEvery {
            ImageProcessingUtil.writeImageToTkpdPath(
                mockBitmap,
                Bitmap.CompressFormat.PNG
            )
        } returns null
        shopPageHeaderViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageHeaderViewModel.shopImagePath.value == null)
    }

    @Test
    fun `check whether shopImagePath value is null when onLoadCleared is called on saveShopImageToPhoneStorage`() {
        val mockDrawable = mockk<Drawable>()
        mockkObject(ShopUtil)
        every { ShopUtil.loadImageWithEmptyTarget(any(), any(), any(), any()) } answers {
            (lastArg() as MediaBitmapEmptyTarget<Bitmap>).onLoadCleared(mockDrawable)
        }

        mockkStatic(ImageProcessingUtil::class)
        shopPageHeaderViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageHeaderViewModel.shopImagePath.value == null)
    }

    @Test
    fun `check whether shopImagePath value is null when ImageHandler loadImageWithTarget throws exception`() {
        mockkObject(ShopUtil)
        every { ShopUtil.loadImageWithEmptyTarget(any(), any(), any(), any()) } throws Exception()
        shopPageHeaderViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageHeaderViewModel.shopImagePath.value == null)
    }

    @Test
    fun `check whether userShopId should return same value as mocked userSessionInterface shopId`() {
        val mockUserShopId = "123"
        every {
            userSessionInterface.shopId
        } returns mockUserShopId
        assert(shopPageHeaderViewModel.userShopId == mockUserShopId)
    }

    @Test
    fun `check whether userId should return same value as mocked userId`() {
        val mockUserId = "123"
        every {
            userSessionInterface.userId
        } returns mockUserId
        assert(shopPageHeaderViewModel.userId == mockUserId)
    }

    @Test
    fun `check whether checkShopRequestModerateStatus post shopModerateRequestStatus success value`() {
        coEvery {
            shopModerateRequestStatusUseCase.get().executeOnBackground()
        } returns ShopModerateRequestData(ShopModerateRequestStatus())
        shopPageHeaderViewModel.checkShopRequestModerateStatus()
        assert(shopPageHeaderViewModel.shopModerateRequestStatus.value is Success)
    }

    @Test
    fun `check whether checkShopRequestModerateStatus post shopModerateRequestStatus fail value`() {
        coEvery {
            shopModerateRequestStatusUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageHeaderViewModel.checkShopRequestModerateStatus()
        assert(shopPageHeaderViewModel.shopModerateRequestStatus.value is Fail)
    }

    @Test
    fun `check whether sendRequestUnmoderateShop post shopUnmoderateData success value`() {
        val mockShopId = 123.0
        val mockOptionValue = "optionValue"
        coEvery {
            shopRequestUnmoderateUseCase.get().executeOnBackground()
        } returns ShopRequestUnmoderateSuccessResponse()
        shopPageHeaderViewModel.sendRequestUnmoderateShop(mockShopId, mockOptionValue)
        assert(shopPageHeaderViewModel.shopUnmoderateData.value is Success)
    }

    @Test
    fun `check whether sendRequestUnmoderateShop post shopUnmoderateData fail value`() {
        val mockShopId = 123.0
        val mockOptionValue = "optionValue"
        coEvery {
            shopRequestUnmoderateUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageHeaderViewModel.sendRequestUnmoderateShop(mockShopId, mockOptionValue)
        assert(shopPageHeaderViewModel.shopUnmoderateData.value is Fail)
    }

    @Test
    fun `check whether sendShopShareTracker post shopShareTracker success value`() {
        val mockShopId = "123"
        val mockChannel = "channel"
        coEvery {
            shopQuestGeneralTrackerUseCase.get().executeOnBackground()
        } returns ShopQuestGeneralTracker()
        shopPageHeaderViewModel.sendShopShareTracker(mockShopId, mockChannel)
        assert(shopPageHeaderViewModel.shopShareTracker.value is Success)
    }

    @Test
    fun `check whether sendShopShareTracker post shopShareTracker fail value`() {
        val mockShopId = "123"
        val mockChannel = "channel"
        coEvery {
            shopQuestGeneralTrackerUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageHeaderViewModel.sendShopShareTracker(mockShopId, mockChannel)
        assert(shopPageHeaderViewModel.shopShareTracker.value is Fail)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and streamAllowed is false`() {
        val mockShopId = "123"
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.streamAllowed == false)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and streamAllowed is true`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterAuthorConfig.get().executeOnBackground()
        } returns Broadcaster.Config(true)
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.streamAllowed == true)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and streamAllowed false when error get data`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterAuthorConfig.get().executeOnBackground()
        } throws Exception()
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.streamAllowed == false)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post Success value if get network data error`() {
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterAuthorConfig.get().executeOnBackground()
        } throws Throwable()
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and shortVideoAllowed is false and remoteConfig is false`() {
        coEvery { playShortsEntryPointRemoteConfig.isShowEntryPoint() } returns false
        val mockShopId = "123"
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.shortVideoAllowed == false)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and shortVideoAllowed is true and remoteConfig is false`() {
        coEvery { playShortsEntryPointRemoteConfig.isShowEntryPoint() } returns false
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterAuthorConfig.get().executeOnBackground()
        } returns Broadcaster.Config(shortVideoAllowed = true)
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.shortVideoAllowed == false)
    }

    @Test
    fun `check whether shopSellerPLayWidgetData post success value and shortVideoAllowed is true and remoteConfig is true`() {
        coEvery { playShortsEntryPointRemoteConfig.isShowEntryPoint() } returns true
        val mockShopId = "123"
        every { userSessionInterface.shopId } returns mockShopId
        coEvery {
            getBroadcasterAuthorConfig.get().executeOnBackground()
        } returns Broadcaster.Config(shortVideoAllowed = true)
        shopPageHeaderViewModel.getSellerPlayWidgetData(mockShopId)
        val shopSellerPLayWidgetData = shopPageHeaderViewModel.shopSellerPLayWidgetData.value
        assert(shopSellerPLayWidgetData is Success)
        assert((shopSellerPLayWidgetData as? Success)?.data?.shortVideoAllowed == true)
    }

    @Test
    fun `check whether shopPageTickerData and shopPageShopShareData post success value`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } returns ShopInfo()
        coEvery {
            gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
        } returns ShopOperationalHourStatus()
        shopPageHeaderViewModel.getShopShareAndOperationalHourStatusData(mockShopId, mockShopDomain, false)
        assert(shopPageHeaderViewModel.shopPageHeaderTickerData.value is Success)
        assert(shopPageHeaderViewModel.shopPageShopShareData.value is Success)

        shopPageHeaderViewModel.getShopShareAndOperationalHourStatusData("0", mockShopDomain, true)
        assert(shopPageHeaderViewModel.shopPageHeaderTickerData.value is Success)
        assert(shopPageHeaderViewModel.shopPageShopShareData.value is Success)
    }

    @Test
    fun `check whether shopPageShopShareData value is null if error when get shopInfo data`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } throws Exception()
        coEvery {
            gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageHeaderViewModel.getShopShareAndOperationalHourStatusData(mockShopId, mockShopDomain, false)
        assert(shopPageHeaderViewModel.shopPageShopShareData.value == null)
    }

    @Test
    fun `check whether shopPageTickerData value is null if error when get shopOperationalHourStatus data`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } returns ShopInfo()
        coEvery {
            gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
        } throws Exception()
        shopPageHeaderViewModel.getShopShareAndOperationalHourStatusData(mockShopId, mockShopDomain, false)
        assert(shopPageHeaderViewModel.shopPageHeaderTickerData.value == null)
    }

    @Test
    fun `check whether shopPageShopShareData and shopPageTickerData value is null if exception is not caught`() {
        val mockShopId = "123"
        val mockShopDomain = "mock domain"
        coEvery {
            gqlGetShopInfoForHeaderUseCase.get().executeOnBackground()
        } throws Throwable()
        coEvery {
            gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground()
        } returns ShopOperationalHourStatus()
        shopPageHeaderViewModel.getShopShareAndOperationalHourStatusData(mockShopId, mockShopDomain, false)
        assert(shopPageHeaderViewModel.shopPageHeaderTickerData.value == null)
        assert(shopPageHeaderViewModel.shopPageShopShareData.value == null)
    }

    @Test
    fun `check when call shopLandingPageInitAffiliateCookie is success`() {
        val mockAffiliateUUId = "123"
        val mockAffiliateChannel = "channel"
        val mockShopId = "456"
        coEvery {
            affiliateCookieHelper.initCookie(any(), any(), any())
        } returns Unit
        shopPageHeaderViewModel.shopLandingPageInitAffiliateCookie(
            affiliateCookieHelper,
            mockAffiliateUUId,
            mockAffiliateChannel,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(), any(), any()) }
    }

    @Test
    fun `when when call shopLandingPageInitAffiliateCookie is not success`() {
        val mockAffiliateUUId = "123"
        val mockAffiliateChannel = "channel"
        val mockShopId = "456"
        coEvery {
            affiliateCookieHelper.initCookie(any(), any(), any())
        } throws Exception()
        shopPageHeaderViewModel.shopLandingPageInitAffiliateCookie(
            affiliateCookieHelper,
            mockAffiliateUUId,
            mockAffiliateChannel,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(), any(), any()) }
    }

    @Test
    fun `when call createAffiliateCookieShopAtcDirectPurchase is success`() {
        val mockAffiliateChannel = "channel"
        val mockUUID = "1234"
        val mockIsVariant = true
        val mockProductId = "678"
        val mockStockQty = 11
        val mockShopId = "5423"
        coEvery {
            affiliateCookieHelper.initCookie(any(), any(), any())
        } returns Unit
        shopPageHeaderViewModel.createAffiliateCookieShopAtcProduct(
            mockUUID,
            affiliateCookieHelper,
            mockAffiliateChannel,
            mockProductId,
            mockIsVariant,
            mockStockQty,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(), any(), any()) }
    }

    @Test
    fun `when call createAffiliateCookieShopAtcDirectPurchase is error`() {
        val mockAffiliateChannel = "channel"
        val mockUUID = "1234"
        val mockIsVariant = true
        val mockProductId = "678"
        val mockStockQty = 11
        val mockShopId = "5423"
        coEvery {
            affiliateCookieHelper.initCookie(any(), any(), any())
        } throws Exception()
        shopPageHeaderViewModel.createAffiliateCookieShopAtcProduct(
            mockUUID,
            affiliateCookieHelper,
            mockAffiliateChannel,
            mockProductId,
            mockIsVariant,
            mockStockQty,
            mockShopId
        )
        coVerify { affiliateCookieHelper.initCookie(any(), any(), any()) }
    }

    @Test
    fun `when saveAffiliateTrackerId success, then shared preferences getString should return mocked value`() {
        val mockAffiliateChannel = "channel"
        every { sharedPreferences.getString(any(), any()) } returns mockAffiliateChannel
        shopPageHeaderViewModel.saveAffiliateChannel(mockAffiliateChannel)
        assert(sharedPreferences.getString("", "") == mockAffiliateChannel)
    }

    @Test
    fun `when saveAffiliateTrackerId error, then shared preferences getString should return empty value`() {
        val mockAffiliateChannel = "channel"
        every { sharedPreferences.getString(any(), any()) } returns ""
        coEvery {
            sharedPreferences.edit().putString(any(), any())
        } throws Exception()
        shopPageHeaderViewModel.saveAffiliateChannel(mockAffiliateChannel)
        assert(sharedPreferences.getString("", "")?.isEmpty() == true)
    }

    @Test
    fun `when check affiliate is success`() {
        val mockData = GenerateAffiliateLinkEligibility()
        val mockParam = AffiliateInput()

        coEvery {
            eligibilityCheckUseCase.get().executeOnBackground()
        } returns mockData

        shopPageHeaderViewModel.checkAffiliate(mockParam)
        coVerify {
            eligibilityCheckUseCase.get().executeOnBackground()
        }
        assertTrue(shopPageHeaderViewModel.resultAffiliate.value is Success)
    }

    @Test
    fun `when check affiliate throws error`() {
        val mockError = Exception()
        val mockParam = AffiliateInput()
        coEvery {
            eligibilityCheckUseCase.get().executeOnBackground()
        } throws mockError

        shopPageHeaderViewModel.checkAffiliate(mockParam)
        coVerify {
            eligibilityCheckUseCase.get().executeOnBackground()
        }
        coVerify {
            eligibilityCheckUseCase.get().executeOnBackground()
        }
        assertTrue(shopPageHeaderViewModel.resultAffiliate.value is Fail)
    }
}
