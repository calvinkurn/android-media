package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopSettingsEtalaseListReorderPresenterTest: ShopSettingsEtalaseTestFixture() {

    @Test
    fun `reorder shop etalase success should be successful`() {
        every {
            reorderShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsEtalaseListReorderPresenter.reorderShopNotes(arrayListOf())

        verify {
            reorderShopEtalaseUseCase.unsubscribe()
            reorderShopEtalaseUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsEtalaseListReorderPresenter.detachView()

        verify {
            reorderShopEtalaseUseCase.unsubscribe()
        }
    }
}