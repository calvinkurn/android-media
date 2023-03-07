package com.tokopedia.play.viewmodel.featuredproducts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.model.UiModelBuilder
import com.tokopedia.play.robot.play.createPlayViewModelRobot
import com.tokopedia.play.util.assertEqualTo
import com.tokopedia.play.view.uimodel.recom.tagitem.TagItemUiModel
import com.tokopedia.unit.test.rule.CoroutineTestRule
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Created by kenny.hadisaputra on 08/08/22
 */
class PlayFeaturedProductsTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()
    private val testDispatcher = coroutineTestRule.dispatchers

    private val modelBuilder = UiModelBuilder.get()

    private val mockRepo = mockk<PlayViewerRepository>(relaxed = true)

    @Test
    fun `test 1 - featured products from one section, no pinned`() {
        val maxFeatured = 5
        val productList = List(15) {
            modelBuilder.buildProduct(id = it.toString())
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                productList.take(5)
            )
        }
    }

    @Test
    fun `test 2 - featured products from one section, no pinned`() {
        val maxFeatured = 5
        val productList = List(3) {
            modelBuilder.buildProduct(id = it.toString())
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                productList.take(3)
            )
        }
    }

    @Test
    fun `test 3 - featured products from one section, with pinned position inside original featured products`() {
        val maxFeatured = 5
        val productList = List(15) {
            modelBuilder.buildProduct(id = it.toString(), isPinned = it == 0)
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                productList.take(5)
            )
        }
    }

    @Test
    fun `test 4 - featured products from one section, with pinned position inside original featured products`() {
        val maxFeatured = 5
        val productList = List(3) {
            modelBuilder.buildProduct(id = it.toString(), isPinned = it == 0)
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                productList.take(3)
            )
        }
    }

    @Test
    fun `test 5 - featured products from one section, with pinned position inside original featured products`() {
        val maxFeatured = 5
        val pinnedPosition = 3
        val productList = List(15) {
            modelBuilder.buildProduct(id = it.toString(), isPinned = it == pinnedPosition)
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                listOf(productList[pinnedPosition]) + productList.take(5).filterNot { product -> product.isPinned }
            )
        }
    }

    @Test
    fun `test 6 - featured products from one section, with pinned position inside original featured products`() {
        val maxFeatured = 5
        val pinnedPosition = 3
        val productList = List(4) {
            modelBuilder.buildProduct(id = it.toString(), isPinned = it == pinnedPosition)
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                listOf(productList[pinnedPosition]) + productList.take(4).filterNot { product -> product.isPinned }
            )
        }
    }

    @Test
    fun `test 7 - featured products from one section, with pinned position outside original featured products`() {
        val maxFeatured = 5
        val pinnedPosition = 8
        val productList = List(15) {
            modelBuilder.buildProduct(id = it.toString(), isPinned = it == pinnedPosition)
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = listOf(
                        modelBuilder.buildProductSection(
                            productList = productList
                        )
                    )
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                listOf(productList[pinnedPosition]) + productList.take(5)
            )
        }
    }

    @Test
    fun `test 8 - featured products from 2 sections, no pinned`() {
        val maxFeatured = 5
        val sectionList = List(2) { section ->
            modelBuilder.buildProductSection(
                productList = List(15) {
                    modelBuilder.buildProduct(id = it.toString())
                }
            )
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = sectionList
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                sectionList.flatMap { section -> section.productList }.take(5)
            )
        }
    }

    @Test
    fun `test 9 - featured products from 2 sections, no pinned`() {
        val maxFeatured = 20
        val sectionList = List(2) { section ->
            modelBuilder.buildProductSection(
                productList = List(15) {
                    modelBuilder.buildProduct(id = it.toString())
                }
            )
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = sectionList
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                sectionList.flatMap { section -> section.productList }.take(20)
            )
        }
    }

    @Test
    fun `test 10 - featured products from 2 sections, with pinned position inside original featured products at section 1`() {
        val maxFeatured = 5
        val pinnedSection = 0
        val pinnedPosition = 2
        val sectionList = List(2) { section ->
            modelBuilder.buildProductSection(
                productList = List(15) {
                    modelBuilder.buildProduct(id = it.toString(), isPinned = pinnedSection == section && pinnedPosition == it)
                }
            )
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = sectionList
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                listOf(sectionList[pinnedSection].productList[pinnedPosition]) +
                        sectionList.flatMap { section -> section.productList }
                            .take(5)
                            .filterNot { product -> product.isPinned }
            )
        }
    }

    @Test
    fun `test 11 - featured products from 2 sections, with pinned position inside original featured products at section 2`() {
        val maxFeatured = 20
        val pinnedSection = 1
        val pinnedPosition = 2
        val sectionList = List(2) { section ->
            modelBuilder.buildProductSection(
                productList = List(15) {
                    modelBuilder.buildProduct(id = it.toString(), isPinned = pinnedSection == section && pinnedPosition == it)
                }
            )
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = sectionList
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                listOf(sectionList[pinnedSection].productList[pinnedPosition]) +
                        sectionList.flatMap { section -> section.productList }
                            .take(20)
                            .filterNot { product -> product.isPinned }
            )
        }
    }

    @Test
    fun `test 11 - featured products from 2 sections, with pinned position outside original featured products at section 2`() {
        val maxFeatured = 6
        val pinnedSection = 1
        val pinnedPosition = 2
        val sectionList = List(2) { section ->
            modelBuilder.buildProductSection(
                productList = List(15) {
                    modelBuilder.buildProduct(id = it.toString(), isPinned = pinnedSection == section && pinnedPosition == it)
                }
            )
        }

        setMockRepoTagItems(
            modelBuilder.buildTagItem(
                product = modelBuilder.buildProductModel(
                    productList = sectionList
                ),
                maxFeatured = maxFeatured,
            )
        )

        val robot = createPlayViewModelRobot(
            dispatchers = testDispatcher,
            repo = mockRepo,
        )

        robot.use {
            val state = it.recordState {
                createPage(mockRepo.getChannelData("1")!!)
            }
            state.featuredProducts.assertEqualTo(
                listOf(sectionList[pinnedSection].productList[pinnedPosition]) +
                        sectionList.flatMap { section -> section.productList }
                            .take(6)
                            .filterNot { product -> product.isPinned }
            )
        }
    }

    /**
     * Helper
     */
    private fun setMockRepoTagItems(tagItem: TagItemUiModel) {
        every { mockRepo.getChannelData(any()) } returns modelBuilder.buildChannelData(
            tagItems = tagItem
        )
    }
}