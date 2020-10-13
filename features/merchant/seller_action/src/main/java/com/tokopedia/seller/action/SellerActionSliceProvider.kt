package com.tokopedia.seller.action

import android.net.Uri
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.common.di.SellerActionModule
import com.tokopedia.seller.action.common.exception.SellerActionException
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.presenter.SliceSellerActionPresenter
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
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
    }

    @Inject
    lateinit var sliceSellerActionPresenter: SliceSellerActionPresenter

    @Inject
    lateinit var handler: Handler

    @Inject
    lateinit var userSession: UserSessionInterface

    private var isOrderListHasLoaded = false
    private var isReviewStarsListHasLoaded = false

    private val sellerOrderObserver = Observer<Result<Pair<Uri, List<Order>>>> { result ->
        if (result != null) {
            isOrderListHasLoaded = true
            when(result) {
                is Success -> {
                    updateSlice(result.data.first)
                }
                is Fail -> (result.throwable as? SellerActionException)?.sliceUri?.let { updateSlice(it) }
            }
        }
    }

    private val sellerReviewStarsObserver = Observer<Result<Pair<Uri, List<InboxReviewList>>>> { result ->
        if (result != null) {
            isReviewStarsListHasLoaded = true
            when(result) {
                is Success -> {
                    val sliceUri = result.data.first
                    updateSlice(sliceUri)
                }
                is Fail -> (result.throwable as? SellerActionException)?.sliceUri?.let {
                    updateSlice(it)
                }
            }
        }
    }

    private var orderListLiveData: LiveData<Result<Pair<Uri, List<Order>>>>? = null
    private var reviewStarsListLiveData: LiveData<Result<Pair<Uri, List<InboxReviewList>>>>? = null

    override fun onCreateSliceProvider(): Boolean {
        context?.let { GraphqlClient.init(it) }
        injectDependencies()
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        return context != null
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (userSession.isLoggedIn) {
                when(sliceUri.path) {
                    SellerActionConst.Deeplink.ORDER -> {
                        if (!isOrderListHasLoaded) {
                            orderListLiveData = getOrderListLiveData(sliceUri)
                            isOrderListHasLoaded = false
                        }
                    }
                    SellerActionConst.Deeplink.STARS -> {
                        if (!isReviewStarsListHasLoaded) {
                            sliceUri.getQueryParameter(SellerActionConst.Params.RATING).let { ratingStar ->
                                reviewStarsListLiveData = getReviewStarsListLiveData(sliceUri, ratingStar.toIntOrZero())
                                isReviewStarsListHasLoaded = false
                            }
                        }
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
                .sellerActionModule(SellerActionModule(requireNotNull(context)))
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
            else -> SellerFailureSlice(notNullContext, sliceUri)
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

    private fun getOrderListLiveData(sliceUri: Uri) =
            sliceSellerActionPresenter.getOrderList(sliceUri).apply {
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

}