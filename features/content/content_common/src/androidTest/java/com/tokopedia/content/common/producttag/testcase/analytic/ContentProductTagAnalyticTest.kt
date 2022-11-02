package com.tokopedia.content.common.producttag.testcase.analytic

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.content.common.R
import com.tokopedia.content.common.const.DEFAULT_DELAY
import com.tokopedia.content.common.producttag.analytic.product.ContentProductTagAnalytic
import com.tokopedia.content.common.producttag.builder.GlobalSearchModelBuilder
import com.tokopedia.content.common.producttag.builder.LastPurchasedModelBuilder
import com.tokopedia.content.common.producttag.builder.LastTaggedModelBuilder
import com.tokopedia.content.common.producttag.builder.ProductTagSourceBuilder
import com.tokopedia.content.common.producttag.container.ContentProductTagTestActivity
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.helper.*
import com.tokopedia.content.common.producttag.helper.breadcrumb
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.content.test.util.*
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created By : Jonathan Darwin on October 05, 2022
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ContentProductTagAnalyticTest {

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    /** Mock */
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockUserSession: UserSessionInterface = mockk(relaxed = true)
    private val mockAnalytic: ContentProductTagAnalytic = mockk(relaxed = true)

    /** Builder */
    private val productTagSourceBuilder = ProductTagSourceBuilder()
    private val lastTaggedModelBuilder = LastTaggedModelBuilder()
    private val lastPurchasedBuilder = LastPurchasedModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()

    /** Helper */
    private val daggerHelper = ContentProductTagDaggerHelper(targetContext)

    /** Model */
    private val completeSource = productTagSourceBuilder.buildSourceList()
    private val lastTaggedProduct = lastTaggedModelBuilder.buildPagedDataModel()
    private val lastPurchasedProduct = lastPurchasedBuilder.buildModel()
    private val aceProduct = globalSearchModelBuilder.buildResponseModel()
    private val aceShop = globalSearchModelBuilder.buildShopResponseModel()
    private val quickFilter = globalSearchModelBuilder.buildQuickFilterList()
    private val sortFilter = globalSearchModelBuilder.buildSortFilterResponseModel(sizeFilter = 1, sizeSort = 0)
    private val sortFilterProductCount = "3"
    private val maxSelectedProduct = 3
    private val keyword = "pokemon"

    /** Model for Impression
     * Note : Because the number of impression is dependent on test device screen height,
     * the test might be flaky. so we need to display the products that will fit to all
     * screen height, 1 or 2 should be okay.
     * */
    private val lastTaggedProductForImpression = lastTaggedModelBuilder.buildPagedDataModel(size = 2, hasNextPage = false)
    private val aceProductForImpression = globalSearchModelBuilder.buildResponseModel(size = 2, hasNextPage = false)
    private val aceShopForImpression = globalSearchModelBuilder.buildShopResponseModel(size = 2, hasNextPage = false)

    private fun launchActivity(argumentBuilder: ContentProductTagArgument.Builder) {
        ActivityScenario.launch<ContentProductTagTestActivity>(
            Intent(targetContext, ContentProductTagTestActivity::class.java).apply {
                putExtra(ContentProductTagTestActivity.EXTRA_ARGUMENT, argumentBuilder.build())
            }
        )

        delay(DEFAULT_DELAY)
    }

    init {
        coEvery { mockUserSession.userId } returns "123"
        coEvery { mockUserSession.shopId } returns "456"

        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns lastTaggedProduct
        coEvery { mockRepo.getLastPurchasedProducts(any(), any()) } returns lastPurchasedProduct
        coEvery { mockRepo.searchAceProducts(any()) } returns aceProduct
        coEvery { mockRepo.searchAceShops(any()) } returns aceShop
        coEvery { mockRepo.getQuickFilter(any(), any()) } returns quickFilter
        coEvery { mockRepo.getSortFilter(any()) } returns sortFilter
        coEvery { mockRepo.getSortFilterProductCount(any()) } returns sortFilterProductCount

        coEvery { mockAnalytic.clickBreadcrumb(any()) } returns Unit

        daggerHelper.setupDagger(
            mockUserSession = mockUserSession,
            mockRepo = mockRepo,
            mockAnalytic = mockAnalytic,
        )
    }

    /**
     * Notes : This test only verify whether the
     * analytic interface is being hit or not
     * because the analytic implementation can be vary
     * from each other that implement it.
     *
     * If you want verify the analytic field as well,
     * please do so on your module.
     */

    /**
     * Table of Test (search this keyword below to navigate directly to the section)
     * 1. trackGlobalSearchProductTest
     * 2. trackGlobalSearchShopTest
     * 3. clickBreadcrumbTest
     * 4. clickProductTagSourceTest
     * 5. impressProductCardTest
     * 6. clickProductCardTest
     * 7. clickSearchBarTest
     * 8. clickGlobalSearchTabTest
     * 9. clickBackButtonTest
     * 10. impressShopCardTest
     * 11. clickShopCardTest
     * 12. clickSearchBarOnShopTest
     * 13. impressProductCardOnShopTest
     * 14. clickProductCardOnShopTest
     * 15. clickSaveProductTest
     * 16. clickAdvancedProductFilterTest
     * 17. clickSaveAdvancedProductFilterTest
     * 18. clickProductFilterChipsTest
     */

    /** trackGlobalSearchProductTest */
    @Test
    fun contentProductTag_ugc_trackGlobalSearchProduct() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearch(keyword)
        delay()

        verify { mockAnalytic.trackGlobalSearchProduct(aceProduct.header, any()) }
    }

    /** trackGlobalSearchShopTest */
    @Test
    fun contentProductTag_ugc_trackGlobalSearchShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearchShopSection(targetContext, keyword)

        verify { mockAnalytic.trackGlobalSearchShop(aceShop.header, any()) }
    }

    /** clickBreadcrumbTest */
    @Test
    fun contentProductTag_ugc_clickBreadcrumb_nonShopSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
        )

        click(breadcrumb)

        verify { mockAnalytic.clickBreadcrumb(false) }
    }

    @Test
    fun contentProductTag_ugc_clickBreadcrumb_shopSource() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openShopSectionFromGlobalSearch(targetContext, keyword, 0)
        click(breadcrumb)

        verify { mockAnalytic.clickBreadcrumb(true) }
    }

    /** clickProductTagSourceTest */
    @Test
    fun contentProductTag_ugc_clickProductTagSource_tokopedia() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
        )

        openTokopediaSection()

        verify { mockAnalytic.clickProductTagSource(ProductTagSource.GlobalSearch) }
    }

    @Test
    fun contentProductTag_ugc_clickProductTagSource_lastPurchased() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
        )

        openLastPurchasedSection()

        verify { mockAnalytic.clickProductTagSource(ProductTagSource.LastPurchase) }
    }

    @Test
    fun contentProductTag_ugc_clickProductTagSource_myShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
        )

        openMyShopSection()

        verify { mockAnalytic.clickProductTagSource(ProductTagSource.MyShop) }
    }

    /** impressProductCardTest */
    @Test
    fun contentProductTag_ugc_impressProductCard_lastTagged() {
        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns lastTaggedProductForImpression
        var counter = 0
        val impressedModel = lastTaggedProductForImpression.dataList.associateWith { counter++ }.toList()

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
        )

        click(backButton)

        verify {
            mockAnalytic.impressProductCard(ProductTagSource.LastTagProduct, impressedModel, true)
            mockAnalytic.sendAll()
        }
    }

    @Test
    fun contentProductTag_ugc_impressProductCard_globalSearch() {
        coEvery { mockRepo.searchAceProducts(any()) } returns aceProductForImpression
        var counter = 0
        val impressedModel = aceProductForImpression.pagedData.dataList.associateWith { counter++ }.toList()

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearch(keyword)

        click(backButton)

        verify {
            mockAnalytic.impressProductCard(ProductTagSource.GlobalSearch, impressedModel, false)
            mockAnalytic.sendAll()
        }
    }

    @Test
    fun contentProductTag_ugc_impressProductCard_myShop() {
        coEvery { mockRepo.searchAceProducts(any()) } returns aceProductForImpression
        var counter = 0
        val impressedModel = aceProductForImpression.pagedData.dataList.associateWith { counter++ }.toList()

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
            .setProductTagSource(completeSource)
        )

        openMyShopSection()

        click(backButton)

        verify {
            mockAnalytic.impressProductCard(ProductTagSource.MyShop, impressedModel, true)
            mockAnalytic.sendAll()
        }
    }

    /** clickProductCardTest */
    @Test
    fun contentProductTag_ugc_clickProductCard_lastTagged() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
        )

        clickItemRecyclerView(lastTaggedRv, 0)

        verify { mockAnalytic.clickProductCard(ProductTagSource.LastTagProduct, lastTaggedProduct.dataList[0], 0, true) }
    }

    @Test
    fun contentProductTag_ugc_clickProductCard_lastPurchased() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
        )

        openLastPurchasedSection()

        clickItemRecyclerView(lastPurchasedRv, 0)

        verify { mockAnalytic.clickProductCard(ProductTagSource.LastPurchase, lastPurchasedProduct.products[0], 0, true) }
    }

    @Test
    fun contentProductTag_ugc_clickProductCard_myShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
        )

        openMyShopSection()

        clickItemRecyclerView(myShopRv, 0)

        verify { mockAnalytic.clickProductCard(ProductTagSource.MyShop, aceProduct.pagedData.dataList[0], 0, true) }
    }

    @Test
    fun contentProductTag_ugc_clickProductCard_globalSearchProduct() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
        )

        openGlobalSearch(keyword)

        clickItemRecyclerView(globalSearchProductRv, 0)

        verify { mockAnalytic.clickProductCard(ProductTagSource.GlobalSearch, aceProduct.pagedData.dataList[0], 0, false) }
    }

    /** clickSearchBarTest */
    @Test
    fun contentProductTag_ugc_clickSearchBar_lastTagged() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        click(lastTaggedSearchBar)

        verify { mockAnalytic.clickSearchBar(ProductTagSource.LastTagProduct) }
    }

    @Test
    fun contentProductTag_ugc_clickSearchBar_myShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openMyShopSection()

        click(myShopSearchBar)

        verify { mockAnalytic.clickSearchBar(ProductTagSource.MyShop) }
    }

    @Test
    fun contentProductTag_ugc_clickSearchBar_globalSearch() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearch(keyword)

        click(globalSearchBar)

        verify { mockAnalytic.clickSearchBar(ProductTagSource.GlobalSearch) }
    }

    /** clickGlobalSearchTabTest */
    @Test
    fun contentProductTag_ugc_clickGlobalSearchTab_product() {
        val tabName = targetContext.getString(R.string.content_creation_barang_text)

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        /** Need to go to Toko tab first to trigger tab changing */
        openGlobalSearchShopSection(targetContext, keyword)

        click(targetContext.getString(R.string.content_creation_barang_text))

        verify { mockAnalytic.clickGlobalSearchTab(tabName.lowercase()) }
    }

    @Test
    fun contentProductTag_ugc_clickGlobalSearchTab_shop() {
        val tabName = targetContext.getString(R.string.content_creation_toko_text)

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearchShopSection(targetContext, keyword)

        verify { mockAnalytic.clickGlobalSearchTab(tabName.lowercase()) }
    }

    /** clickBackButtonTest */
    @Test
    fun contentProductTag_ugc_clickBackButton_lastTagged() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
        )

        click(backButton)

        verify { mockAnalytic.clickBackButton(ProductTagSource.LastTagProduct) }
    }

    @Test
    fun contentProductTag_ugc_clickBackButton_lastPurchased() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
        )

        openLastPurchasedSection()

        click(backButton)

        verify { mockAnalytic.clickBackButton(ProductTagSource.LastPurchase) }
    }

    @Test
    fun contentProductTag_ugc_clickBackButton_myShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setIsAutoHandleBackPressed(true)
        )

        openMyShopSection()

        click(backButton)

        verify { mockAnalytic.clickBackButton(ProductTagSource.MyShop) }
    }

    @Test
    fun contentProductTag_ugc_clickBackButton_globalSearch() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearch(keyword)

        click(backButton)

        verify { mockAnalytic.clickBackButton(ProductTagSource.GlobalSearch) }
    }

    @Test
    fun contentProductTag_ugc_clickBackButton_shop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openShopSectionFromGlobalSearch(targetContext, keyword, 0)

        click(backButton)

        verify { mockAnalytic.clickBackButton(ProductTagSource.Shop) }
    }

    /** impressShopCardTest */
    @Test
    fun contentProductTag_ugc_impressShopCard() {
        coEvery { mockRepo.searchAceShops(any()) } returns aceShopForImpression

        var counter = 0
        val impressionModel = aceShopForImpression.pagedData.dataList.associateWith { counter++ }.toList()

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearchShopSection(targetContext, keyword)

        click(backButton)

        verify { mockAnalytic.impressShopCard(ProductTagSource.GlobalSearch, impressionModel) }
    }

    /** clickShopCardTest */
    @Test
    fun contentProductTag_ugc_clickShopCard() {
        val positionInRv = 0
        val positionInAnalytic = positionInRv + 1

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setIsAutoHandleBackPressed(true)
            .setFullPageAutocomplete(false, "")
        )

        openGlobalSearchShopSection(targetContext, keyword)

        clickItemRecyclerView(globalSearchShopRv, positionInRv)

        verify { mockAnalytic.clickShopCard(aceShop.pagedData.dataList[positionInRv], positionInAnalytic) }
    }

    /** clickSearchBarOnShopTest */
    @Test
    fun contentProductTag_ugc_clickSearchBarOnShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openShopSectionFromGlobalSearch(targetContext, keyword, 0)

        click(shopSearchBar)

        verify { mockAnalytic.clickSearchBarOnShop() }
    }

    /** impressProductCardOnShopTest */
    @Test
    fun contentProductTag_ugc_impressProductCardOnShop() {
        coEvery { mockRepo.searchAceProducts(any()) } returns aceProductForImpression
        var counter = 0
        val impressedModel = aceProductForImpression.pagedData.dataList.associateWith { counter++ }.toList()

        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openShopSectionFromGlobalSearch(targetContext, keyword, 0)

        click(backButton)
        delay(DEFAULT_DELAY)

        verify {
            mockAnalytic.impressProductCardOnShop(impressedModel)
            mockAnalytic.sendAll()
        }
    }

    /** clickProductCardOnShopTest */
    @Test
    fun contentProductTag_ugc_clickProductCardOnShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setFullPageAutocomplete(false, "")
        )

        openShopSectionFromGlobalSearch(targetContext, keyword, 0)

        clickItemRecyclerView(shopRv, 0)

        verify {
            mockAnalytic.clickProductCardOnShop(aceProduct.pagedData.dataList[0], 0)
        }
    }

    /** clickSaveProductTest */
    @Test
    fun contentProductTag_ugc_clickSaveProduct_lastTagged() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        clickItemRecyclerView(lastTaggedRv, 0)
        clickItemRecyclerView(lastTaggedRv, 1)
        click(saveButton)

        verify {
            mockAnalytic.clickSaveProduct(ProductTagSource.LastTagProduct)
        }
    }

    @Test
    fun contentProductTag_ugc_clickSaveProduct_lastPurchased() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openLastPurchasedSection()

        clickItemRecyclerView(lastPurchasedRv, 0)
        clickItemRecyclerView(lastPurchasedRv, 1)
        click(saveButton)

        verify {
            mockAnalytic.clickSaveProduct(ProductTagSource.LastPurchase)
        }
    }

    @Test
    fun contentProductTag_ugc_clickSaveProduct_myShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openMyShopSection()

        clickItemRecyclerView(myShopRv, 0)
        clickItemRecyclerView(myShopRv, 1)
        click(saveButton)

        verify {
            mockAnalytic.clickSaveProduct(ProductTagSource.MyShop)
        }
    }

    @Test
    fun contentProductTag_ugc_clickSaveProduct_globalSearch() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openGlobalSearch(keyword)

        clickItemRecyclerView(globalSearchProductRv, 0)
        clickItemRecyclerView(globalSearchProductRv, 1)
        click(saveButton)

        verify {
            mockAnalytic.clickSaveProduct(ProductTagSource.GlobalSearch)
        }
    }

    @Test
    fun contentProductTag_ugc_clickSaveProduct_shop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openShopSectionFromGlobalSearch(targetContext, keyword, 0)

        clickItemRecyclerView(shopRv, 0)
        clickItemRecyclerView(shopRv, 1)
        click(saveButton)

        verify {
            mockAnalytic.clickSaveProduct(ProductTagSource.Shop)
        }
    }

    /** clickAdvancedProductFilterTest */
    @Test
    fun contentProductTag_ugc_clickAdvancedProductFilter() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openGlobalSortFilterProduct(keyword)

        verify { mockAnalytic.clickAdvancedProductFilter() }
    }

    /** clickSaveAdvancedProductFilterTest */
    @Test
    fun contentProductTag_ugc_clickSaveAdvancedProductFilter() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openGlobalSortFilterProduct(keyword)

        clickItemRecyclerView(sortFilterOptionRv, 0, sortFilterChip)

        clickWithMatcher(
            withParent(withId(sortFilterSaveButtonContainer)),
            withId(sortFilterSaveButton)
        )

        verify { mockAnalytic.clickSaveAdvancedProductFilter() }
    }

    /** clickProductFilterChipsTest */
    @Test
    fun contentProductTag_ugc_clickProductFilterChips() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
            .setMultipleSelectionProduct(true, maxSelectedProduct)
            .setIsAutoHandleBackPressed(true)
        )

        openGlobalSearch(keyword)

        clickWithMatcher(
            isDescendantOfA(withId(globalSearchProductContainer)),
            withParent(withId(globalSearchProductQuickFilterContainer)),
            withParentIndex(1),
        )

        verify { mockAnalytic.clickProductFilterChips() }
    }
}