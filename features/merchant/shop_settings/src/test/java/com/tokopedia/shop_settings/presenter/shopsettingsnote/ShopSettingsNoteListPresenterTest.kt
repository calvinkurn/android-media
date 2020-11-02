package com.tokopedia.shop_settings.presenter.shopsettingsnote

import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopSettingsNoteListPresenterTest: ShopSettingsNoteTestFixture() {

    @Test
    fun `get shop note should be successful`() {
        every {
            getShopNoteUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<ArrayList<ShopNoteModel>>>().onCompleted()
            secondArg<Subscriber<ArrayList<ShopNoteModel>>>().onError(Throwable())
            secondArg<Subscriber<ArrayList<ShopNoteModel>>>().onNext(arrayListOf(ShopNoteModel()))
        }

        shopSettingsNoteListPresenter.getShopNotes()

        verify {
            getShopNoteUseCase.unsubscribe()
            getShopNoteUseCase.execute(any(), any())
        }
    }

    @Test
    fun `delete shop note should be successful`() {
        every {
            deleteShopNoteUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsNoteListPresenter.deleteShopNote("12123")

        verify {
            deleteShopNoteUseCase.unsubscribe()
            deleteShopNoteUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsNoteListPresenter.detachView()

        verify {
            getShopNoteUseCase.unsubscribe()
            deleteShopNoteUseCase.unsubscribe()
        }
    }
}