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

    @Inject
    lateinit var mvcSummaryUseCase: MVCSummaryUseCase

    private val _mvcData = MutableLiveData<MvcData>()
    private val _errorState = MutableLiveData<Boolean>()
    val mvcData: LiveData<MvcData> = _mvcData
    val errorState: LiveData<Boolean> = _errorState


    fun fetchDataForCoupons() {
        if (_mvcData.value == null && _errorState.value != true)
            getDataFromUseCase()
    }

    fun getDataFromUseCase() {
        launchCatchError(block = {
            if (getShopID().isNotEmpty()) {
                val response =
                    mvcSummaryUseCase.getResponse(mvcSummaryUseCase.getQueryParams(getShopID()))
                response.data?.let {
                    _mvcData.value = MvcData(it.animatedInfoList)
                    _errorState.value = it.isShown?.not()
                }
            }else{
                _errorState.value = true
            }
        },
            onError = {
                _errorState.value = true
            })
    }

    fun getShopID(): String {
        return components.data?.firstOrNull()?.shopIds?.firstOrNull()?.toString() ?: ""
    }

    fun updateData(shopID: Any, isShown: Boolean, listInfo: ArrayList<AnimatedInfos>?) {
        if(shopID == getShopID()){
            _mvcData.value = MvcData(listInfo)
            _errorState.value = !isShown
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}