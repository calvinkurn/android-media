package com.tokopedia.createpost.viewmodel.producttag

import com.tokopedia.createpost.model.CommonModelBuilder
import com.tokopedia.createpost.model.ShopModelBuilder
import com.tokopedia.createpost.producttag.domain.repository.ProductTagRepository
import com.tokopedia.createpost.producttag.util.preference.ProductTagPreference
import com.tokopedia.createpost.producttag.view.uimodel.ProductTagSource
import com.tokopedia.createpost.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.createpost.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.createpost.robot.ProductTagViewModelRobot
import com.tokopedia.createpost.util.andThen
import com.tokopedia.createpost.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.fail
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on May 30, 2022
 */
class ProductTagNavigationViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    private val testDispatcher = rule.dispatchers
    private val mockRepo: ProductTagRepository = mockk(relaxed = true)
    private val mockSharedPref: ProductTagPreference = mockk(relaxed = true)

    private val commonModelBuilder = CommonModelBuilder()
    private val shopModelBuilder = ShopModelBuilder()

    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user press back press, it should remove the most top fragment from stack`() {
        val selectedShop = shopModelBuilder.buildUiModel()

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
        )

        /** Setup State */
        robot.use {
            robot.recordState {
                submitAction(ProductTagAction.SelectProductTagSource(ProductTagSource.GlobalSearch))
                submitAction(ProductTagAction.ShopSelected(selectedShop))
            }.andThen {
                productTagSource.productTagSourceStack.size.assertEqualTo(2)
            }

            /** Test State 1 */
            robot.recordState {
                submitAction(ProductTagAction.BackPressed)
            }.andThen {
                productTagSource.productTagSourceStack.size.assertEqualTo(1)
            }

            /** Test State 2 */
            robot.recordState {
                submitAction(ProductTagAction.BackPressed)
            }.andThen {
                productTagSource.productTagSourceStack.size.assertEqualTo(0)
            }
        }
    }

    @Test
    fun `when user click breadcrumb, it should emit event to open bottomsheet`() {
        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordEvent {
                submitAction(ProductTagAction.ClickBreadcrumb)
            }.andThen {
                last().assertEqualTo(ProductTagUiEvent.ShowSourceBottomSheet)
                verify{ mockSharedPref.setNotFirstGlobalTag() }
            }
        }
    }

    @Test
    fun `when user click breadcrumb but the stack is more than 1, it should still open select product tag source bottomsheet`() {
        val selectedShop = shopModelBuilder.buildUiModel()

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        /** Setup State */
        robot.use {
            robot.getViewModel().apply {
                submitAction(ProductTagAction.SelectProductTagSource(ProductTagSource.GlobalSearch))
                submitAction(ProductTagAction.ShopSelected(selectedShop))
            }

            /** Test State */
            robot.recordStateAndEvent {
                submitAction(ProductTagAction.ClickBreadcrumb)
            }.andThen { state, events ->
                state.productTagSource.productTagSourceStack.size.assertEqualTo(2)
                events.last().assertEqualTo(ProductTagUiEvent.ShowSourceBottomSheet)
            }
        }
    }

    @Test
    fun `when user wants to open autocomplete page, it should emit event to open autocomplete and pass the current query`() {
        val query = "pokemon"

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            /** Setup State */
            robot.getViewModel().apply {
                submitAction(ProductTagAction.SetDataFromAutoComplete(ProductTagSource.GlobalSearch, query, "", ""))
            }

            /** Test State */
            robot.recordEvent {
                submitAction(ProductTagAction.OpenAutoCompletePage)
            }.andThen {
                last().assertEqualTo(ProductTagUiEvent.OpenAutoCompletePage(query))
            }
        }
    }

    @Test
    fun `when user click suggested product and come back from autocomplete page, it should emit state with appropriate page`() {
        val query = "pokemon"
        val source = ProductTagSource.GlobalSearch

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordState {
                submitAction(ProductTagAction.SetDataFromAutoComplete(source, query, "", ""))
            }.andThen {
                productTagSource.productTagSourceStack.last().assertEqualTo(source)
                globalSearchProduct.param.query.assertEqualTo(query)
            }
        }
    }

    @Test
    fun `when user click suggested shop and come back from autocomplete page, it should emit state with appropriate page`() {
        val query = "pokemon"
        val source = ProductTagSource.Shop
        val shopId = 1
        val mockResponse = shopModelBuilder.buildUiModel(shopId.toString())

        coEvery { mockRepo.getShopInfoByID(listOf(shopId)) } returns mockResponse

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordState {
                submitAction(ProductTagAction.SetDataFromAutoComplete(source, query, shopId.toString(), ""))
            }.andThen {
                productTagSource.productTagSourceStack.last().assertEqualTo(source)
                shopProduct.param.query.assertEqualTo(query)
            }
        }
    }

    @Test
    fun `when user click suggested shop and error happens, it should emit error event`() {
        val query = "pokemon"
        val source = ProductTagSource.Shop
        val shopId = 1

        coEvery { mockRepo.getShopInfoByID(listOf(shopId)) } throws mockException

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordEvent {
                submitAction(ProductTagAction.SetDataFromAutoComplete(source, query, shopId.toString(), ""))
            }.andThen {
                val lastEvent = last()
                if(lastEvent is ProductTagUiEvent.ShowError) {
                    lastEvent.throwable.assertEqualTo(mockException)
                }
                else {
                    fail("Event should be ProductTagUiEvent.ShowError")
                }
            }
        }
    }

    @Test
    fun `when user select product tag source, it should emit new state of stack`() {
        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordState {
                submitAction(ProductTagAction.SelectProductTagSource(ProductTagSource.LastTagProduct))
            }.andThen {
                productTagSource.productTagSourceStack.size.assertEqualTo(1)
                productTagSource.productTagSourceStack.first().assertEqualTo(ProductTagSource.LastTagProduct)
            }

            robot.recordState {
                submitAction(ProductTagAction.SelectProductTagSource(ProductTagSource.LastPurchase))
            }.andThen {
                productTagSource.productTagSourceStack.size.assertEqualTo(1)
                productTagSource.productTagSourceStack.first().assertEqualTo(ProductTagSource.LastPurchase)
            }
        }
    }

    @Test
    fun `when user try forcing to open global search without query, it should emit last tagged fragment`() {
        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordState {
                submitAction(ProductTagAction.SelectProductTagSource(ProductTagSource.GlobalSearch))
            }.andThen {
                productTagSource.productTagSourceStack.first().assertEqualTo(ProductTagSource.LastTagProduct)
            }
        }
    }

    @Test
    fun `when user selects a product, it should emit product selected event`() {
        val selectedProduct = commonModelBuilder.buildProduct()

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
        )

        robot.use {
            robot.recordEvent {
                submitAction(ProductTagAction.ProductSelected(selectedProduct))
            }.andThen {
                last().assertEqualTo(ProductTagUiEvent.ProductSelected(selectedProduct))
            }
        }
    }
}