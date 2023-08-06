package com.tokopedia.addon.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addon.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnMapper
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.addon.presentation.uimodel.AddOnParam
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.gifting.presentation.uimodel.AddOnType
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddOnViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getAddOnUseCase: GetAddOnByProductUseCase,
    private val saveAddOnStateUseCase: SaveAddOnStateUseCase,
    private val getAddOnDetailUseCase: GetAddOnUseCase
) : BaseViewModel(dispatchers.main) {

    private val mGetAddOnResult = MutableLiveData<List<AddOnGroupUIModel>>()
    val getAddOnResult = Transformations.map(mGetAddOnResult) {
        val addonGroups = AddOnMapper.mapAddOnWithSelectedIds(it, preselectedAddonIds, predeselectedAddonIds)
        AddOnMapper.simplifyAddonGroup(addonGroups, isSimplified)
    }

    private val mErrorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable> get() = mErrorThrowable

    private val mModifiedAddOnGroups = MutableLiveData<List<AddOnGroupUIModel>>()
    val modifiedAddOnGroups = Transformations.map(mModifiedAddOnGroups) { addonGroups ->
        addonGroups.forEach { addon ->
            addon.addon.forEach {
                it.isPreselected = it.id in preselectedAddonIds
            }
        }
        addonGroups
    }

    private val mAggregatedData = MutableLiveData<AddOnPageResult.AggregatedData>()
    val aggregatedData: LiveData<AddOnPageResult.AggregatedData> get() = mAggregatedData

    private val mSaveSelectionResult = MutableLiveData<Result<List<AddOnGroupUIModel>>>()
    val saveSelectionResult: LiveData<Result<List<AddOnGroupUIModel>>> get() = mSaveSelectionResult

    private val mAutoSave = MutableLiveData<AutoSaveAddonModel>()
    val autoSave: LiveData<AutoSaveAddonModel> get() = mAutoSave

    val isAddonDataEmpty = Transformations.map(getAddOnResult) {
        it.isEmpty()
    }

    val shouldShowSeeAll = Transformations.map(mGetAddOnResult) { addonGroups ->
        isSimplified && addonGroups.any {
            it.addon.size > Int.ONE
        }
    }

    val totalPrice = Transformations.map(modifiedAddOnGroups) { modifiedAddOnGroups ->
        var total: Long = 0
        modifiedAddOnGroups.forEach { modifiedAddOnGroup ->
            total += modifiedAddOnGroup.addon
                .filter { it.isSelected }
                .sumOf { it.price }
        }
        total
    }

    var preselectedAddonIds: List<String> = emptyList()
    var predeselectedAddonIds: List<String> = emptyList()
    var lastSelectedAddOnGroups: List<AddOnGroupUIModel> = emptyList()
    var lastSelectedAddOn: MutableList<AddOnUIModel> = mutableListOf()
    var isSimplified = false

    private fun generateEmptyAggregatedData(): AddOnPageResult.AggregatedData {
        return AddOnPageResult.AggregatedData(
            isGetDataSuccess = true,
            selectedAddons = AddOnMapper.getPPSelectedAddons(modifiedAddOnGroups.value)
        )
    }

    fun getAddOn(param: AddOnParam, isSimplified: Boolean) {
        this.isSimplified = isSimplified
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnUseCase.setParams(param, listOf(AddOnType.INSTALLATION_TYPE,
                    AddOnType.PRODUCT_PROTECTION_INSURANCE_TYPE))
                getAddOnUseCase.executeOnBackground()
            }
            val addonGroups = AddOnMapper.mapAddonToUiModel(result)
            mGetAddOnResult.value = addonGroups
        }, onError = {
                mErrorThrowable.value = it
            })
    }

    fun setPreselectedAddOn(preselectedAddonIds: List<String>) {
        this.preselectedAddonIds = preselectedAddonIds
    }

    fun setPredeselectedAddOn(addonIds: List<String>) {
        this.predeselectedAddonIds = addonIds
    }

    fun saveAddOnState(cartId: Long, source: String) {
        if (AddOnMapper.flatmapToChangedAddonSelection(modifiedAddOnGroups.value).isEmpty()) return
        mSaveSelectionResult.value = Success(emptyList())
        saveAddOnStateUseCase.setParams(
            AddOnMapper.mapToSaveAddOnStateRequest(
                cartId,
                source,
                modifiedAddOnGroups.value,
                lastSelectedAddOn
            ),
            false
        )
        saveAddOnStateUseCase.execute(
            onSuccess = {
                mSaveSelectionResult.value = if (it.saveAddOns.errorMessage.isEmpty()) {
                    Success(modifiedAddOnGroups.value.orEmpty())
                } else {
                    Fail(MessageErrorException(it.saveAddOns.errorMessage.joinToString()))
                }
            },
            onError = { throwable ->
                mSaveSelectionResult.value = Fail(throwable)
            }
        )
    }

    fun setSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>) {
        mModifiedAddOnGroups.value = addOnGroupUIModels
        mAutoSave.value?.let {
            if (it.isActive) {
                mAutoSave.value = it.copy()
            }
        }
    }

    fun getAddOnAggregatedData(
        addOnIds: List<String>,
        addOnTypes: List<String>,
        addOnWidgetParam: AddOnParam
    ) {
        if (addOnIds.isEmpty()) {
            mAggregatedData.value = generateEmptyAggregatedData()
            return
        }
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnDetailUseCase.setParams(addOnIds, addOnTypes, addOnWidgetParam)
                getAddOnDetailUseCase.executeOnBackground().getAddOnByID
            }
            mAggregatedData.value = AddOnPageResult.AggregatedData(
                title = result.aggregatedData.title,
                price = result.aggregatedData.price,
                selectedAddons = lastSelectedAddOn,
                isGetDataSuccess = result.error.messages.isEmpty(),
                getDataErrorMessage = result.error.messages
            )
        }, onError = {
            mAggregatedData.value = AddOnPageResult.AggregatedData(
                getDataErrorMessage = getAddOnDetailUseCase.getErrorString(it)
            )
        })
    }

    fun setAutosave(cartId: Long, atcSource: String) {
        mAutoSave.value = AutoSaveAddonModel(
            isActive = true,
            cartId = cartId,
            atcSource = atcSource
        )
    }

    fun restoreSelection() {
        if (lastSelectedAddOnGroups.isNotEmpty()) {
            mGetAddOnResult.value = lastSelectedAddOnGroups
        }
    }

    fun desimplifyAddonList() {
        isSimplified = false
        mGetAddOnResult.value = mGetAddOnResult.value
    }

    data class AutoSaveAddonModel(
        val isActive: Boolean = false,
        val cartId: Long = 0,
        val atcSource: String = ""
    )
}
