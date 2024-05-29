package com.tokopedia.navigation.domain

import com.tokopedia.navigation.domain.adjustor.BottomNavigationAdjustor
import com.tokopedia.navigation.domain.model.defaultBottomNavModel
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import com.tokopedia.navigation_common.ui.BottomNavBarItemType
import com.tokopedia.navigation_common.ui.BottomNavBarJumper
import com.tokopedia.navigation_common.ui.BottomNavBarUiModel
import com.tokopedia.navigation_common.ui.DiscoId
import com.tokopedia.navigation_common.ui.plus
import org.junit.Assert
import org.junit.Test
import kotlin.random.Random

class BottomNavigationAdjustorTest {

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

    private val adjustor = BottomNavigationAdjustor()

    @Test
    fun `test adjust model if empty`() {
        val adjustedEmptyList = adjustor.adjust(emptyList())
        Assert.assertEquals(defaultBottomNavModel, adjustedEmptyList)
    }

    @Test
    fun `test adjust model if home is not the first index`() {
        val firstModels = List(2) { buildRandomBottomNavBar() }
        val secondModels = List(2) { buildRandomBottomNavBar() }
        val model = firstModels + mockBottomNavBarHome + secondModels
        val adjustedModel = adjustor.adjust(model)
        Assert.assertEquals(
            listOf(mockBottomNavBarHome) + firstModels + secondModels,
            adjustedModel
        )
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
