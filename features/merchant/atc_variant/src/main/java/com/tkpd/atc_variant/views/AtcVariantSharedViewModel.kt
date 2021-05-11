package com.tkpd.atc_variant.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams

/**
 * Created by Yehezkiel on 11/05/21
 */
class AtcVariantSharedViewModel : ViewModel() {

    val aggregatorParams: LiveData<ProductVariantBottomSheetParams>
        get() = _aggregatorParams
    private val _aggregatorParams = MutableLiveData<ProductVariantBottomSheetParams>()

    fun setAtcBottomSheetParams(aggregatorParams: ProductVariantBottomSheetParams) {
        _aggregatorParams.value = aggregatorParams
    }
}