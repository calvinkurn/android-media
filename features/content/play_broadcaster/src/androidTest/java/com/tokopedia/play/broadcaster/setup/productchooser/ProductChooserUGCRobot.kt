package com.tokopedia.play.broadcaster.setup.productchooser

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistryOwner
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.content.common.producttag.analytic.coordinator.ProductImpressionCoordinator
import com.tokopedia.content.common.producttag.analytic.coordinator.ShopImpressionCoordinator
import com.tokopedia.content.common.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.content.common.producttag.view.fragment.ContentAutocompleteFragment
import com.tokopedia.content.common.producttag.view.fragment.GlobalSearchFragment
import com.tokopedia.content.common.producttag.view.fragment.GlobalSearchProductTabFragment
import com.tokopedia.content.common.producttag.view.fragment.GlobalSearchShopTabFragment
import com.tokopedia.content.common.producttag.view.fragment.LastPurchasedProductFragment
import com.tokopedia.content.common.producttag.view.fragment.LastTaggedProductFragment
import com.tokopedia.content.common.producttag.view.fragment.ShopProductFragment
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.producttag.view.viewmodel.factory.ProductTagViewModelFactory
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.test.espresso.clickOnViewChild
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.ugc.ProductPickerUGCAnalytic
import com.tokopedia.play.broadcaster.helper.analyticUserSession
import com.tokopedia.play.broadcaster.setup.ProductSetupContainer
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.setup.productSetupViewModel
import com.tokopedia.play.broadcaster.setup.productUGCViewModel
import com.tokopedia.play.broadcaster.ui.state.PlayBroadcastUiState
import com.tokopedia.play.broadcaster.view.bottomsheet.ProductPickerUGCBottomSheet
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.hamcrest.core.AllOf

/**
 * Created by kenny.hadisaputra on 16/09/22
 */
