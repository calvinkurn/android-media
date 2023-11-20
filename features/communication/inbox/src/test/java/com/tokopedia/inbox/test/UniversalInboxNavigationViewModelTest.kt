package com.tokopedia.inbox.test

import android.content.Intent
import app.cash.turbine.test
import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.view.UniversalInboxAction
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class UniversalInboxNavigationViewModelTest : UniversalInboxViewModelTestFixture() {
    @Test
    fun `navigate with intent, update state intent`() {
        runTest {
            // Given
            val expectedIntent = mockk<Intent>()

            viewModel.inboxNavigationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When navigate with intent
                viewModel.processAction(UniversalInboxAction.NavigateWithIntent(expectedIntent))
                // Then new state
                Assert.assertEquals(expectedIntent, awaitItem().intent)

                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    fun `navigate with applink, update state applink`() {
        runTest {
            // Given
            val expectedApplink = "tokopedia://topchat"

            viewModel.inboxNavigationUiState.test {
                // When initial state
                viewModel.setupViewModelObserver()

                // When navigate with applink
                viewModel.processAction(UniversalInboxAction.NavigateToPage(expectedApplink))
                // Then new state
                Assert.assertEquals(expectedApplink, awaitItem().applink)

                cancelAndConsumeRemainingEvents()
            }
        }
    }
}
