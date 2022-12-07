package com.tokopedia.gamification.giftbox.pdp.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.gamification.pdp.presentation.viewmodels.PdpDialogViewModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
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
    val addToWishlistV2UseCase: AddToWishlistV2UseCase = mockk()
    val deleteWishlistV2UseCase: DeleteWishlistV2UseCase = mockk()
    val userSessionInterface: UserSessionInterface = mockk()

    @Before
    fun setup() {
        MockKAnnotations.init(this)


    }

    private fun getRealViewModel(): PdpDialogViewModel {
        return (PdpDialogViewModel(recommendationProductUseCase, addToWishlistV2UseCase, deleteWishlistV2UseCase, userSessionInterface, dispatcher))
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
    fun testOnCleared(){
        prepareViewModel()
        every { recommendationProductUseCase.unsubscribe() } just runs
        every { addToWishlistV2UseCase.cancelJobs() } just runs
        every { deleteWishlistV2UseCase.cancelJobs() } just runs

        val method = viewModel::class.java.getDeclaredMethod("onCleared")
        method.isAccessible = true
        method.invoke(viewModel)

        verify { recommendationProductUseCase.unsubscribe() }
        verify { addToWishlistV2UseCase.cancelJobs() }
        verify { deleteWishlistV2UseCase.cancelJobs() }
    }

    @After
    fun cleanup() {
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `verify add to wishlistv2 returns success` () {
        prepareViewModel()
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { userSessionInterface.userId} returns "1"
        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.execute(any(), any()) } answers {
            firstArg<(Success<AddToWishlistV2Response.Data.WishlistAddV2>) -> Unit>().invoke(Success(resultWishlistAddV2))
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishlistV2(recommendationItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), "1") }
        coVerify { addToWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `verify add to wishlistv2 returns fail` () {
        prepareViewModel()
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { userSessionInterface.userId} returns "1"
        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.addToWishlistV2(recommendationItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), "1") }
        coVerify { addToWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `verify remove wishlistV2 returns success`(){
        prepareViewModel()
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { userSessionInterface.userId} returns "1"
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.execute(any(), any()) } answers {
            firstArg<(Success<DeleteWishlistV2Response.Data.WishlistRemoveV2>) -> Unit>().invoke(Success(resultWishlistRemoveV2))
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeFromWishlistV2(recommendationItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommendationItem.productId.toString(), "1") }
        coVerify { deleteWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`(){
        prepareViewModel()
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { userSessionInterface.userId} returns "1"
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        viewModel.removeFromWishlistV2(recommendationItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommendationItem.productId.toString(), "1") }
        coVerify { deleteWishlistV2UseCase.execute(any(), any()) }
    }
}