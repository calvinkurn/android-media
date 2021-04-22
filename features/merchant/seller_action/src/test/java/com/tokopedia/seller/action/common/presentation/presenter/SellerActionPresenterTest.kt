package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import com.tokopedia.kotlin.extensions.convertFormatDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.presentation.slices.SellerLoadingSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import io.mockk.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import java.util.*
import kotlin.collections.HashMap

class SellerActionPresenterTest : SellerActionPresenterTestFixture() {

    @Test
    fun `success getting order list should call onSuccessGetOrderList method`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val date = "2020-10-20"
        val orderList = emptyList<Order>()

        onGetOrderList_thenReturn(orderList)

        attachView()
        presenter.getOrderList(sliceUri, date , sliceHashMap)

        verifySliceMainOrderListUseCaseCalled()
        verifyOnSuccessGetOrderListCalled(sliceUri, orderList)
        verifyOnErrorGetOrderListNotCalled()
    }

    @Test
    fun `error getting order list should call onErrorGetOrderList method`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val date = "2020-10-20"
        val throwable = MessageErrorException()

        onGetOrderList_thenReturn(throwable)

        attachView()
        presenter.getOrderList(sliceUri, date, sliceHashMap)

        verifySliceMainOrderListUseCaseCalled()
        verifyOnErrorGetOrderListCalled(sliceUri)
        verifyOnSuccessGetOrderListNotCalled()
    }

    @Test
    fun `correct date will be converted to the other formatted date`() {
        mockkObject(SliceMainOrderListUseCase.Companion)
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val date = "2020-10-20"
        val orderList = emptyList<Order>()

        onGetOrderList_thenReturn(orderList)
        coEvery {
            provider.getFormattedDate(date)
        } returns convertFormatDate(date, SellerActionConst.SLICE_DATE_FORMAT, SellerActionConst.REQUEST_DATE_FORMAT)

        attachView()
        presenter.getOrderList(sliceUri, date , sliceHashMap)

        val expectedDate = convertFormatDate(date, SellerActionConst.SLICE_DATE_FORMAT, SellerActionConst.REQUEST_DATE_FORMAT)

        verify(exactly = 0) {
            provider.getDefaultDate()
        }
        verify(exactly = 1) {
            SliceMainOrderListUseCase.createRequestParam(expectedDate, expectedDate, SellerActionOrderCode.STATUS_CODE_DEFAULT)
        }
    }

    @Test
    fun `null date will be converted to the default formatted date`() {
        mockkObject(SliceMainOrderListUseCase.Companion)
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val orderList = emptyList<Order>()
        val expectedDateString = Date().toFormattedString(SellerActionConst.REQUEST_DATE_FORMAT)

        onGetOrderList_thenReturn(orderList)
        coEvery {
            provider.getDefaultDate()
        } returns expectedDateString

        attachView()
        presenter.getOrderList(sliceUri, null , sliceHashMap)

        verify(exactly = 1) {
            provider.getDefaultDate()
            SliceMainOrderListUseCase.createRequestParam(expectedDateString, expectedDateString, SellerActionOrderCode.STATUS_CODE_DEFAULT)
        }
    }

    @Test
    fun `wrong date format will not execute use case`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val date = "10/10/2020"

        coEvery {
            provider.getFormattedDate(date)
        } throws Exception()

        attachView()
        presenter.getOrderList(sliceUri, date, sliceHashMap)

        verifySliceMainOrderListUseCaseNotCalled()
        verifyOnSuccessGetOrderListNotCalled()
    }

    @Test
    fun `null date will also execute use case`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()

        attachView()
        presenter.getOrderList(sliceUri, null, sliceHashMap)

        verifySliceMainOrderListUseCaseCalled()
    }

    @Test
    fun `not attached view will not run onSuccessGetOrderList even if request is success`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()

        presenter.getOrderList(sliceUri, null, sliceHashMap)

        verifyOnSuccessGetOrderListNotCalled()
    }

    @Test
    fun `not null hash map will not execute use case`() {
        val expectedSlice = SellerLoadingSlice(context, sliceUri)
        val sliceHashMap = HashMap<Uri, SellerSlice?>().apply {
            put(sliceUri, expectedSlice)
        }

        attachView()
        presenter.getOrderList(sliceUri, anyString(), sliceHashMap)

        verifySliceMainOrderListUseCaseNotCalled()
        verifyOnSuccessGetOrderListNotCalled()
        verifyOnErrorGetOrderListNotCalled()
    }

}