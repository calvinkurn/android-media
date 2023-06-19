package com.tokopedia.addon.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addon.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnMapper
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.utils.ErrorHandler
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
        AddOnMapper.mapAddOnWithSelectedIds(it, selectedAddonIds)
    }

    private val mErrorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable> get() = mErrorThrowable

    private val mSelectedAddOn = MutableLiveData<List<AddOnUIModel>>()
    val selectedAddon: LiveData<List<AddOnUIModel>> get() = mSelectedAddOn

    private val mAggregatedData = MutableLiveData<AddOnPageResult.AggregatedData>()
    val aggregatedData: LiveData<AddOnPageResult.AggregatedData> get() = mAggregatedData

    private val mSaveSelectionResult = MutableLiveData<Result<List<AddOnGroupUIModel>>>()
    val saveSelectionResult: LiveData<Result<List<AddOnGroupUIModel>>> get() = mSaveSelectionResult

    private val mAutoSave = MutableLiveData<AutoSaveAddonModel>()
    val autoSave: LiveData<AutoSaveAddonModel> get() = mAutoSave

    val isAddonDataEmpty = Transformations.map(getAddOnResult) {
        it.isEmpty()
    }

    val totalPrice = Transformations.map(selectedAddon) { selectedAddons ->
        selectedAddons.sumOf { it.price }
    }

    var selectedAddonIds: List<String> = emptyList()
    var lastSelectedAddOn: List<AddOnGroupUIModel> = emptyList()
    private var selectedAddonGroup: AddOnGroupUIModel? = null

    fun getAddOn(productId: String, warehouseId: String, isTokocabang: Boolean) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnUseCase.setParams(productId, warehouseId, isTokocabang)
                getAddOnUseCase.executeOnBackground()
            }
            val addonGroups = AddOnMapper.mapAddonToUiModel(result)
            mGetAddOnResult.value = addonGroups
        }, onError = {
            mErrorThrowable.value = it
        })
    }

    fun setSelectedAddOn(selectedAddonIds: List<String>) {
        this.selectedAddonIds = selectedAddonIds
    }

    fun saveAddOnState(cartId: Long, source: String) {
        mSaveSelectionResult.value = Success(emptyList())
        saveAddOnStateUseCase.setParams(
            AddOnMapper.mapToSaveAddOnStateRequest(cartId, source, selectedAddonGroup, selectedAddon.value)
        )
        saveAddOnStateUseCase.execute(
            onSuccess = {
                mSaveSelectionResult.value = Success(getAddOnResult.value.orEmpty())
            },
            onError = { throwable ->
                mSaveSelectionResult.value = Fail(throwable)
            }
        )
    }

    fun setSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>, index: Int) {
        mSelectedAddOn.value = AddOnMapper.getSelectedAddons(addOnGroupUIModels)
        selectedAddonGroup = addOnGroupUIModels.getOrNull(index)

        mAutoSave.value?.let {
            if (it.isActive) {
                mAutoSave.value = it.copy(
                    addOnGroupUIModels = addOnGroupUIModels
                )
            }
        }
    }

    fun getAddOnAggregatedData(context: Context, addOnIds: List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnDetailUseCase.setParams(addOnIds)
                getAddOnDetailUseCase.executeOnBackground().getAddOnByID
            }
            mAggregatedData.value = AddOnPageResult.AggregatedData(
                title = result.aggregatedData.title,
                price = result.aggregatedData.price,
                isGetDataSuccess = result.error.messages.isNotEmpty(),
                getDataErrorMessage = result.error.messages
            )
        }, onError = {
            mAggregatedData.value = AddOnPageResult.AggregatedData(
                getDataErrorMessage = ErrorHandler.getErrorMessage(context, it))
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
        mGetAddOnResult.value = lastSelectedAddOn
    }

    data class AutoSaveAddonModel (
        val isActive: Boolean = false,
        val cartId: Long = 0,
        val atcSource: String = "",
        val addOnGroupUIModels: List<AddOnGroupUIModel> = emptyList()
    )
}
