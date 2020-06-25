package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_IDENTIFIER_HAS_SIZECHART
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
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

    var clickedVariantPhotoItemPosition: Int? = null

    var isSingleVariantTypeIsSelected = false

    private var variantValuesLayoutMap: TreeMap<Int, Int> = TreeMap()

    private var selectedVariantUnit: HashMap<Int, MutableList<Unit>> = HashMap()

    private var selectedVariantUnitValuesMap: HashMap<Int, MutableList<UnitValue>> = HashMap()

    private val mSelectedVariantUnitValuesLevel1 = MutableLiveData<List<UnitValue>>()

    private val mSelectedVariantUnitValuesLevel2 = MutableLiveData<List<UnitValue>>()

    private val mGetCategoryVariantCombinationResult = MutableLiveData<Result<GetCategoryVariantCombinationResponse>>()
    val getCategoryVariantCombinationResult: LiveData<Result<GetCategoryVariantCombinationResponse>>
        get() = mGetCategoryVariantCombinationResult

    var productInputModel = MutableLiveData<ProductInputModel>()

    private var mVariantSizechart = MutableLiveData(PictureVariantInputModel())
    val variantSizechart: LiveData<PictureVariantInputModel> get() = mVariantSizechart

    private var mIsVariantSizechartVisible = MutableLiveData(false)
    val isVariantSizechartVisible: LiveData<Boolean> get() = mIsVariantSizechartVisible

    private val mIsInputValid = MediatorLiveData<Boolean>().apply {

        addSource(mSelectedVariantUnitValuesLevel1) {
            val isVariantUnitValuesLevel1Empty = mSelectedVariantUnitValuesLevel1.value?.isEmpty()
                    ?: true
            val isVariantUnitValuesLevel2Empty = mSelectedVariantUnitValuesLevel2.value?.isEmpty()
                    ?: true
            this.value = isInputValid(isVariantUnitValuesLevel1Empty, isVariantUnitValuesLevel2Empty, isSingleVariantTypeIsSelected)
        }
        addSource(mSelectedVariantUnitValuesLevel2) {
            val isVariantUnitValuesLevel1Empty = mSelectedVariantUnitValuesLevel1.value?.isEmpty()
                    ?: true
            val isVariantUnitValuesLevel2Empty = mSelectedVariantUnitValuesLevel2.value?.isEmpty()
                    ?: true
            this.value = isInputValid(isVariantUnitValuesLevel1Empty, isVariantUnitValuesLevel2Empty, isSingleVariantTypeIsSelected)
        }
    }
    val isInputValid: LiveData<Boolean> get() = mIsInputValid

    private fun isInputValid(isVariantUnitValuesLevel1Empty: Boolean, isVariantUnitValuesLevel2Empty: Boolean, isSingleVariantTypeIsSelected: Boolean): Boolean {

        if (isSingleVariantTypeIsSelected && !isVariantUnitValuesLevel1Empty) return true
        if (isSingleVariantTypeIsSelected && !isVariantUnitValuesLevel2Empty) return true

        return !isVariantUnitValuesLevel1Empty && !isVariantUnitValuesLevel2Empty
    }

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

    fun updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValues: List<UnitValue>) {
        mSelectedVariantUnitValuesLevel1.value = selectedVariantUnitValues
    }

    fun updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValues: List<UnitValue>) {
        mSelectedVariantUnitValuesLevel2.value = selectedVariantUnitValues
    }

    fun updateVariantInputModel(variantDetails: List<VariantDetail>) {
        val variantInputModel = productInputModel.value?.variantInputModel
        val variantTypesSelected = variantValuesLayoutMap.map { it.key }
        val variantDetailsSelected =
                variantDetails.filterIndexed { index, _ -> variantTypesSelected.contains(index) }

        variantInputModel?.apply {
            this.products = mapProducts()
            this.selections = mapSelections(variantDetailsSelected)
            this.sizecharts = variantSizechart.value ?: PictureVariantInputModel()
        }
    }

    fun updateSizechart(url: String) {
        val newSizechart = PictureVariantInputModel()
        newSizechart.filePath = url
        mVariantSizechart.value = newSizechart
    }

    fun updateSizechart(newSizechart: PictureVariantInputModel) {
        mVariantSizechart.value = newSizechart
    }

    fun updateSizechartFieldVisibility(variantDetail: VariantDetail) {
        if (variantDetail.identifier == VARIANT_IDENTIFIER_HAS_SIZECHART) {
            // toggle boolean for visibility
            mIsVariantSizechartVisible.value = mIsVariantSizechartVisible.value != true
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
            unit?.run {
                result.add(SelectionInputModel(
                        variantDetail.variantID.toString(),
                        variantDetail.name,
                        unit.variantUnitID.toString(),
                        unit.unitName,
                        variantDetail.identifier,
                        mapOptions(it.value)
                ))
            }
            index++
        }
        return result
    }

    private fun mapUnit(variantDetail: VariantDetail, value: List<UnitValue>): Unit? {
        val unitValue = value.firstOrNull()
        return variantDetail.units.filter {
            it.unitValues.contains(unitValue)
        }.firstOrNull()
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
        val selectedLevel1 = selectedVariantUnitValuesMap[0]
        selectedLevel1?.let { unitValueLevel1 ->
            val selectedLevel2 = selectedVariantUnitValuesMap[1]
            if (selectedLevel2.isNullOrEmpty()) {
                unitValueLevel1.forEachIndexed { optionIndex, _ ->
                    result.add(
                            ProductVariantInputModel(
                                    combination = listOf(optionIndex),
                                    status = STATUS_ACTIVE_STRING
                            )
                    )
                }
            } else {
                unitValueLevel1.forEachIndexed { optionIndexLevel1, _ ->
                    selectedLevel2.forEachIndexed { optionIndexLevel2, _ ->
                        result.add(
                                ProductVariantInputModel(
                                        combination = listOf(optionIndexLevel1, optionIndexLevel2),
                                        status = STATUS_ACTIVE_STRING
                                )
                        )
                    }
                }
            }
        }
        return result
    }
}