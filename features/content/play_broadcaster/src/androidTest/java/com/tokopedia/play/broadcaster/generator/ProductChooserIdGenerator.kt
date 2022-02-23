package com.tokopedia.play.broadcaster.generator

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.factory.PlayBroTestFragmentFactory
import com.tokopedia.play.broadcaster.helper.PrintCondition
import com.tokopedia.play.broadcaster.helper.ScreenshotTestRule
import com.tokopedia.play.broadcaster.helper.ViewHierarchyPrinter
import com.tokopedia.play.broadcaster.R as R
import com.tokopedia.play.broadcaster.setup.product.analytic.ProductChooserAnalyticManager
import com.tokopedia.play.broadcaster.setup.product.view.ProductSetupFragment
import com.tokopedia.play.broadcaster.setup.product.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.play.broadcaster.setup.product.viewmodel.PlayBroProductSetupViewModel
import com.tokopedia.play.broadcaster.type.DiscountedPrice
import com.tokopedia.play.broadcaster.ui.model.campaign.ProductTagSectionUiModel
import com.tokopedia.play.broadcaster.ui.model.paged.PagedDataUiModel
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class ProductChooserIdGenerator {

    @get:Rule
    val screenshotTestRule = ScreenshotTestRule()

    private val mockSelectedProducts = listOf(
        ProductUiModel(
            id = "1",
            name = "Product 1",
            imageUrl = "",
            stock = 5,
            price = DiscountedPrice(
                "1000",
                originalPriceNumber = 1000.0,
                discountPercent = 50,
                discountedPrice = "500",
                discountedPriceNumber = 500.0,
            )
        )
    )

//    private val mockProductSections = listOf(
//        ProductTagSectionUiModel(
//            name = "Section Test",
//            campaignStatus = CampaignStatus.Ongoing,
//            products = mockSelectedProducts,
//        )
//    )

    private val mockProductSections = emptyList<ProductTagSectionUiModel>()

    private val repo = mockk<PlayBroadcastRepository>(relaxed = true)
    private val configStore = mockk<HydraConfigStore>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val mockViewModelFactory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return mockk<PlayBroadcastViewModel>(relaxed = true) as T
        }
    }
    private val mockProductSetupViewModelFactory = object : PlayBroProductSetupViewModel.Factory {
        override fun create(productSectionList: List<ProductTagSectionUiModel>): PlayBroProductSetupViewModel {
            return PlayBroProductSetupViewModel(
                productSectionList = mockProductSections,
                repo = repo,
                configStore = configStore,
                userSession = userSession,
                dispatchers = CoroutineDispatchersProvider,
            )
        }
    }

    private val fragmentFactory = PlayBroTestFragmentFactory(
        mapOf(
            ProductSetupFragment::class.java to {
                ProductSetupFragment(mockViewModelFactory, mockProductSetupViewModelFactory)
            },
            ProductChooserBottomSheet::class.java to {
                ProductChooserBottomSheet(
                    CoroutineDispatchersProvider,
                    NavigationBarColorDialogCustomizer(),
                    ProductChooserAnalyticManager(
                        mockk(relaxed = true),
                        CoroutineDispatchersProvider,
                    )
                )
            }
        )
    )

    private val viewPrinter = ViewHierarchyPrinter(
        listOf(
            PrintCondition { view ->
                val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
                val packageName = parent::class.java.`package`?.name.orEmpty()
                val className = parent::class.java.name
                !packageName.startsWith("com.tokopedia") || !className.contains("unify", ignoreCase = true)
            },
            PrintCondition { view ->
                view.id != View.NO_ID || view is ViewGroup
            }
        )
    )

    init {
        coEvery { repo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
            dataList = mockSelectedProducts,
            hasNextPage = false,
        )
    }

    @Test
    fun testEventFragment() {

        val scenario = launchFragment<ProductSetupFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme,
        )

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment {
            val bottomSheet = ProductChooserBottomSheet.getFragment(it.childFragmentManager, it.requireActivity().classLoader)
            viewPrinter.print(view = bottomSheet.requireView())
        }
    }
}