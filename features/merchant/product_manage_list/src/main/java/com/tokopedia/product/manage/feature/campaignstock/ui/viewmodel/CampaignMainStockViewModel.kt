package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import javax.inject.Inject

class CampaignMainStockViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private val mVariantStockAvailabilityLiveData = MutableLiveData<HashMap<String, Boolean>>(hashMapOf())
    private val mShowStockInfo = MutableLiveData<Boolean>()

    private val mShouldDisplayVariantStockWarningLiveData = MediatorLiveData<Boolean>().apply {
        addSource(mVariantStockAvailabilityLiveData) { variantMap ->
            variantMap.all { it.value }.let { isAllStockEmpty ->
                if (value != isAllStockEmpty) {
                    postValue(isAllStockEmpty)
                }
                setShowStockInfo(isAllStockEmpty)
            }
        }
    }

    val shouldDisplayVariantStockWarningLiveData: LiveData<Boolean>
        get() = mShouldDisplayVariantStockWarningLiveData

    val showStockInfo: LiveData<Boolean>
        get() = mShowStockInfo

    fun setStockAvailability(sellableProductList: List<SellableStockProductUIModel>) {
        val variantStockMap = hashMapOf<String, Boolean>()

        sellableProductList.forEach {
            val isStockEmpty = it.stock.toIntOrZero() == 0
            variantStockMap[it.productId] = isStockEmpty
        }

        mVariantStockAvailabilityLiveData.postValue(variantStockMap)
    }

    fun setVariantStock(productId: String, stock: Int) {
        mVariantStockAvailabilityLiveData.value?.let {
            val isStockEmpty = stock == 0
            it[productId] = isStockEmpty
            mVariantStockAvailabilityLiveData.postValue(it)
        }
    }

    private fun setShowStockInfo(isAllStockEmpty: Boolean) {
        val showStockInfo = mShowStockInfo.value
        val shouldShow = !isAllStockEmpty

        if(showStockInfo != shouldShow) {
            mShowStockInfo.postValue(shouldShow)
        }
    }
}