package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.merchantvouchergrid

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.Redirection
import com.tokopedia.discovery2.usecase.MerchantVoucherUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.notifications.common.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MerchantVoucherGridViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    @JvmField
    @Inject
    var useCase: MerchantVoucherUseCase? = null

    private val _couponList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    val couponList: LiveData<ArrayList<ComponentsItem>> = _couponList

    private val _seeMore: MutableLiveData<Redirection> = MutableLiveData()
    val seeMore: LiveData<Redirection> = _seeMore

    private val _loadError: MutableLiveData<Boolean> = MutableLiveData()
    val loadError: LiveData<Boolean> = _loadError

    fun fetchCoupons() {
        launchCatchError(block = {
            useCase?.loadFirstPageComponents(component.id, component.pageEndPoint).run {
                setVoucherList()

                this@MerchantVoucherGridViewModel.syncData.value = this
            }

        }, onError = {
            Timber.e(it)
        })
    }

    private fun setVoucherList() {
        getVoucherList()?.let {
            if (it.isNotEmpty()) {
                _loadError.value = false
                _couponList.value = it

                setSeeMoreInfo(it.first())
            } else {
                _loadError.value = true
            }
        }
    }

    private fun setSeeMoreInfo(component: ComponentsItem) {
        val redirection = component.compAdditionalInfo?.redirection

        if (redirection?.ctaText?.isEmpty() == false) {
            _seeMore.value = redirection
        }
    }

    private fun getVoucherList(): ArrayList<ComponentsItem>? {
        component.getComponentsItem()?.let { productList ->
            return productList as ArrayList<ComponentsItem>
        }
        return null
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()
}
