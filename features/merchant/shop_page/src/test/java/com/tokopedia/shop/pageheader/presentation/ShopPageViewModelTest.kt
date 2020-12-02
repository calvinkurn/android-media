package com.tokopedia.shop.pageheader.presentation

import android.content.Context
import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.imagepicker.common.util.ImageUtils
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.domain.interactor.GQLGetShopFavoriteStatusUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopOperationalHourStatusUseCase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopoperationalhourstatus.ShopOperationalHourStatus
import com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata.GetShopReputationUseCase
import com.tokopedia.shop.common.view.model.ShopProductFilterParameter
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.shop.pageheader.domain.interactor.GetBroadcasterShopConfigUseCase
import com.tokopedia.shop.pageheader.domain.interactor.GetShopPageP1DataUseCase
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.stickylogin.domain.usecase.StickyLoginUseCase
import com.tokopedia.stickylogin.internal.StickyLoginConstant
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
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
    lateinit var stickyLoginUseCase: Lazy<StickyLoginUseCase>
    @RelaxedMockK
    lateinit var gqlGetShopOperationalHourStatusUseCase: Lazy<GQLGetShopOperationalHourStatusUseCase>
    @RelaxedMockK
    lateinit var getShopPageP1DataUseCase: Lazy<GetShopPageP1DataUseCase>
    @RelaxedMockK
    lateinit var context: Context

    private val testCoroutineDispatcherProvider by lazy {
        CoroutineTestDispatchersProvider
    }

    private lateinit var shopPageViewModel : ShopPageViewModel

    private val SAMPLE_SHOP_ID = "123"

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
                toggleFavouriteShopUseCase,
                stickyLoginUseCase,
                gqlGetShopOperationalHourStatusUseCase,
                getShopPageP1DataUseCase,
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
        shopPageViewModel.getShopPageTabData(
                SAMPLE_SHOP_ID.toIntOrZero(),
                "shop domain",
                1,
                10,
                ShopProductFilterParameter(),
                "",
                "",
                true
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }

        assertTrue(shopPageViewModel.shopPageP1Data.value is Success)
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
                true
        )
        coVerify { getShopPageP1DataUseCase.get().executeOnBackground() }
        assertTrue(shopPageViewModel.shopPageP1Data.value is Fail)
    }

    @Test
    fun `check whether shopPageP1Data value is null when shopId and shopDomain value is empty`() {
        shopPageViewModel.getShopPageTabData(
                0,
                "",
                1,
                10,
                ShopProductFilterParameter(),
                "",
                "",
                true
        )
        assertTrue(shopPageViewModel.shopPageP1Data.value == null)
    }

    @Test
    fun `check whether toggleFavorite call onSuccess`() {
        val onSuccess: (Boolean) -> Unit = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopUseCase.get().execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onNext(true)
        }
        shopPageViewModel.toggleFavorite(SAMPLE_SHOP_ID, onSuccess, {})
        verify { onSuccess.invoke(true) }
    }

    @Test
    fun `check whether toggleFavorite call onError`() {
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        val throwable = Throwable()
        every { userSessionInterface.isLoggedIn } returns true
        every { toggleFavouriteShopUseCase.get().execute(any(), any()) } answers {
            (secondArg() as Subscriber<Boolean>).onError(throwable)
        }
        shopPageViewModel.toggleFavorite(SAMPLE_SHOP_ID, {}, onError)
        verify { onError.invoke(throwable) }
    }

    @Test
    fun `check whether toggleFavorite call onError when user not login`() {
        val onError: (Throwable) -> Unit = mockk(relaxed = true)
        every { userSessionInterface.isLoggedIn } returns false
        shopPageViewModel.toggleFavorite(SAMPLE_SHOP_ID, {}, onError)
        verify { onError.invoke(any()) }
    }

    @Test
    fun `check whether getStickyLoginContent call onSuccess`() {
        val tickerDetail = StickyLoginTickerPojo.TickerDetail(
                "Mock message",
                StickyLoginConstant.LAYOUT_FLOATING
        )
        val stickyLoginMockModel = StickyLoginTickerPojo.TickerResponse(
                StickyLoginTickerPojo(
                        listOf(tickerDetail)
                )
        )
        val onSuccess: (StickyLoginTickerPojo.TickerDetail) -> Unit = mockk(relaxed = true)
        every { stickyLoginUseCase.get().execute(any(), any()) } answers {
            (firstArg() as (StickyLoginTickerPojo.TickerResponse) -> Unit).invoke(stickyLoginMockModel)
        }
        shopPageViewModel.getStickyLoginContent(onSuccess, {})
        verify { onSuccess.invoke(any()) }
    }

    @Test
    fun `check whether getStickyLoginContent call onError when empty model given`() {
        val onError: (Throwable) -> Unit = spyk({ throwable -> })
        every { stickyLoginUseCase.get().execute(any(), any()) } answers {
            (firstArg() as (StickyLoginTickerPojo.TickerResponse) -> Unit).invoke(StickyLoginTickerPojo.TickerResponse())
        }
        shopPageViewModel.getStickyLoginContent({}, onError)
        verify { onError.invoke(any()) }
    }

    @Test
    fun `check whether getStickyLoginContent call onError when error get data`() {
        val onError: (Throwable) -> Unit = spyk({ throwable -> })
        every { stickyLoginUseCase.get().execute(any(), any()) } answers {
            (secondArg() as (Throwable) -> Unit).invoke(Throwable())
        }
        shopPageViewModel.getStickyLoginContent({}, onError)
        verify { onError.invoke(any()) }
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
                "",
                true
        )
        coVerify { gqlGetShopInfoForHeaderUseCase.get().executeOnBackground() }
        coVerify { getShopReputationUseCase.get().executeOnBackground() }
        coVerify { gqlGetShopOperationalHourStatusUseCase.get().executeOnBackground() }
        coVerify { gqlGetShopFavoriteStatusUseCase.get().executeOnBackground() }
        coVerify { getBroadcasterShopConfigUseCase.get().executeOnBackground() }
        assert(shopPageViewModel.shopPageHeaderContentData.value is Success)
    }

    @Test
    fun `check whether required function is called when flush`() {
        every { toggleFavouriteShopUseCase.get().unsubscribe() } returns mockk(relaxed = true)
        every { stickyLoginUseCase.get().cancelJobs() } returns mockk(relaxed = true)

        shopPageViewModel.flush()
        verify {
            toggleFavouriteShopUseCase.get().unsubscribe()
            stickyLoginUseCase.get().cancelJobs()
        }
    }

    @Test
    fun `check whether shopImagePath value is set when call saveShopImageToPhoneStorage`() {
        val mockBitmap = mockk<Bitmap>()
        val mockTransition = mockk<Transition<in Bitmap>>()

        mockkStatic(ImageHandler::class)
        every { ImageHandler.loadImageWithTarget(any(), any(), any()) } answers {
            (thirdArg() as CustomTarget<Bitmap>).onResourceReady(mockBitmap, mockTransition)
        }

        mockkStatic(ImageUtils::class)
        every {
            ImageUtils.writeImageToTkpdPath(
                    ImageUtils.DirectoryDef.DIRECTORY_TOKOPEDIA_CACHE,
                    mockBitmap,
                    true)
        } returns File("path")
        shopPageViewModel.saveShopImageToPhoneStorage(context, "")
        assert(shopPageViewModel.shopImagePath.value.orEmpty().isNotEmpty())
    }


}