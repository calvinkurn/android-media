package com.tokopedia.play.broadcaster.setup.productchooser

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.data.CassavaDatabase
import com.tokopedia.content.product.picker.ugc.domain.repository.ProductTagRepository
import com.tokopedia.content.product.picker.ugc.model.PagedGlobalSearchProductResponse
import com.tokopedia.content.product.picker.ugc.model.PagedGlobalSearchShopResponse
import com.tokopedia.content.product.picker.ugc.view.uimodel.LastPurchasedProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedDataUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.PagedState
import com.tokopedia.content.product.picker.ugc.view.uimodel.ProductUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.QuickFilterUiModel
import com.tokopedia.content.product.picker.ugc.view.uimodel.ShopUiModel
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.helper.containsEventAction
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.setup.productUGCViewModel
import com.tokopedia.test.application.annotations.CassavaTest
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 16/09/22
 */
@CassavaTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductChooserUGCAnalyticTest {

    private val mockRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockConfigStore: HydraConfigStore = mockk(relaxed = true)

    private val mockProducts = List(5) {
        ProductUiModel(
            id = it.toString(),
            name = "Product $it",
        )
    }

    private val mockShops = List(5) {
        ShopUiModel(
            shopId = it.toString(),
            shopName = "Shop $it"
        )
    }

    private val mockFilter = listOf<QuickFilterUiModel>(
        QuickFilterUiModel(
            name = "Official Store",
            icon = "",
            key = "shop_tier",
            value = "2",
        ),
        QuickFilterUiModel(
            name = "Pre Order",
            icon = "",
            key = "preorder",
            value = "true",
        ),
    )

    private val mockSortFilter = DynamicFilterModel(
        data = DataValue(
            filter = listOf(
                Filter(
                    title = "Bebas Ongkir",
                    options = listOf(
                        Option(
                            name = "Bebas Ongkir Chips",
                            key = "bebas_ongkir_extra",
                            value = "true",
                            inputType = "checkbox",
                            isPopular = true,
                            isNew = false,
                        )
                    )
                )
            )
        )
    )

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val cassavaDao = CassavaDatabase.getInstance(context).cassavaDao()

    init {
        val mockPagedProducts = PagedDataUiModel(
            dataList = mockProducts,
            hasNextPage = false,
            nextCursor = "1",
        )

        val mockPagedShops = PagedDataUiModel(
            dataList = mockShops,
            hasNextPage = false,
            nextCursor = "1"
        )

        coEvery { mockRepo.getLastTaggedProducts(any(), any(), any(), any()) } returns mockPagedProducts

        coEvery { mockRepo.getLastPurchasedProducts(any(), any()) } returns LastPurchasedProductUiModel(
            products = mockProducts,
            nextCursor = "1",
            state = PagedState.Success(false),
            coachmark = "",
            isCoachmarkShown = false,
        )

        coEvery { mockRepo.searchAceProducts(any()) } returns PagedGlobalSearchProductResponse(
            pagedData = mockPagedProducts,
            header = mockk(relaxed = true),
            suggestion = mockk(relaxed = true),
            ticker = mockk(relaxed = true),
        )

        coEvery { mockRepo.getSortFilter(any()) } returns mockSortFilter

        coEvery { mockRepo.searchAceShops(any()) } returns PagedGlobalSearchShopResponse(
            totalShop = mockShops.size,
            pagedData = mockPagedShops,
            header = mockk(relaxed = true),
        )

        coEvery { mockRepo.getQuickFilter(any(), any()) } returns mockFilter

        every { mockConfigStore.getMaxProduct() } returns 25
    }

    private fun createRobot() = ProductChooserUGCRobot(
        viewModel = {
            productSetupViewModel(
                productSectionList = emptyList(),
                repo = mockk(relaxed = true),
            )
        },
        productTagViewModel = { source, config ->
            productUGCViewModel(
                productTagSourceRaw = source,
                productTagConfig = config,
                authorType = "content-user",
                repo = mockRepo,
            )
        }
    )

    @Test
    fun testAnalytic_productChooserUGC() {
        createRobot()
            .selectProductSource()
            .assertEventAction("click - product tagging source")

            .selectProductSourceOptionTokopedia()
            .assertEventAction("click - product source tokopedia")

            .selectSearchBar()
            .assertEventAction("click - cari di tokopedia")

            .typeInSearchBar("baju")
            .pressImeActionInGlobalSearchBar()
            .selectAdvancedFilterChipsInProductTab()
            .assertEventAction("click - filter produk")

            .await(300) //give time for recycler view to show properly
            .selectFilterInAdvancedFilterBottomSheet(0)
            .selectApplyInAdvancedFilterBottomSheet()
            .assertEventAction("click - simpan filter produk")

            .selectFilterChipsInProductTab(0)
            .assertEventAction("click - chips di filter produk")

            .selectProductTabInGlobalSearch()
            .assertEventAction("click - barang atau toko tab")

            .selectProductInGlobalSearchProduct(0)
            .assertEventAction("click - product card tokopedia")

            .selectShopTabInGlobalSearch()
            .assertEventAction("click - barang atau toko tab")

            .await(1000) //give time for impression to send
            .assertEventAction("impression - toko")

            .selectShopInGlobalSearchShop(0)
            .assertEventAction("click - toko")
    }

    @Test
    fun testAnalytic_clickProductCard_inLastTaggedProduct() {
        val robot = createRobot()

        robot
            .selectProductInLastTaggedProduct(0)
            .assertEventAction("click - product card tokopedia")
    }

    @Test
    fun testAnalytic_impressProductCard_inGlobalSearchProduct() {
        val robot = createRobot()

        robot.selectSearchBar()
            .typeInSearchBar("baju")
            .pressImeActionInGlobalSearchBar()
            .selectProductTabInGlobalSearch()
            .sendPendingImpressions()
            .await(1000) //give time for impression to send
            .assertEventAction("impression - product card tokopedia")
    }

    @Test
    fun testAnalytic_impressProductCard_inLastTaggedProduct() {
        val robot = createRobot()

        robot.sendPendingImpressions()
            .await(1000) //give time for impression to send
            .assertEventAction("impression - product card tokopedia")
    }

    @Test
    fun testAnalytic_clickBackSelectProduct_fromShopProductPage() {
        val robot = createRobot()

        robot.selectSearchBar()
            .typeInSearchBar("baju")
            .pressImeActionInGlobalSearchBar()
            .selectShopTabInGlobalSearch()
            .selectShopInGlobalSearchShop(0)
            .clickBackButton()
            .assertEventAction("click - back pilih produk dari toko")
    }

    @Test
    fun testAnalytic_clickSearchBar_InShopProductPage() {
        val robot = createRobot()

        robot.selectSearchBar()
            .typeInSearchBar("baju")
            .pressImeActionInGlobalSearchBar()
            .selectShopTabInGlobalSearch()
            .selectShopInGlobalSearchShop(0)
            .selectSearchBarInShopProductPage()
            .assertEventAction("click - cari di toko")
    }

    @Test
    fun testAnalytic_clickSaveButton_inLastTaggedProduct() {
        val robot = createRobot()

        robot.selectProductInLastTaggedProduct(0)
            .clickSaveButton()
            .assertEventAction("click - simpan produk tokopedia")
    }

    @Test
    fun testAnalytic_clickSaveButton_inGlobalSearch() {
        val robot = createRobot()

        robot
            .selectSearchBar()
            .typeInSearchBar("baju")
            .pressImeActionInGlobalSearchBar()
            .selectProductInGlobalSearchProduct(0)
            .clickSaveButton()
            .assertEventAction("click - simpan produk tokopedia")
    }

    //TODO("uncomment if last tagged product is enabled")
//    @Test
//    fun testAnalytic_clickProductCard_inLastPurchased() {
//        val robot = createRobot()
//
//        robot
//            .selectProductSource()
//            .selectProductSourceOptionLastPurchased()
//            .selectProductInLastPurchasedProduct(0)
//
//        ViewMatchers.assertThat(
//            cassavaTestRule.validate(analyticFile),
//            containsEventAction("click - product card terakhir dibeli")
//        )
//    }

    //TODO("uncomment if last tagged product is enabled")
//    @Test
//    fun testAnalytic_clickSaveButton_inLastPurchased() {
//        val robot = createRobot()
//
//        robot
//            .selectProductSource()
//            .selectProductSourceOptionLastPurchased()
//            .selectProductInLastPurchasedProduct(0)
//            .clickSaveButton()
//
//        ViewMatchers.assertThat(
//            cassavaTestRule.validate(analyticFile),
//            containsEventAction("click - simpan produk terakhir dibeli")
//        )
//    }
}
