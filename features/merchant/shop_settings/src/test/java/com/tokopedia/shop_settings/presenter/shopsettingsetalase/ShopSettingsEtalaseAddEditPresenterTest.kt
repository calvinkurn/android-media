package com.tokopedia.shop_settings.presenter.shopsettingsetalase

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseUiModel
import com.tokopedia.shop.settings.etalase.view.listener.ShopSettingsEtalaseAddEditView
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import rx.Subscriber
import java.util.ArrayList


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ShopSettingsEtalaseAddEditPresenterTest: ShopSettingsEtalaseTestFixture() {

    @Test
    fun `add etalase should be successful`() {
        every {
            addShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsEtalaseAddEditPresenter.saveEtalase(ShopEtalaseUiModel())

        verify {
            addShopEtalaseUseCase.execute(any(), any())
        }
    }

    @Test
    fun `update etalase should be successful`() {
        every {
            updateShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<String>>().onCompleted()
            secondArg<Subscriber<String>>().onError(Throwable())
            secondArg<Subscriber<String>>().onNext("success")
        }

        shopSettingsEtalaseAddEditPresenter.saveEtalase(ShopEtalaseUiModel(), true)

        verify {
            updateShopEtalaseUseCase.execute(any(), any())
        }
    }

    @Test
    fun `get etalase list with filled view and should be successful`() {
        val view: ShopSettingsEtalaseAddEditView? = ShopSettingsEtalaseAddEditImplViewTest()
        shopSettingsEtalaseAddEditPresenter.attachView(view)

        every {
            getShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onCompleted()
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onError(Throwable())
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onNext(arrayListOf(ShopEtalaseModel(name = "toko")))
        }

        Assert.assertFalse(shopSettingsEtalaseAddEditPresenter.isEtalaseDuplicate("toko"))

        shopSettingsEtalaseAddEditPresenter.getEtalaseList()

        verify {
            getShopEtalaseUseCase.execute(any(), any())
            view?.showLoading()
        }

        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.listEtalaseModel == arrayListOf(ShopEtalaseModel(name = "toko")))
        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.isEtalaseDuplicate("toko"))
        Assert.assertFalse(shopSettingsEtalaseAddEditPresenter.isEtalaseDuplicate("shop"))
    }

    @Test
    fun `get etalase list with null view should be successful`() {
        shopSettingsEtalaseAddEditPresenter.attachView(null)

        every {
            getShopEtalaseUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onCompleted()
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onError(Throwable())
            secondArg<Subscriber<ArrayList<ShopEtalaseModel>>>().onNext(arrayListOf(ShopEtalaseModel(name = "toko")))
        }

        Assert.assertFalse(shopSettingsEtalaseAddEditPresenter.isEtalaseDuplicate("toko"))

        shopSettingsEtalaseAddEditPresenter.getEtalaseList()

        verify {
            getShopEtalaseUseCase.execute(any(), any())
        }

        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.listEtalaseModel == arrayListOf(ShopEtalaseModel(name = "toko")))
        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.isEtalaseDuplicate("toko"))
        Assert.assertFalse(shopSettingsEtalaseAddEditPresenter.isEtalaseDuplicate("shop"))
    }

    @Test
    fun `when etalase count at max more or less than`() {
        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.etalaseCount == 0)

        shopSettingsEtalaseAddEditPresenter.etalaseCount = 12
        var isEtalaseCountAtMax = shopSettingsEtalaseAddEditPresenter.isEtalaseCountAtMax()
        Assert.assertTrue(isEtalaseCountAtMax)

        shopSettingsEtalaseAddEditPresenter.etalaseCount = 6
        isEtalaseCountAtMax = shopSettingsEtalaseAddEditPresenter.isEtalaseCountAtMax()
        Assert.assertFalse(isEtalaseCountAtMax)
    }

    @Test
    fun `when power merchant is true`() {
        every {
            userSession.isGoldMerchant
        } returns true

        every {
            userSession.isPowerMerchantIdle
        } returns true

        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.isIdlePowerMerchant())
        Assert.assertTrue(shopSettingsEtalaseAddEditPresenter.isPowerMerchant())
    }

    @Test
    fun `detach view should be successful`() {
        shopSettingsEtalaseAddEditPresenter.detachView()

        verify {
            addShopEtalaseUseCase.unsubscribe()
            updateShopEtalaseUseCase.unsubscribe()
        }
    }

}