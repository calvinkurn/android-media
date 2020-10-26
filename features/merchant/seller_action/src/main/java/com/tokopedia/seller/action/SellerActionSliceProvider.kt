package com.tokopedia.seller.action

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.kotlin.extensions.convertFormatDate
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
import kotlin.collections.HashMap

class SellerActionSliceProvider: SliceProvider(){

    companion object {
        private const val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"

        private const val INITIAL_DATE_FORMAT = "yyyy-MM-dd"
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var dispatcher: SellerActionDispatcherProvider

    @Inject
    lateinit var sliceMainOrderListUseCase: SliceMainOrderListUseCase

    private var mainOrderStatus: SellerActionStatus? = null
    private var isLoading: Boolean = false
    private var isAlreadyInjected: Boolean = false

    private var sliceHashMap: HashMap<Uri, SellerSlice?> = HashMap()

    override fun onCreateSliceProvider(): Boolean {
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        return context != null
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Returning slice if the uri has been called/bind before
            // This will avoid infinite gql call loop in certain cases
            sliceHashMap[sliceUri]?.let {
                return it.getSlice()
            }

            if (!isAlreadyInjected) {
                // Init GraphqlClient first before injecting because GraphqlRepository would require GraphqlClient to be initialized first
                context?.let { GraphqlClient.init(it) }
                injectDependencies()
                isAlreadyInjected = true
            }

            if (userSession.isLoggedIn) {
                when(sliceUri.path) {
                    SellerActionConst.Deeplink.ORDER -> {
                        val date = sliceUri.getDateFromOrderUri()
                        val canLoadData = (mainOrderStatus == null || mainOrderStatus == SellerActionStatus.NotLogin) && !isLoading
                        if (canLoadData) {
                            isLoading = true
                            loadMainOrderList(sliceUri, date)
                        }
                    }
                }
            } else {
                mainOrderStatus = SellerActionStatus.NotLogin
            }
            return createNewSlice(sliceUri, isLoading)?.getSlice()
        } else {
            return null
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
                val date = sliceUri.getDateFromOrderUri()
                SellerOrderMapper(notNullContext, sliceUri, date).run {
                    if (isLoading) {
                        mainOrderStatus = SellerActionStatus.Loading
                    }
                    getSlice(mainOrderStatus).also {
                        if (mainOrderStatus is SellerActionStatus.Success || mainOrderStatus is SellerActionStatus.Fail) {
                            mainOrderStatus = null
                            sliceHashMap[sliceUri] = it
                        }
                    }
                }
            }
            else -> {
                mainOrderStatus = null
                SellerFailureSlice(notNullContext, sliceUri).also {
                    sliceHashMap[sliceUri] = it
                }
            }
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        context?.contentResolver?.notifyChange(sliceUri, null)
    }

    private fun loadMainOrderList(sliceUri: Uri, date: String?) {
        GlobalScope.launch(dispatcher.io()) {
            if (sliceHashMap[sliceUri] == null) {
                try {
                    getSliceMainOrderList(sliceUri, date)
                } catch (ex: Exception) {
                    isLoading = false
                    mainOrderStatus = SellerActionStatus.Fail
                    updateSlice(sliceUri)
                }
            }
        }
    }

    private suspend fun getSliceMainOrderList(sliceUri: Uri, date: String?) {
        val filteredDate = date?.let {
            convertFormatDate(it, INITIAL_DATE_FORMAT, DATE_FORMAT)
        } ?: Date().toFormattedString(DATE_FORMAT)
        with(sliceMainOrderListUseCase) {
            params = SliceMainOrderListUseCase.createRequestParam(filteredDate, filteredDate, SellerActionOrderCode.STATUS_CODE_DEFAULT)
            mainOrderStatus = SellerActionStatus.Success(sliceMainOrderListUseCase.executeOnBackground())
            isLoading = false
            updateSlice(sliceUri)
        }
    }

    private fun Uri.getDateFromOrderUri(): String? {
        (getQueryParameter(SellerActionConst.Params.ORDER_DATE)).let { orderDate ->
            orderDate?.split(SellerActionConst.DATE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }.let { delimitedDate ->
                return delimitedDate?.split(SellerActionConst.DATE_RANGE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }
            }
        }
    }

}