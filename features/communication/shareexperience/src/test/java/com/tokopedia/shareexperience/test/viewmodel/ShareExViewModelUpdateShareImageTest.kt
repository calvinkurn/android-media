package com.tokopedia.shareexperience.test.viewmodel

import app.cash.turbine.test
import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.ui.ShareExAction
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ShareExViewModelUpdateShareImageTest : ShareExViewModelTestFixture() {
    @Test
    fun `update selected image, get updated image generator state`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = "",
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                listImage = listOf("oldUrl", "newUrl"),
                                imageGenerator = ShareExImageGeneratorPropertyModel(
                                    sourceId = "sourceId",
                                    args = mapOf()
                                )
                            )
                        )
                    )
                )
            )

            viewModel.imageGeneratorModel.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.UpdateShareImage("newUrl"))

                // Then
                val initialValue = awaitItem()
                assertEquals("", initialValue.selectedImageUrl)
                assertEquals(null, initialValue.sourceId)
                assertEquals(null, initialValue.args?.size)

                val updatedValue = awaitItem()
                assertEquals("oldUrl", updatedValue.selectedImageUrl)
                assertEquals("sourceId", updatedValue.sourceId)
                assertEquals(0, updatedValue.args?.size)

                val expectedValue = awaitItem()
                assertEquals("newUrl", expectedValue.selectedImageUrl)
                assertEquals("sourceId", expectedValue.sourceId)
                assertEquals(0, expectedValue.args?.size)

                println(cancelAndConsumeRemainingEvents())
            }
        }
    }

    @Test
    fun `update selected image without image generator param, get updated image generator state`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = "",
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(""),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                listImage = listOf("oldUrl", "newUrl"),
                                imageGenerator = null
                            )
                        )
                    )
                )
            )

            viewModel.imageGeneratorModel.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.UpdateShareImage("newUrl"))

                // Then
                val initialValue = awaitItem()
                assertEquals("", initialValue.selectedImageUrl)
                assertEquals(null, initialValue.sourceId)
                assertEquals(null, initialValue.args?.size)

                val updatedValue = awaitItem()
                assertEquals("oldUrl", updatedValue.selectedImageUrl)
                assertEquals(null, updatedValue.sourceId)
                assertEquals(null, updatedValue.args?.size)

                val expectedValue = awaitItem()
                assertEquals("newUrl", expectedValue.selectedImageUrl)
                assertEquals(null, expectedValue.sourceId)
                assertEquals(null, expectedValue.args?.size)

                println(cancelAndConsumeRemainingEvents())
            }
        }
    }

    @Test
    fun `update selected image without bottomsheet args, get updated image generator state`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = null

            viewModel.imageGeneratorModel.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.UpdateShareImage("newUrl"))

                // Then
                val initialValue = awaitItem()
                assertEquals("", initialValue.selectedImageUrl)
                assertEquals(null, initialValue.sourceId)
                assertEquals(null, initialValue.args?.size)

                val updatedValue = awaitItem()
                assertEquals("newUrl", updatedValue.selectedImageUrl)
                assertEquals(null, updatedValue.sourceId)
                assertEquals(null, updatedValue.args?.size)

                expectNoEvents()
            }
        }
    }
}
