package com.tokopedia.play.uitest.pinnedproduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.play.di.DaggerPlayTestComponent
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.di.PlayTestModule
import com.tokopedia.play.di.PlayTestRepositoryModule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.uitest.robot.PlayActivityRobot
import com.tokopedia.play.view.storage.PagingChannel
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play_common.model.result.ResultState
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.test.application.annotations.UiTest
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@UiTest
class PlayPinnedProductUiTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val channelId = "12669"

    private val socket: PlayWebSocket = mockk(relaxed = true)

    init {
        coEvery { repo.getChannels(any(), any()) } returns PagingChannel(
            channelList = listOf(
                uiModelBuilder.buildChannelData(
                    id = channelId,
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
                .playTestModule(PlayTestModule(targetContext))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo, socket))
                .build()
        )
    }

    @Test
    fun pinnedProduct_productCarouselView_oneSection_noPinned() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> false })

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot.assertHasPinnedItemInCarousel(false)
    }

    @Test
    fun pinnedProduct_productCarouselView_oneSection_hasPinned() {
        val sectionPinned = 0
        val productPinned = 3
        val tagItem = buildTagItemWithPinned(
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot.assertHasPinnedItemInCarousel(
            true,
            buildMockProductName(sectionPinned, productPinned)
        )
    }

    @Test
    fun pinnedProduct_productCarouselView_threeSections_noPinned() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> false })

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot.assertHasPinnedItemInCarousel(false)
    }

    @Test
    fun pinnedProduct_productCarouselView_threeSections_hasPinned_sectionOne_productTwo() {
        val sectionPinned = 0
        val productPinned = 1
        val tagItem = buildTagItemWithPinned(
            numOfSections = 3,
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot.assertHasPinnedItemInCarousel(
            true,
            buildMockProductName(sectionPinned, productPinned)
        )
    }

    @Test
    fun pinnedProduct_productCarouselView_threeSections_hasPinned_sectionTwo_productOne() {
        val sectionPinned = 1
        val productPinned = 0
        val tagItem = buildTagItemWithPinned(
            numOfSections = 3,
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot.assertHasPinnedItemInCarousel(
            true,
            buildMockProductName(sectionPinned, productPinned)
        )
    }

    @Test
    fun pinnedProduct_productCarouselView_threeSections_hasPinned_sectionThree_productThree() {
        val sectionPinned = 2
        val productPinned = 2
        val tagItem = buildTagItemWithPinned(
            numOfSections = 3,
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot.assertHasPinnedItemInCarousel(
            true,
            buildMockProductName(sectionPinned, productPinned)
        )
    }

    @Test
    fun pinnedProduct_productBottomSheet_oneSection_noPinned() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> false })

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot
            .wait(5000)
            .openProductBottomSheet()
            .assertHasPinnedItemInProductBottomSheet(false)
    }

    @Test
    fun pinnedProduct_productBottomSheet_oneSection_hasPinned() {
        val totalSection = 1
        val productPerSection = 5

        val sectionPinned = 0
        val productPinned = 3
        val tagItem = buildTagItemWithPinned(
            numOfSections = totalSection,
            numOfProducts = productPerSection,
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        val position = sectionPinned + 1 + (sectionPinned * productPerSection) + productPinned

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot
            .openProductBottomSheet()
            .scrollProductBottomSheet(position)
            .assertHasPinnedItemInProductBottomSheet(
                buildMockProductName(sectionPinned, productPinned),
                true,
            )
    }

    @Test
    fun pinnedProduct_productBottomSheet_threeSections_hasPinned_sectionThree_productThree() {
        val numOfSections = 3
        val productPerSection = 5

        val sectionPinned = 2
        val productPinned = 2
        val tagItem = buildTagItemWithPinned(
            numOfSections = numOfSections,
            numOfProducts = productPerSection,
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )
        val position = sectionPinned + 1 + (sectionPinned * productPerSection) + productPinned

        coEvery { repo.getTagItem(any(), any(), any()) } returns tagItem

        val robot = createRobot()
        robot
            .openProductBottomSheet()
            .scrollProductBottomSheet(position)
            .assertHasPinnedItemInProductBottomSheet(
                buildMockProductName(sectionPinned, productPinned),
                true,
            )
    }

    private fun createRobot() = PlayActivityRobot(channelId, 3000, isYouTube = true)

    private fun buildTagItemWithPinned(
        numOfSections: Int = 1,
        numOfProducts: Int = 5,
        hasPinned: (Int, Int) -> Boolean,
    ): TagItemUiModel {
        return uiModelBuilder.buildTagItem(
            product = uiModelBuilder.buildProductModel(
                productList = List(numOfSections) { sectionIndex ->
                    uiModelBuilder.buildProductSection(
                        productList = List(numOfProducts) { productIndex ->
                            uiModelBuilder.buildProduct(
                                id = "$sectionIndex, $productIndex",
                                shopId = "2",
                                title = buildMockProductName(sectionIndex, productIndex),
                                stock = StockAvailable(1),
                                price = OriginalPrice("${productIndex}000", productIndex * 1000.0),
                                isPinned = hasPinned(sectionIndex, productIndex),
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
}
