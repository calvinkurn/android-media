package com.tokopedia.home_explore_category

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home_explore_category.domain.usecase.GetExploreCategoryUseCase
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryResultUiModel
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryState
import com.tokopedia.home_explore_category.presentation.uimodel.ExploreCategoryUiModel
import com.tokopedia.home_explore_category.presentation.viewmodel.ExploreCategoryViewModel
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.ConnectException

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreCategoryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = UnconfinedTestRule()

    @RelaxedMockK
    lateinit var getExploreCategoryUseCase: GetExploreCategoryUseCase

    private lateinit var viewModel: ExploreCategoryViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = ExploreCategoryViewModel(
            { getExploreCategoryUseCase },
            CoroutineTestDispatchersProvider
        )
    }

    @Test
    fun `when fetchExploreCategory, then should return success`() {
        val exploreCategoryList = listOf(
            ExploreCategoryUiModel(
                id = "1",
                categoryTitle = "Fitur Terkini",
                categoryImageUrl = "https://images.tokopedia.net/img/cache/260/FVBOuz/2023/11/29/4ada4131-8d93-43c9-a9db-02103e56bc8f.png",
                subExploreCategoryList = listOf(
                    ExploreCategoryUiModel.SubExploreCategoryUiModel(
                        id = "659",
                        name = "Kamera Terkini",
                        imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
                        appLink = "tokopedia://home?source=semua-kategori-page.fitur-terkini.kamera-terkini.659",
                        categoryLabel = "",
                        buIdentifier = ""
                    ),
                    ExploreCategoryUiModel.SubExploreCategoryUiModel(
                        id = "661",
                        name = "Kamera Terkini 2",
                        imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
                        appLink = "tokopedia://home?source=semua-kategori-page.fitur-terkini.kamera-terkini.659",
                        categoryLabel = "",
                        buIdentifier = ""
                    )
                )
            )
        )

        val exploreCategoryResultUiModel = ExploreCategoryResultUiModel(exploreCategoryList)

        coEvery {
            getExploreCategoryUseCase.execute()
        } returns exploreCategoryResultUiModel

        assertCollectingExploreCategoryState {
            assertTrue(it.first() is ExploreCategoryState.Loading)
        }

        viewModel.fetchExploreCategory()

        assertCollectingExploreCategoryState {
            assertTrue(it.first() is ExploreCategoryState.Success)

            val actualResult = (it.first() as ExploreCategoryState.Success).data.exploreCategoryList
            assertEquals(exploreCategoryList, actualResult)
        }
    }

    @Test
    fun `when fetchExploreCategory, then should return fail`() {
        val connectException = ConnectException("something went wrong")

        coEvery {
            getExploreCategoryUseCase.execute()
        } throws connectException

        assertCollectingExploreCategoryState {
            assertTrue(it.first() is ExploreCategoryState.Loading)
        }

        viewModel.fetchExploreCategory()

        assertCollectingExploreCategoryState {
            assertTrue(it.first() is ExploreCategoryState.Fail)

            val actualResult = (it.first() as ExploreCategoryState.Fail).throwable
            assertEquals(connectException.localizedMessage, actualResult.cause?.localizedMessage)

            val categoryId = "2"

            viewModel.toggleSelectedCategory(categoryId)

            // assert nothing
        }
    }

    @Test
    fun `when toggleSelectedCategory, then should return select category item is selected`() {
        val categoryId = "2"

        val exploreCategoryList = listOf(
            ExploreCategoryUiModel(
                id = "1",
                categoryTitle = "Fitur Terkini",
                categoryImageUrl = "https://images.tokopedia.net/img/cache/260/FVBOuz/2023/11/29/4ada4131-8d93-43c9-a9db-02103e56bc8f.png",
                subExploreCategoryList = listOf(
                    ExploreCategoryUiModel.SubExploreCategoryUiModel(
                        id = "659",
                        name = "Kamera Terkini",
                        imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
                        appLink = "tokopedia://home?source=semua-kategori-page.fitur-terkini.kamera-terkini.659",
                        categoryLabel = "",
                        buIdentifier = ""
                    ),
                    ExploreCategoryUiModel.SubExploreCategoryUiModel(
                        id = "661",
                        name = "Kamera Terkini 2",
                        imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
                        appLink = "tokopedia://home?source=semua-kategori-page.fitur-terkini.kamera-terkini.659",
                        categoryLabel = "",
                        buIdentifier = ""
                    )
                )
            ),
            ExploreCategoryUiModel(
                id = "2",
                categoryTitle = "Fitur Terkini 2",
                categoryImageUrl = "https://images.tokopedia.net/img/cache/260/FVBOuz/2023/11/29/4ada4131-8d93-43c9-a9db-02103e56bc8f.png",
                subExploreCategoryList = listOf(
                    ExploreCategoryUiModel.SubExploreCategoryUiModel(
                        id = "660",
                        name = "Kamera Terkini 2",
                        imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
                        appLink = "tokopedia://home?source=semua-kategori-page.fitur-terkini.kamera-terkini.659",
                        categoryLabel = "",
                        buIdentifier = ""
                    ),
                    ExploreCategoryUiModel.SubExploreCategoryUiModel(
                        id = "663",
                        name = "Kamera Terkini 4",
                        imageUrl = "https://images.tokopedia.net/img/MIPuRC/2023/11/29/115eec51-442d-4454-b2e2-6a8528c36987.png",
                        appLink = "tokopedia://home?source=semua-kategori-page.fitur-terkini.kamera-terkini.659",
                        categoryLabel = "",
                        buIdentifier = ""
                    )
                )
            )
        )

        val exploreCategoryResultUiModel = ExploreCategoryResultUiModel(exploreCategoryList)

        coEvery {
            getExploreCategoryUseCase.execute()
        } returns exploreCategoryResultUiModel

        assertCollectingExploreCategoryState {
            assertTrue(it.first() is ExploreCategoryState.Loading)
        }

        viewModel.fetchExploreCategory()

        coEvery {
            getExploreCategoryUseCase.execute()
        } returns exploreCategoryResultUiModel

        assertCollectingExploreCategoryState {
            assertTrue(it.first() is ExploreCategoryState.Success)

            viewModel.toggleSelectedCategory(categoryId)

            val actualResult = (it[1] as ExploreCategoryState.Success).data.exploreCategoryList
            val exploreCategory = actualResult.find { it.id == categoryId }

            assertEquals(true, exploreCategory?.isSelected)
        }
    }

    private fun assertCollectingExploreCategoryState(block: (List<ExploreCategoryState<ExploreCategoryResultUiModel>>) -> Unit) {
        val scope = CoroutineScope(rule.dispatchers.coroutineDispatcher)
        val testCollectorList =
            mutableListOf<ExploreCategoryState<ExploreCategoryResultUiModel>>()
        val uiStateCollectorJob = scope.launch {
            viewModel.exploreCategoryState.toList(testCollectorList)
        }
        block.invoke(testCollectorList)
        uiStateCollectorJob.cancel()
    }
}
