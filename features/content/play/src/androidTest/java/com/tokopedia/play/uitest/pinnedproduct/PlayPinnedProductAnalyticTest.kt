package com.tokopedia.play.uitest.pinnedproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.content.test.cassava.containsEventAction
import com.tokopedia.content.test.espresso.delay
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.annotations.CassavaTest
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 03/08/22
 */
//TODO("Add more cases especially cases when pinned product has variant")
@RunWith(AndroidJUnit4ClassRunner::class)
@CassavaTest
class PlayPinnedProductAnalyticTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val trackingQueue = TrackingQueue(targetContext)

    private val channelId = "12669"
    private val analyticFile = "tracker/content/play/play_pinned_product.json"

    private val mockUserSession = mockk<UserSessionInterface>(relaxed = true)

    private val socket: PlayWebSocket = mockk(relaxed = true)

    init {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                uiModelBuilder.buildChannelData(
                    id = channelId,
                    channelDetail = PlayChannelDetailUiModel(
                        channelInfo = PlayChannelInfoUiModel(id = channelId, channelType = PlayChannelType.Live)
                    ),
                    tagItems = uiModelBuilder.buildTagItem(
                        product = uiModelBuilder.buildProductModel(
                            canShow = true,
                        )
                    ),
                    videoMetaInfo = PlayVideoMetaInfoUiModel(
                        //Use YouTube for now because non Youtube shows Unify Loader that can prevent app from being Idle
                        videoPlayer = PlayVideoPlayerUiModel.YouTube(""),
                        videoStream = PlayVideoStreamUiModel(
                            "", VideoOrientation.Vertical, "Video Keren"
                        ),
                    ),
                )
            ),
            cursor = "",
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(
                    PlayTestModule(
                        targetContext,
                        trackingQueue = trackingQueue,
                        userSession = { mockUserSession },
                    )
                )
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )
    }

    @Test
    fun onClicked_pinnedProduct_in_ProductCarousel() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> true })

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        with(robot) {
            delay(CASUAL_DELAY)
            clickPinnedProductCarousel()
            trackingQueue.sendAll()
            delay(CASUAL_DELAY)
        }

        assertCassavaByEventAction("click pinned featured product tagging")
    }

    @Test
    fun onImpressed_pinnedProduct_in_ProductCarousel() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> true })

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        with(robot) {
            delay(CASUAL_DELAY)
            trackingQueue.sendAll()
            delay(CASUAL_DELAY)
        }

        assertCassavaByEventAction("view on pinned featured product")
    }

    @Test
    fun onBuyClicked_pinnedProduct_in_ProductCarousel() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> true })
        val cartId = "123"

        every { mockUserSession.isLoggedIn } returns true
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem
        coEvery { repo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createRobot()
        with(robot) {
            delay(CASUAL_DELAY)
            clickBuyPinnedProductCarousel()
            trackingQueue.sendAll()
            delay(CASUAL_DELAY)
        }

        assertCassavaByEventAction("click buy pinned product")
    }

    @Test
    fun onAtcClicked_pinnedProduct_in_ProductCarousel() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> true })
        val cartId = "123"

        every { mockUserSession.isLoggedIn } returns true
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem
        coEvery { repo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createRobot()
        with(robot) {
            delay(CASUAL_DELAY)
            clickAtcPinnedProductCarousel()
            trackingQueue.sendAll()
            delay(CASUAL_DELAY)
        }

        assertCassavaByEventAction("click atc pinned product")
    }

    @Test
    fun onToasterImpressedAfterAtc_pinnedProduct_in_ProductCarousel() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> true })
        val cartId = "123"

        every { mockUserSession.isLoggedIn } returns true
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem
        coEvery { repo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createRobot()
        with(robot) {
            delay(CASUAL_DELAY)
            clickAtcPinnedProductCarousel()
            trackingQueue.sendAll()
            delay(CASUAL_DELAY)
        }

        assertCassavaByEventAction("view - pinned lihat keranjang")
    }

    @Test
    fun onToasterActionClickedAfterAtc_pinnedProduct_in_ProductCarousel() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> true })
        val cartId = "123"

        every { mockUserSession.isLoggedIn } returns true
        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem
        coEvery { repo.addProductToCart(any(), any(), any(), any(), any()) } returns cartId

        val robot = createRobot()
        with(robot) {
            delay(CASUAL_DELAY)
            clickAtcPinnedProductCarousel()
            clickToasterAction()
            trackingQueue.sendAll()
            delay(CASUAL_DELAY)
        }

        assertCassavaByEventAction("click - pinned lihat keranjang")
    }

    private fun createRobot() = PlayActivityRobot(channelId, initialDelay = 3500, isYouTube = true)

    private fun buildTagItemWithPinned(
        numOfSections: Int = 1,
        numOfProducts: Int = 5,
        isVariantAvailable: Boolean = false,
        hasPinned: (Int, Int) -> Boolean,
        isOCC: Boolean = false,
    ): TagItemUiModel {
        return uiModelBuilder.buildTagItem(
            product = uiModelBuilder.buildProductModel(
                productList = List(numOfSections) { sectionIndex ->
                    uiModelBuilder.buildProductSection(
                        productList = List(numOfProducts) { productIndex ->
                            uiModelBuilder.buildProduct(
                                id = "$sectionIndex$productIndex",
                                shopId = "2",
                                title = buildMockProductName(sectionIndex, productIndex),
                                stock = StockAvailable(1),
                                price = OriginalPrice("${productIndex}000", productIndex * 1000.0),
                                isPinned = hasPinned(sectionIndex, productIndex),
                                isVariantAvailable = isVariantAvailable,
                                buttons = listOf<ProductButtonUiModel>(
                                    uiModelBuilder.buildButton(
                                        text = "+ Keranjang",
                                        type = ProductButtonType.ATC
                                    ),
                                    uiModelBuilder.buildButton(
                                        text = "Beli",
                                        type = if(isOCC) ProductButtonType.OCC else ProductButtonType.GCR
                                    )
                                ),
                            )
                        }
                    )
                },
                canShow = true,
            ),
            maxFeatured = 3,
            bottomSheetTitle = "Product List",
            resultState = ResultState.Success,
        )
    }

    private fun buildMockProductName(
        sectionIndex: Int,
        productIndex: Int,
    ) = "Barang $sectionIndex, $productIndex"

    private fun assertCassavaByEventAction(eventAction: String) {
        ViewMatchers.assertThat(
            cassavaTestRule.validate(analyticFile),
            containsEventAction(eventAction)
        )
    }

    companion object {
        private const val CASUAL_DELAY = 1000L
    }
}
