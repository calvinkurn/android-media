package com.tokopedia.affiliate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateSSAShopUiModel
import com.tokopedia.affiliate.usecase.AffiliateSSAShopUseCase
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import javax.inject.Inject

class AffiliateSSAShopViewModel @Inject constructor(
    private val affiliateSSAShopUseCase: AffiliateSSAShopUseCase
) : BaseViewModel() {

    private val ssaShopList = MutableLiveData<List<Visitable<AffiliateAdapterTypeFactory>>>()
    private val errorMessage = MutableLiveData<Throwable>()
    private val progressBar = MutableLiveData(true)
    private val noMoreDataAvailable = MutableLiveData(false)

    companion object {
        private const val SUCCESS = 1
    }

    fun fetchSSAShopList(page: Int, limit: Int = 10) {
        launchCatchError(
            block = {
                affiliateSSAShopUseCase.getSSAShopList(page, limit).getSSAShopList?.let {
                    if (it.data?.status == SUCCESS) {
                        progressBar.value = false
                        noMoreDataAvailable.value = !it.data.pageInfo?.hasNext.orFalse()
                        ssaShopList.value =
                            it.data.shopData?.mapNotNull { ssaShop ->
                                AffiliateSSAShopUiModel(
                                    ssaShop
                                )
                            }
                    } else {
                        progressBar.value = false
                        errorMessage.value = Throwable(it.data?.error?.message)
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
