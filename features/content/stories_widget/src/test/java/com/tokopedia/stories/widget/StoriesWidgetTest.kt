package com.tokopedia.stories.widget

import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.unit.test.rule.StandardTestRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by kenny.hadisaputra on 23/08/23
 */
@OptIn(ExperimentalCoroutinesApi::class)
class StoriesWidgetTest {

    @get:Rule
    val coroutineTestRule = StandardTestRule()

    private val testDispatcher = coroutineTestRule.dispatchers

    private val modelBuilder = StoriesWidgetModelBuilder()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher.coroutineDispatcher)
    }

    @Test
    fun `test observe stories with no shop id`() = runTest {
        val shopStoriesStates = List(5) {
            modelBuilder.buildStoriesWidgetState(
                shopId = it.toString(),
            )
        }
        val repo = StoriesWidgetFakeRepository(
            forbiddenEntryPoints = listOf(StoriesEntryPoint.ProductDetail)
        )
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(emptyList())).waitUntilIdle()

        viewModel.stories.createHelper()
            .onValues {
                Assertions
                    .assertThat(it.last())
                    .isEmpty()
            }
    }

    @Test
    fun `test observe stories for specific entry point`() = runTest {
        val shopStoriesStates = List(5) {
            modelBuilder.buildStoriesWidgetState(
                shopId = it.toString(),
            )
        }
        val repo = StoriesWidgetFakeRepository(
            forbiddenEntryPoints = listOf(StoriesEntryPoint.ProductDetail)
        )
        repo.setStoriesWidgetState(shopStoriesStates)

        val shopEPViewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        val shopFlowHelper = shopEPViewModel.stories.createHelper()
        shopFlowHelper.run {
            shopEPViewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1"))).waitUntilIdle()
        }.onValues {
            Assertions.assertThat(it.last().values)
                .containsExactly(*shopStoriesStates.toTypedArray())
        }

        val pdpEPViewModel = StoriesWidgetViewModel(StoriesEntryPoint.ProductDetail, repo)
        val pdpFlowHelper = pdpEPViewModel.stories.createHelper()
        pdpFlowHelper.run {
            pdpEPViewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1"))).waitUntilIdle()
        }.onValues {
            Assertions.assertThat(it.last().values)
                .isEmpty()
        }
    }

    @Test
    fun `test observe specific shop id stories`() = runTest {
        val shopStoriesStates = List(5) {
            modelBuilder.buildStoriesWidgetState(
                shopId = it.toString(),
            )
        }
        val repo = StoriesWidgetFakeRepository()
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1"))).waitUntilIdle()

        viewModel.getStoriesState("0").createHelper()
            .onValues {
                Assertions.assertThat(it.last())
                    .isEqualTo(shopStoriesStates[0])
            }

        viewModel.getStoriesState("1").createHelper()
            .onValues {
                Assertions.assertThat(it.last())
                    .isEqualTo(shopStoriesStates[1])
            }
    }

    @Test
    fun `test update latest stories status`() = runTest {
        val shopStoriesStates = listOf(
            modelBuilder.buildStoriesWidgetState(
                shopId = "0",
                status = StoriesStatus.NoStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "1",
                status = StoriesStatus.HasUnseenStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "2",
                status = StoriesStatus.HasUnseenStories
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "3",
                status = StoriesStatus.AllStoriesSeen
            )
        )
        val repo = StoriesWidgetFakeRepository()
        repo.setStoriesWidgetState(shopStoriesStates)
        repo.setSeenStatus("1", true)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1"))).waitUntilIdle()

        val firstStoryHelper = viewModel.getStoriesState("0").createHelper()
        val secondStoryHelper = viewModel.getStoriesState("1").createHelper()
        val thirdStoryHelper = viewModel.getStoriesState("2").createHelper()
        val fourthStoryHelper = viewModel.getStoriesState("3").createHelper()

        firstStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[0])
        }
        secondStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[1])
        }
        thirdStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[2])
        }
        fourthStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[3])
        }

        viewModel.onIntent(StoriesWidgetIntent.GetLatestStoriesStatus).waitUntilIdle()

        firstStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[0])
        }
        secondStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[1].copy(status = StoriesStatus.AllStoriesSeen))
        }
        thirdStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[2])
        }
        fourthStoryHelper.onValues {
            Assertions.assertThat(it.last())
                .isEqualTo(shopStoriesStates[3])
        }
    }

    @Test
    fun `test calling show CoachMark if there is unseen stories and has not been shown`() = runTest {
        val shopStoriesStates = listOf(
            modelBuilder.buildStoriesWidgetState(
                shopId = "0",
                status = StoriesStatus.NoStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "1",
                status = StoriesStatus.HasUnseenStories
            )
        )
        val repo = StoriesWidgetFakeRepository(initialHasSeenCoachMark = false)
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1")))
        viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark).waitUntilIdle()
        viewModel.uiMessage.createHelper()
            .onValues {
                Assertions.assertThat(it)
                    .hasAtLeastOneElementOfType(StoriesWidgetMessage.ShowCoachMark::class.java)

                Assertions.assertThat(it.last())
                    .isEqualTo(StoriesWidgetMessage.ShowCoachMark("1"))
            }
    }

    @Test
    fun `test calling show CoachMark if there is unseen stories but it has been shown previously`() = runTest {
        val shopStoriesStates = listOf(
            modelBuilder.buildStoriesWidgetState(
                shopId = "0",
                status = StoriesStatus.NoStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "1",
                status = StoriesStatus.HasUnseenStories
            )
        )
        val repo = StoriesWidgetFakeRepository(initialHasSeenCoachMark = true)
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1")))
        viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark).waitUntilIdle()
        viewModel.uiMessage.createHelper()
            .onValues {
                Assertions.assertThat(it)
                    .doesNotHaveSameClassAs(StoriesWidgetMessage.ShowCoachMark(""))
            }
    }

    @Test
    fun `test calling show CoachMark if there is no unseen stories and has not been shown`() = runTest {
        val shopStoriesStates = listOf(
            modelBuilder.buildStoriesWidgetState(
                shopId = "0",
                status = StoriesStatus.NoStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "1",
                status = StoriesStatus.AllStoriesSeen
            )
        )
        val repo = StoriesWidgetFakeRepository(initialHasSeenCoachMark = false)
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1")))
        viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark).waitUntilIdle()
        viewModel.uiMessage.createHelper()
            .onValues {
                Assertions.assertThat(it)
                    .doesNotHaveSameClassAs(StoriesWidgetMessage.ShowCoachMark(""))
            }
    }

    @Test
    fun `test calling show CoachMark if there is no unseen stories but has previously been shown`() = runTest {
        val shopStoriesStates = listOf(
            modelBuilder.buildStoriesWidgetState(
                shopId = "0",
                status = StoriesStatus.NoStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "1",
                status = StoriesStatus.AllStoriesSeen
            )
        )
        val repo = StoriesWidgetFakeRepository(initialHasSeenCoachMark = true)
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1")))
        viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark).waitUntilIdle()
        viewModel.uiMessage.createHelper()
            .onValues {
                Assertions.assertThat(it)
                    .doesNotHaveSameClassAs(StoriesWidgetMessage.ShowCoachMark(""))
            }
    }

    @Test
    fun `test calling set has seen CoachMark`() = runTest {
        val shopStoriesStates = listOf(
            modelBuilder.buildStoriesWidgetState(
                shopId = "0",
                status = StoriesStatus.NoStories,
            ),
            modelBuilder.buildStoriesWidgetState(
                shopId = "1",
                status = StoriesStatus.HasUnseenStories
            )
        )
        val repo = StoriesWidgetFakeRepository()
        repo.setStoriesWidgetState(shopStoriesStates)

        val viewModel = StoriesWidgetViewModel(StoriesEntryPoint.ShopPage, repo)
        viewModel.onIntent(StoriesWidgetIntent.GetStoriesStatus(listOf("1")))
        viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark).waitUntilIdle()
        viewModel.uiMessage.createHelper()
            .onValues {
                val lastValue = it.last()
                Assertions.assertThat(lastValue)
                    .isEqualTo(StoriesWidgetMessage.ShowCoachMark("1"))

                viewModel.clearMessage(lastValue!!.id)
            }
            .run {
                viewModel.onIntent(StoriesWidgetIntent.HasSeenCoachMark)
                viewModel.onIntent(StoriesWidgetIntent.ShowCoachMark).waitUntilIdle()
            }.onValues {
                Assertions.assertThat(it.lastOrNull())
                    .isEqualTo(null)
            }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
