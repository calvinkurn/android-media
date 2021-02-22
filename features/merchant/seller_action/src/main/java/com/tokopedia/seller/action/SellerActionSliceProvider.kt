package com.tokopedia.seller.action

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.slice.Slice
import androidx.slice.SliceProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.seller.action.common.analytics.SellerActionAnalytics
import com.tokopedia.seller.action.common.const.SellerActionConst
import com.tokopedia.seller.action.common.di.DaggerSellerActionComponent
import com.tokopedia.seller.action.common.interfaces.SellerActionContract
import com.tokopedia.seller.action.common.presentation.model.SellerActionStatus
import com.tokopedia.seller.action.common.presentation.presenter.SellerActionPresenter
import com.tokopedia.seller.action.common.presentation.slices.SellerFailureSlice
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice
import com.tokopedia.seller.action.order.domain.model.Order
import com.tokopedia.seller.action.order.presentation.mapper.SellerOrderMapper
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SellerActionSliceProvider: SliceProvider(), SellerActionContract.View{

    companion object {
        private const val APPLINK_DEBUGGER = "APPLINK_DEBUGGER"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfigImpl

    @Inject
    lateinit var presenter: SellerActionPresenter

    @Inject
    lateinit var analytics: SellerActionAnalytics

    private var mainOrderStatus: SellerActionStatus? = null
    private var isLoading: Boolean = false
    private var isAlreadyInjected: Boolean = false

    private var sliceHashMap: HashMap<Uri, SellerSlice?> = HashMap()

    override fun onCreateSliceProvider(): Boolean {
        LocalCacheHandler(context, APPLINK_DEBUGGER)
        return context != null
    }

    override fun onBindSlice(sliceUri: Uri): Slice? {
        if (!isAlreadyInjected) {
            // Init GraphqlClient first before injecting because GraphqlRepository would require GraphqlClient to be initialized first
            context?.let { GraphqlClient.init(it) }
            injectDependencies()
            presenter.attachView(this)
            isAlreadyInjected = true
        }

        if (!remoteConfig.getBoolean(RemoteConfigKey.ENABLE_SLICE_ACTION_SELLER)) {
            context?.let {
                return SellerFailureSlice(it, sliceUri).getSlice()
            }
        }

        // Returning slice if the uri has been called/bind before
        // This will avoid infinite gql call loop in certain cases
        sliceHashMap[sliceUri]?.let {
            return it.getSlice()
        }

        if (userSession.isLoggedIn) {
            when(sliceUri.path) {
                SellerActionConst.Deeplink.ORDER -> {
                    val date = sliceUri.getDateFromOrderUri()
                    val canLoadData = (mainOrderStatus == null || mainOrderStatus == SellerActionStatus.NotLogin) && !isLoading
                    if (canLoadData) {
                        isLoading = true
                        presenter.getOrderList(sliceUri, date, sliceHashMap)
                    }
                }
            }
        } else {
            mainOrderStatus = SellerActionStatus.NotLogin
            sendTrackingByStatus()
        }
        return createNewSlice(sliceUri, isLoading)?.getSlice()
    }

    override fun onSuccessGetOrderList(sliceUri: Uri, orderList: List<Order>) {
        mainOrderStatus = SellerActionStatus.Success(orderList)
        isLoading = false
        updateSlice(sliceUri)
    }

    override fun onErrorGetOrderList(sliceUri: Uri) {
        mainOrderStatus = SellerActionStatus.Fail
        isLoading = false
        updateSlice(sliceUri)
    }

    private fun injectDependencies() {
        DaggerSellerActionComponent.builder()
                .baseAppComponent((requireNotNull(context).applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createNewSlice(sliceUri: Uri, isLoading: Boolean = false): SellerSlice? {
        return when (sliceUri.path) {
            SellerActionConst.Deeplink.ORDER -> {
                val date = sliceUri.getDateFromOrderUri()
                context?.let {
                    SellerOrderMapper(it, sliceUri, date).run {
                        if (isLoading) {
                            analytics.sendSellerActionImpression(SellerActionStatus.Loading)
                            mainOrderStatus = SellerActionStatus.Loading
                        }
                        sendTrackingByStatus()
                        getSlice(mainOrderStatus).also { slice ->
                            if (mainOrderStatus is SellerActionStatus.Success || mainOrderStatus is SellerActionStatus.Fail) {
                                mainOrderStatus = null
                                sliceHashMap[sliceUri] = slice
                            }
                        }
                    }
                }
            }
            else -> {
                sendTrackingByStatus()
                mainOrderStatus = null
                context?.let {
                    SellerFailureSlice(it, sliceUri).also { slice ->
                        sliceHashMap[sliceUri] = slice
                    }
                }
            }
        }
    }

    private fun updateSlice(sliceUri: Uri) {
        context?.contentResolver?.notifyChange(sliceUri, null)
    }

    private fun Uri.getDateFromOrderUri(): String? {
        (getQueryParameter(SellerActionConst.Params.ORDER_DATE)).let { orderDate ->
            orderDate?.split(SellerActionConst.DATE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }.let { delimitedDate ->
                return delimitedDate?.split(SellerActionConst.DATE_RANGE_DELIMITER)?.first()?.takeIf { it.isNotEmpty() }
            }
        }
    }

    private fun sendTrackingByStatus() {
        mainOrderStatus?.let { status ->
            analytics.sendSellerActionImpression(status)
        }
    }

}