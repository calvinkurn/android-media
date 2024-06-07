package com.tokopedia.navigation.domain

import com.tokopedia.navigation.domain.validator.BottomNavigationValidator
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarJumper
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.plus
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class BottomNavigationValidatorTest {

    private val mockBottomNavBarHome = BottomNavBarUiModel(
        id = 1,
        title = "Home",
        type = BottomNavBarItemType("home"),
        jumper = BottomNavBarJumper(
            id = 1,
            title = "Buat Kamu",
            assets = mapOf(
                BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("home_jumper_img"),
                BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("home_jumper_img"),
                BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("home_jumper_img"),
                BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("home_jumper_img")
            )
        ),
        assets = mapOf(
            BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("home_img"),
            BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Light to BottomNavBarAsset.Type.ImageUrl("home_img"),
            BottomNavBarAsset.Key.ImageActive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("home_img"),
            BottomNavBarAsset.Key.ImageInactive + BottomNavBarAsset.Variant.Dark to BottomNavBarAsset.Type.ImageUrl("home_img")
        ),
        discoId = DiscoId.Empty,
        queryParams = ""
    )

    private val validator = BottomNavigationValidator()

    @Test
    fun `test model with home nav`() {
        val homeNavAtFirstPosition = listOf(mockBottomNavBarHome) + buildRandomBottomNavBars(5)

        Assert.assertTrue(runCatching { validator.validate(homeNavAtFirstPosition) }.isSuccess)

        val homeNavAtLastPosition = buildRandomBottomNavBars(5) + listOf(mockBottomNavBarHome)

        Assert.assertTrue(runCatching { validator.validate(homeNavAtLastPosition) }.isSuccess)
    }

    @Test
    fun `test model without home nav`() {
        val navBar = buildRandomBottomNavBars(5)

        Assert.assertFalse(runCatching { validator.validate(navBar) }.isSuccess)
    }

    @Test
    fun `test model with and without image`() {
        val withImage = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(idFrom = mockBottomNavBarHome.id, withImage = true))

        Assert.assertTrue(runCatching { validator.validate(withImage) }.isSuccess)

        val withoutImage = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(idFrom = mockBottomNavBarHome.id, withImage = false))

        Assert.assertFalse(runCatching { validator.validate(withoutImage) }.isSuccess)
    }

    @Test
    fun `test model with duplicated id`() {
        val randomNavBar = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(idFrom = mockBottomNavBarHome.id))

        Assert.assertTrue(runCatching { validator.validate(randomNavBar) }.isSuccess)

        val multipleRandomNavBar = randomNavBar + randomNavBar

        Assert.assertFalse(runCatching { validator.validate(multipleRandomNavBar) }.isSuccess)
    }

    @Test
    fun `test model with and without jumper`() {
        val withJumper = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(idFrom = mockBottomNavBarHome.id, withJumper = true))

        Assert.assertTrue(runCatching { validator.validate(withJumper) }.isSuccess)

        val withoutJumper = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(idFrom = mockBottomNavBarHome.id, withJumper = false))

        Assert.assertTrue(runCatching { validator.validate(withoutJumper) }.isSuccess)
    }

    private fun buildRandomBottomNavBars(
        count: Int,
        idFrom: Int = 1,
        withImage: Boolean = true,
        withJumper: Boolean = false
    ): List<BottomNavBarUiModel> {
        val bottomNavBars = mutableListOf<BottomNavBarUiModel>()
        while (bottomNavBars.size < count) {
            val model = buildRandomBottomNavBar(idFrom, withImage, withJumper)
            if (bottomNavBars.any { it.id == model.id }) continue
            bottomNavBars.add(model)
        }

        return bottomNavBars
    }

    private fun buildRandomBottomNavBar(
        idFrom: Int = 1,
        withImage: Boolean = true,
        withJumper: Boolean = false
    ): BottomNavBarUiModel {
        val id = Random.nextInt(idFrom, Integer.MAX_VALUE)
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
