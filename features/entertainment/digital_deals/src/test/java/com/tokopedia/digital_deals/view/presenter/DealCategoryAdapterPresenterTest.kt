package com.tokopedia.digital_deals.view.presenter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.digital_deals.domain.postusecase.PostUpdateDealLikesUseCase
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity
import com.tokopedia.digital_deals.view.model.Brand
import com.tokopedia.digital_deals.view.model.Page
import com.tokopedia.digital_deals.view.model.response.BrandDetailsResponse
import com.tokopedia.digital_deals.view.model.response.LikeUpdateResult
import com.tokopedia.digital_deals.view.utils.Utils
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
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

//    @Test
//    fun `Set Deal Like onNext contains liked event more than 0`() {
////        val presenterSpyk = spyk(DealCategoryAdapterPresenter(postUpdateDealLikesUseCase), recordPrivateCalls = true)
//        val contextMock = mockk<DealDetailsActivity>()
//        mockkStatic(Utils::class)
//
//        val token = object : TypeToken<DataResponse<LikeUpdateResult?>?>() {}.type
//        val restResponse = RestResponse(
//            DataResponse<LikeUpdateResult>().apply {
//                data = LikeUpdateResult().apply {
//                    isLiked = true
//                }
//            },
//            200,
//            false
//        )
//
//        every {
//            presenter setProperty "userSession" value any<UserSessionInterface>()
//        } propertyType UserSessionInterface::class answers {
//            fieldValue = UserSession(contextMock).apply {
//                setIsLogin(true)
//            }
//        }
//
//        every {
//            Utils.getSingletonInstance().containsLikedEvent(any())
//        } answers {
//            2
//        }
//
//        every {
//            postUpdateDealLikesUseCase.execute(any())
//        } answers {
//            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
//                mapOf(token to restResponse)
//            )
//        }
//
//        presenter.initialize()
//        presenter.setDealLike(123, true, 123, 123)
//
//        verify { postUpdateDealLikesUseCase.setRequestParams(any()) }
//        verify { postUpdateDealLikesUseCase.execute(any()) }
//    }
}