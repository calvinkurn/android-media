package com.tokopedia.seller.action

import android.net.Uri
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.action.balance.presentation.mapper.SellerBalanceMapper
import com.tokopedia.seller.action.balance.presentation.model.SellerActionBalance
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.common.exception.SellerActionException
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.presenter.SliceSellerActionPresenter
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderType
import com.tokopedia.seller.action.order.presentation.mapper.SellerOrderMapper
import com.tokopedia.seller.action.review.domain.model.InboxReviewList
import com.tokopedia.seller.action.review.presentation.mapper.SellerReviewStarsMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SellerActionSliceProvider: SliceProvider(){

    companion object {
        private const val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"

        private const val DATE_DELIMITER = "T"
    }

    @Inject
    lateinit var sliceSellerActionPresenter: SliceSellerActionPresenter

    @Inject
    lateinit var handler: Handler

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    private val sellerOrderObserver = Observer<Result<Pair<Uri, List<Order>>>> { result ->
        result.observeResult {}
    }

    private val sellerReviewStarsObserver = Observer<Result<Pair<Uri, List<InboxReviewList>>>> { result ->
        result.observeResult {}
    }

    private val sellerBalanceObserver = Observer<Result<Pair<Uri, List<SellerActionBalance>>>> { result ->
        result.observeResult {}
    }

    private var orderListLiveData: LiveData<Result<Pair<Uri, List<Order>>>>? = null
    private var reviewStarsListLiveData: LiveData<Result<Pair<Uri, List<InboxReviewList>>>>? = null
    private var sellerBalanceLiveData: LiveData<Result<Pair<Uri, List<SellerActionBalance>>>>? = null

    override fun onCreateSliceProvider(): Boolean {
        injectDependencies()
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        return context != null
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (userSession.isLoggedIn) {
                when(sliceUri.path) {
                    SellerActionConst.Deeplink.ORDER -> {
                        (sliceUri.getQueryParameter(SellerActionConst.Params.ORDER_TYPE) ?: SellerActionOrderType.ORDER_DEFAULT).let { orderType ->
                            (sliceUri.getQueryParameter(SellerActionConst.Params.ORDER_DATE)).let { orderDate ->
                                val date = orderDate?.split(DATE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }
                                orderListLiveData = getOrderListLiveData(sliceUri, orderType, date)
                            }
                        }
                    }
                    SellerActionConst.Deeplink.STARS -> {
                        sliceUri.getQueryParameter(SellerActionConst.Params.RATING).let { ratingStar ->
                            reviewStarsListLiveData = getReviewStarsListLiveData(sliceUri, ratingStar.toIntOrZero())
                        }
                    }
                    SellerActionConst.Deeplink.BALANCE -> {
                        sellerBalanceLiveData = getSellerBalanceLiveData(sliceUri)
                    }
                }
            }
            createNewSlice(sliceUri).getSlice()
        } else {
            null
        }
    }

    private fun injectDependencies() {
        DaggerSellerActionComponent.builder()
                .baseAppComponent((requireNotNull(context).applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createNewSlice(sliceUri: Uri): SellerSlice {
        val notNullContext = requireNotNull(context)
        val status =
                when (sliceUri.path) {
                    SellerActionConst.Deeplink.ORDER -> getStatus(orderListLiveData, sellerOrderObserver)
                    SellerActionConst.Deeplink.STARS -> getStatus(reviewStarsListLiveData, sellerReviewStarsObserver)
                    SellerActionConst.Deeplink.BALANCE -> getStatus(sellerBalanceLiveData, sellerBalanceObserver)
                    else -> SellerActionStatus.Fail
                }
        return when (sliceUri.path) {
            SellerActionConst.Deeplink.ORDER -> {
                SellerOrderMapper(notNullContext, sliceUri).run {
                    getSlice(status)
                }
            }
            SellerActionConst.Deeplink.STARS -> {
                SellerReviewStarsMapper(notNullContext, sliceUri).run {
                    getSlice(status)
                }
            }
            SellerActionConst.Deeplink.BALANCE -> {
                SellerBalanceMapper(notNullContext, sliceUri, remoteConfig).run {
                    getSlice(status)
                }
            }
            else -> SellerFailureSlice(notNullContext, sliceUri)
        }
    }

    private fun Result<Pair<Uri, Any>>?.observeResult(updateValue: () -> Unit) {
        updateValue()
        when(this) {
            is Success -> {
                updateSlice(data.first)
            }
            is Fail -> (throwable as? SellerActionException)?.sliceUri?.let {
                updateSlice(it)
            }
        }
    }

    private fun <T : SellerSuccessItem> getStatus(liveData: LiveData<Result<Pair<Uri, List<T>>>>?,
                                                  observer: Observer<Result<Pair<Uri, List<T>>>>): SellerActionStatus {
        val result = liveData?.value
        return when {
            !userSession.isLoggedIn -> SellerActionStatus.NotLogin
            result == null -> SellerActionStatus.Loading
            result is Success -> {
                handler.post {
                    liveData.removeObserver(observer)
                }
                SellerActionStatus.Success(result.data.second)
            }
            result is Fail -> {
                handler.post {
                    liveData.removeObserver(observer)
                }
                SellerActionStatus.Fail
            }
            else -> {
                handler.post {
                    liveData.removeObserver(observer)
                }
                SellerActionStatus.Fail
            }
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        context?.contentResolver?.notifyChange(sliceUri, null)
    }

    private fun getOrderListLiveData(sliceUri: Uri, @SellerActionOrderType orderType: String, date: String? = null) =
            sliceSellerActionPresenter.getOrderList(sliceUri, orderType, date).apply {
                handler.post {
                    observeForever(sellerOrderObserver)
                }
            }

    private fun getReviewStarsListLiveData(sliceUri: Uri, stars: Int) =
            sliceSellerActionPresenter.getShopReviewList(sliceUri, stars).apply {
                handler.post {
                    observeForever(sellerReviewStarsObserver)
                }
            }

    private fun getSellerBalanceLiveData(sliceUri: Uri) =
            sliceSellerActionPresenter.getBalance(sliceUri, userSession.shopId.toIntOrZero()).apply {
                handler.post {
                    observeForever(sellerBalanceObserver)
                }
            }

}