package com.tokopedia.shop_settings.presenter.shopsettingsaddress

import com.tokopedia.shop.settings.address.data.ShopLocationUiModel
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopSettingsAddressAddEditPresenterTest: ShopSettingsAddressTestFixture() {

    @Test
    fun `add shop location should be successful`() {
        every {
            addShopLocationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsAddressAddEditPresenter.saveAddress(ShopLocationUiModel(), true)

        verify {
            addShopLocationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `edit shop location should be successful`() {
        every {
            updateShopLocationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }
        shopSettingsAddressAddEditPresenter.saveAddress(ShopLocationUiModel(), false)

        verify {
            updateShopLocationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsAddressAddEditPresenter.detachView()

        verify {
            addShopLocationUseCase.unsubscribe()
            updateShopLocationUseCase.unsubscribe()
        }
    }
}