package com.tokopedia.content.common.viewmodel.producttag

import com.tokopedia.content.common.model.CommonModelBuilder
import com.tokopedia.content.common.model.ConfigModelBuilder
import com.tokopedia.content.common.model.ShopModelBuilder
import com.tokopedia.content.common.producttag.domain.repository.ProductTagRepository
import com.tokopedia.content.common.producttag.util.extension.currentSource
import com.tokopedia.content.common.producttag.util.preference.ProductTagPreference
import com.tokopedia.content.common.producttag.view.uimodel.ProductTagSource
import com.tokopedia.content.common.producttag.view.uimodel.action.ProductTagAction
import com.tokopedia.content.common.producttag.view.uimodel.config.ContentProductTagConfig
import com.tokopedia.content.common.producttag.view.uimodel.event.ProductTagUiEvent
import com.tokopedia.content.common.robot.ProductTagViewModelRobot
import com.tokopedia.content.common.util.andThen
import com.tokopedia.unit.test.rule.CoroutineTestRule
import com.tokopedia.content.common.util.assertEqualTo
import com.tokopedia.content.common.util.equalTo
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
    private val configModelBuilder = ConfigModelBuilder()

    private val feedConfig = configModelBuilder.buildFeedConfig()
    private val playConfig = configModelBuilder.buildPlayConfig()
    private val mockException = commonModelBuilder.buildException()

    @Test
    fun `when user press back press, it should remove the most top fragment from stack`() {
        val selectedShop = shopModelBuilder.buildUiModel()

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            productTagConfig = feedConfig,
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
            productTagConfig = feedConfig,
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
            productTagConfig = feedConfig,
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
            productTagConfig = feedConfig,
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
    fun `open autocomplete page & isFullPageAutocomplete false - add autocomplete source to stack`() {

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
            productTagConfig = playConfig,
        )

        robot.use {
            /** Test State */
            robot.recordState {
                submitAction(ProductTagAction.OpenAutoCompletePage)
            }.andThen {
                productTagSource.productTagSourceStack.size equalTo 2
                productTagSource.productTagSourceStack.currentSource equalTo ProductTagSource.Autocomplete
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
            productTagConfig = feedConfig,
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
        val shopId = 1L
        val mockResponse = shopModelBuilder.buildUiModel(shopId.toString())

        coEvery { mockRepo.getShopInfoByID(listOf(shopId)) } returns mockResponse

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
            productTagConfig = feedConfig,
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
        val shopId = 1L

        coEvery { mockRepo.getShopInfoByID(listOf(shopId)) } throws mockException

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
            productTagConfig = feedConfig,
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
            productTagConfig = feedConfig,
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
            productTagConfig = feedConfig,
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
    fun `user selects a product & isMultipleProductSelection false - emit event finish product tag`() {
        val product = commonModelBuilder.buildProduct()
        val selectedProduct = product.toSelectedProduct()

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
            productTagConfig = feedConfig,
        )

        robot.use {
            robot.recordEvent {
                submitAction(ProductTagAction.ProductSelected(product))
            }.andThen {
                last().assertEqualTo(ProductTagUiEvent.FinishProductTag(listOf(selectedProduct)))
            }
        }
    }

    @Test
    fun `user selects a product & isMultipleProductSelection true with maxSelected 5 - emit new selected product list`() {
        val products = List(6) {
            commonModelBuilder.buildProduct(id = it.toString())
        }

        val selectedProducts = products.map {
            commonModelBuilder.buildSelectedProduct(id = it.id)
        }

        val robot = ProductTagViewModelRobot(
            dispatcher = testDispatcher,
            repo = mockRepo,
            sharedPref = mockSharedPref,
            productTagConfig = playConfig,
        )

        robot.use {
            robot.recordState {
                products.forEach { product ->
                    submitAction(ProductTagAction.ProductSelected(product))
                }
            } andThen {
                this.selectedProduct equalTo selectedProducts.take(playConfig.maxSelectedProduct)
            }

            robot.recordState {
                submitAction(ProductTagAction.ProductSelected(products[0]))
            } andThen {
                this.selectedProduct equalTo selectedProducts.take(5).filterNot { it.id == products[0].id }
            }
        }
    }
}
