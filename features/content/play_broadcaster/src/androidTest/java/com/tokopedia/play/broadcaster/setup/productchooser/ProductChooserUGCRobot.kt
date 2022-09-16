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
import com.tokopedia.content.common.producttag.analytic.coordinator.ProductImpressionCoordinator
import com.tokopedia.content.common.producttag.view.bottomsheet.ProductTagSourceBottomSheet
import com.tokopedia.content.common.producttag.view.fragment.LastTaggedProductFragment
import com.tokopedia.content.common.producttag.view.fragment.base.ProductTagParentFragment
import com.tokopedia.content.common.producttag.view.uimodel.SelectedProductUiModel
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.viewmodel.ProductTagViewModel
import com.tokopedia.content.common.producttag.view.viewmodel.factory.ProductTagViewModelFactory
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
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
import com.tokopedia.play.test.espresso.delay
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

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
            productTagConfig = config,
        )
    }
) {

    private val context = InstrumentationRegistry.getInstrumentation().context

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
                            productTagConfig,
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

    val scenario = launchFragment(themeResId = R.style.AppTheme) {
        ProductSetupContainer(viewModel, onAttach) {
            when (it) {
                ProductTagParentFragment::class.java.name -> {
                    ProductTagParentFragment(
                        analyticUserSession,
                        ugcViewModel,
                    )
                }
                LastTaggedProductFragment::class.java.name -> {
                    LastTaggedProductFragment(
                        impressionCoordinator = ProductImpressionCoordinator()
                    )
                }
                ProductTagSourceBottomSheet::class.java.name -> {
                    ProductTagSourceBottomSheet(
                        analyticUserSession,
                    )
                }
                else -> {
                    ProductPickerUGCBottomSheet(
                        mockk(relaxed = true),
                        parentViewModelFactoryCreator,
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
    }

    fun selectProductSourceOptionTokopedia() = chainable {
        Espresso.onView(
            ViewMatchers.withId(com.tokopedia.content.common.R.id.cl_global_search)
        ).perform(ViewActions.click())
    }

    fun selectLastTaggedProduct(position: Int = 0) {
        Espresso.onView(
            ViewMatchers.withId(R.id.rv_products)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position, ViewActions.click()
            )
        )
    }

    fun await(durationInMs: Long) = chainable {
        delay(durationInMs)
    }

    private fun chainable(fn: () -> Unit): ProductChooserUGCRobot {
        fn()
        return this
    }
}