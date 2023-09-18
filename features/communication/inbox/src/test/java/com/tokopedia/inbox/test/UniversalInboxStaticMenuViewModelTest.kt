package com.tokopedia.inbox.test

import com.tokopedia.inbox.base.UniversalInboxViewModelTestFixture
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSeparatorUiModel
import io.mockk.every
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UniversalInboxStaticMenuViewModelTest : UniversalInboxViewModelTestFixture() {
    @Test
    fun `should always give static menu after generate`() {
        runBlocking {
            // Given
            val dummyStaticMenu = listOf(UniversalInboxMenuSeparatorUiModel())
            every {
                inboxMenuMapper.getStaticMenu(any())
            } returns dummyStaticMenu

            // When
            viewModel.generateStaticMenu()

            // Then
            Assert.assertEquals(
                dummyStaticMenu,
                viewModel.inboxMenu.value
            )
        }
    }
}
