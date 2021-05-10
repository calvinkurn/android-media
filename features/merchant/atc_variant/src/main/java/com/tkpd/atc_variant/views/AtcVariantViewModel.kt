package com.tkpd.atc_variant.views

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkpd.atc_variant.usecase.GetProductVariantAggregatorUseCase
import com.tkpd.atc_variant.util.AtcVariantMapper
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregator
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import javax.inject.Inject

/**
 * Created by Yehezkiel on 10/05/21
 */
class AtcVariantViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                              private val aggregatorUseCase: GetProductVariantAggregatorUseCase) : ViewModel() {

    private val _initialVariantData = MutableLiveData<List<VariantCategory>?>()
    val initialVariantData: LiveData<List<VariantCategory>?>
        get() = _initialVariantData

    private val _onVariantClickedData = MutableLiveData<VariantClickedUiData>()
    val onVariantClickedData: LiveData<VariantClickedUiData>
        get() = _onVariantClickedData

    private val _aggregatorData = MutableLiveData<ProductVariantAggregator>()
    val aggregatorData: LiveData<ProductVariantAggregator>
        get() = _aggregatorData

    fun processVariant(data: ProductVariant, selectedVariantIds: MutableMap<String, String>?) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _initialVariantData.postValue(AtcVariantMapper.processVariant(data, selectedVariantIds))
        }) {}
    }

    fun onVariantClicked(selectedVariantIds: MutableMap<String, String>?,
                         isPartialySelected: Boolean, variantLevel: Int, imageVariant: String) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val processedVariant = AtcVariantMapper.processVariant(aggregatorData.value?.variantData, selectedVariantIds, variantLevel, isPartialySelected)
            _onVariantClickedData.postValue(VariantClickedUiData(isPartialySelected, processedVariant, imageVariant, selectedVariantIds))
        }) {}
    }

    fun getAggregatorData() {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            val result = aggregatorUseCase.executeOnBackground(GetProductVariantAggregatorUseCase.createRequestParams("123", "", "", "", UserLocationRequest()))
            _aggregatorData.postValue(result)
        }) {
            Log.e("datanya", "error ${it.message}")
        }
    }
}

data class VariantClickedUiData(
        val isPartialySelected: Boolean = false,
        val variantCategory: List<VariantCategory>? = null,
        val selectedVariantImage: String = "",
        val selectedVariantIds: MutableMap<String, String>? = null
)