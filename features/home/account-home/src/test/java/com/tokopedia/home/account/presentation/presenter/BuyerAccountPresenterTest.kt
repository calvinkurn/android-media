package com.tokopedia.home.account.presentation.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase
import com.tokopedia.home.account.presentation.BuyerAccount
import com.tokopedia.home.account.presentation.subscriber.GetBuyerAccountSubscriber
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import rx.Subscriber

@ExperimentalCoroutinesApi
class BuyerAccountPresenterTest {

    @RelaxedMockK
    lateinit var getBuyerAccountUseCase: GetBuyerAccountUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var topAdsWishlishedUseCase: TopAdsWishlishedUseCase

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    @RelaxedMockK
    lateinit var userSession: UserSessionInterface

    @RelaxedMockK
    lateinit var view: BuyerAccount.View

    private val buyerAccountPresenter by lazy {
        BuyerAccountPresenter(
                getBuyerAccountUseCase,
                getRecommendationUseCase,
                topAdsWishlishedUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                userSession
        )
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        buyerAccountPresenter.attachView(view)
    }

    /**
     * get buyer data
     * */
    @Test
    fun `get buyer data | on success`() {
        val buyerViewModel = BuyerViewModel()
        every {
            getBuyerAccountUseCase.execute(any(), any())
        } answers {
            secondArg<GetBuyerAccountSubscriber>().onStart()
            secondArg<GetBuyerAccountSubscriber>().onCompleted()
            secondArg<GetBuyerAccountSubscriber>().onNext(buyerViewModel)
        }

        buyerAccountPresenter.getBuyerData(anyString(), anyString(), anyString())

        verify {
            getBuyerAccountUseCase.execute(any(), any())
        }

        verify {
            view.loadBuyerData(buyerViewModel)
            view.hideLoading()
        }
    }

    @Test
    fun `get buyer data | on error`() {
        every {
            getBuyerAccountUseCase.execute(any(), any())
        } answers {
            secondArg<GetBuyerAccountSubscriber>().onStart()
            secondArg<GetBuyerAccountSubscriber>().onCompleted()
            secondArg<GetBuyerAccountSubscriber>().onError(Throwable())
        }

        buyerAccountPresenter.getBuyerData(anyString(), anyString() , anyString())

        verify {
            getBuyerAccountUseCase.execute(any(), any())
        }

        verify {
            view.loadBuyerData(null)
            view.hideLoading()
        }
    }
    
    /**
     * Recommendation widget
     * */
    @Test
    fun `get first recommendation data | on success`() {
        val listOfRecommendationWidget = arrayListOf(RecommendationWidget(), RecommendationWidget())

        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onNext(listOfRecommendationWidget)
        }

        buyerAccountPresenter.getFirstRecomData()

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            view.hideLoadMoreLoading()
            view.onRenderRecomAccountBuyer(any())
        }
    }

    @Test
    fun `get first recommendation data | on fail`() {
        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onError(Throwable())
        }

        buyerAccountPresenter.getFirstRecomData()

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            view.hideLoadMoreLoading()
        }
    }

    @Test
    fun `get recommendation data | on success`() {
        val page = 0
        val listOfRecommendationWidget = arrayListOf(RecommendationWidget(), RecommendationWidget())

        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onNext(listOfRecommendationWidget)
        }

        buyerAccountPresenter.getRecomData(page)

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            view.hideLoadMoreLoading()
            view.onRenderRecomAccountBuyer(any())
        }
    }

    @Test
    fun `get recommendation data | on fail`() {
        val page = 0
        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onError(Throwable())
        }

        buyerAccountPresenter.getRecomData(page)

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            view.hideLoadMoreLoading()
        }
    }

    /**
     * WisList
     * */
    @Test
    fun `add top ads wishList | on success`() {
        val recommendationItem = RecommendationItem(
                isTopAds = true
        )

        val wishListViewModel = WishlistModel()

        every {
            topAdsWishlishedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<WishlistModel>>().onNext(wishListViewModel)
        }

        buyerAccountPresenter.addWishlist(recommendationItem) { b, throwable ->
            Assert.assertTrue(b)
        }

        verify {
            topAdsWishlishedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `add top ads wishList | on fail`() {
        val recommendationItem = RecommendationItem(
                isTopAds = true
        )

        every {
            topAdsWishlishedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<WishlistModel>>().onError(Throwable())
        }

        buyerAccountPresenter.addWishlist(recommendationItem) { b, throwable ->
            Assert.assertFalse(b)
        }

        verify {
            topAdsWishlishedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `add wishList | on success`() {
        val productId = "123"
        val recommendationItem = RecommendationItem(
                isTopAds = false
        )

        every {
            addWishListUseCase.createObservable(any(), any(), any())
        } answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessAddWishlist(productId)
        }

        buyerAccountPresenter.addWishlist(recommendationItem) { b, throwable ->
            Assert.assertTrue(b)
        }

        verify {
            addWishListUseCase.createObservable(any(), any(), any())
        }
    }

    @Test
    fun `add wishList | on fail`() {
        val productId = "123"
        val message = ""
        val recommendationItem = RecommendationItem(
                isTopAds = false
        )

        every {
            addWishListUseCase.createObservable(any(), any(), any())
        } answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList(message, productId)
        }

        buyerAccountPresenter.addWishlist(recommendationItem) { b, throwable ->
            Assert.assertFalse(b)
        }

        verify {
            addWishListUseCase.createObservable(any(), any(), any())
        }
    }

    @Test
    fun `remove wishList | on success`() {
        val productId = "123"
        val recommendationItem = RecommendationItem()

        every {
            removeWishListUseCase.createObservable(any(), any(), any())
        } answers {
            val listener = args[2] as WishListActionListener
            listener.onSuccessRemoveWishlist(productId)
        }

        buyerAccountPresenter.removeWishlist(recommendationItem) { b, throwable ->
            Assert.assertTrue(b)
        }

        verify {
            removeWishListUseCase.createObservable(any(), any(), any())
        }
    }

    @Test
    fun `remove wishList | on fail`() {
        val productId = "123"
        val message = ""
        val recommendationItem = RecommendationItem()

        every {
            removeWishListUseCase.createObservable(any(), any(), any())
        } answers {
            val listener = args[2] as WishListActionListener
            listener.onErrorAddWishList(message, productId)
        }

        buyerAccountPresenter.removeWishlist(recommendationItem) { b, throwable ->
            Assert.assertFalse(b)
        }

        verify {
            removeWishListUseCase.createObservable(any(), any(), any())
        }
    }

    /**
     * user session
     * */
    @Test
    fun `is logged in`() {
        buyerAccountPresenter.isLoggedIn()

        verify {
            userSession.isLoggedIn
        }
    }

    /**
    * On Destroy
    * */
    @Test
    fun `on destroy`() {
        buyerAccountPresenter.detachView()

        verify {
            getBuyerAccountUseCase.unsubscribe()
        }
    }
}