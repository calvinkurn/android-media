package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.exception.SellerActionException
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.seller.action.review.domain.model.InboxReviewList
import com.tokopedia.seller.action.review.domain.usecase.SliceReviewStarsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class SliceSellerActionPresenterImpl(
        private val sliceMainOrderListUseCase: SliceMainOrderListUseCase,
        private val sliceReviewStarsUseCase: SliceReviewStarsUseCase,
        private val dispatcher: SellerActionDispatcherProvider): SliceSellerActionPresenter {

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val DAYS_BEFORE = -90
    }

    private val mOrderListLiveData = MutableLiveData<Result<Pair<Uri, List<Order>>>>()
    private val mReviewStarsListLiveData = MutableLiveData<Result<Pair<Uri, List<InboxReviewList>>>>()

    override fun getOrderList(sliceUri: Uri): LiveData<Result<Pair<Uri, List<Order>>>> {
        loadMainOrderList(sliceUri)
        return mOrderListLiveData
    }

    override fun getShopReviewList(sliceUri: Uri, stars: Int): LiveData<Result<Pair<Uri, List<InboxReviewList>>>> {
        loadReviewStarsList(sliceUri, stars)
        return mReviewStarsListLiveData
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

    private fun loadReviewStarsList(sliceUri: Uri, stars: Int) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                getSliceReviewStarsList(sliceUri, stars)
            } catch (ex: Exception) {
                mReviewStarsListLiveData.postValue(Fail(SellerActionException(sliceUri, ex.message.orEmpty())))
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

    private suspend fun getSliceReviewStarsList(sliceUri: Uri, stars: Int) {
        with(sliceReviewStarsUseCase) {
            params = SliceReviewStarsUseCase.createRequestParams(stars)
            mReviewStarsListLiveData.postValue(Success(Pair(sliceUri, executeOnBackground())))
        }
    }

}