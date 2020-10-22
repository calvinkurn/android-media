package com.tokopedia.seller.action

import android.net.Uri
import android.os.Build
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.action.balance.domain.usecase.SliceSellerBalanceUseCase
import com.tokopedia.seller.action.balance.domain.usecase.SliceTopadsBalanceUseCase
import com.tokopedia.seller.action.balance.presentation.mapper.SellerBalanceMapper
import com.tokopedia.seller.action.balance.presentation.model.SellerActionBalance
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.presentation.presenter.SliceSellerActionPresenter
import com.tokopedia.seller.action.common.presentation.presenter.SliceSellerActionPresenterImpl
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.mapper.SellerActionOrderCodeMapper
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderType
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.seller.action.order.presentation.mapper.SellerOrderMapper
import com.tokopedia.seller.action.review.domain.usecase.SliceReviewStarsUseCase
import com.tokopedia.seller.action.review.presentation.mapper.SellerReviewStarsMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
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

    @Inject
    lateinit var dispatcher: SellerActionDispatcherProvider

    @Inject
    lateinit var sliceMainOrderListUseCase: SliceMainOrderListUseCase

    @Inject
    lateinit var sliceReviewStarsUseCase: SliceReviewStarsUseCase

    @Inject
    lateinit var sliceSellerBalanceUseCase: SliceSellerBalanceUseCase

    @Inject
    lateinit var sliceTopadsBalanceUseCase: SliceTopadsBalanceUseCase

    private var mainOrderStatus: SellerActionStatus? = null
    private var reviewStarsStatus: SellerActionStatus? = null
    private var sellerBalanceStatus: SellerActionStatus? = null

    override fun onCreateSliceProvider(): Boolean {
        injectDependencies()
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        return context != null
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            var isLoading = false
            if (userSession.isLoggedIn) {
                when(sliceUri.path) {
                    SellerActionConst.Deeplink.ORDER -> {
                        if (mainOrderStatus == null || mainOrderStatus == SellerActionStatus.NotLogin) {
                            (sliceUri.getQueryParameter(SellerActionConst.Params.ORDER_TYPE) ?: SellerActionOrderType.ORDER_DEFAULT).let { orderType ->
                                (sliceUri.getQueryParameter(SellerActionConst.Params.ORDER_DATE)).let { orderDate ->
                                    val date = orderDate?.split(DATE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }
                                    isLoading = true
                                    loadMainOrderList(sliceUri, orderType, date)
                                }
                            }
                        }
                    }
                    SellerActionConst.Deeplink.STARS -> {
                        if (reviewStarsStatus == null || reviewStarsStatus == SellerActionStatus.NotLogin) {
                            sliceUri.getQueryParameter(SellerActionConst.Params.RATING).let { ratingStar ->
                                isLoading = true
                                loadReviewStarsList(sliceUri, ratingStar.toIntOrZero())
                            }
                        }
                    }
                    SellerActionConst.Deeplink.BALANCE -> {
                        if (sellerBalanceStatus == null || sellerBalanceStatus == SellerActionStatus.NotLogin) {
                            isLoading = true
                            loadBalance(sliceUri, userSession.shopId.toIntOrZero())
                        }
                    }
                }
            } else {
                mainOrderStatus = SellerActionStatus.NotLogin
                reviewStarsStatus = SellerActionStatus.NotLogin
                sellerBalanceStatus = SellerActionStatus.NotLogin
            }
            createNewSlice(sliceUri, isLoading)?.getSlice()
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
    private fun createNewSlice(sliceUri: Uri, isLoading: Boolean = false): SellerSlice? {
        val notNullContext = requireNotNull(context)
        return when (sliceUri.path) {
            SellerActionConst.Deeplink.ORDER -> {
                SellerOrderMapper(notNullContext, sliceUri).run {
                    if (isLoading) {
                        mainOrderStatus = SellerActionStatus.Loading
                    }
                    getSlice(mainOrderStatus)
                }
            }
            SellerActionConst.Deeplink.STARS -> {
                SellerReviewStarsMapper(notNullContext, sliceUri).run {
                    if (isLoading) {
                        reviewStarsStatus = SellerActionStatus.Loading
                    }
                    getSlice(reviewStarsStatus)
                }
            }
            SellerActionConst.Deeplink.BALANCE -> {
                SellerBalanceMapper(notNullContext, sliceUri, remoteConfig).run {
                    if (isLoading) {
                        sellerBalanceStatus = SellerActionStatus.Loading
                    }
                    getSlice(sellerBalanceStatus)
                }
            }
            else -> SellerFailureSlice(notNullContext, sliceUri)
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        context?.contentResolver?.notifyChange(sliceUri, null)
    }

    private fun loadMainOrderList(sliceUri: Uri, @SellerActionOrderType orderType: String, date: String?) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                getSliceMainOrderList(sliceUri, orderType, date)
            } catch (ex: Exception) {
                mainOrderStatus = SellerActionStatus.Fail
                updateSlice(sliceUri)
            }
        }
    }

    private fun loadReviewStarsList(sliceUri: Uri, stars: Int) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                getSliceReviewStarsList(sliceUri, stars)
            } catch (ex: Exception) {
                reviewStarsStatus = SellerActionStatus.Fail
                updateSlice(sliceUri)
            }
        }
    }

    private fun loadBalance(sliceUri: Uri, shopId: Int) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                val balance = async { getSliceBalanceData() }
                val topAdsBalance = async { getSliceTopAdsData(shopId) }
                sellerBalanceStatus = SellerActionStatus.Success(listOf(SellerActionBalance(balance.await(), topAdsBalance.await())))
                updateSlice(sliceUri)
            } catch (ex: Exception) {
                SellerActionStatus.Fail
                updateSlice(sliceUri)
            }
        }
    }

    private suspend fun getSliceMainOrderList(sliceUri: Uri, @SellerActionOrderType orderType: String, date: String?) {
        val startDate = date ?: getCalculatedFormattedDate(SliceSellerActionPresenterImpl.DATE_FORMAT, SliceSellerActionPresenterImpl.DAYS_BEFORE)
        val endDate = date ?: Date().toFormattedString(SliceSellerActionPresenterImpl.DATE_FORMAT)
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(startDate, endDate, SellerActionOrderCodeMapper.mapOrderCodeByType(orderType))
            sellerBalanceStatus = SellerActionStatus.Success(sliceMainOrderListUseCase.executeOnBackground())
            updateSlice(sliceUri)
        }
    }

    private suspend fun getSliceReviewStarsList(sliceUri: Uri, stars: Int) {
        with(sliceReviewStarsUseCase) {
            params = SliceReviewStarsUseCase.createRequestParams(stars)
            reviewStarsStatus = SellerActionStatus.Success(sliceReviewStarsUseCase.executeOnBackground())
            updateSlice(sliceUri)
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