package com.tokopedia.shareexperience.test.viewmodel

import app.cash.turbine.test
import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.domain.model.property.ShareExImageGeneratorPropertyModel
import com.tokopedia.shareexperience.domain.model.property.ShareExPropertyModel
import com.tokopedia.shareexperience.ui.ShareExAction
import com.tokopedia.shareexperience.ui.model.ShareExLinkShareUiModel
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ShareExViewModelUpdateShareBodyTest : ShareExViewModelTestFixture() {
    @Test
    fun `update share body, get updated share ui state`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = ShareExBottomSheetArg(
                identifier = "testId",
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg(),
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel(
                        listChip = listOf("first", "second"),
                        listShareProperty = listOf(
                            ShareExPropertyModel(
                                shareId = "testShareId1",
                                title = "first",
                                imageGenerator = null
                            ),
                            ShareExPropertyModel(
                                shareId = "testSharedId2",
                                title = "second",
                                imageGenerator = ShareExImageGeneratorPropertyModel()
                            )
                        )
                    )
                )
            )
            viewModel.bottomSheetUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)
                viewModel.processAction(ShareExAction.UpdateShareBody(1, "second"))

                // Then
                skipItems(1) // initial
                val firstPage = awaitItem()
                println(firstPage)
                assertEquals(
                    "first",
                    (firstPage.uiModelList?.get(2) as ShareExLinkShareUiModel).title
                )
                assertEquals(0, firstPage.chipPosition)

                val secondPage = awaitItem()
                assertEquals(
                    "second",
                    (secondPage.uiModelList?.get(2) as ShareExLinkShareUiModel).title
                )
                assertEquals(1, secondPage.chipPosition)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `update null share body, do nothing`() {
        runTest {
            // Given
            viewModel.bottomSheetArgs = null

            viewModel.bottomSheetUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.UpdateShareBody(1, "second"))

                // Then
                skipItems(1) // initial

                expectNoEvents()
            }
        }
    }
}