class ProductChooserUGCRobot(
    listener: ProductPickerUGCBottomSheet.Listener? = null,
    viewModel: (SavedStateHandle) -> PlayBroProductSetupViewModel = {
        productSetupViewModel(handle = it)
    },
    productTagViewModel: (source: String, config: ContentProductTagConfig) -> ProductTagViewModel = { source, config ->
        productUGCViewModel(
            productTagSourceRaw = source,
            productTagConfig = config
        )
    }
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

    private val productImpressionCoordinator: ProductImpressionCoordinator = ProductImpressionCoordinator()
    private val shopImpressionCoordinator: ShopImpressionCoordinator = ShopImpressionCoordinator()

    private val onAttach: (Fragment) -> Unit = {
        when (it) {
            is ProductPickerUGCBottomSheet -> it.setListener(listener)
        }
    }

    private val parentViewModel = mockk<PlayBroadcastViewModel>(relaxed = true).apply {
        every { uiState } returns MutableStateFlow(
            mockk<PlayBroadcastUiState>(relaxed = true).apply {
                every { selectedContentAccount } returns ContentAccountUiModel.Empty
            }
        )
    }

    private val ugcViewModel = object : ProductTagViewModelFactory.Creator {
        override fun create(
            owner: SavedStateRegistryOwner,
            productTagSourceRaw: String,
            shopBadge: String,
            authorId: String,
            authorType: String,
            initialSelectedProduct: List<SelectedProductUiModel>,
            productTagConfig: ContentProductTagConfig
        ): ProductTagViewModelFactory {
            return ProductTagViewModelFactory(
                owner,
                productTagSourceRaw,
                shopBadge,
                authorId,
                authorType,
                initialSelectedProduct,
                productTagConfig,
                object : ProductTagViewModel.Factory {
                    override fun create(
                        productTagSourceRaw: String,
                        shopBadge: String,
                        authorId: String,
                        authorType: String,
                        initialSelectedProduct: List<SelectedProductUiModel>,
                        productTagConfig: ContentProductTagConfig
                    ): ProductTagViewModel {
                        return productTagViewModel(
                            productTagSourceRaw,
                            productTagConfig
                        )
                    }
                }
            )
        }
    }

    private val parentViewModelFactoryCreator = object : PlayBroadcastViewModelFactory.Creator {
        override fun create(activity: FragmentActivity): PlayBroadcastViewModelFactory {
            return PlayBroadcastViewModelFactory(
                activity = activity,
                playBroViewModelFactory = object : PlayBroadcastViewModel.Factory {
                    override fun create(handle: SavedStateHandle): PlayBroadcastViewModel {
                        return parentViewModel
                    }
                }
            )
        }
    }

    val scenario = launchFragment(themeResId = com.tokopedia.empty_state.R.style.AppTheme) {
        ProductSetupContainer(viewModel, onAttach) {
            when (it) {
                ProductTagParentFragment::class.java.name -> {
                    ProductTagParentFragment(
                        analyticUserSession,
                        ugcViewModel,
                        CoroutineDispatchersProvider,
                        mockk(relaxed = true)
                    )
                }
                LastTaggedProductFragment::class.java.name -> {
                    LastTaggedProductFragment(
                        impressionCoordinator = productImpressionCoordinator
                    )
                }
                ProductTagSourceBottomSheet::class.java.name -> {
                    ProductTagSourceBottomSheet(
                        analyticUserSession
                    )
                }
                ContentAutocompleteFragment::class.java.name -> {
                    ContentAutocompleteFragment()
                }
                GlobalSearchFragment::class.java.name -> {
                    GlobalSearchFragment()
                }
                GlobalSearchProductTabFragment::class.java.name -> {
                    GlobalSearchProductTabFragment(
                        impressionCoordinator = productImpressionCoordinator
                    )
                }
                GlobalSearchShopTabFragment::class.java.name -> {
                    GlobalSearchShopTabFragment(
                        impressionCoordinator = shopImpressionCoordinator
                    )
                }
                ShopProductFragment::class.java.name -> {
                    ShopProductFragment(
                        impressionCoordinator = productImpressionCoordinator
                    )
                }
                LastPurchasedProductFragment::class.java.name -> {
                    LastPurchasedProductFragment()
                }
                else -> {
                    ProductPickerUGCBottomSheet(
                        mockk(relaxed = true),
                        ProductPickerUGCAnalytic(analyticUserSession)
                    )
                }
            }
        }
    }

    init {
        scenario.moveToState(Lifecycle.State.RESUMED)
        delay()
    }

    fun close() {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.bottom_sheet_close)
        ).perform(ViewActions.click())
    }

    fun selectProductSource() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.tv_cc_product_tag_product_source)
        ).perform(ViewActions.click())

        await(500)
    }

    fun selectProductSourceOptionTokopedia() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.cl_global_search)
        ).perform(ViewActions.click())
    }

    fun selectProductSourceOptionLastPurchased() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.cl_last_purchase)
        ).perform(ViewActions.click())
    }

    fun selectSearchBar() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.cl_search)
        ).perform(ViewActions.click())
    }

    fun typeInSearchBar(query: String) = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)
        ).perform(ViewActions.replaceText(query))
    }

    fun selectSearchBarInShopProductPage() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.sb_shop_product)
        ).perform(ViewActions.click())
    }

    fun clickBackButton() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.ic_cc_product_tag_back)
        ).perform(ViewActions.click())
    }

    fun clickSaveButton() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.btn_save)
        ).perform(ViewActions.click())
    }

    fun pressImeActionInGlobalSearchBar() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)
        ).perform(ViewActions.pressImeActionButton())
    }

    fun selectProductInLastTaggedProduct(position: Int) = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.rv_last_tagged_product)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }

    fun selectProductInLastPurchasedProduct(position: Int) = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.rv_last_purchased_product)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }

    fun selectProductInGlobalSearchProduct(position: Int) = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.rv_global_search_product)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }

    fun selectShopInGlobalSearchShop(position: Int) = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.rv_global_search_shop)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                ViewActions.click()
            )
        )
    }

    fun selectProductTabInGlobalSearch() = chainable {
        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.isDescendantOfA(
                    ViewMatchers.withId(com.tokopedia.content.common.R.id.tab_layout)
                ),
                ViewMatchers.withText("Barang")
            )
        ).perform(ViewActions.click())
    }

    fun selectShopTabInGlobalSearch() = chainable {
        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.isDescendantOfA(
                    ViewMatchers.withId(com.tokopedia.content.common.R.id.tab_layout)
                ),
                ViewMatchers.withText("Toko")
            )
        ).perform(ViewActions.click())
    }

    fun selectFilterChipsInProductTab(position: Int) = chainable {
        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.isDescendantOfA(
                    ViewMatchers.withId(com.tokopedia.content.common.R.id.cl_global_search_product)
                ),
                ViewMatchers.withParent(
                    ViewMatchers.withId(com.tokopedia.sortfilter.R.id.sort_filter_items)
                ),
                ViewMatchers.withParentIndex(position)
            )
        ).perform(ViewActions.click())
    }

    fun selectAdvancedFilterChipsInProductTab() = chainable {
        Espresso.onView(
            AllOf.allOf(
                ViewMatchers.isDescendantOfA(
                    ViewMatchers.withId(com.tokopedia.content.common.R.id.cl_global_search_product)
                ),
                ViewMatchers.withId(com.tokopedia.sortfilter.R.id.sort_filter_prefix)
            )
        ).perform(ViewActions.click())

        await(500)
    }

    fun selectFilterInAdvancedFilterBottomSheet(position: Int) = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.filter.R.id.optionRecyclerView)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                clickOnViewChild(com.tokopedia.filter.R.id.sortFilterChipsUnify)
            )
        )
    }

    fun selectApplyInAdvancedFilterBottomSheet() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.filter.R.id.buttonApplyContainer)
        ).perform(ViewActions.click())
    }

    fun sendPendingImpressions() = chainable {
        productImpressionCoordinator.sendProductImpress()
        shopImpressionCoordinator.sendShopImpress()
    }

    fun await(durationInMs: Long) = chainable {
        delay(durationInMs)
    }

    private fun chainable(fn: () -> Unit): ProductChooserUGCRobot {
        fn()
        return this
    }
}
