package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.variant.data.model.GetCategoryVariantCombinationResponse
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.domain.GetCategoryVariantCombinationUseCase
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
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

    private var selectedVariantUnitValuesMap: HashMap<Int, MutableList<UnitValue>> = HashMap()

    private var selectedVariantDetailValues: HashMap<Int, VariantDetail> = HashMap()

    private val mSelectedVariantUnitValuesLevel1 = MutableLiveData<List<UnitValue>>()
    val selectedVariantUnitValuesLevel1: LiveData<List<UnitValue>> get() = mSelectedVariantUnitValuesLevel1

    private val mSelectedVariantUnitValuesLevel2 = MutableLiveData<List<UnitValue>>()
    val selectedVariantUnitValues2: LiveData<List<UnitValue>> get() = mSelectedVariantUnitValuesLevel2

    private val mGetCategoryVariantCombinationResult = MutableLiveData<Result<GetCategoryVariantCombinationResponse>>()
    val getCategoryVariantCombinationResult: LiveData<Result<GetCategoryVariantCombinationResponse>>
        get() = mGetCategoryVariantCombinationResult

    var variantSizechartUrl = MutableLiveData<String>("")

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

}