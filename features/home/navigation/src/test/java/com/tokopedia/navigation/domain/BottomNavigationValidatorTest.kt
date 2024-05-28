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
        val homeNavAtFirstPosition = listOf(mockBottomNavBarHome) + List(5) { buildRandomBottomNavBar() }

        Assert.assertTrue(runCatching { validator.validate(homeNavAtFirstPosition) }.isSuccess)

        val homeNavAtLastPosition = List(5) { buildRandomBottomNavBar() } + listOf(mockBottomNavBarHome)

        Assert.assertTrue(runCatching { validator.validate(homeNavAtLastPosition) }.isSuccess)
    }

    @Test
    fun `test model without home nav`() {
        val navBar = List(5) { buildRandomBottomNavBar() }

        Assert.assertFalse(runCatching { validator.validate(navBar) }.isSuccess)
    }

    @Test
    fun `test model with and without image`() {
        val withImage = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(withImage = true))

        Assert.assertTrue(runCatching { validator.validate(withImage) }.isSuccess)

        val withoutImage = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(withImage = false))

        Assert.assertFalse(runCatching { validator.validate(withoutImage) }.isSuccess)
    }

    @Test
    fun `test model with duplicated id`() {
        val randomNavBar = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar())

        Assert.assertTrue(runCatching { validator.validate(randomNavBar) }.isSuccess)

        val multipleRandomNavBar = randomNavBar + randomNavBar

        Assert.assertFalse(runCatching { validator.validate(multipleRandomNavBar) }.isSuccess)
    }

    @Test
    fun `test model with and without jumper`() {
        val withJumper = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(withJumper = true))

        Assert.assertTrue(runCatching { validator.validate(withJumper) }.isSuccess)

        val withoutJumper = listOf(mockBottomNavBarHome) + listOf(buildRandomBottomNavBar(withJumper = false))

        Assert.assertTrue(runCatching { validator.validate(withoutJumper) }.isSuccess)
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
