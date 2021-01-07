package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.product.manage.feature.campaignstock.ui.dataview.uimodel.SellableStockProductUIModel
import javax.inject.Inject

class CampaignMainStockViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

    private var variantStockAvailability: HashMap<String, Boolean> = hashMapOf()

    private val mShowStockInfoLiveData = MutableLiveData<Boolean>()
    private val mShouldDisplayVariantStockWarningLiveData = MutableLiveData<Boolean>()

    val shouldDisplayVariantStockWarningLiveData: LiveData<Boolean>
        get() = mShouldDisplayVariantStockWarningLiveData

    val showStockInfo: LiveData<Boolean>
        get() = mShowStockInfoLiveData

    fun setStockAvailability(sellableProductList: List<SellableStockProductUIModel>) {
        sellableProductList.forEach {
            val isStockEmpty = it.stock.toIntOrZero() == 0
            variantStockAvailability[it.productId] = isStockEmpty
        }
        setStockWarningAndInfo()
    }

    fun setVariantStock(productId: String, stock: Int) {
        val isStockEmpty = stock == 0
        variantStockAvailability[productId] = isStockEmpty
        setStockWarningAndInfo()
    }

    private fun setShowStockInfo(isAllStockEmpty: Boolean) {
        val showStockInfo = mShowStockInfoLiveData.value
        val shouldShow = !isAllStockEmpty

        if(showStockInfo != shouldShow) {
            mShowStockInfoLiveData.postValue(shouldShow)
        }
    }

    private fun setStockWarningAndInfo() {
        variantStockAvailability.all { it.value }.let { isAllStockEmpty ->
            if (mShouldDisplayVariantStockWarningLiveData.value != isAllStockEmpty) {
                mShouldDisplayVariantStockWarningLiveData.postValue(isAllStockEmpty)
            }
            setShowStockInfo(isAllStockEmpty)
        }
    }
}