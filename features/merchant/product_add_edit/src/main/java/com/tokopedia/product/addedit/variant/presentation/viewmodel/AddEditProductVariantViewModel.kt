package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.data.model.GetCategoryVariantCombinationResponse
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.domain.GetCategoryVariantCombinationUseCase
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.COLOUR_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_IDENTIFIER_HAS_SIZECHART
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.model.*
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

    // adapter - layout position map
    private var variantValuesLayoutMap: TreeMap<Int, Int> = TreeMap()

    // layout position - selected unit map
    private var selectedVariantUnitMap: HashMap<Int, Unit> = HashMap()

    // layout position - selected unit values map
    private var selectedVariantUnitValuesMap: HashMap<Int, MutableList<UnitValue>> = HashMap()

    private var selectedVariantDetails = mutableListOf<VariantDetail>()
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

    val isEditMode: LiveData<Boolean> = Transformations.map(productInputModel) {
        it.productId > 0
    }

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

    fun updateSelectedVariantUnitMap(layoutPosition: Int, unit: Unit) {
        selectedVariantUnitMap[layoutPosition] = unit
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

    fun updateVariantInputModel(variantPhotos: List<VariantPhoto>) {
        productInputModel.value?.variantInputModel?.apply {
            products = mapProducts(variantPhotos)
            selections = mapSelections(selectedVariantDetails)
            sizecharts = variantSizechart.value ?: PictureVariantInputModel()
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

    fun updateSizechartFieldVisibility(variantDetail: VariantDetail, isVisible: Boolean) {
        if (variantDetail.identifier == VARIANT_IDENTIFIER_HAS_SIZECHART) {
            mIsVariantSizechartVisible.value = isVisible
        }
    }

    fun updateSizechartFieldVisibility() {
        mIsVariantSizechartVisible.value = selectedVariantDetails.any {
            it.identifier == VARIANT_IDENTIFIER_HAS_SIZECHART
        }
    }

    fun getSelectedVariantUnit(layoutPosition: Int): Unit {
        return if (selectedVariantUnitMap.containsKey(layoutPosition)) {
            selectedVariantUnitMap[layoutPosition] ?: Unit()
        } else Unit()
    }

    fun getSelectedVariantUnitValues(layoutPosition: Int): MutableList<UnitValue> {
        return if (selectedVariantUnitValuesMap.containsKey(layoutPosition)) {
            selectedVariantUnitValuesMap[layoutPosition] ?: mutableListOf()
        } else mutableListOf()
    }

    fun removeVariantValueLayoutMapEntry(adapterPosition: Int) {
        variantValuesLayoutMap.remove(adapterPosition)
    }

    fun isVariantUnitValuesEmpty(layoutPosition: Int): Boolean {
        if (selectedVariantUnitValuesMap.containsKey(layoutPosition)) {
            return selectedVariantUnitValuesMap[layoutPosition]?.isEmpty() ?: true
        }
        return true
    }

    fun removeSelectedVariantUnitValue(layoutPosition: Int, position: Int) {
        val selectedVariantUnitValues = this.selectedVariantUnitValuesMap[layoutPosition]
        selectedVariantUnitValues?.removeAt(position)
    }

    fun removeVariant() {
        productInputModel.value?.variantInputModel = VariantInputModel()
        variantValuesLayoutMap = TreeMap()
        selectedVariantUnitValuesMap = HashMap()
        mSelectedVariantUnitValuesLevel1.value = emptyList()
        mSelectedVariantUnitValuesLevel2.value = emptyList()
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
        return variantDetail.units.firstOrNull {
            it.unitValues.contains(unitValue)
        }
    }

    private fun mapOptions(unit: List<UnitValue>): List<OptionInputModel> =
            unit.map {
                OptionInputModel(
                        it.variantUnitValueID.toString(),
                        it.value,
                        it.hex
                )
            }

    private fun mapProducts(variantPhotos: List<VariantPhoto>): List<ProductVariantInputModel> {
        val result: MutableList<ProductVariantInputModel> = mutableListOf()
        val selectedLevel1: MutableList<UnitValue> = selectedVariantUnitValuesMap
                .getOrElse(VARIANT_VALUE_LEVEL_ONE_POSITION) { mutableListOf() }
        val selectedLevel2: MutableList<UnitValue> = selectedVariantUnitValuesMap
                .getOrElse(VARIANT_VALUE_LEVEL_TWO_POSITION) { mutableListOf() }

        selectedLevel1.forEachIndexed { optionIndexLevel1, _ ->
            val variantPicture = mapVariantPhoto(variantPhotos.getOrNull(optionIndexLevel1))
            if (selectedLevel2.isEmpty()) {
                result.add(mapProductVariant(
                        variantPicture,
                        listOf(optionIndexLevel1)
                ))
            } else {
                selectedLevel2.forEachIndexed { optionIndexLevel2, _ ->
                    result.add(mapProductVariant(
                            variantPicture,
                            listOf(optionIndexLevel1, optionIndexLevel2)
                    ))
                }
            }
        }

        return result
    }

    private fun mapProductVariant(
            variantPicture: List<PictureVariantInputModel>,
            combination: List<Int>
    ): ProductVariantInputModel {
        val products =
                productInputModel.value?.variantInputModel?.products.orEmpty()
        val productVariant = products.firstOrNull {
            it.combination == combination
        }

        return if (productVariant == null) {
            ProductVariantInputModel(
                    pictures = variantPicture,
                    combination = combination,
                    status = STATUS_ACTIVE_STRING
            )
        } else {
            productVariant.pictures = variantPicture
            productVariant
        }
    }

    private fun mapVariantPhoto(variantPhoto: VariantPhoto?): List<PictureVariantInputModel> {
        return variantPhoto?.let {
            val result = PictureVariantInputModel(
                    filePath = variantPhoto.imageUrlOrPath
            )
            listOf(result)
        } ?: emptyList()
    }

    fun extractSelectedVariantDetails(productInputModel: ProductInputModel): List<VariantDetail> {
        // part of the product input model
        val selectedVariantInputModels = productInputModel.variantInputModel.selections
        // selected variant detail models
        val selectedVariantDetails = mutableListOf<VariantDetail>()
        selectedVariantInputModels.forEach { inputModel ->
            // selected variant types
            val id = inputModel.variantId.toIntOrNull() ?: 0
            val identifier = inputModel.identifier
            val name = inputModel.variantName
            // selected variant unit values
            val unitValues = mutableListOf<UnitValue>()
            val optionInputModels = inputModel.options
            optionInputModels.forEach {
                val unitValueId = it.unitValueID.toIntOrNull() ?: 0
                val unitValueName = it.value
                val unitValue = UnitValue(variantUnitValueID = unitValueId, value = unitValueName)
                unitValues.add(unitValue)
            }
            // selected variant unit
            val unitId = inputModel.unitID.toIntOrNull() ?: 0
            val unitName = inputModel.unitName
            val unit = Unit(variantUnitID = unitId, unitName = unitName, unitValues = unitValues)
            // selected variant detail
            val selectedVariantDetail = VariantDetail(variantID = id, identifier = identifier, name = name, units = listOf(unit))
            // fill selected variant details
            selectedVariantDetails.add(selectedVariantDetail)
        }
        return selectedVariantDetails
    }

    fun getSelectedVariantDetails(): List<VariantDetail> {
        return this.selectedVariantDetails
    }

    fun setSelectedVariantDetails(selectedVariantDetails: List<VariantDetail>) {
        this.selectedVariantDetails = selectedVariantDetails.toMutableList()
    }

    fun getProductVariantPhotos(productInputModel: ProductInputModel): List<VariantPhoto> {
        val variantPhotos = mutableListOf<VariantPhoto>()
        // get variant photo name
        val colorVariant = productInputModel.variantInputModel.selections.firstOrNull {
            it.variantId == COLOUR_VARIANT_TYPE_ID.toString()
        } ?: SelectionInputModel()
        // get variant image urls
        val photoUrls = mutableListOf<String>()
        productInputModel.variantInputModel.products.forEach {
            if (it.pictures.isNotEmpty()) {
                it.pictures.forEach { pictureInputModel ->
                    photoUrls.add(pictureInputModel.urlOriginal)
                }
            }
        }
        // compile variant photos
        colorVariant.options.forEachIndexed { index, optionInputModel ->
            val variantUnitValueName = optionInputModel.value
            val photoUrl = photoUrls.getOrNull(index) ?: ""
            variantPhotos.add(VariantPhoto(variantUnitValueName, photoUrl))
        }
        return variantPhotos
    }
}