package com.tokopedia.digital_deals.view.presenter

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.digital_deals.domain.getusecase.GetBrandDetailsUseCase
import com.tokopedia.digital_deals.domain.postusecase.PostNsqEventUseCase
import com.tokopedia.digital_deals.view.model.Brand
import com.tokopedia.digital_deals.view.model.Page
import com.tokopedia.digital_deals.view.model.response.BrandDetailsResponse
import com.tokopedia.network.data.model.response.DataResponse
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rx.Subscriber
import java.lang.reflect.Type

class BrandDetailsPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var presenter: BrandDetailsPresenter

    private var getBrandDetailsUseCase = mockk<GetBrandDetailsUseCase>(relaxed = true)
    private var postNsqEventUseCase = mockk<PostNsqEventUseCase>(relaxed = true)

    @Before
    fun setup() {
        presenter = BrandDetailsPresenter(getBrandDetailsUseCase, postNsqEventUseCase)
    }
//
//    @Test
//    fun `On RecyclerView Scrolled Should Call checkIfToLoad`() {
//        val presenterSpyk = spyk(BrandDetailsPresenter(getBrandDetailsUseCase, postNsqEventUseCase), recordPrivateCalls = true)
//        val context = mockk<Context>(relaxed = true)
//
//        every { presenterSpyk["checkIfToLoad"](allAny<LinearLayoutManager>()) } returns Unit
//
//        presenterSpyk.onRecyclerViewScrolled(LinearLayoutManager(context))
//        verify { presenterSpyk["checkIfToLoad"](allAny<LinearLayoutManager>()) }
//    }
//
//    @Test
//    fun `Send NSQ Event should execute use case`() {
//        presenter.sendNSQEvent("123", "action")
//
//        verify { postNsqEventUseCase.setRequestModel(any()) }
//        verify { postNsqEventUseCase.execute(any()) }
//    }
//
//    @Test
//    fun `Get Brand Details should not show progress`() {
//        val isShowProgress = false
//        presenter.getBrandDetails(isShowProgress)
//
//        verify { presenter.view.showProgressBar() wasNot Called }
//        verify { presenter.view.hideCollapsingHeader() wasNot Called }
//        verify { getBrandDetailsUseCase.setRequestParams(any()) }
//        verify { getBrandDetailsUseCase.execute(any()) }
//    }
//
//    @Test
//    fun `Get Brand Details onNext`() {
//        val presenterSpyk = spyk(BrandDetailsPresenter(getBrandDetailsUseCase, postNsqEventUseCase), recordPrivateCalls = true)
//        val isShowProgress = false
//        val token = object : TypeToken<DataResponse<BrandDetailsResponse?>?>() {}.type
//        val restResponse = RestResponse(
//            DataResponse<BrandDetailsResponse>().apply {
//                data = BrandDetailsResponse().apply {
//                    dealItems = listOf()
//                    dealBrand = Brand()
//                    page = Page()
//                }
//            },
//            200,
//            false
//        )
//
//        every {
//            getBrandDetailsUseCase.execute(any())
//        } answers {
//            (firstArg() as Subscriber<Map<Type, RestResponse>>).onNext(
//                mapOf(token to restResponse)
//            )
//        }
//
//        presenterSpyk.getBrandDetails(isShowProgress)
//
//        verify { presenterSpyk.view.showProgressBar() wasNot Called }
//        verify { presenterSpyk.view.hideCollapsingHeader() wasNot Called }
//        verify { getBrandDetailsUseCase.setRequestParams(any()) }
//        verify { getBrandDetailsUseCase.execute(any()) }
//
//        verify { presenterSpyk.view.renderBrandDetails(any(), any(), any()) }
//        verify { presenterSpyk["getNextPageUrl"]() }
//    }
}