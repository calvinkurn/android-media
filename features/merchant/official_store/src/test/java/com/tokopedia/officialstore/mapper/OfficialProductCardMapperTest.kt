package com.tokopedia.officialstore.mapper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.visitable.FeaturedShopDataModel
import com.tokopedia.officialstore.official.data.model.OfficialStoreChannel
import com.tokopedia.officialstore.official.data.model.OfficialStoreBanners
import com.tokopedia.officialstore.official.data.model.OfficialStoreBenefits
import com.tokopedia.officialstore.official.data.model.OfficialStoreFeaturedShop
import com.tokopedia.officialstore.official.data.model.Banner
import com.tokopedia.officialstore.official.data.model.Shop
import com.tokopedia.officialstore.official.data.model.Benefit
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBannerDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialBenefitDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialFeaturedShopDataModel
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.OfficialLoadingMoreDataModel
import com.tokopedia.officialstore.official.presentation.mapper.OfficialHomeMapper
import com.tokopedia.officialstore.official.presentation.mapper.OfficialHomeMapper.BENEFIT_POSITION
import com.tokopedia.officialstore.official.presentation.mapper.OfficialHomeMapper.FEATURE_SHOP_POSITION
import com.tokopedia.officialstore.official.presentation.mapper.OfficialHomeMapper.RECOM_WIDGET_POSITION
import com.tokopedia.officialstore.official.presentation.mapper.OfficialHomeMapper.WIDGET_NOT_FOUND
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class OfficialProductCardMapperTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

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
    private val mockOfficialStoreFeaturedShop2 = OfficialStoreFeaturedShop(mutableListOf(Shop(), Shop()))
    private val mockCategory = "category1"

    private var officialStoreList = mutableListOf<Visitable<*>>()

    private fun addDefaultDynamicChannel() {
        OfficialHomeMapper.mappingDynamicChannel(
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
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun addDynamicChannelEmpty() {
        OfficialHomeMapper.mappingDynamicChannel(
            emptyList(),
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataWithSpecificBestSellerId() {
        OfficialHomeMapper.mappingDynamicChannel(
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
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataNotContainsBestSeller() {
        OfficialHomeMapper.mappingDynamicChannel(
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
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataNotContainsBestSellerLessThanDefaultSize() {
        OfficialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
            ),
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataContainsDifferentIdFeaturedShop() {
        OfficialHomeMapper.mappingDynamicChannel(
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
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataNotContainsBenefitDataModel() {
        OfficialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
            ),
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataNotContainsFeaturedShopDataModel() {
        OfficialHomeMapper.mappingDynamicChannel(
            listOf(
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
                OfficialStoreChannel(Channel(layout = DynamicChannelLayout.LAYOUT_MIX_LEFT)),
            ),
            officialStoreList
        ) {
            officialStoreList = it
        }
    }

    private fun dynamicChannelDataContainsFeaturedShop() {
        OfficialHomeMapper.mappingDynamicChannel(
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
            officialStoreList
        ) {
            officialStoreList = it
        }
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
    fun `given list official store not contains banner when mapping banner with new banner then list official store banner should contains banner`() {
        `given list official store not contains banner`()
        OfficialHomeMapper.mappingBanners(
            OfficialStoreBanners(),
            officialStoreList,
            "",
        ) {
            officialStoreList = it
        }
        Assert.assertTrue(
            officialStoreList.find { it is OfficialBannerDataModel } != null
        )
    }

    private fun `mapping banner first time`() {
        `given list official store not contains banner`()
        OfficialHomeMapper.mappingBanners(
            OfficialStoreBanners(),
            officialStoreList,
            "",
        ) {
            officialStoreList = it
        }
    }

    @Test
    fun `given list official store banner when mapping banner second time then banner result value will be not equals with given data`() {
        `mapping banner first time`()

        val bannerModel = officialStoreList.find { it is OfficialBannerDataModel } as OfficialBannerDataModel
        Assert.assertNotEquals(bannerModel.banner.size, defaultBanner.size)
    }

    @Test
    fun `given list official store banner when mapping banner second time then banner result value will be equals with given data`() {
        `mapping banner first time`()
        OfficialHomeMapper.mappingBanners(
            mockOfficialStoreBanners,
            officialStoreList,
            "",
        ) {
            officialStoreList = it
        }

        val bannerModel = officialStoreList.find { it is OfficialBannerDataModel } as OfficialBannerDataModel
        Assert.assertEquals(bannerModel.banner, defaultBanner)
    }

    @Test
    fun `given empty list official store show loading when mapping banner with new banner then loading will be gone and banner added to the list`() {
        `given empty list official store`()
        officialStoreList.add(OfficialLoadingMoreDataModel())
        val defaultBanner = mutableListOf(
            Banner(),
        )
        OfficialHomeMapper.mappingBanners(
            OfficialStoreBanners(
                banners = defaultBanner
            ),
            officialStoreList,
            "",
        ) {
            officialStoreList = it
        }

        val isLoadingNotShowed = officialStoreList.indexOfFirst { it is OfficialLoadingMoreDataModel } == -1
        Assert.assertTrue(isLoadingNotShowed)

        val bannerModel = officialStoreList.find { it is OfficialBannerDataModel } as OfficialBannerDataModel
        Assert.assertEquals(bannerModel.banner, defaultBanner)
    }

    @Test
    fun `given list official store contains banner when mapping banner with new banner then list official store banner should replace banner`() {
        addDefaultDynamicChannel()
        val defaultBanner = mutableListOf(
            Banner(),
            Banner(),
            Banner(),
            Banner(),
            Banner()
        )
        OfficialHomeMapper.mappingBanners(
            OfficialStoreBanners(
                banners = defaultBanner
            ),
            officialStoreList,
            "",
        ) {
            officialStoreList = it
        }

        val bannerModel = officialStoreList.find { it is OfficialBannerDataModel } as? OfficialBannerDataModel
        Assert.assertTrue(bannerModel != null)
        Assert.assertTrue(bannerModel?.banner?.size == defaultBanner.size)
    }

    @Test
    fun `given list official store contains best seller when mapping recom widget then total data after process it will not changed`() {
        `given list official store contains best seller`()
        val totalPreviousData = officialStoreList.toMutableList().size
        OfficialHomeMapper.mappingRecomWidget(
            mockBestSellerDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        val totalAfterMappingRecomWidget = officialStoreList.toMutableList().size
        Assert.assertEquals(totalPreviousData, totalAfterMappingRecomWidget)
    }

    @Test
    fun `given list official store contains different channel id best seller when mapping recom widget then data will not replace best seller data from the list`() {
        `given list official store contains best seller`()
        Assert.assertNotEquals(officialStoreList[8], mockBestSellerDataModel)
        OfficialHomeMapper.mappingRecomWidget(
            mockBestSellerDataModel, 
            officialStoreList
        ) {
            officialStoreList = it
        }
        Assert.assertNotEquals(officialStoreList[8], mockBestSellerDataModel)
    }

    @Test
    fun `given list official store contains best seller equals channel best seller id when mapping recom widget then data will replace best seller data from the list`() {
        `given list official store contains best seller equals channel id`()
        Assert.assertNotEquals(officialStoreList[8], mockBestSellerDataModel)
        OfficialHomeMapper.mappingRecomWidget(
            mockBestSellerDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        Assert.assertEquals(officialStoreList[8], mockBestSellerDataModel)
    }

    @Test
    fun `given list official store not contains best seller when mapping recom widget then recom widget position will be specific as given`() {
        `given list official store not contains best seller`()
        OfficialHomeMapper.mappingRecomWidget(
            mockBestSellerDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        Assert.assertEquals(officialStoreList[RECOM_WIDGET_POSITION], mockBestSellerDataModel)
    }

    @Test
    fun `given list official store not contains best seller with size less than default when mapping recom widget then recom widget position will top of the list`() {
        `given list official store not contains best seller less than default size`()
        OfficialHomeMapper.mappingRecomWidget(
            mockBestSellerDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        val indexBestSellerWidgetInList = officialStoreList.indexOfFirst { it is BestSellerDataModel }
        Assert.assertTrue(indexBestSellerWidgetInList < RECOM_WIDGET_POSITION)
    }

    @Test
    fun `given list official store contains featured shop when mapping featured shop total data after process it will not changed`() {
        `given list official store contains featured shop`()
        val totalPreviousData = officialStoreList.toMutableList().size
        OfficialHomeMapper.updateFeaturedShop(
            mockFeaturedShopDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        val totalAfterUpdateFeaturedShop = officialStoreList.toMutableList().size
        Assert.assertEquals(totalPreviousData, totalAfterUpdateFeaturedShop)
    }

    @Test
    fun `given list official store contains featured shop when mapping featured shop then data will replace featured shop data from the list`() {
        `given list official store contains featured shop`()
        OfficialHomeMapper.updateFeaturedShop(
            mockFeaturedShopDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        Assert.assertEquals(officialStoreList[3], mockFeaturedShopDataModel)
    }

    @Test
    fun `given list official store contains featured shop different id when mapping featured shop then data will replace featured based on channel id`() {
        `given list official store contains different channel id featured shop`()
        OfficialHomeMapper.updateFeaturedShop(
            mockFeaturedShopDataModel,
            officialStoreList
        ) {
            officialStoreList = it
        }
        Assert.assertNotEquals(officialStoreList[3], mockFeaturedShopDataModel)
        Assert.assertEquals(officialStoreList[4], mockFeaturedShopDataModel)
    }

    @Test
    fun `given list official store not contains benefit data when mapping benefit then contains benefit data with default position`() {
        `given list official store not contains benefit data`()
        OfficialHomeMapper.mappingBenefit(mockBenefit, officialStoreList) {
            officialStoreList = it
        }
        val indexBenefit = officialStoreList.indexOfFirst { it is OfficialBenefitDataModel }
        Assert.assertEquals(BENEFIT_POSITION, indexBenefit)
    }

    private fun `mapping benefit first time`() {
        `given list official store not contains benefit data`()
        OfficialHomeMapper.mappingBenefit(mockBenefit, officialStoreList) {
            officialStoreList = it
        }
    }

    @Test
    fun `given list official store when mapping benefit second time then value of benefit will repaced`() {
        `mapping benefit first time`()
        val benefitBefore = officialStoreList.find { it is OfficialBenefitDataModel }
        OfficialHomeMapper.mappingBenefit(mockBenefit2, officialStoreList) {
            officialStoreList = it
        }
        val benefitAfter = officialStoreList.find { it is OfficialBenefitDataModel }
        Assert.assertNotEquals(benefitBefore, benefitAfter)
    }

    @Test
    fun `given empty list official store when mapping benefit then benefit will be added to the list`() {
        `given empty list official store`()
        OfficialHomeMapper.mappingBenefit(mockBenefit, officialStoreList) {
            officialStoreList = it
        }
        val isBenefitExisted = officialStoreList.indexOfFirst { it is OfficialBenefitDataModel } != WIDGET_NOT_FOUND
        Assert.assertTrue(isBenefitExisted)
    }

    private fun `mapping featured shop first time`() {
        `given list official store not contains featured shop data`()
        OfficialHomeMapper.mappingFeaturedShop(
            mockOfficialStoreFeaturedShop,
            officialStoreList,
            mockCategory
        ) {
            officialStoreList = it
        }
    }

    @Test
    fun `give list official store not contains featured shop data when mapping featured shop then featured shop data in list official store with default position`() {
        `mapping featured shop first time`()
        val indexFeaturedShop = officialStoreList.indexOfFirst { it is OfficialFeaturedShopDataModel }
        Assert.assertNotEquals(WIDGET_NOT_FOUND, indexFeaturedShop)
        Assert.assertEquals(indexFeaturedShop, FEATURE_SHOP_POSITION)
    }

    @Test
    fun `given list official store when mapping feature shop second time then value of benefit will repaced`() {
        `mapping featured shop first time`()
        val featuredShopBefore = officialStoreList.find { it is OfficialFeaturedShopDataModel }
        OfficialHomeMapper.mappingFeaturedShop(
            mockOfficialStoreFeaturedShop2,
            officialStoreList,
            mockCategory
        ) {
            officialStoreList = it
        }
        val featuredShopAfter = officialStoreList.find { it is OfficialFeaturedShopDataModel }
        Assert.assertNotEquals(featuredShopBefore, featuredShopAfter)
    }

    @Test
    fun `given empty list official store when mapping official featured shop then benefit will be added to the list`() {
        `given empty list official store`()
        OfficialHomeMapper.mappingFeaturedShop(
            mockOfficialStoreFeaturedShop,
            officialStoreList,
            mockCategory
        ) {
            officialStoreList = it
        }
        val isOfficialFeaturedShopExisted = officialStoreList.indexOfFirst { it is OfficialFeaturedShopDataModel } != WIDGET_NOT_FOUND
        Assert.assertTrue(isOfficialFeaturedShopExisted)
    }
}