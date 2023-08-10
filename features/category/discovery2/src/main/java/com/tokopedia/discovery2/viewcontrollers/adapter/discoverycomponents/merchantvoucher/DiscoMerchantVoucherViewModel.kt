package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvoucher

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.mvcwidget.MvcData
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.ArrayList
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class DiscoMerchantVoucherViewModel(
    application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    @JvmField
    @Inject
    var mvcSummaryUseCase: MVCSummaryUseCase? = null

    private val _mvcData = MutableLiveData<MvcData>()
    private val _errorState = MutableLiveData<Boolean>()
    val mvcData: LiveData<MvcData> = _mvcData
    val errorState: LiveData<Boolean> = _errorState

    fun fetchDataForCoupons() {
        if (_mvcData.value == null && _errorState.value != true) {
            getDataFromUseCase()
        }
    }

    fun getDataFromUseCase() {
        launchCatchError(
            block = {
                if (getShopID().isNotEmpty()) {
                    val response =
                        mvcSummaryUseCase?.getQueryParams(getShopID())?.let {
                            mvcSummaryUseCase?.getResponse(it)
                        }
                    response?.data?.let {
                        _errorState.value = it.isShown?.not() ?: true
                        if (it.isShown == true) {
                            _mvcData.value = MvcData(it.animatedInfoList)
                        }
                    }
                } else {
                    _errorState.value = true
                }
            },
            onError = {
                _errorState.value = true
            }
        )
    }

    fun getShopID(): String {
        return components.data?.firstOrNull()?.shopIds?.firstOrNull()?.toString() ?: ""
    }

    fun getProductId(): String {
        return components.data?.firstOrNull()?.productId ?: ""
    }

    fun updateData(shopID: Any, isShown: Boolean, listInfo: ArrayList<AnimatedInfos>?) {
        if (shopID == getShopID()) {
            _errorState.value = !isShown
            if (isShown) {
                _mvcData.value = MvcData(listInfo)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()
}
