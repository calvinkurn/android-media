package com.tokopedia.play.uitest.cart

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
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by kenny.hadisaputra on 14/03/23
 */
@RunWith(AndroidJUnit4ClassRunner::class)
@UiTest
class PlayCartUiTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val repo: PlayViewerRepository = mockk(relaxed = true)

    private val uiModelBuilder = UiModelBuilder.get()
    private val userSession = mockk<UserSessionInterface>(relaxed = true)

    private val channelId = "12669"


    init {
        coEvery { userSession.isLoggedIn } returns true

        coEvery { repo.getTagItem(channelId, any(), any()) } returns buildTagItems()

        PlayInjector.set(
            DaggerPlayTestComponent.builder()
                .playTestModule(
                    PlayTestModule(
                        targetContext,
                        userSession = { userSession },
                    )
                )
                .baseAppComponent((targetContext.applicationContext as BaseMainApplication).baseAppComponent)
                .playTestRepositoryModule(PlayTestRepositoryModule(repo = repo))
                .build()
        )
    }

    private fun createRobot() = PlayActivityRobot(channelId, 3000, isYouTube = true)

    @Test
    fun test_cartCount_empty() {
        coEvery { repo.getChannels(any(), any()) } returns buildPagingChannel(true)
        coEvery { repo.getCartCount() } returns 0

        val robot = createRobot()
        robot.openProductBottomSheet()
            .assertShowCartIconInProductBottomSheet(true)
            .assertShowCartCountInProductBottomSheet(false)
    }

    @Test
    fun test_cartCount_notEmpty() {
        coEvery { repo.getChannels(any(), any()) } returns buildPagingChannel(true)

        val cartCount = 3
        coEvery { repo.getCartCount() } returns cartCount

        val robot = createRobot()
        robot.openProductBottomSheet()
            .assertShowCartIconInProductBottomSheet(true)
            .assertShowCartCountInProductBottomSheet(true)
            .assertCartCountInProductBottomSheet(cartCount.toString())
    }

    @Test
    fun test_cartCount_moreThan99() {
        coEvery { repo.getChannels(any(), any()) } returns buildPagingChannel(true)

        val cartCount = 300
        coEvery { repo.getCartCount() } returns cartCount

        val robot = createRobot()
        robot.openProductBottomSheet()
            .assertShowCartIconInProductBottomSheet(true)
            .assertShowCartCountInProductBottomSheet(true)
            .assertCartCountInProductBottomSheet("99+")
    }

    @Test
    fun test_cart_shouldNotBeShown() {
        coEvery { repo.getChannels(any(), any()) } returns buildPagingChannel(false)

        val robot = createRobot()
        robot.openProductBottomSheet()
            .assertShowCartIconInProductBottomSheet(false)
    }

    /**
     * Test Helper
     */


    private fun buildTagItems(
        numOfSections: Int = 1,
        numOfProducts: Int = 5,
    ): TagItemUiModel {
        return uiModelBuilder.buildTagItem(
            product = uiModelBuilder.buildProductModel(
                productList = List(numOfSections) { sectionIndex ->
                    uiModelBuilder.buildProductSection(
                        productList = List(numOfProducts) { productIndex ->
                            uiModelBuilder.buildProduct(
                                id = "$sectionIndex, $productIndex",
                                shopId = "2",
                                title = "Product $productIndex",
                                stock = StockAvailable(1),
                                price = OriginalPrice("${productIndex}000", productIndex * 1000.0),
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

    private fun buildPagingChannel(
        showCart: Boolean = true,
    ) = PagingChannel(
        channelList = listOf(
            uiModelBuilder.buildChannelData(
                id = channelId,
                channelDetail = uiModelBuilder.buildChannelDetail(
                    showCart = showCart,
                ),
                tagItems = buildTagItems(),
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
}
