package com.tokopedia.officialstore.official.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelStyle
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.usecase.featuredshop.DisplayHeadlineAdsEntity
import com.tokopedia.home_component.usecase.featuredshop.GetDisplayHeadlineAds
import com.tokopedia.home_component.usecase.featuredshop.mappingTopAdsHeaderToChannelGrid
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.mapper.OfficialStoreDynamicChannelComponentMapper
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBannerUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreBenefitUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreDynamicChannelUseCase
import com.tokopedia.officialstore.official.domain.GetOfficialStoreFeaturedUseCase
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBannerDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBenefitDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsHeadlineDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationTitleDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialTopAdsBannerDataModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsHeadlineResponse
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Cpm
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.topads.sdk.utils.TopAdsAddressHelper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import io.mockk.just
import io.mockk.Runs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.spyk
import io.mockk.mockk
import io.mockk.CapturingSlot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Observable
import rx.observables.BlockingObservable

@ExperimentalCoroutinesApi
class OfficialStoreHomeViewModelTest {

    @RelaxedMockK
    lateinit var getOfficialStoreBannersUseCase: GetOfficialStoreBannerUseCase

    @RelaxedMockK
    lateinit var getOfficialStoreBenefitUseCase: GetOfficialStoreBenefitUseCase

    @RelaxedMockK
    lateinit var getOfficialStoreFeaturedShopUseCase: GetOfficialStoreFeaturedUseCase

    @RelaxedMockK
    lateinit var getOfficialStoreDynamicChannelUseCase: GetOfficialStoreDynamicChannelUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var topAdsWishlishedUseCase: TopAdsWishlishedUseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var getDisplayHeadlineAds: GetDisplayHeadlineAds

    @RelaxedMockK
    lateinit var getTopAdsHeadlineUseCase: GetTopAdsHeadlineUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCaseCoroutine: com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase

    @RelaxedMockK
    lateinit var bestSellerMapper: BestSellerMapper

    @RelaxedMockK
    lateinit var topAdsAddressHelper: TopAdsAddressHelper

    @RelaxedMockK
    lateinit var topAdsImageViewUseCase: TopAdsImageViewUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private val viewModel by lazy {
        spyk(OfficialStoreHomeViewModel(
            getOfficialStoreBannersUseCase,
            getOfficialStoreBenefitUseCase,
            getOfficialStoreFeaturedShopUseCase,
            getOfficialStoreDynamicChannelUseCase,
            getRecommendationUseCase,
            userSessionInterface,
            addToWishlistV2UseCase,
            topAdsWishlishedUseCase,
            deleteWishlistV2UseCase,
            getDisplayHeadlineAds,
            topAdsImageViewUseCase,
            getRecommendationUseCaseCoroutine,
            bestSellerMapper,
            getTopAdsHeadlineUseCase,
            CoroutineTestDispatchersProvider,
            topAdsAddressHelper
        ))
    }

