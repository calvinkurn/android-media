package com.tokopedia.shareexperience.test.viewmodel

import app.cash.turbine.test
import com.tokopedia.shareexperience.base.ShareExViewModelTestFixture
import com.tokopedia.shareexperience.domain.model.ShareExBottomSheetModel
import com.tokopedia.shareexperience.domain.model.ShareExPageTypeEnum
import com.tokopedia.shareexperience.domain.model.property.ShareExBottomSheetPageModel
import com.tokopedia.shareexperience.ui.ShareExAction
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExBottomSheetResultArg
import com.tokopedia.shareexperience.ui.model.arg.ShareExTrackerArg
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ShareExViewModelInitializePageTest : ShareExViewModelTestFixture() {

    @Test
    fun `initialize page, get share bottom sheet`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                bottomSheetModel = ShareExBottomSheetModel(
                    title = "testTitle",
                    subtitle = "testSubtitle",
                    bottomSheetPage = ShareExBottomSheetPageModel()
                )
            )

            viewModel.bottomSheetUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)

                // Then
                val initialValue = awaitItem()
                assertEquals(initialValue.title, "")
                assertEquals(initialValue.chipPosition, 0)
                assertEquals(initialValue.uiModelList?.size, null)

                val updatedValue = awaitItem()
                assertEquals(updatedValue.title, "testTitle")
                assertEquals(updatedValue.chipPosition, 0)
                assertEquals(updatedValue.uiModelList?.size, 1)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `initialize page, get default share bottom sheet`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = ShareExBottomSheetArg.Builder(
                pageTypeEnum = ShareExPageTypeEnum.PDP,
                defaultUrl = "",
                trackerArg = ShareExTrackerArg("")
            ).withProductId(dummyIdentifier).build()
            viewModel.bottomSheetResultArg = ShareExBottomSheetResultArg(
                throwable = dummyThrowable
            )

            coEvery {
                getSharePropertiesUseCase.getDefaultData("")
            } returns ShareExBottomSheetModel(
                title = "testTitle",
                subtitle = "testSubtitle",
                bottomSheetPage = ShareExBottomSheetPageModel()
            )

            viewModel.bottomSheetUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)

                // Then
                skipItems(1) // skip initial

                val updatedValue = awaitItem()
                assertEquals(updatedValue.title, "testTitle")
                assertEquals(updatedValue.chipPosition, 0)
                assertEquals(updatedValue.uiModelList?.size, 1)

                expectNoEvents()
            }
        }
    }

    @Test
    fun `initialize page, throw error and do nothing`() {
        runTest {
            // Given
            viewModel.bottomSheetArg = null

            viewModel.bottomSheetUiState.test {
                // When
                viewModel.setupViewModelObserver()
                viewModel.processAction(ShareExAction.InitializePage)

                // Then
                skipItems(1) // skip initial

                val updatedValue = awaitItem()
                assertEquals(updatedValue.title, "")
                assertEquals(updatedValue.chipPosition, 0)
                assertEquals(updatedValue.uiModelList?.size, 1) // Error ui

                expectNoEvents()
            }
        }
    }
}
