package com.tokopedia.manageaddress.ui.shoplocation.shopaddress

import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.shop.common.graphql.data.shoplocation.ShopLocationModel
import com.tokopedia.shop_settings.presenter.shopsettingsaddress.ShopSettingsAddressTestFixture
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber

@ExperimentalCoroutinesApi
class ShopLocationOldPresenterTest: ShopSettingsAddressTestFixture() {

    @Test
    fun `get shop location should be successful`() {
        every {
            getShopLocationUseCase.execute(any())
        } answers {
            firstArg<Subscriber<List<ShopLocationModel>>>().onCompleted()
            firstArg<Subscriber<List<ShopLocationModel>>>().onError(Throwable())
            firstArg<Subscriber<List<ShopLocationModel>>>().onNext(arrayListOf(ShopLocationModel()))
        }

        shopLocationPresenter.getShopAddress()

        verify {
            getShopLocationUseCase.execute(any())
        }
    }

    @Test
    fun `delete shop location should be successful`() {
        every {
            deleteShopLocationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopLocationPresenter.deleteItem(ShopLocationOldUiModel())

        verify {
            deleteShopLocationUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopLocationPresenter.detachView()

        verify {
            getShopLocationUseCase.unsubscribe()
            deleteShopLocationUseCase.unsubscribe()
        }
    }
}