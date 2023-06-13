package com.tokopedia.play.broadcaster.shorts.viewmodel.preparation

import com.tokopedia.play.broadcaster.shorts.robot.PlayShortsViewModelRobot
import com.tokopedia.play.broadcaster.shorts.view.custom.DynamicPreparationMenu
import com.tokopedia.play.broadcaster.util.assertEqualTo
import com.tokopedia.unit.test.rule.CoroutineTestRule
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Jonathan Darwin on December 06, 2022
 */
class PlayShortsMenuViewModelTest {

    @get:Rule
    val rule: CoroutineTestRule = CoroutineTestRule()

    @Test
    fun playShorts_preparation_menu() {
        val robot = PlayShortsViewModelRobot()

        robot.use {
            val state = it.recordState { }

            state.menuList.size.assertEqualTo(3)
            state.menuList[0].assertEqualTo(DynamicPreparationMenu.createTitle(true))
            state.menuList[1].assertEqualTo(DynamicPreparationMenu.createProduct(true))
            state.menuList[2].assertEqualTo(DynamicPreparationMenu.createCover(false))
        }
    }
}
