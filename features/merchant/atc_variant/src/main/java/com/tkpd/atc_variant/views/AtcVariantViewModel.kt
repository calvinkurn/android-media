package com.tkpd.atc_variant.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tkpd.atc_variant.data.uidata.VariantComponentDataModel
import com.tkpd.atc_variant.data.uidata.VariantHeaderDataModel
import com.tkpd.atc_variant.data.uidata.VariantShimmeringDataModel
import com.tkpd.atc_variant.usecase.GetProductVariantAggregatorUseCase
import com.tkpd.atc_variant.util.AtcCommonMapper
import com.tkpd.atc_variant.util.AtcCommonMapper.asFail
import com.tkpd.atc_variant.util.AtcCommonMapper.asSuccess
import com.tkpd.atc_variant.util.AtcCommonMapper.generateHeaderDataModel
import com.tkpd.atc_variant.util.AtcVariantMapper
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregator
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Yehezkiel on 10/05/21
 */
class AtcVariantViewModel @Inject constructor(private val dispatcher: CoroutineDispatchers,
                                              private val aggregatorUseCase: GetProductVariantAggregatorUseCase) : ViewModel() {

    private val _initialData = MutableLiveData<Result<List<AtcVariantVisitable>>>()
    val initialData: LiveData<Result<List<AtcVariantVisitable>>>
        get() = _initialData

    private val _aggregatorData = MutableLiveData<ProductVariantAggregator>()

    fun onVariantClicked(selectedOptionKey: String,
                         selectedOptionId: String,
                         variantImage: String, // only use when user click partially to update the image
                         variantLevel: Int) {
        viewModelScope.launchCatchError(dispatcher.io, block = {

            val variantDataModel = (_initialData.value as Success).data.firstOrNull {
                it is VariantComponentDataModel
            } as? VariantComponentDataModel

            val selectedVariantIds = variantDataModel?.mapOfSelectedVariant?.toMutableMap()
            selectedVariantIds?.let { selectedIds ->
                selectedIds[selectedOptionKey] = selectedOptionId
            }

            val processedVariant = AtcVariantMapper.processVariant(_aggregatorData.value?.variantData,
                    selectedVariantIds,
                    variantLevel
            )

            val list = updateVisitable(
                    _aggregatorData.value?.variantData ?: ProductVariant(),
                    processedVariant,
                    selectedVariantIds,
                    variantImage)

            _initialData.postValue(list.asSuccess())
        }) {}
    }

    fun decideInitialValue(aggregatorParams: ProductVariantBottomSheetParams) {
        viewModelScope.launchCatchError(dispatcher.io, block = {
            _initialData.postValue(listOf(VariantShimmeringDataModel(99L)).asSuccess())
            /**
             * If data completely provided from previous page, use that
             * if not call GQL
             */
            val result = if (aggregatorParams.variantAggregator.isAggregatorEmpty()) {
                aggregatorUseCase.executeOnBackground(GetProductVariantAggregatorUseCase.createRequestParams(aggregatorParams.productId,
                        aggregatorParams.pageSource, aggregatorParams.whId, aggregatorParams.pdpSession, UserLocationRequest()))
            } else {
                aggregatorParams.variantAggregator
            }

            _aggregatorData.postValue(result)

            val initialVariantSelectedOptionIds = AtcVariantMapper.mapVariantIdentifierToHashMap(result.variantData)
            val variantData = AtcVariantMapper.processVariant(result.variantData, initialVariantSelectedOptionIds)
            val visitables = AtcCommonMapper.mapToVisitable(aggregatorParams.productId, aggregatorParams.isTokoNow, initialVariantSelectedOptionIds, result, variantData)

            if (visitables != null) {
                _initialData.postValue(visitables.asSuccess())
            } else {
                _initialData.postValue(Throwable().asFail())
            }
        }) {

        }
    }

    private fun updateVisitable(variantData: ProductVariant,
                                processedVariant: List<VariantCategory>?,
                                selectedVariantIds: MutableMap<String, String>?,
                                variantImage: String): List<AtcVariantVisitable> {
        val isPartiallySelected = AtcVariantMapper.isPartiallySelectedOptionId(selectedVariantIds)

        return (initialData.value as Success).data.map {
            when (it) {
                is VariantComponentDataModel -> {
                    it.copy(listOfVariantCategory = processedVariant,
                            mapOfSelectedVariant = selectedVariantIds
                                    ?: mutableMapOf())
                }
                is VariantHeaderDataModel -> {
                    if (isPartiallySelected) {
                        //update image only when exist
                        it.copy(productImage = variantImage)
                    } else {
                        val selectedChild = variantData.getChildByOptionId(selectedVariantIds?.values?.toList()
                                ?: listOf())
                        val headerData = generateHeaderDataModel(selectedChild)
                        it.copy(productImage = headerData.first, headerData = headerData.second)
                    }
                }
                else -> {
                    it
                }
            }
        }
    }
}