package com.tokopedia.navigation.presentation.presenter

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.domain.GetDrawerNotificationUseCase
import com.tokopedia.navigation.domain.subscriber.InboxSubscriber
import com.tokopedia.navigation.presentation.view.InboxView
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.topads.sdk.domain.model.WishlistModel
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.util.ArrayList

@ExperimentalCoroutinesApi
class InboxPresenterTest {

    @RelaxedMockK
    lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    lateinit var getDrawerNotificationUseCase: GetDrawerNotificationUseCase

    @RelaxedMockK
    lateinit var getRecommendationUseCase: GetRecommendationUseCase

    @RelaxedMockK
    lateinit var addWishListUseCase: AddWishListUseCase

    @RelaxedMockK
    lateinit var removeWishListUseCase: RemoveWishListUseCase

    @RelaxedMockK
    lateinit var topAdsWishListedUseCase: TopAdsWishlishedUseCase

    @RelaxedMockK
    lateinit var addToWishlistV2UseCase: AddToWishlistV2UseCase

    @RelaxedMockK
    lateinit var deleteWishlistV2UseCase: DeleteWishlistV2UseCase

    @RelaxedMockK
    lateinit var inboxView: InboxView

    private val inboxPresenter by lazy {
        InboxPresenter(
                getDrawerNotificationUseCase,
                getRecommendationUseCase,
                userSessionInterface,
                addWishListUseCase,
                removeWishListUseCase,
                addToWishlistV2UseCase,
                deleteWishlistV2UseCase,
                topAdsWishListedUseCase
        )
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        inboxPresenter.setView(inboxView)
    }

    /**
     * Inbox data
     * */
    @Test
    fun `get inbox data | on success`() {
        val notificationEntity = NotificationEntity()

        every {
            getDrawerNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<InboxSubscriber>().onStart()
            secondArg<InboxSubscriber>().onCompleted()
            secondArg<InboxSubscriber>().onNext(notificationEntity)
        }

        inboxPresenter.getInboxData()

        verify {
            getDrawerNotificationUseCase.execute(any(), any())
        }

        verify {
            inboxView.onRenderNotifInbox(any())
        }
    }

