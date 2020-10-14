package com.tokopedia.seller.action.common.presentation.presenter

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.balance.domain.usecase.SliceSellerBalanceUseCase
import com.tokopedia.seller.action.balance.domain.usecase.SliceTopadsBalanceUseCase
import com.tokopedia.seller.action.balance.presentation.model.SellerActionBalance
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.exception.SellerActionException
import com.tokopedia.seller.action.order.domain.mapper.SellerActionOrderCodeMapper
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderType
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.seller.action.review.domain.model.InboxReviewList
import com.tokopedia.seller.action.review.domain.usecase.SliceReviewStarsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SliceSellerActionPresenterImpl(
        private val sliceMainOrderListUseCase: SliceMainOrderListUseCase,
        private val sliceReviewStarsUseCase: SliceReviewStarsUseCase,
        private val sliceSellerBalanceUseCase: SliceSellerBalanceUseCase,
        private val sliceTopadsBalanceUseCase: SliceTopadsBalanceUseCase,
        private val dispatcher: SellerActionDispatcherProvider): SliceSellerActionPresenter {

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val DAYS_BEFORE = -90
    }

    private val mOrderListLiveData = MutableLiveData<Result<Pair<Uri, List<Order>>>>()
    private val mReviewStarsListLiveData = MutableLiveData<Result<Pair<Uri, List<InboxReviewList>>>>()
    private val mBalanceLiveData = MutableLiveData<Result<Pair<Uri, List<SellerActionBalance>>>>()

    override fun getOrderList(sliceUri: Uri, @SellerActionOrderType orderType: String): LiveData<Result<Pair<Uri, List<Order>>>> {
        loadMainOrderList(sliceUri, orderType)
        return mOrderListLiveData
    }

    override fun getShopReviewList(sliceUri: Uri, stars: Int): LiveData<Result<Pair<Uri, List<InboxReviewList>>>> {
        loadReviewStarsList(sliceUri, stars)
        return mReviewStarsListLiveData
    }

    override fun getBalance(sliceUri: Uri, shopId: Int): LiveData<Result<Pair<Uri, List<SellerActionBalance>>>> {
        loadBalance(sliceUri, shopId)
        return mBalanceLiveData
    }

    private fun loadMainOrderList(sliceUri: Uri, @SellerActionOrderType orderType: String) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                getSliceMainOrderList(sliceUri, orderType)
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

    private fun loadBalance(sliceUri: Uri, shopId: Int) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                val balance = async { getSliceBalanceData() }
                val topAdsBalance = async { getSliceTopAdsData(shopId) }
                mBalanceLiveData.postValue(
                        Success(Pair(
                                sliceUri,
                                listOf(SellerActionBalance(balance.await(), topAdsBalance.await())))))
            } catch (ex: Exception) {
                mBalanceLiveData.postValue(Fail(SellerActionException(sliceUri, ex.message.orEmpty())))
            }
        }
    }

    private suspend fun getSliceMainOrderList(sliceUri: Uri, @SellerActionOrderType orderType: String) {
        val startDate = getCalculatedFormattedDate(DATE_FORMAT, DAYS_BEFORE)
        val endDate = Date().toFormattedString(DATE_FORMAT)
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(startDate, endDate, SellerActionOrderCodeMapper.mapOrderCodeByType(orderType))
            mOrderListLiveData.postValue(Success(Pair(sliceUri, executeOnBackground())))
        }
    }


    private suspend fun getSliceReviewStarsList(sliceUri: Uri, stars: Int) {
        with(sliceReviewStarsUseCase) {
            params = SliceReviewStarsUseCase.createRequestParams(stars)
            mReviewStarsListLiveData.postValue(Success(Pair(sliceUri, executeOnBackground())))
        }
    }

    private suspend fun getSliceBalanceData(): String? {
        return withContext(dispatcher.io()) {
            try {
                sliceSellerBalanceUseCase.executeOnBackground()
            } catch (ex: Exception) {
                null
            }
        }
    }

    private suspend fun getSliceTopAdsData(shopId: Int): String? {
        sliceTopadsBalanceUseCase.params = SliceTopadsBalanceUseCase.createRequestParams(shopId)
        return withContext(dispatcher.io()) {
            try {
                sliceTopadsBalanceUseCase.executeOnBackground()
            } catch (ex: Exception) {
                null
            }
        }
    }

}