package com.tokopedia.shop_settings.presenter.shopsettingsnote

import android.text.TextUtils
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel
import com.tokopedia.shop.settings.notes.data.ShopNoteUiModel
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopSettingsNoteAddEditPresenterTest: ShopSettingsNoteTestFixture() {

    @Test
    fun `add shop note should be successful`() {
        every {
            addShopNoteUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("note")
        }

        val note = ShopNoteModel(
                "1000",
                "noname",
                "full of history",
                false,
                3,
                "http://www.noname.com",
                "",
                ""
        )
        val shopNoteViewModel = ShopNoteUiModel(note)
        shopSettingsNoteAddEditPresenter.saveNote(shopNoteViewModel, false)

        verify {
            addShopNoteUseCase.execute(any(), any())
        }
    }

    @Test
    fun `edit shop note should be successful`() {
        every {
            updateShopNoteUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("note")
        }

        val note = ShopNoteModel(
                "1000",
                "noname",
                "full of history",
                false,
                3,
                "http://www.noname.com",
                "",
                ""
        )
        val shopNoteViewModel = ShopNoteUiModel(note)
        shopSettingsNoteAddEditPresenter.saveNote(shopNoteViewModel, true)

        verify {
            updateShopNoteUseCase.execute(any(), any())
        }
    }

    @Test
    fun `when title and content shop note should return`() {
        val testEmpty = ""
        val testFilled = "test"

        every {
            TextUtils.isEmpty(testEmpty)
        } returns false

        every {
            TextUtils.isEmpty(testFilled)
        } returns true

        var note = ShopNoteModel(
                "1000",
                testFilled,
                testFilled,
                false,
                3,
                "http://www.noname.com",
                "",
                ""
        )
        var shopNoteViewModel = ShopNoteUiModel(note)
        shopSettingsNoteAddEditPresenter.saveNote(shopNoteViewModel, false)

        note = ShopNoteModel(
                "1000",
                testEmpty,
                testFilled,
                false,
                3,
                "http://www.noname.com",
                "",
                ""
        )
        shopNoteViewModel = ShopNoteUiModel(note)
        shopSettingsNoteAddEditPresenter.saveNote(shopNoteViewModel, false)

        note = ShopNoteModel(
                "1000",
                testFilled,
                testEmpty,
                false,
                3,
                "http://www.noname.com",
                "",
                ""
        )
        shopNoteViewModel = ShopNoteUiModel(note)
        shopSettingsNoteAddEditPresenter.saveNote(shopNoteViewModel, false)

        note = ShopNoteModel(
                "1000",
                testEmpty,
                testEmpty,
                false,
                3,
                "http://www.noname.com",
                "",
                ""
        )
        shopNoteViewModel = ShopNoteUiModel(note)
        shopSettingsNoteAddEditPresenter.saveNote(shopNoteViewModel, false)

        verify {
            TextUtils.isEmpty(testFilled)
            TextUtils.isEmpty(testEmpty)
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsNoteAddEditPresenter.detachView()

        verify {
            addShopNoteUseCase.unsubscribe()
            updateShopNoteUseCase.unsubscribe()
        }
    }
}