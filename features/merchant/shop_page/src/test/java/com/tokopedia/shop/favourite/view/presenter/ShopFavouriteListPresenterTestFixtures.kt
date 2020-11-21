package com.tokopedia.shop.favourite.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.feedcomponent.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.favourite.domain.interactor.GetShopFollowerListUseCase
import com.tokopedia.shop.favourite.view.listener.ShopFavouriteListView
import com.tokopedia.shop.pageheader.domain.interactor.ToggleFavouriteShopAndDeleteCacheUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    private val testCoroutineDispatcherProvider by lazy {
        object : CoroutineDispatcherProvider {
            override val io: CoroutineDispatcher
                get() = Dispatchers.Unconfined

            override val main: CoroutineDispatcher
                get() = Dispatchers.Unconfined

            override val default: CoroutineDispatcher
                get() = Dispatchers.Unconfined
        }
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        shopFavouriteListPresenter = ShopFavouriteListPresenter(
                getShopFollowerListUseCase,
                gqlGetShopInfoUseCase,
                userSessionInterface,
                toggleFavouriteShopAndDeleteCacheUseCase,
                testCoroutineDispatcherProvider)

        shopFavouriteListPresenter.attachView(shopFavouriteListView)
    }
}