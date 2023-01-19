package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.usecase.AffiliateSSAShopUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class AffiliateSSAShopViewModel @Inject constructor(
    private val affiliateSSAShopUseCase: AffiliateSSAShopUseCase
) : BaseViewModel() {

    private val ssaShopList = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val errorMessage = MutableLiveData<Throwable>()
    private val progressBar = MutableLiveData<Boolean>()
    private val noMoreDataAvailable = MutableLiveData(true)

    companion object {
        private const val SUCCESS = 1
    }

    fun fetchSSAShopList(page: Int, limit: Int = 10) {
        launchCatchError(
            block = {
                affiliateSSAShopUseCase.getSSAShopList(page, limit).data?.let {
                    if (it.status == SUCCESS) {
                        noMoreDataAvailable.value = it.shopData.isNullOrEmpty()
                        // TODO convert to promotion shop model
                    } else {
                        progressBar.value = false
                        errorMessage.value = Throwable(it.error?.message)
                    }
                }
            },
            onError = {
                progressBar.value = false
                it.printStackTrace()
                errorMessage.value = it
            }
        )
    }

    fun getSSAShopList(): LiveData<List<Visitable<AffiliateAdapterTypeFactory>>> = ssaShopList
    fun getErrorMessage(): LiveData<Throwable> = errorMessage
    fun progressBar(): LiveData<Boolean> = progressBar
    fun noMoreDataAvailable(): LiveData<Boolean> = noMoreDataAvailable
}
