package com.tokopedia.topchat.chatroom.view.presenter

import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

class TopChatRoomPresenterTestCont : BaseTopChatRoomPresenterTest() {

    @Test
    fun `On success get chat template`() {
        // Given
        val slot = slot<Subscriber<GetTemplateUiModel>>()
        every {
            getTemplateChatRoomUseCase.execute(
                any(),
                capture(slot)
            )
        } answers {
            val subs = slot.captured
            subs.onNext(GetTemplateUiModel())
        }

        // When
        presenter.getTemplate(true)

        // Then
        verify(exactly = 1) { view.onSuccessGetTemplate(emptyList()) }
    }

    @Test
    fun `On error get chat template`() {
        // Given
        val slot = slot<Subscriber<GetTemplateUiModel>>()
        every {
            getTemplateChatRoomUseCase.execute(
                any(),
                capture(slot)
            )
        } answers {
            val subs = slot.captured
            subs.onError(Throwable())
        }

        // When
        presenter.getTemplate(true)

        // Then
        verify(exactly = 1) { view.onErrorGetTemplate() }
    }

}