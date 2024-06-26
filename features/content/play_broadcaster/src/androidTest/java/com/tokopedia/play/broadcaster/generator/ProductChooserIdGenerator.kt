package com.tokopedia.play.broadcaster.generator

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.content.common.types.ContentCommonUserType
import com.tokopedia.content.common.ui.model.ContentAccountUiModel
import com.tokopedia.content.common.util.bottomsheet.NavigationBarColorDialogCustomizer
import com.tokopedia.content.product.picker.ProductSetupFragment
import com.tokopedia.content.product.picker.seller.analytic.manager.EtalaseListAnalyticManager
import com.tokopedia.content.product.picker.seller.analytic.manager.ProductChooserAnalyticManager
import com.tokopedia.content.product.picker.seller.domain.repository.ProductPickerSellerCommonRepository
import com.tokopedia.content.product.picker.seller.model.DiscountedPrice
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatus
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignStatusUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.CampaignUiModel
import com.tokopedia.content.product.picker.seller.model.campaign.ProductTagSectionUiModel
import com.tokopedia.content.product.picker.seller.model.etalase.EtalaseUiModel
import com.tokopedia.content.product.picker.seller.model.paged.PagedDataUiModel
import com.tokopedia.content.product.picker.seller.model.pinnedproduct.PinProductUiModel
import com.tokopedia.content.product.picker.seller.model.product.ProductUiModel
import com.tokopedia.content.product.picker.seller.view.bottomsheet.EtalaseListBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductChooserBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSortBottomSheet
import com.tokopedia.content.product.picker.seller.view.bottomsheet.ProductSummaryBottomSheet
import com.tokopedia.content.product.picker.seller.view.viewmodel.ContentProductPickerSellerViewModel
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.factory.PlayBroTestFragmentFactory
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.test.application.id_generator.FileWriter
import com.tokopedia.test.application.id_generator.PrintCondition
import com.tokopedia.test.application.id_generator.ViewHierarchyPrinter
import com.tokopedia.test.application.id_generator.writeGeneratedViewIds
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import com.tokopedia.content.product.picker.R as contentproductpickerR
import com.tokopedia.empty_state.R as empty_stateR

/**
 * Created by kenny.hadisaputra on 23/02/22
 */