    @Test
    fun given_get_data_success__when_load_first_data__should_set_success_value() {
        runBlocking {
            val prefixUrl = "prefix"
            val slug = "slug"
            val category = createCategory(prefixUrl, slug)
            val channelType = "$prefixUrl$slug"
            val osBanners = OfficialStoreBanners(banners = mutableListOf(Banner()))
            val osBenefits = OfficialStoreBenefits()
            val osFeatured = OfficialStoreFeaturedShop()
            val osDynamicChannel = mutableListOf<OfficialStoreChannel>()

            onGetOfficialStoreBanners_thenReturn(osBanners)
            onGetOfficialStoreBenefits_thenReturn(osBenefits)
            onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)
            onGetDynamicChannel_thenReturn(osDynamicChannel)
            onSetupDynamicChannelParams_thenCompleteWith(channelType)

            viewModel.loadFirstData(category)

            assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialBannerDataModel && it.banner == osBanners.banners })
            assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialBenefitDataModel && it.benefit == osBenefits.benefits })
            assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialFeaturedShopDataModel && it.featuredShop == osFeatured.featuredShops })
        }
    }

    @Test
    fun given_get_data_error__when_load_first_data__should_set_error_value() {
        runBlocking {
            val error = MessageErrorException()
            val prefixUrl = "prefix"
            val slug = "slug"
            val channelType = "$prefixUrl$slug"

            onGetOfficialStoreData_thenReturn(error)
            onSetupDynamicChannelParams_thenCompleteWith(channelType)

            viewModel.loadFirstData(null)

            assertEquals(viewModel.officialStoreError.value, error)
        }
    }

    @Test
    fun given_get_data_success__when_load_more__should_set_value_with_first_product_recommendation() {
        val page = 1
        val categoryId = "0"
        val listOfRecom = mutableListOf(RecommendationWidget(recommendationItemList = listOf(
            RecommendationItem()
        )))

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        val resultProductRecommendationDataModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<ProductRecommendationDataModel>()
        assertFalse(resultProductRecommendationDataModel.isNullOrEmpty())
        assertNotNull(resultProductRecommendationDataModel?.find { it.productItem in listOfRecom.first().recommendationItemList })
    }

    @Test
    fun given_get_data_success__when_load_more__should_set_value_with_first_product_recommendation_with_topads_headline_ads() {
        val page = 1
        val categoryId = "0"
        val listOfRecom = mutableListOf(RecommendationWidget(recommendationItemList = listOf(RecommendationItem())))
        val topAdsHeadlineAd = OfficialTopAdsHeadlineDataModel(TopAdsHeadlineResponse(CpmModel().apply {
            data = mutableListOf(CpmData().apply {
                cpm = Cpm().apply {
                    position = 0
                }
            })
        }))

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        coEvery { viewModel.isFeaturedShopAllowed } returns true

        coEvery {
            viewModel.getTopAdsHeadlineData(any())
        } returns topAdsHeadlineAd

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        val productRecommendationDataModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<ProductRecommendationDataModel>()
        assertFalse(productRecommendationDataModel.isNullOrEmpty())
        assertNotNull(productRecommendationDataModel?.find { it.productItem in listOfRecom.first().recommendationItemList })
        assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find{ it is OfficialTopAdsHeadlineDataModel && it == topAdsHeadlineAd })
    }

    @Test
    fun given_impressed_shop_not_empty__when_get_topads_headline_ads__then__add_to_seen_ads_param() {
        runBlocking {
            val page = 1
            val channelId = "1"
            val shopId1 = "1"
            val shopId2 = "2"
            val shopId3 = "3"

            viewModel.impressedShop[channelId] = mutableSetOf(shopId1, shopId2, shopId3)

            viewModel.getTopAdsHeadlineData(page)

            verify {
                getTopAdsHeadlineUseCase.createParams(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    seenAds = "3"
                )
            }
        }

    }

    @Test
    fun given_get_data_success__topads_headline_ads() {
        runBlocking {
            val page = 1
            val topAdsHeadlineAdResponse = TopAdsHeadlineResponse(CpmModel())
            val topAdsHeadlineAd = OfficialTopAdsHeadlineDataModel(topAdsHeadlineAdResponse)
            every { userSessionInterface.userId } returns "userId"
            every {
                getTopAdsHeadlineUseCase.createParams(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns "parmas"

            every { getTopAdsHeadlineUseCase.setParams(any(), any()) } just Runs
            coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } returns topAdsHeadlineAdResponse

            val topAdsData = viewModel.getTopAdsHeadlineData(page)

            assertEquals(topAdsData, topAdsHeadlineAd)
        }

    }

    @Test
    fun test_null_topads_headline_ads() {
        runBlocking {
            val page = 1
            val topAdsHeadlineAdResponse = TopAdsHeadlineResponse(CpmModel())
            val topAdsHeadlineAd = OfficialTopAdsHeadlineDataModel(topAdsHeadlineAdResponse)
            every { userSessionInterface.userId } returns "userId"
            every {
                getTopAdsHeadlineUseCase.createParams(
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any(),
                    any()
                )
            } returns "parmas"

            every { getTopAdsHeadlineUseCase.setParams(any(), any()) } just Runs
            coEvery { getTopAdsHeadlineUseCase.executeOnBackground() } throws Throwable("error")

            val topAdsData = viewModel.getTopAdsHeadlineData(page)

            assertEquals(topAdsData, null)
        }

    }

    @Test
    fun test_record_shop_widget_impression_when_map_is_empty() {
        val channelId = "1"
        val shopId = "2"

        viewModel.recordShopWidgetImpression(channelId, shopId)

        val expected = viewModel.impressedShop[channelId]?.size
        assertEquals(expected, 1)
    }

    @Test
    fun test_record_shop_widget_impression_when_map_is_not_empty() {
        val channelId = "1"
        val shopId = "2"
        viewModel.impressedShop[channelId] = mutableSetOf("3")

        viewModel.recordShopWidgetImpression(channelId, shopId)

        val expected = viewModel.impressedShop[channelId]?.size
        Assert.assertEquals(expected, 2)
    }

    @Test
    fun test_reset_shop_widget_impression_count() {
        val channelId = "1"
        val shopId = "2"
        viewModel.impressedShop[channelId] = mutableSetOf(shopId)

        viewModel.resetShopWidgetImpressionCount()
        assertTrue(viewModel.impressedShop.isEmpty())
    }

    @Test
    fun test_reset_is_feature_shop_allowed() {

        viewModel.resetIsFeatureShopAllowed()
        assertFalse(viewModel.isFeaturedShopAllowed)
    }

    @Test
    fun given_get_data_error__when_load_more__should_set_official_store_error_value() {
        val page = 1
        val categoryId = "0"
        val error = MessageErrorException()

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking()
        } throws error

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }
        Assert.assertTrue(viewModel.officialStoreError.value == error)
    }

    @Test
    fun given_get_headlineAds_success_when_get_osDynamicChannel_featured_shop_then_update_list() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
                listOf(
                        OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId))
                )
        )

        val headlineAdsResponse: MutableList<DisplayHeadlineAdsEntity.DisplayHeadlineAds> = mutableListOf()
        headlineAdsResponse.addAll(
                listOf(
                        DisplayHeadlineAdsEntity.DisplayHeadlineAds(id = "1"),
                        DisplayHeadlineAdsEntity.DisplayHeadlineAds(id = "2"),
                        DisplayHeadlineAdsEntity.DisplayHeadlineAds(id = "3"),
                ))

        val expectedFeatureShopResult = FeaturedShopDataModel(
                channelModel = ChannelModel(
                        id = channelId,
                        groupId = "",
                        channelGrids = headlineAdsResponse.mappingTopAdsHeaderToChannelGrid(),
                        style = ChannelStyle.ChannelOS,

                )
        )
        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns headlineAdsResponse

        viewModel.loadFirstData(category)

        val resultFeaturedShopDataModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<FeaturedShopDataModel>()
        assertFalse(resultFeaturedShopDataModel.isNullOrEmpty())
        assertNotNull(resultFeaturedShopDataModel?.find { it.channelModel.id == expectedFeatureShopResult.channelModel.id })
        assertNotNull(resultFeaturedShopDataModel?.find { it.channelModel.channelGrids == expectedFeatureShopResult.channelModel.channelGrids })
    }

    @Test
    fun given_get_headlineAds_empty_list_when_get_osDynamicChannel_featured_shop_then_pass_error_to_view() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"
        val sizeZero = 0

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        val dcResponseFeaturedShop = OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId))
        dynamicChannelResponse.addAll(
                listOf(dcResponseFeaturedShop)
        )

        val headlineAdsResponse: MutableList<DisplayHeadlineAdsEntity.DisplayHeadlineAds> = mutableListOf()

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getDisplayHeadlineAds.executeOnBackground() } returns headlineAdsResponse

        viewModel.loadFirstData(category)

        val resultFeaturedShopDataModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<FeaturedShopDataModel>()
        assertFalse(resultFeaturedShopDataModel.isNullOrEmpty())
        assertNotNull(resultFeaturedShopDataModel?.find { it.channelModel.channelGrids == dcResponseFeaturedShop.channel.grids })
    }

    @Test
    fun given_get_headlineAds_error_when_get_osDynamicChannel_featured_shop_then_remove_from_list() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
            listOf(
                OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId))
            )
        )

        val expectedFeaturedShopDataModel = FeaturedShopDataModel(
            OfficialStoreDynamicChannelComponentMapper.mapChannelToComponent(
                dynamicChannelResponse.first().channel, 0
            )
        )

        val error = MessageErrorException()

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getDisplayHeadlineAds.executeOnBackground() } throws error

        viewModel.loadFirstData(category)

        assertNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is FeaturedShopDataModel && it.channelModel.id == expectedFeaturedShopDataModel.channelModel.id })
    }

    @Test
    fun given_get_tdn_carousel_success_when_get_osDynamicChannel_banner_ads_carousel_then_update_list() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelHeader = Header(123L, "channelHeader", "", "", "",
            "", 0L, "", "", "")

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
            listOf(
                OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_BANNER_ADS_CAROUSEL, header = channelHeader))
            )
        )

        val tdnCarouselResponse: ArrayList<TopAdsImageViewModel> = ArrayList<TopAdsImageViewModel> ()
        tdnCarouselResponse.addAll(
            listOf(
                TopAdsImageViewModel(),
                TopAdsImageViewModel(),
            ))

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap(
            "",
            "12",
            "",
            3,
            3,
            ""
        )) } returns tdnCarouselResponse

        viewModel.loadFirstData(category)

        val resultTdnBannerAdsModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<OfficialTopAdsBannerDataModel>()
        assertFalse(resultTdnBannerAdsModel.isNullOrEmpty())
        assertNotNull(resultTdnBannerAdsModel?.find { it.tdnBanner == tdnCarouselResponse})
    }

    @Test
    fun `when getting empty list for tdn carousel on Dynamic channel banner ads carousel then remove from list`() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelHeader = Header(123L, "channelHeader", "", "", "",
            "", 0L, "", "", "")

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
            listOf(
                OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_BANNER_ADS_CAROUSEL, header = channelHeader))
            )
        )

        val tdnCarouselResponse: ArrayList<TopAdsImageViewModel> = ArrayList<TopAdsImageViewModel> ()
        tdnCarouselResponse.addAll(
            listOf())

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap(
            "",
            "12",
            "",
            3,
            3,
            ""
        )) } returns tdnCarouselResponse

        viewModel.loadFirstData(category)

        val resultTdnBannerAdsModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<OfficialTopAdsBannerDataModel>()
        assertTrue(resultTdnBannerAdsModel.isNullOrEmpty())
        assertNull(resultTdnBannerAdsModel?.find { it.tdnBanner == tdnCarouselResponse})
    }

    @Test
    fun given_get_tdn_carousel_error_when_get_osDynamicChannel_banner_ads_carousel_then_remove_from_list() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)

        val dynamicChannelResponse: MutableList<OfficialStoreChannel> = mutableListOf()
        dynamicChannelResponse.addAll(
            listOf(
                OfficialStoreChannel(channel = Channel(layout = DynamicChannelLayout.LAYOUT_BANNER_ADS_CAROUSEL))
            )
        )

        val tdnCarouselResponse: ArrayList<TopAdsImageViewModel> = ArrayList<TopAdsImageViewModel> ()
        tdnCarouselResponse.addAll(
            listOf(
                TopAdsImageViewModel(),
                TopAdsImageViewModel(),
            ))

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { topAdsImageViewUseCase.getImageData(topAdsImageViewUseCase.getQueryMap(
            "",
            "12",
            "",
            3,
            3,
            ""
        )) } throws Exception("this is mock exception")

        viewModel.loadFirstData(category)

        val resultTdnBannerAdsModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<OfficialTopAdsBannerDataModel>()
        assertTrue(resultTdnBannerAdsModel.isNullOrEmpty())
    }

    // ===================================== //
    private fun onGetOfficialStoreBanners_thenReturn(osBanners: OfficialStoreBanners) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground(any()) } returns osBanners
    }

    private fun onGetOfficialStoreBenefits_thenReturn(osBenefits: OfficialStoreBenefits) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } returns osBenefits
    }

    private fun onGetOfficialStoreFeaturedShop_thenReturn(osFeatured: OfficialStoreFeaturedShop) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } returns osFeatured
    }

    private fun onGetDynamicChannel_thenReturn(list: List<OfficialStoreChannel>) {
        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns list
    }

    private fun createCategory(prefixUrl: String, slug: String): Category {
        return Category(categoryId = "0", prefixUrl = prefixUrl, slug = slug, title = "Home")
    }

    private fun onGetOfficialStoreData_thenReturn(error: Throwable) {
        onGetOfficialStoreBanners_thenReturn(error)
        onGetOfficialStoreBenefits_thenReturn(error)
        onGetOfficialStoreFeaturedShop_thenReturn(error)
        onGetDynamicChannel_thenReturn(error)
    }

    private fun onGetOfficialStoreBanners_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBannersUseCase.executeOnBackground(any()) } throws error
    }

    private fun onGetOfficialStoreBenefits_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreBenefitUseCase.executeOnBackground() } throws error
    }

    private fun onGetOfficialStoreFeaturedShop_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreFeaturedShopUseCase.executeOnBackground() } throws error
    }

    private fun onGetDynamicChannel_thenReturn(error: Throwable) {
        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } throws error
    }

    private fun onSetupDynamicChannelParams_thenCompleteWith(channelType: String) {
        coEvery { getOfficialStoreDynamicChannelUseCase.setupParams(channelType, "") } returns Unit
    }

    private fun createRecommendation(productId: String, isTopAds: Boolean): RecommendationItem {
        return RecommendationItem(productId = productId.toLongOrZero(), isTopAds = isTopAds)
    }

    private fun <T> mockObservable(data: T): Observable<T> {
        val obs = mockk<Observable<T>>()
        val blockingObs = mockk<BlockingObservable<T>>()

        coEvery { blockingObs.first() } returns data
        coEvery { obs.toBlocking() } returns blockingObs

        return obs
    }

    private fun <T> LiveData<T>.assertSuccess(expectedValue: Success<*>) {
        val actualValue = value
        assertEquals(expectedValue, actualValue)
    }

    private fun <T> LiveData<T>.assertPairSuccess(expectedValue: Success<*>) {
        val actualValue = (value as? Pair<Any, Any>)?.second
        assertEquals(expectedValue, actualValue)
    }

    private fun ((Boolean, Throwable?) -> Unit).assertSuccess() {
        coVerify { this@assertSuccess.invoke(true, null) }
    }

    private fun <T> LiveData<T>.assertError(error: Fail) {
        val actualError = value.toString()
        val expectedError = error.toString()
        assertEquals(expectedError, actualError)
    }

    private fun <T> LiveData<T>.assertPairError(error: Fail) {
        val actualError = (value as? Pair<Any, Any>)?.second.toString()
        val expectedError = error.toString()
        assertEquals(expectedError, actualError)
    }

    private fun ((Boolean, Throwable?) -> Unit).assertError(error: Throwable?) {
        val throwable = CapturingSlot<Throwable>()
        coVerify { this@assertError.invoke(false, capture(throwable)) }

        val expectedError = error.toString().trim()
        val actualError = throwable.captured.toString().trim()

        assertEquals(expectedError, actualError)
    }

    @Test
    fun given_success__when_add_to_wishlistv2__then_call_onSuccessAddWishlist() {
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(recommendationItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        verify { mockListener.onSuccessAddWishlist(any(),any()) }
    }

    @Test
    fun given_failed__when_add_to_wishlistv2__then_call_onErrorAddWishList() {
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addWishlistV2(recommendationItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
        verify { mockListener.onErrorAddWishList(any(),any()) }
    }

    @Test
    fun given_success__when_remove_wishlistV2__then_call_onSuccessRemoveWishlist(){
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishlistV2(recommItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
        verify { mockListener.onSuccessRemoveWishlist(any(),any()) }
    }

    @Test
    fun given_failed__when_remove_wishlistV2__then_call_onErrorRemoveWishList(){
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeWishlistV2(recommItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
        verify { mockListener.onErrorRemoveWishlist(any(),any()) }
    }

    @Test
    fun given_get_recomData_success_when_get_osDynamicChannel_bestSelling_then_pass_to_view() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"
        val recomWidgetTitle = "Rekomendasi Untukmu"

        val dynamicChannelResponse = mutableListOf(
            OfficialStoreChannel(channel = Channel(
                layout = DynamicChannelLayout.LAYOUT_BEST_SELLING, id = channelId)
            )
        )

        val recommendationItemList = listOf(
            RecommendationItem(productId = 123L, isTopAds = false)
        )

        val recommendationResponse: List<RecommendationWidget> = listOf(
            RecommendationWidget(recommendationItemList, title = recomWidgetTitle)
        )

        val bestSellerDataModel = BestSellerDataModel(
            title = recomWidgetTitle,
            recommendationItemList = recommendationItemList,
            channelId = channelId
        )

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getRecommendationUseCaseCoroutine.getData(any()) } returns recommendationResponse
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel

        viewModel.loadFirstData(category)

        val resultBestSellerDataModel = viewModel.officialStoreLiveData.value?.dataList?.filterIsInstance<BestSellerDataModel>()
        assertFalse(resultBestSellerDataModel.isNullOrEmpty())
        assertNotNull(resultBestSellerDataModel?.find { it.channelId == channelId })
        assertNotNull(resultBestSellerDataModel?.find { it.recommendationItemList == recommendationItemList })
        assertNotNull(resultBestSellerDataModel?.find { it.title == recomWidgetTitle })
    }

    @Test
    fun given_get_recomData_error_when_get_osDynamicChannel_bestSelling_then_set_error_value() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelId = "123"

        val dynamicChannelResponse = mutableListOf(
            OfficialStoreChannel(channel = Channel(
                layout = DynamicChannelLayout.LAYOUT_BEST_SELLING, id = channelId)
            )
        )

        val error = MessageErrorException()

        coEvery { getOfficialStoreDynamicChannelUseCase.executeOnBackground() } returns dynamicChannelResponse
        coEvery { getRecommendationUseCaseCoroutine.getData(any()) } throws error

        viewModel.loadFirstData(category)

        assertTrue(viewModel.officialStoreError.value == error)
    }

    @Test
    fun given_performance_monitoring_error_when_load_first_data_then_set_error_value() {
        val error = MessageErrorException()
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelType = "$prefixUrl$slug"
        val osBanners = OfficialStoreBanners()
        val osBenefits = OfficialStoreBenefits()
        val osFeatured = OfficialStoreFeaturedShop()
        val osDynamicChannel = mutableListOf<OfficialStoreChannel>()

        onGetOfficialStoreBanners_thenReturn(osBanners)
        onGetOfficialStoreBenefits_thenReturn(osBenefits)
        onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)
        onGetDynamicChannel_thenReturn(osDynamicChannel)
        onSetupDynamicChannelParams_thenCompleteWith(channelType)

        viewModel.loadFirstData(category, "",
            onBannerCacheStartLoad = { throw error },
            onBannerCacheStopLoad = { throw error },
            onBannerCloudStartLoad = { throw error },
            onBannerCloudStopLoad = { throw error }
        )

        assertTrue(viewModel.officialStoreError.value == error)
    }

    @Test
    fun given_load_more__when_scrolling__then_add_loading_more_state() {
        viewModel.addLoadingMore()
        assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialLoadingMoreDataModel })
    }

    @Test
    fun given_countdown_finished__when_dynamic_channel_flashsale__then_remove_widget() {
        val prefixUrl = "prefix"
        val slug = "slug"
        val category = createCategory(prefixUrl, slug)
        val channelType = "$prefixUrl$slug"
        val osBanners = OfficialStoreBanners(banners = mutableListOf(Banner()))
        val osBenefits = OfficialStoreBenefits()
        val osFeatured = OfficialStoreFeaturedShop()
        val osDynamicChannel = mutableListOf(
            OfficialStoreChannel(channel = Channel(
                layout = DynamicChannelLayout.LAYOUT_SPRINT_LEGO)
            )
        )
        val page = 1
        val title = "Rekomendasi Untukmu"

        onGetOfficialStoreBanners_thenReturn(osBanners)
        onGetOfficialStoreBenefits_thenReturn(osBenefits)
        onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)
        onGetDynamicChannel_thenReturn(osDynamicChannel)
        onSetupDynamicChannelParams_thenCompleteWith(channelType)

        val listOfRecom = mutableListOf(
            RecommendationWidget(
                title = title,
                recommendationItemList = listOf(
                    RecommendationItem()
                )
            )
        )

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadFirstData(category)
        viewModel.loadMoreProducts(category.categoryId, page)

        viewModel.removeFlashSale()
        assertNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is DynamicChannelDataModel })
        assertNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is ProductRecommendationDataModel })
        assertEquals(viewModel.productRecommendationTitleSection, title)
    }

    @Test
    fun given_reset_state__when_open_page_or_change_address__then_clear_list_and_add_banner_shimmering() {
        viewModel.resetState()
        assertTrue(viewModel.officialStoreLiveData.value?.dataList?.size == 1)
        assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialLoadingDataModel })
    }

    @Test
    fun given_success__when_recom_wishlist_action__then_update_wishlist_status() {
        val page = 1
        val categoryId = "0"
        val listOfRecom = mutableListOf(RecommendationWidget(recommendationItemList = listOf(
            RecommendationItem()
        )))
        val newWishlistStatus = true
        val position = 0

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        viewModel.updateWishlist(newWishlistStatus, position)

        val resultProductRecom = viewModel.officialStoreLiveData.value?.dataList?.get(position)
        assertTrue(resultProductRecom is ProductRecommendationDataModel && resultProductRecom.productItem.isWishlist == newWishlistStatus)
    }

    @Test
    fun given_get_data_success__when_first_load_more__should_add_recommendation_title() {
        val page = 1
        val categoryId = "0"
        val title = "Rekomendasi Untukmu"
        val listOfRecom = mutableListOf(RecommendationWidget(title = title, recommendationItemList = listOf(
            RecommendationItem()
        )))

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.counterTitleShouldBeRendered += 1
        viewModel.loadMoreProducts(categoryId, page)

        val resultRecomTitle = viewModel.officialStoreLiveData.value?.dataList?.find { it is ProductRecommendationTitleDataModel } as? ProductRecommendationTitleDataModel
        assertNotNull(resultRecomTitle)
        assertEquals(resultRecomTitle?.title, title)
        assertEquals(viewModel.productRecommendationTitleSection, title)
    }

    @Test
    fun given_banner_empty__when_load_first_data__should_not_have_banners() {
        runBlocking {
            val prefixUrl = "prefix"
            val slug = "slug"
            val category = createCategory(prefixUrl, slug)
            val channelType = "$prefixUrl$slug"
            val osBanners = OfficialStoreBanners(banners = mutableListOf())
            val osBenefits = OfficialStoreBenefits()
            val osFeatured = OfficialStoreFeaturedShop()
            val osDynamicChannel = mutableListOf<OfficialStoreChannel>()

            onGetOfficialStoreBanners_thenReturn(osBanners)
            onGetOfficialStoreBenefits_thenReturn(osBenefits)
            onGetOfficialStoreFeaturedShop_thenReturn(osFeatured)
            onGetDynamicChannel_thenReturn(osDynamicChannel)
            onSetupDynamicChannelParams_thenCompleteWith(channelType)

            viewModel.loadFirstData(category)

            assertNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialBannerDataModel && it.banner == osBanners.banners })
            assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialBenefitDataModel && it.benefit == osBenefits.benefits })
            assertNotNull(viewModel.officialStoreLiveData.value?.dataList?.find { it is OfficialFeaturedShopDataModel && it.featuredShop == osFeatured.featuredShops })
        }
    }

    @Test
    fun given_get_data_success__when_load_more__should_update_recom_updated_value() {
        val page = 1
        val categoryId = "0"
        val listOfRecom = mutableListOf(RecommendationWidget(recommendationItemList = listOf(
            RecommendationItem()
        )))

        coEvery {
            getRecommendationUseCase.createObservable(any()).toBlocking().first()
        } returns listOfRecom

        viewModel.loadMoreProducts(categoryId, page)

        coVerify {
            getRecommendationUseCase.createObservable(any())
        }

        val recomUpdated = viewModel.recomUpdated
        assertTrue(recomUpdated.value?.getContentIfNotHandled() == true)
    }
}
