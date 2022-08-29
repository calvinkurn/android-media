package com.tokopedia.digital_deals.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.digital_deals.domain.postusecase.PostUpdateDealLikesUseCase
import com.tokopedia.digital_deals.view.contractor.DealCategoryAdapterContract
import com.tokopedia.digital_deals.view.model.response.LikeUpdateResult
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.user.session.UserSession
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.lang.reflect.Type

class DealCategoryAdapterPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var presenter: DealCategoryAdapterPresenter

    private var postUpdateDealLikesUseCase = mockk<PostUpdateDealLikesUseCase>(relaxed = true)

    @Before
    fun setup() {
        presenter = DealCategoryAdapterPresenter(postUpdateDealLikesUseCase)
    }

    @Test
    fun `Set Deal Like onNext contains liked event more than 0 should removeLikedEvent`() {
        val view = mockk<DealCategoryAdapterContract.View>(relaxed = true)
        val userSessionMock = mockk<UserSession>(relaxed = true)
        mockkStatic(Utils::class)

        val token = object : TypeToken<DataResponse<LikeUpdateResult?>?>() {}.type
        val restResponse = RestResponse(
            DataResponse<LikeUpdateResult>().apply {
                data = LikeUpdateResult().apply {
                    isLiked = false
                    productId = "1"
                }
            },
            200,
            false
        )

        every {
            postUpdateDealLikesUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(token to restResponse)
            )
        }

        every {
            Utils.getSingletonInstance().containsLikedEvent(any())
        } answers { 1 }
        every {
            Utils.getSingletonInstance().removeLikedEvent(any())
        } just Runs

        every { userSessionMock.isLoggedIn } answers { true }
        every { userSessionMock.userId } answers { "123" }

        presenter.attachView(view)
        presenter.initialize(userSessionMock)

        presenter.setDealLike(123, true, 123, 123)

        verify { postUpdateDealLikesUseCase.setRequestParams(any()) }
        verify { postUpdateDealLikesUseCase.execute(any()) }
    }

    @Test
    fun `Set Deal Like onNext contains liked event less than 0 should addLikedEvent`() {
        val view = mockk<DealCategoryAdapterContract.View>(relaxed = true)
        val userSessionMock = mockk<UserSession>(relaxed = true)
        mockkStatic(Utils::class)

        val token = object : TypeToken<DataResponse<LikeUpdateResult?>?>() {}.type
        val restResponse = RestResponse(
            DataResponse<LikeUpdateResult>().apply {
                data = LikeUpdateResult().apply {
                    isLiked = true
                    productId = "1"
                }
            },
            200,
            false
        )

        every {
            postUpdateDealLikesUseCase.execute(any())
        } answers {
            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
                mapOf(token to restResponse)
            )
        }

        every {
            Utils.getSingletonInstance().containsLikedEvent(any())
        } answers { 0 }
        every {
            Utils.getSingletonInstance().addLikedEvent(any(), any())
        } just Runs

        every { userSessionMock.isLoggedIn } answers { true }
        every { userSessionMock.userId } answers { "123" }

        presenter.attachView(view)
        presenter.initialize(userSessionMock)

        presenter.setDealLike(123, false, 123, 123)

        verify { postUpdateDealLikesUseCase.setRequestParams(any()) }
        verify { postUpdateDealLikesUseCase.execute(any()) }
    }

    @Test
    fun `Set Deal Like when userSession isLoggedIn false`() {
        val view = mockk<DealCategoryAdapterContract.View>(relaxed = true)
        every { view.showLoginSnackbar(any(), any()) } just Runs

        presenter.attachView(view)
        presenter.initialize() // using default initialize() function with isLoggedIn = false
        presenter.setDealLike(123, false, 123, 123)

        verify(exactly = 0) { postUpdateDealLikesUseCase.setRequestParams(any()) }
        verify(exactly = 0) { postUpdateDealLikesUseCase.execute(any()) }
        verify { view.showLoginSnackbar(any(), any()) }
    }

    @Test
    fun `onDestroy should unsubscribe usecase`() {
        presenter.onDestroy()

        verify { postUpdateDealLikesUseCase.unsubscribe() }
    }
}