    @Test
    fun `get inbox data | on fail`() {
        every {
            getDrawerNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<InboxSubscriber>().onStart()
            secondArg<InboxSubscriber>().onCompleted()
            secondArg<InboxSubscriber>().onError(Throwable())
        }

        inboxPresenter.getInboxData()

        verify {
            getDrawerNotificationUseCase.execute(any(), any())
        }

        verify {
            inboxView.onError(any())
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
            secondArg<Subscriber<List<RecommendationWidget>>>().onStart()
            secondArg<Subscriber<List<RecommendationWidget>>>().onCompleted()
            secondArg<Subscriber<List<RecommendationWidget>>>().onNext(listOfRecommendationWidget)
        }

        inboxPresenter.getFirstRecomData()

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            inboxView.hideLoadMoreLoading()
            inboxView.onRenderRecomInbox(any(),any())
        }
    }

    @Test
    fun `get first recommendation data | on fail`() {
        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onStart()
            secondArg<Subscriber<List<RecommendationWidget>>>().onCompleted()
            secondArg<Subscriber<List<RecommendationWidget>>>().onError(Throwable())
        }

        inboxPresenter.getFirstRecomData()

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            inboxView.hideLoadMoreLoading()
        }
    }

    @Test
    fun `get recommendation data | on success`() {
        val page = 0
        val listOfRecommendationWidget = arrayListOf(RecommendationWidget(), RecommendationWidget())

        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onStart()
            secondArg<Subscriber<List<RecommendationWidget>>>().onCompleted()
            secondArg<Subscriber<List<RecommendationWidget>>>().onNext(listOfRecommendationWidget)
        }

        inboxPresenter.getRecomData(page)

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            inboxView.hideLoadMoreLoading()
            inboxView.onRenderRecomInbox(any(), any())
        }
    }

    @Test
    fun `get recommendation data | on fail`() {
        val page = 0
        every {
            getRecommendationUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<List<RecommendationWidget>>>().onStart()
            secondArg<Subscriber<List<RecommendationWidget>>>().onCompleted()
            secondArg<Subscriber<List<RecommendationWidget>>>().onError(Throwable())
        }

        inboxPresenter.getRecomData(page)

        verify {
            getRecommendationUseCase.execute(any(), any())
        }

        verify {
            inboxView.hideLoadMoreLoading()
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
            topAdsWishListedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<WishlistModel>>().onStart()
            secondArg<Subscriber<WishlistModel>>().onCompleted()
            secondArg<Subscriber<WishlistModel>>().onNext(wishListViewModel)
        }

        inboxPresenter.addWishlist(recommendationItem) { b, throwable ->
            Assert.assertTrue(b)
        }

        verify {
            topAdsWishListedUseCase.execute(any(), any())
        }
    }

    @Test
    fun `verify add to wishlistv2 returns success` () {
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.execute(any(), any()) } answers {
            firstArg<(Success<AddToWishlistV2Response.Data.WishlistAddV2>) -> Unit>().invoke(Success(resultWishlistAddV2))
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        inboxPresenter.addWishlistV2(recommendationItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `verify add to wishlistv2 returns fail` () {
        val recommendationItem = RecommendationItem(isTopAds = false, productId = 123L)
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        inboxPresenter.addWishlistV2(recommendationItem, mockListener)

        verify { addToWishlistV2UseCase.setParams(recommendationItem.productId.toString(), userSessionInterface.userId) }
        coVerify { addToWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `verify remove wishlistV2 returns success`(){
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.execute(any(), any()) } answers {
            firstArg<(Success<DeleteWishlistV2Response.Data.WishlistRemoveV2>) -> Unit>().invoke(Success(resultWishlistRemoveV2))
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        inboxPresenter.removeWishlistV2(recommItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `verify remove wishlistV2 returns fail`(){
        val recommItem = RecommendationItem(isTopAds = false, productId = 12L)
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(mockThrowable)
        }

        val mockListener: WishlistV2ActionListener = mockk(relaxed = true)
        inboxPresenter.removeWishlistV2(recommItem, mockListener)

        verify { deleteWishlistV2UseCase.setParams(recommItem.productId.toString(), userSessionInterface.userId) }
        coVerify { deleteWishlistV2UseCase.execute(any(), any()) }
    }

    @Test
    fun `add top ads wishList | on fail`() {
        val recommendationItem = RecommendationItem(
                isTopAds = true
        )

        every {
            topAdsWishListedUseCase.execute(any(), any())
        } answers {
            secondArg<Subscriber<WishlistModel>>().onStart()
            secondArg<Subscriber<WishlistModel>>().onCompleted()
            secondArg<Subscriber<WishlistModel>>().onError(Throwable())
        }

        inboxPresenter.addWishlist(recommendationItem) { b, throwable ->
            Assert.assertFalse(b)
        }

        verify {
            topAdsWishListedUseCase.execute(any(), any())
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

        inboxPresenter.addWishlist(recommendationItem) { b, throwable ->
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

        inboxPresenter.addWishlist(recommendationItem) { b, throwable ->
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

        inboxPresenter.removeWishlist(recommendationItem) { b, throwable ->
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

        inboxPresenter.removeWishlist(recommendationItem) { b, throwable ->
            Assert.assertFalse(b)
        }

        verify {
            removeWishListUseCase.createObservable(any(), any(), any())
        }
    }

    /**
     * on resume
     * */
    @Test
    fun `on resume`() {
        val notificationEntity = NotificationEntity()

        every {
            getDrawerNotificationUseCase.execute(any(), any())
        } answers {
            secondArg<InboxSubscriber>().onNext(notificationEntity)
        }

        inboxPresenter.onResume()

        verify {
            getDrawerNotificationUseCase.execute(any(), any())
        }

        verify {
            inboxView.onRenderNotifInbox(any())
        }
    }

    /**
     * user session
     * */
    @Test
    fun `is logged in`() {
        inboxPresenter.isLoggedIn()

        verify {
            userSessionInterface.isLoggedIn
        }
    }

    /**
     * on destroy
     * */
    @Test
    fun `on destroy`() {
        inboxPresenter.onDestroy()

        verify {
            getRecommendationUseCase.unsubscribe()
        }

        verify {
            getDrawerNotificationUseCase.unsubscribe()
        }
    }

}