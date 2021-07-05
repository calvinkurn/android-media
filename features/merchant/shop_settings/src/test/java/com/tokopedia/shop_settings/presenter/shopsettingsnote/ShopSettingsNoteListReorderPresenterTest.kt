package com.tokopedia.shop_settings.presenter.shopsettingsnote

import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopSettingsNoteListReorderPresenterTest: ShopSettingsNoteTestFixture() {

    @Test
    fun `reorder shop note should be successful`() {
        every {
            reorderShopNoteUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsNoteListReorderPresenter.reorderShopNotes(arrayListOf(""))

        verify {
            reorderShopNoteUseCase.unsubscribe()
            reorderShopNoteUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsNoteListReorderPresenter.detachView()

        verify {
            reorderShopNoteUseCase.unsubscribe()
        }
    }
}