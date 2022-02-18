package com.tokopedia.officialstore.mapper

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.officialstore.common.listener.FeaturedShopListener
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper.Companion.BENEFIT_POSITION
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper.Companion.FEATURE_SHOP_POSITION
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper.Companion.RECOM_WIDGET_POSITION
import com.tokopedia.officialstore.official.data.mapper.OfficialHomeMapper.Companion.WIDGET_NOT_FOUND
import com.tokopedia.officialstore.official.data.model.*
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBannerDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBenefitDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.remoteconfig.RemoteConfig
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OfficialHomeMapperTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val officialHomeMapper by lazy {
        OfficialHomeMapper(
            mockk<Context>(relaxed = true),
            CoroutineTestDispatchersProvider
        )
    }

    private val mockOfficialHomeAdapter = mockk<OfficialHomeAdapter>(relaxed = true)
    private val mockRemoteConfig = mockk<RemoteConfig>(relaxed = true)
    private val bestSellerTitle = "Produk Terlaris"
    private val channelId = "123"
    private val mockBestSellerDataModel = BestSellerDataModel(title = bestSellerTitle, channelId = channelId)
    private val listChannelGrid = listOf(ChannelGrid(id = "136040973", name = "tokomawar"))
    private val mockFeaturedShopDataModel = FeaturedShopDataModel(ChannelModel(channelId, "", channelGrids = listChannelGrid))
    private val defaultBanner = mutableListOf(
        Banner(),
    )
    private val mockOfficialStoreBanners = OfficialStoreBanners(
                banners = defaultBanner
            )
    private val listBenefit = mutableListOf(Benefit())
    private val listBenefit2 = mutableListOf(Benefit(), Benefit())
    private val mockBenefit = OfficialStoreBenefits(listBenefit)
    private val mockBenefit2 = OfficialStoreBenefits(listBenefit2)
    private val mockOfficialStoreFeaturedShop = OfficialStoreFeaturedShop(mutableListOf(Shop()))
    private val mockCategory = "category1"
    private val mockFeaturedShopListener = mockk<FeaturedShopListener>(relaxed = true)

    private fun addDefaultDynamicChannel() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_TOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_BRAND)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_6_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_BEST_SELLING)),
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun addDynamicChannelEmpty() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataWithSpecificBestSellerId() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_TOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_BRAND)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_6_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_BEST_SELLING, id = channelId)),
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataNotContainsBestSeller() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_TOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_BRAND)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_6_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE))
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataNotContainsBestSellerLessThanDefaultSize() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataContainsDifferentIdFeaturedShop() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_TOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_BRAND)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = "12")),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_6_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE))
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataNotContainsBenefitDataModel() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataNotContainsFeaturedShopDataModel() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun dynamicChannelDataContainsFeaturedShop() {
        officialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_TOP)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_BRAND)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_FEATURED_SHOP, id = channelId)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_6_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE))
                ),
            mockOfficialHomeAdapter,
            mockRemoteConfig
        )
    }

    private fun `given list official store not contains banner` () {
        addDefaultDynamicChannel()
    }

    private fun `given empty list official store`() {
        addDynamicChannelEmpty()
    }

    private fun `given list official store contains best seller` () {
        addDefaultDynamicChannel()
    }

    private fun `given list official store contains featured shop` () {
        dynamicChannelDataContainsFeaturedShop()
    }

    private fun `given list official store contains different channel id featured shop` () {
        dynamicChannelDataContainsDifferentIdFeaturedShop()
    }

    private fun `given list official store contains best seller equals channel id` () {
        dynamicChannelDataWithSpecificBestSellerId()
    }

    private fun `given list official store not contains best seller` () {
        dynamicChannelDataNotContainsBestSeller()
    }

    private fun `given list official store not contains best seller less than default size` () {
        dynamicChannelDataNotContainsBestSellerLessThanDefaultSize()
    }

    private fun `given list official store not contains benefit data`() {
        dynamicChannelDataNotContainsBenefitDataModel()
    }

    private fun `given list official store not contains featured shop data`() {
        dynamicChannelDataNotContainsFeaturedShopDataModel()
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `given list official store not contains banner when mapping banner with new banner and remote config true then list official store banner should contains banner`() {
        `given list official store not contains banner`()
        officialHomeMapper.mappingBanners(
            OfficialStoreBanners(),
            mockOfficialHomeAdapter,
            "",
            true
        )
        Assert.assertTrue(
            officialHomeMapper.listOfficialStore.find { it is OfficialBannerDataModel } != null
        )
    }

    @Test
    fun `given list official store not contains banner when mapping banner with new banner and remote config false then list official store banner should contains banner`() {
        `given list official store not contains banner`()
        officialHomeMapper.mappingBanners(
            OfficialStoreBanners(),
            mockOfficialHomeAdapter,
            "",
            false
        )
        Assert.assertTrue(
            officialHomeMapper.listOfficialStore.find { it is OfficialBannerDataModel } != null
        )
    }

    private fun `mapping banner first time`() {
        `given list official store not contains banner`()
        officialHomeMapper.mappingBanners(
            OfficialStoreBanners(),
            mockOfficialHomeAdapter,
            "",
            true
        )
    }

    @Test
    fun `given list official store banner when mapping banner second time with disable from remote config then banner result value will be not equals with given data`() {
        `mapping banner first time`()
        officialHomeMapper.mappingBanners(
            mockOfficialStoreBanners,
            mockOfficialHomeAdapter,
            "",
            true
        )

        val bannerModel = officialHomeMapper.listOfficialStore.find { it is OfficialBannerDataModel } as OfficialBannerDataModel
        Assert.assertNotEquals(bannerModel.banner.size, defaultBanner.size)
    }

    @Test
    fun `given list official store banner when mapping banner second time then banner result value will be equals with given data`() {
        `mapping banner first time`()
        officialHomeMapper.mappingBanners(
            mockOfficialStoreBanners,
            mockOfficialHomeAdapter,
            "",
            false
        )

        val bannerModel = officialHomeMapper.listOfficialStore.find { it is OfficialBannerDataModel } as OfficialBannerDataModel
        Assert.assertEquals(bannerModel.banner, defaultBanner)
    }

    @Test
    fun `given empty list official store show loading when mapping banner with new banner and remote config false then loading will be gone and banner added to the list`() {
        `given empty list official store`()
        officialHomeMapper.showLoadingMore(mockOfficialHomeAdapter)
        val defaultBanner = mutableListOf(
            Banner(),
        )
        officialHomeMapper.mappingBanners(
            OfficialStoreBanners(
                banners = defaultBanner
            ),
            mockOfficialHomeAdapter,
            "",
            false
        )

        val isLoadingNotShowed = officialHomeMapper.listOfficialStore.indexOfFirst { it is OfficialLoadingMoreDataModel } == -1
        Assert.assertTrue(isLoadingNotShowed)

        val bannerModel = officialHomeMapper.listOfficialStore.find { it is OfficialBannerDataModel } as OfficialBannerDataModel
        Assert.assertEquals(bannerModel.banner, defaultBanner)
    }

    @Test
    fun `given list official store contains banner when mapping banner with new banner and remote config false then list official store banner should replace banner`() {
        addDefaultDynamicChannel()
        val defaultBanner = mutableListOf(
            Banner(),
            Banner(),
            Banner(),
            Banner(),
            Banner()
        )
        officialHomeMapper.mappingBanners(
            OfficialStoreBanners(
                banners = defaultBanner
            ),
            mockOfficialHomeAdapter,
            "",
            false
        )

        val bannerModel = officialHomeMapper.listOfficialStore.find { it is OfficialBannerDataModel } as? OfficialBannerDataModel
        Assert.assertTrue(bannerModel != null)
        Assert.assertTrue(bannerModel?.banner?.size == defaultBanner.size)
    }

    @Test
    fun `given list official store contains best seller when mapping recom widget then total data after process it will not changed`() {
        `given list official store contains best seller`()
        val totalPreviousData = officialHomeMapper.listOfficialStore.toMutableList().size
        officialHomeMapper.mappingRecomWidget(data = mockBestSellerDataModel) {}
        val totalAfterMappingRecomWidget = officialHomeMapper.listOfficialStore.toMutableList().size
        Assert.assertEquals(totalPreviousData, totalAfterMappingRecomWidget)
    }

    @Test
    fun `given list official store contains different channel id best seller when mapping recom widget then data will not replace best seller data from the list`() {
        `given list official store contains best seller`()
        Assert.assertNotEquals(officialHomeMapper.listOfficialStore[8], mockBestSellerDataModel)
        officialHomeMapper.mappingRecomWidget(data = mockBestSellerDataModel) {}
        Assert.assertNotEquals(officialHomeMapper.listOfficialStore[8], mockBestSellerDataModel)
    }

    @Test
    fun `given list official store contains best seller equals channel best seller id when mapping recom widget then data will replace best seller data from the list`() {
        `given list official store contains best seller equals channel id`()
        Assert.assertNotEquals(officialHomeMapper.listOfficialStore[8], mockBestSellerDataModel)
        officialHomeMapper.mappingRecomWidget(data = mockBestSellerDataModel) {}
        Assert.assertEquals(officialHomeMapper.listOfficialStore[8], mockBestSellerDataModel)
    }

    @Test
    fun `given list official store not contains best seller when mapping recom widget then recom widget position will be specific as given`() {
        `given list official store not contains best seller`()
        officialHomeMapper.mappingRecomWidget(data = mockBestSellerDataModel) {}
        Assert.assertEquals(officialHomeMapper.listOfficialStore[OfficialHomeMapper.RECOM_WIDGET_POSITION], mockBestSellerDataModel)
    }

    @Test
    fun `given list official store not contains best seller with size less than default when mapping recom widget then recom widget position will top of the list`() {
        `given list official store not contains best seller less than default size`()
        officialHomeMapper.mappingRecomWidget(data = mockBestSellerDataModel) {}
        val indexBestSellerWidgetInList = officialHomeMapper.listOfficialStore.indexOfFirst { it is BestSellerDataModel }
        Assert.assertTrue(indexBestSellerWidgetInList < RECOM_WIDGET_POSITION)
    }

    @Test
    fun `given list official store contains featured shop when mapping featured shop total data after process it will not changed`() {
        `given list official store contains featured shop`()
        val totalPreviousData = officialHomeMapper.listOfficialStore.toMutableList().size
        officialHomeMapper.updateFeaturedShopDC(newData = mockFeaturedShopDataModel) {}
        val totalAfterUpdateFeaturedShop = officialHomeMapper.listOfficialStore.toMutableList().size
        Assert.assertEquals(totalPreviousData, totalAfterUpdateFeaturedShop)
    }

    @Test
    fun `given list official store contains featured shop when mapping featured shop then data will replace featured shop data from the list`() {
        `given list official store contains featured shop`()
        officialHomeMapper.updateFeaturedShopDC(newData = mockFeaturedShopDataModel) {}
        Assert.assertEquals(officialHomeMapper.listOfficialStore[3], mockFeaturedShopDataModel)
    }

    @Test
    fun `given list official store contains featured shop different id when mapping featured shop then data will replace featured based on channel id`() {
        `given list official store contains different channel id featured shop`()
        officialHomeMapper.updateFeaturedShopDC(newData = mockFeaturedShopDataModel) {}
        Assert.assertNotEquals(officialHomeMapper.listOfficialStore[3], mockFeaturedShopDataModel)
        Assert.assertEquals(officialHomeMapper.listOfficialStore[4], mockFeaturedShopDataModel)
    }

    @Test
    fun `given list official store contains best seller when remove recom then the size of the list will different with the list not contains best seller data`() {
        `given list official store contains best seller`()
        val totalPreviousData = officialHomeMapper.listOfficialStore.toMutableList().size
        officialHomeMapper.removeRecomWidget {}
        val totalAfterRemoveRecomWidget = officialHomeMapper.listOfficialStore.toMutableList().size
        Assert.assertTrue(totalPreviousData > totalAfterRemoveRecomWidget)
        val isBestSellerWidgetNotExist = officialHomeMapper.listOfficialStore.indexOfFirst { it is BestSellerDataModel } == WIDGET_NOT_FOUND
        Assert.assertTrue(isBestSellerWidgetNotExist)
    }

    @Test
    fun `given list official store not contains best seller when remove recom then the size of the list will the same as before`() {
        `given list official store not contains best seller`()
        val totalPreviousData = officialHomeMapper.listOfficialStore.toMutableList().size
        officialHomeMapper.removeRecomWidget {}
        val totalAfterRemoveRecomWidget = officialHomeMapper.listOfficialStore.toMutableList().size
        Assert.assertEquals(totalPreviousData, totalAfterRemoveRecomWidget)
    }

    @Test
    fun `given list official store contains featured shop when remove shop widget then data will data will be removed from the list`() {
        `given list official store contains featured shop`()
        val totalPreviousData = officialHomeMapper.listOfficialStore.toMutableList().size
        officialHomeMapper.removeFeaturedShopDC(newData = mockFeaturedShopDataModel) {}
        val totalAfterRemoveShopWidget = officialHomeMapper.listOfficialStore.toMutableList().size
        Assert.assertTrue(totalPreviousData > totalAfterRemoveShopWidget)
        Assert.assertNotEquals(officialHomeMapper.listOfficialStore[3], mockFeaturedShopDataModel)
    }

    @Test
    fun `given list official store contains different channel id featured shop when when remove shop widget then total data in the list will be same`() {
        addDefaultDynamicChannel()
        val totalPreviousData = officialHomeMapper.listOfficialStore.toMutableList().size
        officialHomeMapper.removeFeaturedShopDC(newData = mockFeaturedShopDataModel) {}
        val totalAfterRemoveShopWidget = officialHomeMapper.listOfficialStore.toMutableList().size
        Assert.assertTrue(totalPreviousData == totalAfterRemoveShopWidget)
    }

    @Test
    fun `given list official store not contains benefit data when mapping benefit then contains benefit data with default position`() {
        `given list official store not contains benefit data`()
        officialHomeMapper.mappingBenefit(mockBenefit, mockOfficialHomeAdapter)
        val indexBenefit = officialHomeMapper.listOfficialStore.indexOfFirst { it is OfficialBenefitDataModel }
        Assert.assertEquals(BENEFIT_POSITION, indexBenefit)
    }

    private fun `mapping benefit first time`() {
        `given list official store not contains benefit data`()
        officialHomeMapper.mappingBenefit(mockBenefit, mockOfficialHomeAdapter)
    }

    @Test
    fun `given list official store when mapping benefit second time then value of benefit will repaced`() {
        `mapping benefit first time`()
        val benefitBefore = officialHomeMapper.listOfficialStore.find { it is OfficialBenefitDataModel }
        officialHomeMapper.mappingBenefit(mockBenefit2, mockOfficialHomeAdapter)
        val benefitAfter = officialHomeMapper.listOfficialStore.find { it is OfficialBenefitDataModel }
        Assert.assertNotEquals(benefitBefore, benefitAfter)
    }

    @Test
    fun `given empty list official store when mapping benefit then benefit will be added to the list`() {
        `given empty list official store`()
        officialHomeMapper.mappingBenefit(mockBenefit, mockOfficialHomeAdapter)
        val isBenefitExisted = officialHomeMapper.listOfficialStore.indexOfFirst { it is OfficialBenefitDataModel } != WIDGET_NOT_FOUND
        Assert.assertTrue(isBenefitExisted)
    }

    private fun `mapping featured shop first time`() {
        `given list official store not contains featured shop data`()
        officialHomeMapper.mappingFeaturedShop(mockOfficialStoreFeaturedShop, mockOfficialHomeAdapter, mockCategory, mockFeaturedShopListener)
    }

    @Test
    fun `give list official store not contains featured shop data when mapping featured shop then featured shop data in list official store with default position`() {
        `mapping featured shop first time`()
        val indexFeaturedShop = officialHomeMapper.listOfficialStore.indexOfFirst { it is OfficialFeaturedShopDataModel }
        Assert.assertNotEquals(WIDGET_NOT_FOUND, indexFeaturedShop)
        Assert.assertEquals(indexFeaturedShop, FEATURE_SHOP_POSITION)
    }
}