package com.tokopedia.topchat.chatroom.view.presenter

import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import io.mockk.verify
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TopChatRoomPresenterTest : BaseTopChatRoomPresenterTest() {

    @Test
    fun `on detachView`() {
        // When
        presenter.detachView()

        // Then
        verify {
            getTemplateChatRoomUseCase.unsubscribe()
        }
    }

    @Test
    fun `onGoingStockUpdate added`() {
        // Given
        val productId = "123"
        val product = ProductAttachmentUiModel.Builder().build()

        // When
        presenter.addOngoingUpdateProductStock(productId, product, 0, null)

        // Then
        assertThat(presenter.onGoingStockUpdate.containsKey(productId), `is`(true))
        assertThat(presenter.onGoingStockUpdate.size, `is`(1))
    }

}