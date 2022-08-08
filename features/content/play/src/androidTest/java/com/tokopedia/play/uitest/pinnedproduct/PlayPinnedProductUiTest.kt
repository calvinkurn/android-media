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
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.type.OriginalPrice
import com.tokopedia.play.view.type.StockAvailable
import com.tokopedia.play.view.type.VideoOrientation
import com.tokopedia.play.view.uimodel.recom.PlayGeneralVideoPlayerParams
import com.tokopedia.play.view.uimodel.recom.PlayVideoMetaInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoPlayerUiModel
import com.tokopedia.play.view.uimodel.recom.PlayVideoStreamUiModel
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.play_common.model.PlayBufferControl
import com.tokopedia.play_common.model.result.ResultState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 15/07/22
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class PlayPinnedProductUiTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()

    private val mockChannelStorage = mockk<PlayChannelStateStorage>(relaxed = true)

    private val channelId = "12669"

    init {
        every { mockChannelStorage.getChannelList() } returns listOf(channelId)
        every { mockChannelStorage.getData(any()) } returns uiModelBuilder.buildChannelData(
            id = channelId,
            tagItems = uiModelBuilder.buildTagItem(
                product = uiModelBuilder.buildProductModel(
                    canShow = true,
                )
            ),
            videoMetaInfo = PlayVideoMetaInfoUiModel(
                videoPlayer = PlayVideoPlayerUiModel.General.Incomplete(
                    params = PlayGeneralVideoPlayerParams(
                        videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                        buffer = PlayBufferControl(),
                        lastMillis = null,
                    )
                ),
                videoStream = PlayVideoStreamUiModel(
                    "", VideoOrientation.Vertical, "Video Keren"
                ),
            ),
        )

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(PlayTestModule(targetContext, mockChannelStorage))
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo))
                .build()
        )
    }

    @Test
    fun pinnedProduct_productCarouselView_oneSection_noPinned() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> false })

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
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

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
        robot.assertHasPinnedItemInCarousel(
            true,
            buildMockProductName(sectionPinned, productPinned)
        )
    }

    @Test
    fun pinnedProduct_productCarouselView_threeSections_noPinned() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> false })

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
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

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
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

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
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

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
        robot.assertHasPinnedItemInCarousel(
            true,
            buildMockProductName(sectionPinned, productPinned)
        )
    }

    @Test
    fun pinnedProduct_productBottomSheet_oneSection_noPinned() {
        val tagItem = buildTagItemWithPinned(hasPinned = { _, _ -> false })

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
        robot
            .openProductBottomSheet()
            .assertHasPinnedItemInProductBottomSheet(false)
    }

    @Test
    fun pinnedProduct_productBottomSheet_oneSection_hasPinned() {
        val sectionPinned = 0
        val productPinned = 3
        val tagItem = buildTagItemWithPinned(
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
        robot
            .openProductBottomSheet()
            .assertHasPinnedItemInProductBottomSheet(
                buildMockProductName(sectionPinned, productPinned),
                true,
            )
    }

    @Test
    fun pinnedProduct_productBottomSheet_threeSections_hasPinned_sectionThree_productThree() {
        val sectionPinned = 2
        val productPinned = 2
        val tagItem = buildTagItemWithPinned(
            numOfSections = 3,
            hasPinned = { sectionIndex, productIndex ->
                sectionIndex == sectionPinned && productIndex == productPinned
            }
        )

        coEvery { repo.getTagItem(any(), any()) } returns tagItem

        val robot = PlayActivityRobot(channelId)
        robot
            .openProductBottomSheet()
            .scrollProductBottomSheet(sectionPinned)
            .assertHasPinnedItemInProductBottomSheet(
                buildMockProductName(sectionPinned, productPinned),
                true,
            )
    }

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