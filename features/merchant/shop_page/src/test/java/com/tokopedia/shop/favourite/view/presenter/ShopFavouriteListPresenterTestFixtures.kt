package com.tokopedia.shop.favourite.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.favourite.domain.interactor.GetShopFollowerListUseCase
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView
import com.tokopedia.shop.pageheader.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule

open class ShopFavouriteListPresenterTestFixtures {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    protected lateinit var shopFavouriteListView: ShopFavouriteListView
    @RelaxedMockK
    protected lateinit var getShopFollowerListUseCase: GetShopFollowerListUseCase
    @RelaxedMockK
    protected lateinit var gqlGetShopInfoUseCase: GQLGetShopInfoUseCase
    @RelaxedMockK
    protected lateinit var toggleFavouriteShopAndDeleteCacheUseCase: ToggleFavouriteShopAndDeleteCacheUseCase
    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    protected lateinit var shopFavouriteListPresenter: ShopFavouriteListPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        shopFavouriteListPresenter = ShopFavouriteListPresenter(
                getShopFollowerListUseCase,
                gqlGetShopInfoUseCase,
                userSessionInterface,
                toggleFavouriteShopAndDeleteCacheUseCase,
                CoroutineTestDispatchersProvider
        )

        shopFavouriteListPresenter.attachView(shopFavouriteListView)
    }
}