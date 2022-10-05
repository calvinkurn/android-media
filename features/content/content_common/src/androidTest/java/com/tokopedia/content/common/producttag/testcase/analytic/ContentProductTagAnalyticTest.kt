package com.tokopedia.content.common.producttag.testcase.analytic

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.VerificationModes.times
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
import com.tokopedia.content.common.producttag.helper.fakeSearchBar
import com.tokopedia.content.common.producttag.helper.globalSearchShopRv
import com.tokopedia.content.common.producttag.helper.lastTaggedSearchBar
import com.tokopedia.content.common.producttag.view.uimodel.ContentProductTagArgument
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.SearchParamUiModel
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
    private val lastTaggedModelBuilder = LastTaggedModelBuilder()
    private val lastPurchasedBuilder = LastPurchasedModelBuilder()
    private val globalSearchModelBuilder = GlobalSearchModelBuilder()
    private val productTagSourceBuilder = ProductTagSourceBuilder()

    /** Helper */
    private val daggerHelper = ContentProductTagDaggerHelper(targetContext)

    /** Model */
    private val completeSource = productTagSourceBuilder.buildComplete()
    private val lastTaggedProduct = lastTaggedModelBuilder.buildPagedDataModel()
    private val lastPurchasedProduct = lastPurchasedBuilder.buildModel()
    private val aceProduct = globalSearchModelBuilder.buildResponseModel()
    private val aceShop = globalSearchModelBuilder.buildShopResponseModel()
    private val quickFilter = globalSearchModelBuilder.buildQuickFilterList()
    private val keyword = "pokemon"

    /** Model for Impression
     * Note : Because the number of impression is dependent on test device screen height,
     * the test might be flaky. so we need to display the products that will fit to all
     * screen height, 1 or 2 should be okay.
     * */
    private val lastTaggedProductForImpression = lastTaggedModelBuilder.buildPagedDataModel(size = 2, hasNextPage = false)
    private val aceProductForImpression = globalSearchModelBuilder.buildResponseModel(size = 2, hasNextPage = false)

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

        click(breadcrumb)

        click(sourceTokopedia)

        verify { mockAnalytic.clickProductTagSource(ProductTagSource.GlobalSearch) }
    }

    @Test
    fun contentProductTag_ugc_clickProductTagSource_lastPurchased() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
        )

        click(breadcrumb)

        click(sourceLastPurchased)

        verify { mockAnalytic.clickProductTagSource(ProductTagSource.LastPurchase) }
    }

    @Test
    fun contentProductTag_ugc_clickProductTagSource_myShop() {
        launchActivity(ContentProductTagArgument.Builder()
            .setAuthorType(ContentCommonUserType.TYPE_USER)
            .setProductTagSource(completeSource)
        )

        click(breadcrumb)

        click(sourceMyShop)

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

    /** clickGlobalSearchTabTest */

    /** clickBackButtonTest */

    /** impressShopCardTest */

    /** clickShopCardTest */

    /** clickSearchBarOnShopTest */

    /** impressProductCardOnShopTest */
    @Test
    fun contentProductTag_ugc_impressProductCard_shop() {
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

    /** clickSaveProductTest */

    /** clickAdvancedProductFilterTest */

    /** clickSaveAdvancedProductFilterTest */

    /** clickProductFilterChipsTest */
}