package com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class CampaignMainStockViewModel @Inject constructor(): ViewModel() {

    private val mVariantStockAvailabilityLiveData = MutableLiveData<HashMap<String, Boolean>>()

    private val mShouldDisplayVariantStockWarningLiveData = MediatorLiveData<Boolean>().apply {
        addSource(mVariantStockAvailabilityLiveData) { variantMap ->
            variantMap.any { !it.value }.let { hasZeroStock ->
                if (value != hasZeroStock) {
                    value = hasZeroStock
                }
            }
        }
    }
    val shouldDisplayVariantStockWarningLiveData: LiveData<Boolean>
        get() = mShouldDisplayVariantStockWarningLiveData

    fun setVariantStock(productId: String, stock: Int) {
        mVariantStockAvailabilityLiveData.value?.let { variantStock ->
            mVariantStockAvailabilityLiveData.value = variantStock.apply {
                put(productId, stock > 0)
            }
            return
        }

        mVariantStockAvailabilityLiveData.value = hashMapOf(
                Pair(productId, stock > 0)
        )
    }
}