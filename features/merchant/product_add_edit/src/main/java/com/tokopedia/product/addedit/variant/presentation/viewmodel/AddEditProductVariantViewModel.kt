package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.HTTP_PREFIX
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.variant.data.model.GetCategoryVariantCombinationResponse
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.domain.GetCategoryVariantCombinationUseCase
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.COLOUR_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_PRODUCT_STOCK_LIMIT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_CUSTOM_UNIT_VALUE_ID
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

    private var variantDataMap: HashMap<Int, VariantDetail> = HashMap()

    // adapter - layout position map
    private var variantValuesLayoutMap: TreeMap<Int, Int> = TreeMap()

    // layout position - selected unit map
    private var selectedVariantUnitMap: HashMap<Int, Unit> = HashMap()

    // layout position - selected unit values map
    private var selectedVariantUnitValuesMap: HashMap<Int, MutableList<UnitValue>> = HashMap()

    private var selectedVariantDetails = mutableListOf<VariantDetail>()
    private val mSelectedVariantUnitValuesLevel1 = MutableLiveData<MutableList<UnitValue>>()
    private val mSelectedVariantUnitValuesLevel2 = MutableLiveData<MutableList<UnitValue>>()

    private val mGetCategoryVariantCombinationResult = MutableLiveData<Result<GetCategoryVariantCombinationResponse>>()
    val getCategoryVariantCombinationResult: LiveData<Result<GetCategoryVariantCombinationResponse>>
        get() = mGetCategoryVariantCombinationResult

    var productInputModel = MutableLiveData<ProductInputModel>()

    private var mIsVariantPhotosVisible = MutableLiveData<Boolean>()
    val isVariantPhotosVisible: LiveData<Boolean> get() = mIsVariantPhotosVisible

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

    val isSelectedVariantUnitValuesEmpty = MediatorLiveData<Boolean>().apply {
        addSource(mSelectedVariantUnitValuesLevel1) {
            val isVariantUnitValuesLevel1Empty = mSelectedVariantUnitValuesLevel1.value?.isEmpty()
                    ?: true
            val isVariantUnitValuesLevel2Empty = mSelectedVariantUnitValuesLevel2.value?.isEmpty()
                    ?: true
            this.value = isVariantUnitValuesLevel1Empty && isVariantUnitValuesLevel2Empty
        }
        addSource(mSelectedVariantUnitValuesLevel2) {
            val isVariantUnitValuesLevel1Empty = mSelectedVariantUnitValuesLevel1.value?.isEmpty()
                    ?: true
            val isVariantUnitValuesLevel2Empty = mSelectedVariantUnitValuesLevel2.value?.isEmpty()
                    ?: true
            this.value = isVariantUnitValuesLevel1Empty && isVariantUnitValuesLevel2Empty
        }
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

    fun addCustomVariantUnitValue(layoutPosition: Int, selectedVariantUnit: Unit, customVariantUnitValue: UnitValue) {
        if (variantDataMap.containsKey(layoutPosition)) {
            val selectedVariantData = variantDataMap[layoutPosition]
            selectedVariantData?.let { variantData ->
                if (selectedVariantUnit.unitName.isBlank()) {
                    // add the custom variant unit value
                    variantData.units.firstOrNull()?.unitValues?.add(customVariantUnitValue)
                } else {
                    val selectedVariantUnitData = variantData.units.find { unit ->
                        unit.variantUnitID == selectedVariantUnit.variantUnitID
                    }
                    selectedVariantUnitData?.unitValues?.add(customVariantUnitValue)
                }
            }
        }
    }

    fun getVariantValuesLayoutPosition(adapterPosition: Int): Int {
        return if (variantValuesLayoutMap.isEmpty()) VARIANT_VALUE_LEVEL_ONE_POSITION
        else variantValuesLayoutMap[adapterPosition] ?: VARIANT_VALUE_LEVEL_ONE_POSITION
    }

    fun getRenderedLayoutAdapterPosition(): Int {
        return variantValuesLayoutMap.firstEntry().key
    }

    fun getVariantData(layoutPosition: Int): VariantDetail {
        return if (variantDataMap.containsKey(layoutPosition)) {
            return variantDataMap[layoutPosition] ?: VariantDetail()
        } else VariantDetail()
    }

    fun isVariantUnitValuesLayoutEmpty(): Boolean {
        return variantValuesLayoutMap.isEmpty()
    }

    fun updateVariantDataMap(layoutPosition: Int, variantData: VariantDetail) {
        variantDataMap[layoutPosition] = variantData
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

    fun updateSelectedVariantUnitValuesLevel1(selectedVariantUnitValues: MutableList<UnitValue>) {
        mSelectedVariantUnitValuesLevel1.value = selectedVariantUnitValues
    }

    fun updateSelectedVariantUnitValuesLevel2(selectedVariantUnitValues: MutableList<UnitValue>) {
        mSelectedVariantUnitValuesLevel2.value = selectedVariantUnitValues
    }

    fun updateVariantInputModel(selectedVariantDetails: List<VariantDetail>, variantPhotos: List<VariantPhoto>) {
        productInputModel.value?.variantInputModel?.apply {
            products = mapProducts(selectedVariantDetails, variantPhotos)
            selections = mapSelections(selectedVariantDetails)
            sizecharts = mapSizechart(variantSizechart.value)
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

    fun showProductVariantPhotos(selectedVariantDetail: VariantDetail) {
        // if the product has color variant show the photos
        val hasColorVariant = selectedVariantDetail.variantID == COLOUR_VARIANT_TYPE_ID
        if (hasColorVariant) mIsVariantPhotosVisible.value = true
    }

    fun hideProductVariantPhotos(selectedVariantDetail: VariantDetail) {
        // if the product has color variant show the photos
        val hasColorVariant = selectedVariantDetail.variantID == COLOUR_VARIANT_TYPE_ID
        if (hasColorVariant) mIsVariantPhotosVisible.value = false
    }

    fun updateSizechartFieldVisibility(selectedVariantDetails: List<VariantDetail>) {
        var index = -1
        val isSizechartIdentifierExist = selectedVariantDetails.any {
            index += 1
            it.identifier == VARIANT_IDENTIFIER_HAS_SIZECHART
        }

        // get selected variant values for each level
        val unitValuesLevel1 = selectedVariantUnitValuesMap[VARIANT_VALUE_LEVEL_ONE_POSITION].orEmpty()
        val unitValuesLevel2 = selectedVariantUnitValuesMap[VARIANT_VALUE_LEVEL_TWO_POSITION].orEmpty()

        // if identifier exist, then update sizechart visibility
        if (isSizechartIdentifierExist) {
            when (index) {
                VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                    mIsVariantSizechartVisible.value = unitValuesLevel1.isNotEmpty()
                }
                VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                    mIsVariantSizechartVisible.value = unitValuesLevel2.isNotEmpty()
                }
            }
        }
    }

    fun updateSizechartFieldVisibility(variantDetail: VariantDetail, isVisible: Boolean) {
        if (variantDetail.identifier == VARIANT_IDENTIFIER_HAS_SIZECHART) {
            mIsVariantSizechartVisible.value = isVisible
        }
    }

    fun clearProductVariant() {
        productInputModel.value?.variantInputModel?.products = emptyList()
    }

    fun getSelectedVariantUnit(layoutPosition: Int): Unit {
        val selectedVariantUnit = selectedVariantUnitMap[layoutPosition] ?: Unit()
        return if (selectedVariantUnit.unitName.isNotBlank()) {
            selectedVariantUnitMap[layoutPosition] ?: Unit()
        } else {
            // return either the first unit (default selection case) or empty unit (no variant unit)
            variantDataMap[layoutPosition]?.units?.firstOrNull() ?: Unit()
        }
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

    fun removeSelectedVariantUnitValue(layoutPosition: Int, removedUnitValue: UnitValue) {
        val selectedVariantUnitValues = this.selectedVariantUnitValuesMap[layoutPosition]
        selectedVariantUnitValues?.remove(removedUnitValue)
        when (layoutPosition) {
            VARIANT_VALUE_LEVEL_ONE_POSITION -> {
                val selectedVariants = mSelectedVariantUnitValuesLevel1.value
                selectedVariants?.remove(removedUnitValue)
                mSelectedVariantUnitValuesLevel1.value = selectedVariants
            }
            VARIANT_VALUE_LEVEL_TWO_POSITION -> {
                val selectedVariants = mSelectedVariantUnitValuesLevel2.value
                selectedVariants?.remove(removedUnitValue)
                mSelectedVariantUnitValuesLevel2.value = selectedVariants
            }
        }
    }

    fun removeVariant() {
        val isRemoteDataHasVariant = productInputModel.value?.variantInputModel?.isRemoteDataHasVariant
                ?: false // keep isRemoteDataHasVariant old data
        productInputModel.value?.variantInputModel = VariantInputModel(
                isRemoteDataHasVariant = isRemoteDataHasVariant)
        selectedVariantDetails = mutableListOf()
        mSelectedVariantUnitValuesLevel1.value = mutableListOf()
        mSelectedVariantUnitValuesLevel2.value = mutableListOf()
        selectedVariantUnitValuesMap = HashMap()
        variantValuesLayoutMap = TreeMap()
        selectedVariantUnitMap = HashMap()
        mIsVariantPhotosVisible.value = false

        productInputModel.value?.detailInputModel?.categoryId?.let {
            getCategoryVariantCombination(it)
        }

    }

    private fun mapSelections(variantDetailsSelected: List<VariantDetail>): List<SelectionInputModel> {
        val result: MutableList<SelectionInputModel> = mutableListOf()
        var level = 0 // varaint value level
        selectedVariantUnitValuesMap.toSortedMap().forEach {
            // get selected variant detail selected each level
            variantDetailsSelected.getOrNull(level)?.let { variantDetail ->
                val unit = mapUnit(variantDetail, it.value) // get unit from variant detail
                unit?.run {
                    // if unit is not null then map the SelectionInputModel
                    result.add(SelectionInputModel(
                            variantDetail.variantID.toString(),
                            variantDetail.name,
                            unit.variantUnitID.toString(),
                            unit.unitName,
                            variantDetail.identifier,
                            mapOptions(it.value)
                    ))
                    level++
                }
            }
        }
        return result
    }

    private fun mapSizechart(pictureVariantInputModel: PictureVariantInputModel?): PictureVariantInputModel {
        return if (mIsVariantSizechartVisible.value == true) {
            pictureVariantInputModel ?: PictureVariantInputModel()
        } else {
            PictureVariantInputModel()
        }
    }

    private fun mapUnit(variantDetail: VariantDetail, value: List<UnitValue>): Unit? {
        val unitValue = value.firstOrNull()
        return if (unitValue?.variantUnitValueID == VARIANT_CUSTOM_UNIT_VALUE_ID) {
            // condition for custom value (get first unit)
            variantDetail.units.firstOrNull()
        } else {
            // condition for main value
            variantDetail.units.firstOrNull { unit ->
                unit.unitValues.any {
                    it.variantUnitValueID == unitValue?.variantUnitValueID
                }
            }
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

    private fun mapProducts(variantDetails: List<VariantDetail>, variantPhotos: List<VariantPhoto>): List<ProductVariantInputModel> {
        val result: MutableList<ProductVariantInputModel> = mutableListOf()
        val unitValueList: MutableList<List<UnitValue>> = mutableListOf()
        val variantIdList: MutableList<Int> = mutableListOf()

        // init unitValueList and variantIdList
        selectedVariantUnitValuesMap.toSortedMap().forEach {
            if (it.value.isNotEmpty()) {
                unitValueList.add(it.value)
                variantDetails.getOrNull(it.key)?.let { variantDetail ->
                    variantIdList.add(variantDetail.variantID)
                }
            }
        }

        // get value for each level
        val unitValueLevel1 = unitValueList.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION) ?: emptyList()
        val unitValueLevel2 = unitValueList.getOrNull(VARIANT_VALUE_LEVEL_TWO_POSITION) ?: emptyList()
        val variantIdLevel1 = variantIdList.getOrNull(VARIANT_VALUE_LEVEL_ONE_POSITION)
        val variantIdLevel2 = variantIdList.getOrNull(VARIANT_VALUE_LEVEL_TWO_POSITION)

        unitValueLevel1.forEachIndexed { optionIndexLevel1, _ ->
            // init variant picture
            var variantPicture = emptyList<PictureVariantInputModel>()
            if (variantIdLevel1 == COLOUR_VARIANT_TYPE_ID) {
                variantPicture = mapVariantPhoto(variantPhotos.getOrNull(optionIndexLevel1))
            }

            if (unitValueLevel2.isEmpty()) {
                // loop condition when variant having 1 level
                result.add(mapProductVariant(
                        variantPicture,
                        listOf(optionIndexLevel1)
                ))
            } else {
                // loop condition when variant having 2 level
                unitValueLevel2.forEachIndexed { optionIndexLevel2, _ ->
                    // re-init variant picture (condition if color at level 2)
                    if (variantIdLevel2 == COLOUR_VARIANT_TYPE_ID) {
                        variantPicture = mapVariantPhoto(variantPhotos.getOrNull(optionIndexLevel2))
                    }

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
            // condition if adding new product variant (product variant combination not listed in products)
            ProductVariantInputModel(
                    price = productInputModel.value?.detailInputModel?.price.orZero(),
                    stock = MIN_PRODUCT_STOCK_LIMIT,
                    pictures = variantPicture,
                    combination = combination,
                    status = STATUS_ACTIVE_STRING
            )
        } else {
            // condition if updating existing product variant
            val filePath = variantPicture.firstOrNull()?.filePath.orEmpty()
            if (!filePath.startsWith(HTTP_PREFIX)) {
                // condition if updating picture (the url is changed to path)
                productVariant.pictures = variantPicture
            }
            productVariant
        }
    }

    private fun mapVariantPhoto(variantPhoto: VariantPhoto?): List<PictureVariantInputModel> {
        return if (variantPhoto != null && variantPhoto.imageUrlOrPath.isNotEmpty()) {
            val result = PictureVariantInputModel(
                    filePath = variantPhoto.imageUrlOrPath,
                    urlOriginal = variantPhoto.imageUrlOrPath
            )
            listOf(result)
        } else {
            emptyList()
        }
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
        var colorVariantLevel = -1
        val colorVariant = productInputModel.variantInputModel.selections.firstOrNull {
            colorVariantLevel++
            it.variantId == COLOUR_VARIANT_TYPE_ID.toString()
        }

        // compile variant photos
        colorVariant?.options?.forEachIndexed { index, optionInputModel ->
            val variantUnitValueName = optionInputModel.value
            // get variant image url
            val photoUrl = productInputModel.variantInputModel.products.find {
                it.combination.getOrNull(colorVariantLevel) == index
            }?.pictures?.firstOrNull()?.urlOriginal.orEmpty()

            variantPhotos.add(VariantPhoto(variantUnitValueName, photoUrl))
        }
        return variantPhotos
    }
}