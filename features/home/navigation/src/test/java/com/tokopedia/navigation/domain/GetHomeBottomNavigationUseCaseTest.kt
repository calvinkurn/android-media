package com.tokopedia.navigation.domain

import com.tokopedia.navigation.domain.adjustor.BottomNavigationAdjustor
import com.tokopedia.navigation.domain.validator.BottomNavigationValidator
import com.tokopedia.navigation.util.BottomNavBarItemCacheManager
import com.tokopedia.navigation_common.model.bottomnav.GetHomeBottomNavigationResponse
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarJumper
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.plus
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
class GetHomeBottomNavigationUseCaseTest {

    @get:Rule
    val dispatchersRule = UnconfinedTestRule()

    private val mockGqlRepository = MockGraphqlRepository()
    private val cacheManager = mockk<BottomNavBarItemCacheManager>(relaxed = true)

    private val useCase = GetHomeBottomNavigationUseCase(
        mockGqlRepository,
        cacheManager,
        BottomNavigationValidator(),
        BottomNavigationAdjustor()
    )

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `test get data from cache`() = runTest(dispatchersRule.coroutineDispatcher) {
        val cache = List(5) { buildRandomBottomNavBar() }
        coEvery { cacheManager.getBottomNav() } returns cache

        val actual = useCase(GetHomeBottomNavigationUseCase.FromCache(true))
        Assert.assertEquals(cache, actual.items)
    }

    @Test
    fun `test get valid data from network`() = runTest(dispatchersRule.coroutineDispatcher) {
        val response = GetHomeBottomNavigationResponse(
            GetHomeBottomNavigationResponse.Data(
                bottomNavigations = listOf(
                    GetHomeBottomNavigationResponse.BottomNavigation(
                        id = 1L,
                        name = "home",
                        type = "home",
                        imageList = listOf(
                            GetHomeBottomNavigationResponse.Image(
                                type = "selected_icon_light_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "unselected_icon_light_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "selected_icon_dark_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "unselected_icon_dark_mode",
                                imageType = "image"
                            )
                        )
                    ),
                    GetHomeBottomNavigationResponse.BottomNavigation(
                        id = 2L,
                        name = "feed",
                        type = "feed",
                        imageList = listOf(
                            GetHomeBottomNavigationResponse.Image(
                                type = "selected_icon_light_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "unselected_icon_light_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "selected_icon_dark_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "unselected_icon_dark_mode",
                                imageType = "image"
                            )
                        )
                    )
                )
            )
        )
        mockGqlRepository.addResponse(response)
        val expected = listOf(
            BottomNavBarUiModel(
                id = 1,
                title = "home",
                type = BottomNavBarItemType("home"),
                jumper = null,
                assets = mapOf(
                    BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl(""),
                    BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl(""),
                    BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl(""),
                    BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("")
                ),
                discoId = DiscoId.Empty,
                queryParams = ""
            ),
            BottomNavBarUiModel(
                id = 2,
                title = "feed",
                type = BottomNavBarItemType("feed"),
                jumper = null,
                assets = mapOf(
                    BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl(""),
                    BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl(""),
                    BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl(""),
                    BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("")
                ),
                discoId = DiscoId.Empty,
                queryParams = ""
            )
        )

        val actual = runCatching { useCase(GetHomeBottomNavigationUseCase.FromCache(false)) }
        Assert.assertTrue(actual.isSuccess)
        val actualItems = actual.getOrNull()!!.items
        Assert.assertEquals(expected, actualItems)

        coVerify(exactly = 0) { cacheManager.saveBottomNav(actualItems) }

        actual.getOrNull()!!.completeTask()

        coVerify(exactly = 1) { cacheManager.saveBottomNav(actualItems) }
    }

    @Test
    fun `test get invalid data from network`() = runTest(dispatchersRule.coroutineDispatcher) {
        val response = GetHomeBottomNavigationResponse(
            GetHomeBottomNavigationResponse.Data(
                bottomNavigations = listOf(
                    GetHomeBottomNavigationResponse.BottomNavigation(
                        id = 2L,
                        name = "feed",
                        type = "feed",
                        imageList = listOf(
                            GetHomeBottomNavigationResponse.Image(
                                type = "selected_icon_light_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "unselected_icon_light_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "selected_icon_dark_mode",
                                imageType = "image"
                            ),
                            GetHomeBottomNavigationResponse.Image(
                                type = "unselected_icon_dark_mode",
                                imageType = "image"
                            )
                        )
                    )
                )
            )
        )
        mockGqlRepository.addResponse(response)

        val actual = runCatching { useCase(GetHomeBottomNavigationUseCase.FromCache(false)) }
        Assert.assertTrue(actual.isFailure)
        Assert.assertNull(actual.getOrNull())
    }

    private fun buildRandomBottomNavBar(
        withImage: Boolean = true,
        withJumper: Boolean = false
    ): BottomNavBarUiModel {
        val id = Random.nextInt(1, 100)
        return BottomNavBarUiModel(
            id = id,
            title = "Random NavBar $id",
            jumper = if (withJumper) {
                BottomNavBarJumper(
                    id = 1,
                    title = "jumper",
                    assets = mapOf(
                        BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("navbar $id img light"),
                        BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("navbar $id img light"),
                        BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("navbar $id img dark"),
                        BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("navbar $id img dark")
                    )
                )
            } else {
                null
            },
            assets = if (withImage) {
                mapOf(
                    BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("navbar $id img light"),
                    BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("navbar $id img light"),
                    BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("navbar $id img dark"),
                    BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("navbar $id img dark")
                )
            } else {
                emptyMap()
            },
            discoId = DiscoId(id.toString()),
            type = BottomNavBarItemType(id.toString()),
            queryParams = id.toString()
        )
    }
}
