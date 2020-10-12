package com.tokopedia.seller.action.order.presentation.presenter

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.exception.SellerActionException
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SliceSellerActionPresenterImpl(
        private val sliceMainOrderListUseCase: SliceMainOrderListUseCase,
        private val dispatcher: SellerActionDispatcherProvider): SliceSellerActionPresenter {

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val DAYS_BEFORE = -90
    }

    private val mOrderListLiveData = MutableLiveData<Result<Pair<Uri, List<Order>>>>()

    override fun getOrderList(sliceUri: Uri): LiveData<Result<Pair<Uri, List<Order>>>> {
        loadMainOrderList(sliceUri)
        return mOrderListLiveData
    }

    private fun loadMainOrderList(sliceUri: Uri) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                getSliceMainOrderList(sliceUri)
            } catch (ex: Exception) {
                mOrderListLiveData.postValue(Fail(SellerActionException(sliceUri, ex.message.orEmpty())))
            }
        }
    }

    private suspend fun getSliceMainOrderList(sliceUri: Uri) {
        val startDate = getCalculatedFormattedDate(DATE_FORMAT, DAYS_BEFORE)
        val endDate = Date().toFormattedString(DATE_FORMAT)
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(startDate, endDate)
            mOrderListLiveData.postValue(Success(Pair(sliceUri, executeOnBackground())))
        }
    }
}