package com.tokopedia.gamification.giftbox.pdp.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.*
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import rx.Subscriber

@ExperimentalCoroutinesApi
class PdpDialogViewModelTest {
    lateinit var viewModel: PdpDialogViewModel

    val dispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    val recommendationProductUseCase: GamingRecommendationProductUseCase = mockk()
    val addWishListUseCase: AddWishListUseCase = mockk()
    val removeWishListUseCase: RemoveWishListUseCase = mockk()
    val topAdsWishlishedUseCase: TopAdsWishlishedUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)


    }

    private fun getRealViewModel(): PdpDialogViewModel {
        return (PdpDialogViewModel(recommendationProductUseCase, addWishListUseCase, removeWishListUseCase, topAdsWishlishedUseCase, userSessionInterface, dispatcher))
    }

    private fun prepareViewModel() {
        viewModel = getRealViewModel()
    }

    private fun prepareRelaxedViewModel() {
        viewModel = spyk(getRealViewModel())
    }

    @Test
    fun getProductsSuccess() {
        prepareViewModel()

        val pageNumber = 1
        viewModel.useEmptyShopId = false
        viewModel.shopId = "123"
        viewModel.pageName = "giftbox"

        val requestParams: RequestParams = mockk()
        val recommendationWidgetList: List<RecommendationWidget> = arrayListOf(RecommendationWidget())
        val recommendationList: List<Recommendation> = arrayListOf(Recommendation(RecommendationItem(), ProductCardModel()))

        every { recommendationProductUseCase.useEmptyShopId = viewModel.useEmptyShopId } just runs
        every { recommendationProductUseCase.getRequestParams(pageNumber, viewModel.shopId, viewModel.pageName) } returns requestParams
        every { recommendationProductUseCase.getData(requestParams) } returns recommendationWidgetList
        coEvery { recommendationProductUseCase.mapper.recommWidgetToListOfVisitables(recommendationWidgetList[0]) } returns recommendationList

        viewModel.productLiveData.observeForever { }
        viewModel.getProducts(pageNumber)
        Assert.assertEquals(viewModel.productLiveData.value?.status, LiveDataResult.STATUS.SUCCESS)
    }

    @Test
    fun getProductsFail() {
        prepareViewModel()
        val pageNumber = 1
        viewModel.useEmptyShopId = true
        viewModel.shopId = "123"
        viewModel.pageName = "giftbox"

        val requestParams: RequestParams = mockk()
        val recommendationWidgetList: List<RecommendationWidget> = arrayListOf(RecommendationWidget())
        val recommendationList: List<Recommendation> = arrayListOf()

        every { recommendationProductUseCase.useEmptyShopId = viewModel.useEmptyShopId } just runs
        every { recommendationProductUseCase.getRequestParams(pageNumber, viewModel.shopId, viewModel.pageName) } returns requestParams
        every { recommendationProductUseCase.getData(requestParams) } returns recommendationWidgetList
        coEvery { recommendationProductUseCase.mapper.recommWidgetToListOfVisitables(recommendationWidgetList[0]) } returns recommendationList

        viewModel.productLiveData.observeForever { }
        viewModel.getProducts(pageNumber)
        Assert.assertEquals(viewModel.productLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun getProductsFailException() {
        prepareViewModel()
        val pageNumber = 1
        viewModel.useEmptyShopId = false
        viewModel.shopId = "123"
        viewModel.pageName = "giftbox"

        val requestParams: RequestParams = mockk()
        val recommendationWidgetList: List<RecommendationWidget> = arrayListOf(RecommendationWidget())
        val recommendationList: List<Recommendation> = arrayListOf(Recommendation(RecommendationItem(), ProductCardModel()))

        every { recommendationProductUseCase.useEmptyShopId = viewModel.useEmptyShopId } just runs
        every { recommendationProductUseCase.getRequestParams(pageNumber, viewModel.shopId, viewModel.pageName) } returns requestParams
        every { recommendationProductUseCase.getData(requestParams) } throws Exception()
        coEvery { recommendationProductUseCase.mapper.recommWidgetToListOfVisitables(recommendationWidgetList[0]) } returns recommendationList

        viewModel.productLiveData.observeForever { }
        viewModel.getProducts(pageNumber)
        Assert.assertEquals(viewModel.productLiveData.value?.status, LiveDataResult.STATUS.ERROR)
    }

    @Test
    fun addToWishlistTestForTopAds() {
//        prepareRelaxedViewModel()
        prepareViewModel()
        val recommendationItem: RecommendationItem = RecommendationItem(isTopAds = true)
        val callback: ((Boolean, Throwable?) -> Unit) = { s, t -> }
        coEvery { topAdsWishlishedUseCase.execute(any(), any()) } just runs
        viewModel.addToWishlist(recommendationItem, callback)
        verify { topAdsWishlishedUseCase.execute(any(), any()) }
    }

    @Test
    fun addToWishlistTestForNonTopAds() {
        prepareViewModel()
        val recommendationItem: RecommendationItem = RecommendationItem(productId = 1)
        val callback: ((Boolean, Throwable?) -> Unit) = { s, t -> }
        val userId = "1"
        every { userSessionInterface.userId } returns userId
        every { addWishListUseCase.createObservable(recommendationItem.productId.toString(), userId, any()) } just runs
        viewModel.addToWishlist(recommendationItem, callback)
        verify { addWishListUseCase.createObservable(recommendationItem.productId.toString(), userId, any()) }
    }

    @Test
    fun testGetWishListActionListener() {
        prepareViewModel()
        val callback: ((Boolean, Throwable?) -> Unit) = { s, t -> }
        val wishListActionListener = viewModel.getWishListActionListener(callback)
        wishListActionListener.onSuccessRemoveWishlist("1")
        wishListActionListener.onErrorRemoveWishlist("1", "")
        Assert.assertEquals(wishListActionListener != null, true)
    }

    @Test
    fun testGetWishListActionListenerOnSuccessAddWishlist() {
        prepareViewModel()
        val callback: ((Boolean, Throwable?) -> Unit) = spyk({ s, t -> })
        val wishListActionListener = viewModel.getWishListActionListener(callback)
        wishListActionListener.onSuccessAddWishlist("1")
        verify { callback.invoke(true, null) }
    }

    @Test
    fun testGetWishListActionListenerOnErrorAddWishList() {
        prepareViewModel()
        val callback: ((Boolean, Throwable?) -> Unit) = spyk({ s, t -> })
        val wishListActionListener = viewModel.getWishListActionListener(callback)
        wishListActionListener.onErrorAddWishList("1", "")
        verify { callback.invoke(false, any()) }
    }

    @Test
    fun testGetSubscriberOnCompleted() {
        prepareViewModel()
        val callback: ((Boolean, Throwable?) -> Unit) = spyk({ s, t -> })
        val subsciber: Subscriber<WishlistModel> = viewModel.getSubscriber(callback)
        subsciber.onCompleted()
        Assert.assertEquals(subsciber != null, true)
    }

    @Test
    fun testGetSubscriberOnError() {
        prepareViewModel()
        val th = Exception()
        val callback: ((Boolean, Throwable?) -> Unit) = spyk({ s, t -> })
        val subsciber: Subscriber<WishlistModel> = viewModel.getSubscriber(callback)
        subsciber.onError(th)
        verify { callback.invoke(false, th) }
    }

    @Test
    fun testGetSubscriberOnNext() {
        prepareViewModel()
        val wishlistModel = mockk<WishlistModel>(relaxed = true)
        val mockData = mockk<WishlistModel.Data>(relaxed = true)

        every { wishlistModel.data } returns mockData

        val callback: ((Boolean, Throwable?) -> Unit) = spyk({ s, t -> })
        val subsciber: Subscriber<WishlistModel> = viewModel.getSubscriber(callback)
        subsciber.onNext(wishlistModel)
        verify { callback.invoke(true, null) }
    }

    @Test
    fun removeFromWishlist(){
        prepareViewModel()
        val model:RecommendationItem = RecommendationItem(productId = 1)
        val wishlistCallback: (((Boolean, Throwable?) -> Unit)) = mockk()
        val userId = "1"
        every { userSessionInterface.userId } returns userId
        every { removeWishListUseCase.createObservable(model.productId.toString(),userId, any()) } just runs
        viewModel.removeFromWishlist(model,wishlistCallback)
        verify { removeWishListUseCase.createObservable(model.productId.toString(),userId, any()) }
    }

    @Test
    fun testGetWishListActionListenerForRemoveFromWishList(){
        prepareViewModel()
        val wishlistCallback: (((Boolean, Throwable?) -> Unit)) = spyk()
        val wishListActionListener = viewModel.getWishListActionListenerForRemoveFromWishList(wishlistCallback)
        wishListActionListener.onErrorAddWishList("","")
        wishListActionListener.onSuccessAddWishlist("",)

        wishListActionListener.onErrorRemoveWishlist("error","1")
        verify { wishlistCallback.invoke(false, any()) }

        wishListActionListener.onSuccessRemoveWishlist("1")
        verify { wishlistCallback.invoke(true, null) }
    }

    @Test
    fun testOnCleared(){
        prepareViewModel()
        every { recommendationProductUseCase.unsubscribe() } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { recommendationProductUseCase.unsubscribe() }
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }
}