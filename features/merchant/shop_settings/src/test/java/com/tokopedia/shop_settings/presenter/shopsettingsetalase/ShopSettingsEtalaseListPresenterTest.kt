package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import rx.Subscriber
import java.util.ArrayList

@ExperimentalCoroutinesApi
class ShopSettingsEtalaseListPresenterTest: ShopSettingsEtalaseTestFixture() {

    @Test
    fun `get shop etalase should be successful`() {
        every {
            getShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onCompleted()
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onError(Throwable())
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onNext(arrayListOf(ShopEtalaseModel()))
        }

        shopSettingsEtalaseListPresenter.getShopEtalase()

        verify {
            getShopEtalaseUseCase.unsubscribe()
            getShopEtalaseUseCase.execute(any(), any())
        }
    }

    @Test
    fun `delete shop etalase should be successful`() {
        every {
            deleteShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsEtalaseListPresenter.deleteShopEtalase("12312")

        verify {
            deleteShopEtalaseUseCase.unsubscribe()
            deleteShopEtalaseUseCase.execute(any(), any())
        }
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsEtalaseListPresenter.detachView()

        verify {
            getShopEtalaseUseCase.unsubscribe()
            deleteShopEtalaseUseCase.unsubscribe()
        }
    }

}