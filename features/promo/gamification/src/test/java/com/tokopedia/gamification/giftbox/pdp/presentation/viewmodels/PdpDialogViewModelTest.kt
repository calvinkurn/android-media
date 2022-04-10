package com.tokopedia.gamification.giftbox.pdp.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
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
    val addToWishlistV2UseCase: AddToWishlistV2UseCase = mockk()
    val deleteWishlistV2UseCase: DeleteWishlistV2UseCase = mockk()
    val topAdsWishlishedUseCase: TopAdsWishlishedUseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()
    private var deleteWishlistObserver = mockk<Observer<Result<DeleteWishlistV2Response>>>(relaxed = true)

    @Before
    fun setup() {
        MockKAnnotations.init(this)


    }

    private fun getRealViewModel(): PdpDialogViewModel {
        return (PdpDialogViewModel(recommendationProductUseCase, addWishListUseCase,
            removeWishListUseCase, addToWishlistV2UseCase, deleteWishlistV2UseCase, topAdsWishlishedUseCase, userSessionInterface, dispatcher))
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
        viewModel.addToWishlist(recommendationItem, callback, true)
        verify { topAdsWishlishedUseCase.execute(any(), any()) }
    }

    @Test
    fun addToWishlistTestForNonTopAds() {
        prepareViewModel()
        val recommendationItem: RecommendationItem = RecommendationItem(productId = 1)
        val callback: ((Boolean, Throwable?) -> Unit) = { s, t -> }
        val userId = "1"
        every { userSessionInterface.userId } returns userId
        coEvery {
            addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userId)
            addToWishlistV2UseCase.executeOnBackground() }
        viewModel.addToWishlist(recommendationItem, callback, true)
        coVerify {
            addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userId)
            addToWishlistV2UseCase.executeOnBackground()}
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
        val model = RecommendationItem(productId = 1)
        val wishlistCallback: (((Boolean, Throwable?) -> Unit)) = mockk()
        val userId = "1"
        val responseData = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)
        val result = mockk<DeleteWishlistV2Response>()
        val response = DeleteWishlistV2Response(data = DeleteWishlistV2Response.Data(responseData))
        every { userSessionInterface.userId } returns userId

        every { deleteWishlistV2UseCase.setParams(any(), any())} answers {}

        every { deleteWishlistV2UseCase.execute(any(), any()) } answers {
            firstArg<(DeleteWishlistV2Response) -> Unit>().invoke(result)
        }

        viewModel.removeFromWishlist(model,wishlistCallback)

        verify { deleteWishlistObserver.onChanged(Success(response)) }
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