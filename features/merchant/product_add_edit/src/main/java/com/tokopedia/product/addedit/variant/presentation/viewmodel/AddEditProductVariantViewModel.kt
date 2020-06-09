package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.data.model.GetCategoryVariantCombinationResponse
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.domain.GetCategoryVariantCombinationUseCase
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class AddEditProductVariantViewModel @Inject constructor(
        coroutineDispatcher: CoroutineDispatcher,
        private val getCategoryVariantCombinationUseCase: GetCategoryVariantCombinationUseCase
) : BaseViewModel(coroutineDispatcher) {

    private var variantValuesLayoutMap: TreeMap<Int, Int> = TreeMap()

    private var selectedVariantUnitValuesMap: HashMap<Int, MutableList<UnitValue>> = HashMap()

    private var selectedVariantDetailValues: HashMap<Int, VariantDetail> = HashMap()


    private val mGetCategoryVariantCombinationResult = MutableLiveData<Result<GetCategoryVariantCombinationResponse>>()
    val getCategoryVariantCombinationResult: LiveData<Result<GetCategoryVariantCombinationResponse>>
        get() = mGetCategoryVariantCombinationResult

    var productInputModel = MutableLiveData<ProductInputModel>()
    var variantSizechartUrl = MutableLiveData<String>("")

    fun getCategoryVariantCombination(categoryId: String) {
        launchCatchError(block = {
            val result = withContext(Dispatchers.IO) {
                getCategoryVariantCombinationUseCase.setParams(categoryId)
                getCategoryVariantCombinationUseCase.executeOnBackground()
            }
            mGetCategoryVariantCombinationResult.value = Success(result)
        }, onError = {
            mGetCategoryVariantCombinationResult.value = Fail(it)
        })
    }

    fun addCustomVariantUnitValue(layoutPosition: Int, customVariantUnitValue: UnitValue) {
        val selectedVariantUnitValues = this.selectedVariantUnitValuesMap[layoutPosition]
        selectedVariantUnitValues?.add(customVariantUnitValue)
    }

    fun getVariantValuesLayoutPosition(adapterPosition: Int): Int {
        return if (variantValuesLayoutMap.isEmpty()) VARIANT_VALUE_LEVEL_ONE_POSITION
        else variantValuesLayoutMap[adapterPosition] ?: VARIANT_VALUE_LEVEL_ONE_POSITION
    }

    fun getRenderedLayoutAdapterPosition(): Int {
        return variantValuesLayoutMap.firstEntry().key
    }

    fun isVariantUnitValuesLayoutEmpty(): Boolean {
        return variantValuesLayoutMap.isEmpty()
    }

    fun updateVariantValuesLayoutMap(adapterPosition: Int, layoutPosition: Int) {
        variantValuesLayoutMap[adapterPosition] = layoutPosition
    }

    fun updateSelectedVariantUnitValuesMap(layoutPosition: Int, selectedVariantUnitValues: MutableList<UnitValue>) {
        selectedVariantUnitValuesMap[layoutPosition] = selectedVariantUnitValues
    }

    fun updateVariantInputModel(variantDetails: List<VariantDetail>) {
        val variantInputModel = productInputModel.value?.variantInputModel
        val variantTypesSelected = variantValuesLayoutMap.map { it.key }
        val variantDetailsSelected =
                variantDetails.filterIndexed { index, _ -> variantTypesSelected.contains(index) }

        variantInputModel?.apply {
            this.products = mapProducts()
            this.selections = mapSelections(variantDetailsSelected)
        }
    }

    fun getSelectedVariantUnitValues(layoutPosition: Int): MutableList<UnitValue> {
        return selectedVariantUnitValuesMap[layoutPosition] ?: mutableListOf()
    }

    fun removeVariantValueLayoutMapEntry(adapterPosition: Int) {
        variantValuesLayoutMap.remove(adapterPosition)
    }

    fun removeSelectedVariantUnitValue(layoutPosition: Int, position: Int) {
        val selectedVariantUnitValues = this.selectedVariantUnitValuesMap[layoutPosition]
        selectedVariantUnitValues?.removeAt(position)
    }

    private fun mapSelections(variantDetailsSelected: List<VariantDetail>): List<SelectionInputModel> {
        val result: MutableList<SelectionInputModel> = mutableListOf()
        var index = 0
        selectedVariantUnitValuesMap.forEach {
            val variantDetail = variantDetailsSelected.getOrElse(index) { VariantDetail() }
            val unit = mapUnit(variantDetail, it.value)
            result.add(SelectionInputModel(
                    variantDetail.variantID.toString(),
                    variantDetail.name,
                    unit.variantUnitID.toString(),
                    unit.unitName,
                    variantDetail.identifier,
                    mapOptions(it.value)
            ))
            index++
        }
        return result
    }

    private fun mapUnit(variantDetail: VariantDetail, value: List<UnitValue>): Unit {
        val unitValue = value.firstOrNull()
        val result = variantDetail.units.filter {
            it.unitValues.contains(unitValue)
        }.firstOrNull()
        return result ?: Unit()
    }

    private fun mapOptions(unit: List<UnitValue>): List<OptionInputModel> =
            unit.map {
                OptionInputModel(
                        it.variantUnitValueID.toString(),
                        it.value,
                        it.hex
                )
            }

    private fun mapProducts(): List<ProductVariantInputModel> {
        val result: MutableList<ProductVariantInputModel> = mutableListOf()
        selectedVariantUnitValuesMap.forEach {
            it.value.forEachIndexed { index, unitValue ->
                result.add(
                        ProductVariantInputModel(
                                combination = listOf(it.key, index),
                                status = STATUS_ACTIVE_STRING
                        )
                )
            }
        }
        return result
    }
}