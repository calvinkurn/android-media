package com.tokopedia.play.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.PlayChannelDataModelBuilder
import com.tokopedia.play.model.PlayChannelInfoModelBuilder
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.util.assertType
import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.type.ProductButtonType
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.uimodel.action.AtcProductAction
import com.tokopedia.play.view.uimodel.action.BuyProductAction
import com.tokopedia.play.view.uimodel.action.OCCProductAction
import com.tokopedia.play.view.uimodel.action.PlayViewerNewAction
import com.tokopedia.play.view.uimodel.event.AtcSuccessEvent
import com.tokopedia.play.view.uimodel.event.BuySuccessEvent
import com.tokopedia.play.view.uimodel.event.OCCSuccessEvent
import com.tokopedia.play.view.uimodel.event.OpenPageEvent
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 25/08/22
 */
@ExperimentalCoroutinesApi
class PlayBuyAtcTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val modelBuilder = UiModelBuilder.get()

    private val mockRepo: PlayViewerRepository = mockk(relaxed = true)

    private val channelDataBuilder = PlayChannelDataModelBuilder()
    private val channelInfoBuilder = PlayChannelInfoModelBuilder()

    private val mockLiveChannelData = channelDataBuilder.buildChannelData(
        channelDetail = channelInfoBuilder.buildChannelDetail(
            channelInfo = channelInfoBuilder.buildChannelInfo(
                channelType = PlayChannelType.Live
            )
        )
    )

    private val cartId = "12"

    /**
     * Bottom Sheet
     */
    @Test
    fun `when non login user click buy redirect to login`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(false)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    BuyProductAction(
                        sectionInfo = modelBuilder.buildProductSection(),
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OpenPageEvent> {
                it.applink.assertEqualTo(ApplinkConst.LOGIN)
            }
        }
    }

    @Test
    fun `when logged in user click buy emit buy success event`() {
        coEvery { mockRepo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(true)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    BuyProductAction(
                        sectionInfo = modelBuilder.buildProductSection(),
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<BuySuccessEvent> {
                it.cartId.assertEqualTo(cartId)
            }
            coVerify { mockRepo.addProductToCart(any(), any(), any(), any(), any()) }
        }
    }

    @Test
    fun `when non login user click occ redirect to login`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(false)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    OCCProductAction(
                        sectionInfo = modelBuilder.buildProductSection(),
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli Langsung",
                                    type = ProductButtonType.OCC
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OpenPageEvent> {
                it.applink.assertEqualTo(ApplinkConst.LOGIN)
            }
        }
    }

    @Test
    fun `when logged in user click occ emit occ success event`() {
        coEvery { mockRepo.addProductToCartOcc(any(), any(), any(), any(), any()) } returns cartId

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(true)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    OCCProductAction(
                        sectionInfo = modelBuilder.buildProductSection(),
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli Langsung",
                                    type = ProductButtonType.OCC
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OCCSuccessEvent> {
                it.cartId.assertEqualTo(cartId)
            }
            coVerify { mockRepo.addProductToCartOcc(any(), any(), any(), any(), any()) }
        }
    }

    @Test
    fun `when non login user click atc redirect to login`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(false)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    AtcProductAction(
                        sectionInfo = modelBuilder.buildProductSection(),
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OpenPageEvent> {
                it.applink.assertEqualTo(ApplinkConst.LOGIN)
            }
        }
    }

    @Test
    fun `when logged in user click atc emit atc success event`() {
        coEvery { mockRepo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(true)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    AtcProductAction(
                        sectionInfo = modelBuilder.buildProductSection(),
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<AtcSuccessEvent> {
                it.cartId.assertEqualTo(cartId)
            }
            coVerify { mockRepo.addProductToCart(any(), any(), any(), any(), any()) }
        }
    }

    /**
     * Pinned Product - Carousel
     */

    @Test
    fun `carousel - when non login user click buy redirect to login`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(false)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.BuyProduct(
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OpenPageEvent> {
                it.applink.assertEqualTo(ApplinkConst.LOGIN)
            }
        }
    }

    @Test
    fun `carousel - when logged in user click buy emit buy success event`() {
        coEvery { mockRepo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(true)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.BuyProduct(
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<BuySuccessEvent> {
                it.cartId.assertEqualTo(cartId)
            }
            coVerify { mockRepo.addProductToCart(any(), any(), any(), any(), any()) }
        }
    }

    @Test
    fun `carousel - when non login user click occ redirect to login`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(false)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.OCCProduct(
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli Langsung",
                                    type = ProductButtonType.OCC
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OpenPageEvent> {
                it.applink.assertEqualTo(ApplinkConst.LOGIN)
            }
        }
    }

    @Test
    fun `carousel - when logged in user click occ emit occ success event`() {
        coEvery { mockRepo.addProductToCartOcc(any(), any(), any(), any(), any()) } returns cartId

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(true)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.OCCProduct(
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli Langsung",
                                    type = ProductButtonType.OCC
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OCCSuccessEvent> {
                it.cartId.assertEqualTo(cartId)
            }
            coVerify { mockRepo.addProductToCartOcc(any(), any(), any(), any(), any()) }
        }
    }

    @Test
    fun `carousel - when non login user click atc redirect to login`() {
        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(false)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.AtcProduct(
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli",
                                    type = ProductButtonType.GCR
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<OpenPageEvent> {
                it.applink.assertEqualTo(ApplinkConst.LOGIN)
            }
        }
    }

    @Test
    fun `carousel - logged in user click atc emit success event`() {
        coEvery { mockRepo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        ) {
            createPage(mockLiveChannelData)
            focusPage(mockLiveChannelData)
            setLoggedIn(true)
        }
        robot.use {
            it.recordEvent {
                it.viewModel.submitAction(
                    PlayViewerNewAction.AtcProduct(
                        product = modelBuilder.buildProduct(
                            stock = StockAvailable(10), buttons = listOf(
                                modelBuilder.buildButton(
                                    text = "Beli Langsung",
                                    type = ProductButtonType.OCC
                                ),
                                modelBuilder.buildButton(
                                    text = "+ Keranjang",
                                    type = ProductButtonType.ATC
                                ),
                            )
                        )
                    )
                )
            }.last().assertType<AtcSuccessEvent> {
                it.cartId.assertEqualTo(cartId)
            }

            coVerify { mockRepo.addProductToCart(any(), any(), any(), any(), any()) }
        }
    }
}
