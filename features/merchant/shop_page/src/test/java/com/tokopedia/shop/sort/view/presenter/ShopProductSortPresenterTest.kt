package com.tokopedia.shop.sort.view.presenter

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.shop.sort.data.source.cloud.model.ShopProductSort
import com.tokopedia.shop.sort.domain.interactor.GetShopProductSortUseCase
import com.tokopedia.shop.sort.view.mapper.ShopProductSortMapper
import com.tokopedia.shop.sort.view.model.ShopProductSortModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Subscriber

class ShopProductSortPresenterTest {

    lateinit var shopProductSortPresenter: ShopProductSortPresenter

    @RelaxedMockK
    lateinit var getShopProductFilterUseCase: GetShopProductSortUseCase
    @RelaxedMockK
    lateinit var  shopProductMapper: ShopProductSortMapper
    @RelaxedMockK
    lateinit var  userSession: UserSessionInterface
    @RelaxedMockK
    lateinit var shopProductSortView: BaseListViewListener<ShopProductSortModel>

    @Before
    fun setup(){
        MockKAnnotations.init(this)
        shopProductSortPresenter = ShopProductSortPresenter(
                getShopProductFilterUseCase,
                shopProductMapper,
                userSession
        )
        shopProductSortPresenter.attachView(shopProductSortView)

    }

    @Test
    fun `check whether renderList should be called when success`(){
        every { getShopProductFilterUseCase.execute(any()) } answers {
            (firstArg() as Subscriber<List<ShopProductSort>>).onNext(listOf())
        }
        shopProductSortPresenter.getShopFilterList()
        verify { getShopProductFilterUseCase.execute(any()) }
        verify { shopProductSortView.renderList(any()) }
    }

    @Test
    fun `check whether showGetListError should be called when error`(){
        every { getShopProductFilterUseCase.execute(any()) } answers {
            (firstArg() as Subscriber<List<ShopProductSort>>).onError(Throwable())
        }
        shopProductSortPresenter.getShopFilterList()
        verify { getShopProductFilterUseCase.execute(any()) }
        verify { shopProductSortView.showGetListError(any()) }
    }

    @Test
    fun `check whether isMyShop return true if matched given shopId`(){
        val mockShopId = "123"
        every { userSession.shopId } returns mockShopId
        assert(shopProductSortPresenter.isMyShop(mockShopId))
    }

    @Test
    fun `check whether isMyShop return false if unmatched given shopId`(){
        val mockShopId = "123"
        every { userSession.shopId } returns mockShopId
        assert(!shopProductSortPresenter.isMyShop("5123"))
    }

    @Test
    fun `check whether required function is called when detachView`(){
        shopProductSortPresenter.detachView()
        verify { getShopProductFilterUseCase.unsubscribe() }
    }

}