//@RunWith(AndroidJUnit4ClassRunner::class)
//class ProductChooserIdGenerator {
//
//    private val parentViewModel by lazy { mockk<PlayBroadcastViewModel>(relaxed = true) }
//
//    private val mockSelectedProducts = listOf(
//        ProductUiModel(
//            id = "1",
//            name = "Product 1",
//            imageUrl = "",
//            stock = 5,
//            price = DiscountedPrice(
//                "1000",
//                originalPriceNumber = 1000.0,
//                discountPercent = 50,
//                discountedPrice = "500",
//                discountedPriceNumber = 500.0
//            ),
//            hasCommission = false,
//            commissionFmt = "",
//            commission = 0L,
//            extraCommission = false,
//            pinStatus = PinProductUiModel.Empty,
//            number = "",
//            shopName = "",
//            shopBadge = "",
//            rating = "",
//            countSold = ""
//        )
//    )
//
//    private val campaignList = listOf(
//        CampaignUiModel(
//            id = "1",
//            title = "Campaign 1",
//            imageUrl = "",
//            startDateFmt = "",
//            endDateFmt = "",
//            status = CampaignStatusUiModel(
//                status = CampaignStatus.Ongoing,
//                text = "Berlangsung"
//            ),
//            totalProduct = 50
//        )
//    )
//
//    private val etalaseList = listOf(
//        EtalaseUiModel(
//            id = "1",
//            imageUrl = "",
//            title = "Etalase 1",
//            totalProduct = 50
//        )
//    )
//
//    private val mockProductSections = emptyList<ProductTagSectionUiModel>()
//
//    private val repo = mockk<PlayBroadcastRepository>(relaxed = true)
//    private val productPickerCommonRepo = mockk<ProductPickerSellerCommonRepository>(relaxed = true)
//    private val userSession = mockk<UserSessionInterface>(relaxed = true)
//
//    private val mockProductSetupViewModelFactory = object : ContentProductPickerSellerViewModel.Factory {
//        override fun create(
//            creationId: String,
//            maxProduct: Int,
//            productSectionList: List<ProductTagSectionUiModel>,
//            savedStateHandle: SavedStateHandle,
//            isNumerationShown: Boolean,
//            isEligibleForPin: Boolean,
//            fetchCommissionProduct: Boolean,
//            selectedAccount: ContentAccountUiModel
//        ): ContentProductPickerSellerViewModel {
//            return ContentProductPickerSellerViewModel(
//                creationId = creationId,
//                maxProduct = maxProduct,
//                productSectionList = mockProductSections,
//                savedStateHandle = savedStateHandle,
//                isEligibleForPin = isEligibleForPin,
//                repo = repo,
//                commonRepo = productPickerCommonRepo,
//                userSession = userSession,
//                dispatchers = CoroutineDispatchersProvider,
//                isNumerationShown = isNumerationShown,
//                fetchCommissionProduct = fetchCommissionProduct,
//                selectedAccount = selectedAccount
//            )
//        }
//    }
//
//    private val fragmentFactory = PlayBroTestFragmentFactory(
//        mapOf(
//            ProductSetupFragment::class.java to {
//                ProductSetupFragment(mockProductSetupViewModelFactory, mockk(relaxed = true)).apply {
//                    setDataSource(object : ProductSetupFragment.DataSource {
//                        override fun getProductSectionList(): List<ProductTagSectionUiModel> {
//                            return emptyList()
//                        }
//
//                        override fun isEligibleForPin(): Boolean {
//                            return true
//                        }
//
//                        override fun getSelectedAccount(): ContentAccountUiModel {
//                            return ContentAccountUiModel.Empty.copy(
//                                type = ContentCommonUserType.TYPE_SHOP
//                            )
//                        }
//
//                        override fun creationId(): String {
//                            return ""
//                        }
//
//                        override fun maxProduct(): Int {
//                            return 30
//                        }
//
//                        override fun isNumerationShown(): Boolean {
//                            return true
//                        }
//
//                        override fun fetchCommissionProduct(): Boolean {
//                            return false
//                        }
//                    })
//                }
//            },
//            ProductSummaryBottomSheet::class.java to {
//                ProductSummaryBottomSheet(
//                    analytic = mockk(relaxed = true),
//                    coachMarkSharedPref = mockk(relaxed = true),
//                    pinnedProductAnalytic = mockk(relaxed = true)
//                )
//            },
//            ProductChooserBottomSheet::class.java to {
//                ProductChooserBottomSheet(
//                    CoroutineDispatchersProvider,
//                    NavigationBarColorDialogCustomizer(),
//                    ProductChooserAnalyticManager(
//                        mockk(relaxed = true),
//                        CoroutineDispatchersProvider
//                    ),
//                    mockk(relaxed = true)
//                )
//            },
//            EtalaseListBottomSheet::class.java to {
//                EtalaseListBottomSheet(
//                    NavigationBarColorDialogCustomizer(),
//                    EtalaseListAnalyticManager(
//                        mockk(relaxed = true),
//                        CoroutineDispatchersProvider
//                    )
//                )
//            },
//            ProductSummaryBottomSheet::class.java to {
//                ProductSummaryBottomSheet(
//                    mockk(relaxed = true),
//                    mockk(relaxed = true),
//                    mockk(relaxed = true)
//                )
//            },
//            ProductSortBottomSheet::class.java to {
//                ProductSortBottomSheet(
//                    mockk(relaxed = true)
//                )
//            }
//        )
//    )
//
//    private val printConditions = listOf(
//        PrintCondition { view ->
//            val parent = (view.parent as? ViewGroup) ?: return@PrintCondition true
//            val packageName = parent::class.java.`package`?.name.orEmpty()
//            val className = parent::class.java.name
//            !packageName.startsWith("com.tokopedia") || !className.contains("unify", ignoreCase = true)
//        },
//        PrintCondition { view ->
//            view.id != View.NO_ID || view is ViewGroup
//        }
//    )
//
//    private val viewPrinter = ViewHierarchyPrinter(
//        printConditions,
//        packageName = LIBRARY_PACKAGE_NAME
//    )
//    private val fileWriter = FileWriter()
//
//    init {
//        coEvery { parentViewModel.uiState.value.selectedContentAccount } returns ContentAccountUiModel(
//            id = "1",
//            name = "Halo",
//            iconUrl = "",
//            badge = "",
//            type = ContentCommonUserType.TYPE_SHOP,
//            hasUsername = true,
//            hasAcceptTnc = true,
//            enable = true
//        )
//
//        coEvery { repo.getProductsInEtalase(any(), any(), any(), any()) } returns PagedDataUiModel(
//            dataList = mockSelectedProducts,
//            hasNextPage = false
//        )
//
//        coEvery { productPickerCommonRepo.getEtalaseList() } returns etalaseList
//        coEvery { productPickerCommonRepo.getCampaignList() } returns campaignList
//    }
//
//    @Test
//    fun productChooserBottomSheet() {
//        val scenario = launchFragment<ProductSetupFragment>(
//            factory = fragmentFactory,
//            themeResId = empty_stateR.style.AppTheme
//        )
//
//        scenario.moveToState(Lifecycle.State.RESUMED)
//
//        scenario.onFragment {
//            val bottomSheet = ProductChooserBottomSheet.getFragment(it.childFragmentManager, it.requireActivity().classLoader)
//            val info = viewPrinter.printAsCSV(view = bottomSheet.requireView())
//            fileWriter.writeGeneratedViewIds(
//                fileName = "product_chooser.csv",
//                text = info
//            )
//        }
//    }
//
//    @Test
//    fun sortFilterBottomSheet() {
//        val scenario = launchFragment<ProductSetupFragment>(
//            factory = fragmentFactory,
//            themeResId = empty_stateR.style.AppTheme
//        )
//
//        scenario.moveToState(Lifecycle.State.RESUMED)
//
//        onView(isRoot()).perform(waitFor(500))
//        onView(withId(contentproductpickerR.id.chips_sort)).perform(click())
//
//        scenario.onFragment {
//            val chooserBottomSheet = ProductChooserBottomSheet.getFragment(it.childFragmentManager, it.requireActivity().classLoader)
//            val sortBottomSheet = ProductSortBottomSheet.getFragment(chooserBottomSheet.childFragmentManager, it.requireActivity().classLoader)
//            val info = viewPrinter.printAsCSV(view = sortBottomSheet.requireView())
//            fileWriter.writeGeneratedViewIds(
//                fileName = "product_sort.csv",
//                text = info
//            )
//        }
//    }
//
//    @Test
//    fun etalaseListBottomSheet() {
//        val scenario = launchFragment<ProductSetupFragment>(
//            factory = fragmentFactory,
//            themeResId = empty_stateR.style.AppTheme
//        )
//
//        scenario.moveToState(Lifecycle.State.RESUMED)
//
//        onView(isRoot()).perform(waitFor(500))
//        onView(withId(contentproductpickerR.id.chips_etalase)).perform(click())
//        onView(isRoot()).perform(waitFor(500))
//
//        scenario.onFragment {
//            val bottomSheet = EtalaseListBottomSheet.getFragment(it.childFragmentManager, it.requireActivity().classLoader)
//            val info = viewPrinter.printAsCSV(view = bottomSheet.requireView())
//            fileWriter.writeGeneratedViewIds(
//                fileName = "etalase_list.csv",
//                text = info
//            )
//        }
//    }
//
//    private fun waitFor(delay: Long): ViewAction {
//        return object : ViewAction {
//            override fun getConstraints(): Matcher<View> = isRoot()
//            override fun getDescription(): String = "wait for $delay milliseconds"
//            override fun perform(uiController: UiController, v: View?) {
//                uiController.loopMainThreadForAtLeast(delay)
//            }
//        }
//    }
//
//    companion object {
//        const val LIBRARY_PACKAGE_NAME = "com.tokopedia.play.broadcaster"
//    }
//}
