package com.tokopedia.seller.action

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.getCalculatedFormattedDate
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.common.dispatcher.SellerActionDispatcherProvider
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.SellerActionOrderCode
import com.tokopedia.seller.action.order.domain.usecase.SliceMainOrderListUseCase
import com.tokopedia.seller.action.order.presentation.mapper.SellerOrderMapper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class SellerActionSliceProvider: SliceProvider(){

    companion object {
        private const val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"

        private const val DATE_DELIMITER = "T"
        private const val DATE_FORMAT = "dd/MM/yyyy"
        private const val DAYS_BEFORE = -90
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var dispatcher: SellerActionDispatcherProvider

    @Inject
    lateinit var sliceMainOrderListUseCase: SliceMainOrderListUseCase

    private var mainOrderStatus: SellerActionStatus? = null

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
                            (sliceUri.getQueryParameter(SellerActionConst.Params.ORDER_DATE)).let { orderDate ->
                                val date = orderDate?.split(DATE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }
                                isLoading = true
                                loadMainOrderList(sliceUri, date)
                            }
                        }
                    }
                }
            } else {
                mainOrderStatus = SellerActionStatus.NotLogin
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
            else -> SellerFailureSlice(notNullContext, sliceUri)
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        context?.contentResolver?.notifyChange(sliceUri, null)
    }

    private fun loadMainOrderList(sliceUri: Uri, date: String?) {
        GlobalScope.launch(dispatcher.io()) {
            try {
                getSliceMainOrderList(sliceUri, date)
            } catch (ex: Exception) {
                mainOrderStatus = SellerActionStatus.Fail
                updateSlice(sliceUri)
            }
        }
    }

    private suspend fun getSliceMainOrderList(sliceUri: Uri, date: String?) {
        val startDate = date ?: getCalculatedFormattedDate(DATE_FORMAT, DAYS_BEFORE)
        val endDate = date ?: Date().toFormattedString(DATE_FORMAT)
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(startDate, endDate, SellerActionOrderCode.STATUS_CODE_DEFAULT)
            mainOrderStatus = SellerActionStatus.Success(sliceMainOrderListUseCase.executeOnBackground())
            updateSlice(sliceUri)
        }
    }

}