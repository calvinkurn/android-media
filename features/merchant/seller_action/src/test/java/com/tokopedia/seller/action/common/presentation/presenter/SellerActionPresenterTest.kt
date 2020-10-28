package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class SellerActionPresenterTest : SellerActionPresenterTestFixture() {

    @Test
    fun `success getting order list should call onSuccessGetOrderList method`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val date = "2020-10-20"
        val orderList = emptyList<Order>()

        onGetOrderList_thenReturn(orderList)

        attachView()
        presenter.getOrderList(sliceUri, date , sliceHashMap)

        verifyOnSuccessGetOrderListCalled(sliceUri, orderList)
        verifyOnErrorGetOrderListNotCalled()
    }

    @Test
    fun `error getting order list should call onErrorGetOrderList method`() {
        val sliceHashMap = HashMap<Uri, SellerSlice?>()
        val throwable = MessageErrorException()

        onGetOrderList_thenReturn(throwable)

        attachView()
        presenter.getOrderList(sliceUri, anyString(), sliceHashMap)

        verifyOnErrorGetOrderListCalled(sliceUri)
        verifyOnSuccessGetOrderListNotCalled()
    }

}