package com.tokopedia.product.addedit.variant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.removeFirst
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.picker.common.PickerResult
import com.tokopedia.product.addedit.common.constant.ProductStatus.STATUS_ACTIVE_STRING
import com.tokopedia.product.addedit.common.util.StringValidationUtil.isAllowedString
import com.tokopedia.product.addedit.common.util.getValueOrDefault
import com.tokopedia.product.addedit.common.util.toBigIntegerSafely
import com.tokopedia.product.addedit.detail.domain.usecase.GetProductTitleValidationUseCase
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.preview.presentation.model.VariantTitleValidationStatus
import com.tokopedia.product.addedit.variant.data.model.AllVariant
import com.tokopedia.product.addedit.variant.data.model.GetVariantCategoryCombinationResponse
import com.tokopedia.product.addedit.variant.data.model.Unit
import com.tokopedia.product.addedit.variant.data.model.UnitValue
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.product.addedit.variant.domain.GetAllVariantUseCase
import com.tokopedia.product.addedit.variant.domain.GetVariantCategoryCombinationUseCase
import com.tokopedia.product.addedit.variant.domain.GetVariantDataByIdUseCase
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.ADD_MODE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.ALL_MODE
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.ALL_VARIANT_SEARCH_CATEGORY_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.COLOUR_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.CUSTOM_VARIANT_TYPE_ID
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.MIN_CHAR_VARIANT_TYPE_NAME
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_IDENTIFIER_HAS_SIZECHART
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_ONE_POSITION
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_COUNT
import com.tokopedia.product.addedit.variant.presentation.constant.AddEditProductVariantConstants.Companion.VARIANT_VALUE_LEVEL_TWO_POSITION
import com.tokopedia.product.addedit.variant.presentation.extension.getValueOrDefault
import com.tokopedia.product.addedit.variant.presentation.extension.hasVariant
import com.tokopedia.product.addedit.variant.presentation.extension.removeVariantCombinations
import com.tokopedia.product.addedit.variant.presentation.model.OptionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.PictureVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.SelectionInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantInputModel
import com.tokopedia.product.addedit.variant.presentation.model.VariantPhoto
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.util.*
import javax.inject.Inject

class AddEditProductVariantViewModel @Inject constructor(
    private val coroutineDispatcher: CoroutineDispatchers,
    private val getVariantCategoryCombinationUseCase: GetVariantCategoryCombinationUseCase,
    private val titleValidationUseCase: GetProductTitleValidationUseCase,
    private val getAllVariantUseCase: GetAllVariantUseCase,
    private val getVariantDataByIdUseCase: GetVariantDataByIdUseCase
) : BaseViewModel(coroutineDispatcher.main) {

    var clickedVariantPhotoItemPosition: Int? = null
    var isSingleVariantTypeIsSelected = false
    var isOldVariantData = false

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
    val pictureSizeChart = MutableLiveData<PictureVariantInputModel?>()

    var productInputModel = MutableLiveData<ProductInputModel>()
    var hasDTStock = Transformations.map(productInputModel) {
        it.hasDTStock
    }

    private val mGetVariantCategoryCombinationResult = MutableLiveData<Result<GetVariantCategoryCombinationResponse>>()
    val getVariantCategoryCombinationResult: LiveData<Result<GetVariantCategoryCombinationResponse>>
        get() = mGetVariantCategoryCombinationResult

    private val mVariantTitleValidationStatus = MutableLiveData<VariantTitleValidationStatus>()
    val variantTitleValidationStatus: LiveData<VariantTitleValidationStatus> get() = mVariantTitleValidationStatus

    private var mIsVariantPhotosVisible = MutableLiveData<Boolean>()
    val isVariantPhotosVisible: LiveData<Boolean> get() = mIsVariantPhotosVisible

    private var mVariantSizechart = MutableLiveData(PictureVariantInputModel())
    val variantSizechart: LiveData<PictureVariantInputModel> get() = mVariantSizechart

    private var mIsVariantSizechartVisible = MutableLiveData(false)
    val isVariantSizechartVisible: LiveData<Boolean> get() = mIsVariantSizechartVisible

    private val mIsInputValid = MediatorLiveData<Boolean>().apply {
        addSource(mSelectedVariantUnitValuesLevel1) {
            val isVariantUnitValuesLevel1Empty = mSelectedVariantUnitValuesLevel1.value?.isEmpty().orTrue()
            val isVariantUnitValuesLevel2Empty = mSelectedVariantUnitValuesLevel2.value?.isEmpty().orTrue()
            this.value = isInputValid(
                isVariantUnitValuesLevel1Empty,
                isVariantUnitValuesLevel2Empty,
                isSingleVariantTypeIsSelected)
        }
        addSource(mSelectedVariantUnitValuesLevel2) {
            val isVariantUnitValuesLevel1Empty = mSelectedVariantUnitValuesLevel1.value?.isEmpty().orTrue()
            val isVariantUnitValuesLevel2Empty = mSelectedVariantUnitValuesLevel2.value?.isEmpty().orTrue()
            this.value = isInputValid(isVariantUnitValuesLevel1Empty, isVariantUnitValuesLevel2Empty, isSingleVariantTypeIsSelected)
        }
    }
    val isInputValid: LiveData<Boolean> get() = mIsInputValid

    val isEditMode: LiveData<Boolean> = Transformations.map(productInputModel) {
        it.productId > 0
    }

    private var mIsRemovingVariant = MutableLiveData(false)
    val isRemovingVariant: LiveData<Boolean> get() = mIsRemovingVariant

    private var mVariantTypeSearchResult = MutableLiveData<Result<List<AllVariant>>>()
    val variantTypeSearchResult: LiveData<Result<List<AllVariant>>> get() = mVariantTypeSearchResult

    private var mGetVariantDetailResult = MutableLiveData<Result<VariantDetail>>()
    val getVariantDetailResult: LiveData<Result<VariantDetail>> get() = mGetVariantDetailResult

    private fun isInputValid(
        isVariantUnitValuesLevel1Empty: Boolean,
        isVariantUnitValuesLevel2Empty: Boolean,
        isSingleVariantTypeIsSelected: Boolean
    ): Boolean {
        if (isSingleVariantTypeIsSelected && !isVariantUnitValuesLevel1Empty) return true
        if (isSingleVariantTypeIsSelected && !isVariantUnitValuesLevel2Empty) return true
        return !isVariantUnitValuesLevel1Empty && !isVariantUnitValuesLevel2Empty
    }

    fun getVariantCategoryCombination(categoryId: Int, selections: List<SelectionInputModel>) {
        if (categoryId <= 0) return // only execute valid ID
        val productVariants = mutableListOf<String>()
        var type = ADD_MODE
        isEditMode.value?.let { isEdit ->
            if (isEdit) {
                type = ALL_MODE
                selections.forEach {
                    productVariants.add(it.variantId)
                }
            }
        }
        launchCatchError(block = {
            val result = withContext(coroutineDispatcher.io) {
                getVariantCategoryCombinationUseCase.setParams(categoryId, productVariants, type)
                getVariantCategoryCombinationUseCase.executeOnBackground()
            }
            mGetVariantCategoryCombinationResult.value = Success(result)
        }, onError = {
                mGetVariantCategoryCombinationResult.value = Fail(it)
            })
    }

    fun getAllVariantFromKeyword(keyword: String) {
        if (keyword.length < MIN_CHAR_VARIANT_TYPE_NAME) {
            // don't get data from server, return empty list
            mVariantTypeSearchResult.value = Success(emptyList())
        } else {
            launchCatchError(block = {
                val result = withContext(coroutineDispatcher.io) {
                    getAllVariantUseCase.setParams(ALL_VARIANT_SEARCH_CATEGORY_ID, keyword)
                    getAllVariantUseCase.getDataFiltered()
                }
                mVariantTypeSearchResult.value = Success(result)
            }, onError = {
                    mVariantTypeSearchResult.value = Fail(it)
                })
        }
    }

    fun getVariantDetailFromVariantId(variantId: Int) {
        launchCatchError(block = {
            val result = withContext(coroutineDispatcher.io) {
                getVariantDataByIdUseCase.setParams(variantId)
                getVariantDataByIdUseCase.executeOnBackground()
            }
            mGetVariantDetailResult.value = Success(result.getVariantDataByID.variantDetail)
        }, onError = {
                mGetVariantDetailResult.value = Fail(it)
            })
    }

    fun validateVariantTitle(
        productName: String,
        selectedVariantDetails: List<VariantDetail>,
        customVariantDetails: List<VariantDetail>,
        variantDetails: List<VariantDetail>
    ) {
        when {
            productName.length < MIN_CHAR_VARIANT_TYPE_NAME -> {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.MINIMUM_CHAR
            }
            !isAllowedString(productName) -> {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.SYMBOL_ERROR
            }
            selectedVariantDetails.any { it.name.equals(productName, ignoreCase = true) } -> {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.USED_NAME
            }
            customVariantDetails.any {
                it.variantID == CUSTOM_VARIANT_TYPE_ID &&
                    it.name.equals(productName, ignoreCase = true)
            } -> {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.USED_NAME
            }
            variantDetails.any { it.name.equals(productName, ignoreCase = true) } &&
                selectedVariantDetails.size == VARIANT_VALUE_LEVEL_TWO_COUNT -> {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.USED_NAME
            }
            else -> validateIllegalVariantTitle(productName)
        }
    }

    fun validateIllegalVariantTitle(productName: String) {
        launchCatchError(block = {
            val result = withContext(coroutineDispatcher.io) {
                titleValidationUseCase.setParam(productName)
                titleValidationUseCase.executeOnBackground()
            }
            if (result.getProductTitleValidation.blacklistKeyword.isEmpty()) {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.NO_ERROR
            } else {
                mVariantTitleValidationStatus.value = VariantTitleValidationStatus.ILLEGAL_WORD
            }
        }, onError = { /* no-op */ })
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
        return if (variantValuesLayoutMap.isEmpty()) {
            VARIANT_VALUE_LEVEL_ONE_POSITION
        } else {
            variantValuesLayoutMap[adapterPosition] ?: VARIANT_VALUE_LEVEL_ONE_POSITION
        }
    }

    fun getRenderedLayoutAdapterPosition(): Int {
        // return index 0 when the key is null
        return variantValuesLayoutMap.firstEntry()?.key.orZero()
    }

    fun getVariantData(layoutPosition: Int): VariantDetail {
        return variantDataMap.getOrElse(layoutPosition) { VariantDetail() }
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

    fun updateVariantInputModel(variantPhotos: List<VariantPhoto>) {
        var sortedVariantUnitValuesMap = selectedVariantUnitValuesMap.toSortedMap()
        var sortedSelectedVariantUnitMap = selectedVariantUnitMap
        if (isOldVariantData) {
            var unitValueIndex = 0
            var unitIndex = 0
            sortedVariantUnitValuesMap = sortedMapOf()
            selectedVariantUnitValuesMap.toSortedMap(reverseOrder()).forEach {
                sortedVariantUnitValuesMap[unitValueIndex] = it.value
                unitValueIndex++
            }
            sortedSelectedVariantUnitMap = HashMap()
            selectedVariantUnitMap.toSortedMap(reverseOrder()).forEach {
                sortedSelectedVariantUnitMap[unitIndex] = it.value
                unitIndex++
            }
        }

        productInputModel.getValueOrDefault().variantInputModel.apply {
            updateDefaultValueSource(selections, sortedSelectedVariantUnitMap.map { it.value })
            products = mapProducts(selectedVariantDetails, variantPhotos, sortedVariantUnitValuesMap)
            selections = mapSelections(selectedVariantDetails, sortedVariantUnitValuesMap, sortedSelectedVariantUnitMap)
            sizecharts = mapSizechart(variantSizechart.value)
        }
    }

    fun updateDefaultValueSource(
        selectedVariantList: List<SelectionInputModel>,
        selectedVariantUnitList: List<Unit>
    ) {
        val isUnitChanged = !selectedVariantUnitList.all { unit ->
            selectedVariantList.any {
                it.unitID == unit.variantUnitID.toString()
            }
        }

        if (isUnitChanged) productInputModel.getValueOrDefault().resetVariantData()
    }

    fun updateSizechart(url: String) {
        val newSizechart = PictureVariantInputModel()
        newSizechart.urlOriginal = url
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
            if (selectedVariantDetails.size == VARIANT_VALUE_LEVEL_ONE_COUNT) {
                // handling for 1 leveled variant detail
                mIsVariantSizechartVisible.value = unitValuesLevel1.isNotEmpty() || unitValuesLevel2.isNotEmpty()
            } else {
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
    }

    fun updateSizechartFieldVisibility() {
        val isSizeUnit = this.selectedVariantDetails.any {
            it.identifier == VARIANT_IDENTIFIER_HAS_SIZECHART
        }
        val isVariantValueNotEmpty = selectedVariantUnitValuesMap.any {
            it.value.isNotEmpty()
        }

        mIsVariantSizechartVisible.value = isSizeUnit && isVariantValueNotEmpty
    }

    fun getSelectedVariantUnit(layoutPosition: Int): Unit {
        val selectedVariantUnit = selectedVariantUnitMap.getOrElse(layoutPosition) { Unit() }
        return if (selectedVariantUnit.unitName.isNotBlank()) {
            selectedVariantUnitMap.getOrElse(layoutPosition) { Unit() }
        } else {
            // return either the first unit (default selection case) or empty unit (no variant unit)
            variantDataMap[layoutPosition]?.units?.firstOrNull() ?: Unit()
        }
    }

    fun getSelectedVariantUnitValues(layoutPosition: Int): MutableList<UnitValue> {
        return selectedVariantUnitValuesMap.getOrElse(layoutPosition) { mutableListOf() }
    }

    fun removeVariantValueLayoutMapEntry(adapterPosition: Int) {
        variantValuesLayoutMap.remove(adapterPosition)
    }

    fun isVariantUnitValuesEmpty(layoutPosition: Int): Boolean {
        return selectedVariantUnitValuesMap.getOrElse(layoutPosition) { emptyList() }.isEmpty()
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
        mIsRemovingVariant.value = true
        // keep isRemoteDataHasVariant old data
        val isRemoteDataHasVariant = productInputModel.getValueOrDefault().variantInputModel
            .isRemoteDataHasVariant
        // keep the selections before being cleared
        val selections = productInputModel.getValueOrDefault().variantInputModel.selections.filter {
            it.variantId != CUSTOM_VARIANT_TYPE_ID.toString()
        }

        selectedVariantDetails = mutableListOf()
        mSelectedVariantUnitValuesLevel1.value = mutableListOf()
        mSelectedVariantUnitValuesLevel2.value = mutableListOf()
        selectedVariantUnitValuesMap = HashMap()
        variantValuesLayoutMap = TreeMap()
        selectedVariantUnitMap = HashMap()
        mIsVariantPhotosVisible.value = false

        with(productInputModel.getValueOrDefault()) {
            resetVariantData()
            variantInputModel = VariantInputModel(isRemoteDataHasVariant = isRemoteDataHasVariant)
            detailInputModel.let {
                val categoryId = it.categoryId.toIntOrNull()
                categoryId?.run { getVariantCategoryCombination(this, selections) }
                it.price = lastVariantData.price
                it.sku = ""
            }

            if (isEditMode.value == true) {
                shipmentInputModel.weight = Int.ZERO
                detailInputModel.stock = lastVariantData.stock.orZero()
            } else {
                shipmentInputModel.weight = lastVariantData.weight.orZero()
            }
        }
    }

    private fun mapSelections(
        variantDetailsSelected: List<VariantDetail>,
        variantUnitValuesMap: SortedMap<Int, MutableList<UnitValue>>,
        variantUnitMap: HashMap<Int, Unit>
    ): List<SelectionInputModel> {
        val result: MutableList<SelectionInputModel> = mutableListOf()
        var level = 0 // variant value level
        variantUnitValuesMap.forEach {
            // get selected variant detail selected each level
            if (it.value.isNotEmpty()) {
                variantDetailsSelected.getOrNull(level)?.let { variantDetail ->
                    val unit = variantUnitMap.getOrElse(it.key) { Unit() }
                    result.add(
                        SelectionInputModel(
                            variantDetail.variantID.toString(),
                            variantDetail.name,
                            unit.variantUnitID.toString(),
                            unit.unitName,
                            variantDetail.identifier,
                            mapOptions(it.value)
                        )
                    )
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

    private fun mapOptions(unit: List<UnitValue>): List<OptionInputModel> =
        unit.map {
            OptionInputModel(
                it.variantUnitValueID.toString(),
                it.value,
                it.hex
            )
        }

    private fun mapProducts(
        variantDetails: List<VariantDetail>,
        variantPhotos: List<VariantPhoto>,
        variantUnitValuesMap: SortedMap<Int, MutableList<UnitValue>>
    ): List<ProductVariantInputModel> {
        val result: MutableList<ProductVariantInputModel> = mutableListOf()
        val unitValueList: MutableList<List<UnitValue>> = mutableListOf()
        val variantIdList: MutableList<BigInteger> = mutableListOf()
        var level = 0

        // init unitValueList and variantIdList
        variantUnitValuesMap.forEach {
            if (it.value.isNotEmpty()) {
                unitValueList.add(it.value)
                variantDetails.getOrNull(level)?.let { variantDetail ->
                    variantIdList.add(variantDetail.variantID)
                }
                level++
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
                result.add(
                    mapProductVariant(
                        variantPicture,
                        listOf(optionIndexLevel1)
                    )
                )
            } else {
                // loop condition when variant having 2 level
                unitValueLevel2.forEachIndexed { optionIndexLevel2, _ ->
                    // re-init variant picture (condition if color at level 2)
                    if (variantIdLevel2 == COLOUR_VARIANT_TYPE_ID) {
                        variantPicture = mapVariantPhoto(variantPhotos.getOrNull(optionIndexLevel2))
                    }

                    result.add(
                        mapProductVariant(
                            variantPicture,
                            listOf(optionIndexLevel1, optionIndexLevel2)
                        )
                    )
                }
            }
        }

        return result
    }

    private fun mapProductVariant(
        variantPicture: List<PictureVariantInputModel>,
        combination: List<Int>
    ): ProductVariantInputModel {
        val products = productInputModel.getValueOrDefault().variantInputModel.products
        val productVariant = products.firstOrNull {
            it.combination == combination
        }

        return if (productVariant == null) {
            // condition if adding new product variant (product variant combination not listed in products)
            ProductVariantInputModel(
                price = productInputModel.getValueOrDefault().getVariantDefaultVariantPrice(
                    isEditMode.getValueOrDefault(false)
                ),
                pictures = variantPicture,
                combination = combination,
                status = STATUS_ACTIVE_STRING
            )
        } else {
            // condition if updating existing product variant
            if (variantPicture.isNotEmpty()) { // prevent clearing variant
                productVariant.pictures = variantPicture
            }
            productVariant
        }
    }

    private fun mapVariantPhoto(variantPhoto: VariantPhoto?): List<PictureVariantInputModel> {
        return if (variantPhoto != null && variantPhoto.imageUrlOrPath.isNotEmpty()) {
            val result = PictureVariantInputModel(
                filePath = variantPhoto.filePath,
                urlOriginal = variantPhoto.imageUrlOrPath,
                picID = variantPhoto.picID,
                description = variantPhoto.description,
                fileName = variantPhoto.fileName,
                width = variantPhoto.width,
                height = variantPhoto.height,
                isFromIG = variantPhoto.isFromIG,
                uploadId = variantPhoto.uploadId
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
            val id = inputModel.variantId.toBigIntegerSafely()
            val identifier = inputModel.identifier
            val name = inputModel.variantName
            // selected variant unit values
            val unitValues = mutableListOf<UnitValue>()
            val optionInputModels = inputModel.options
            optionInputModels.forEach {
                val unitValueId = it.unitValueID.toIntSafely()
                val unitValueName = it.value
                val unitValue = UnitValue(variantUnitValueID = unitValueId, value = unitValueName)
                unitValues.add(unitValue)
            }
            // selected variant unit
            val unitId = inputModel.unitID.toIntSafely()
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

    fun removeSelectedVariantDetails(variantDetail: VariantDetail) {
        isOldVariantData = false
        this.selectedVariantDetails.removeFirst {
            it.variantID == variantDetail.variantID && it.name == variantDetail.name
        }

        // reset to non variant product state if there is nothing selected
        if (selectedVariantDetails.isEmpty()) {
            removeVariant()
        } else {
            productInputModel.getValueOrDefault().resetVariantData()
        }
    }

    fun disableRemovingVariant() {
        mIsRemovingVariant.value = false
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
            val picture = productInputModel.variantInputModel.getImageUrl(colorVariantLevel, index)
            val photoUrl = picture?.urlOriginal.orEmpty()
            val photoPicID = picture?.picID.orEmpty()
            val photoDescription = picture?.description.orEmpty()
            val photoFileName = picture?.fileName.orEmpty()
            val photoFilePath = picture?.filePath.orEmpty()
            val photoWidth = picture?.width.orZero()
            val photoHeight = picture?.height.orZero()
            val photoIsFromIG = picture?.isFromIG.orEmpty()
            val photoUploadId = picture?.uploadId.orEmpty()

            variantPhotos.add(
                VariantPhoto(
                    variantUnitValueName,
                    photoUrl,
                    photoPicID,
                    photoDescription,
                    photoFileName,
                    photoFilePath,
                    photoWidth,
                    photoHeight,
                    photoIsFromIG,
                    photoUploadId
                )
            )
        }
        return variantPhotos
    }

    fun updateIsOldVariantData(
        selectedVariantDetails: List<VariantDetail>,
        extractedVariantDetails: List<VariantDetail>
    ) {
        val firstSelectedVariant = selectedVariantDetails.firstOrNull()
        val firstExtractedVariant = extractedVariantDetails.firstOrNull()

        isOldVariantData = if (firstSelectedVariant != null && firstExtractedVariant != null) {
            firstSelectedVariant.variantID != firstExtractedVariant.variantID
        } else {
            false
        }
    }

    fun createCustomVariantTypeModel(variantName: String): VariantDetail {
        return VariantDetail(
            name = variantName,
            units = listOf(Unit()),
            isCustom = true
        )
    }

    fun removeCombinations(position: Int, layoutPosition: Int) {
        if (productInputModel.hasVariant()) {
            productInputModel.removeVariantCombinations(position, layoutPosition)
        }
    }

    fun cleanUrlOrPathPicture(imageResult: PickerResult): String? {
        val editedPath = imageResult.editedImages.firstOrNull()
        val originalPath = imageResult.originalPaths.firstOrNull()
        return if (editedPath.isNullOrEmpty()) originalPath else editedPath
    }
}
