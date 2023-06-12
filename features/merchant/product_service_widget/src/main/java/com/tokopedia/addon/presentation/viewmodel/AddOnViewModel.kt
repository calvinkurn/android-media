package com.tokopedia.addon.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.addon.domain.usecase.GetAddOnByProductUseCase
import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnMapper
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.gifting.domain.model.GetAddOnByID
import com.tokopedia.gifting.domain.usecase.GetAddOnUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
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

    private val mAggregatedData = MutableLiveData<GetAddOnByID.AggregatedData>()
    val aggregatedData: LiveData<GetAddOnByID.AggregatedData> get() = mAggregatedData

    val isAddonDataEmpty = Transformations.map(getAddOnResult) {
        it.isEmpty()
    }

    val totalPrice = Transformations.map(selectedAddon) { selectedAddons ->
        selectedAddons.sumOf { it.price }
    }

    var selectedAddonIds: List<String> = emptyList()

    fun getAddOn(productId: String, warehouseId: String, isTokocabang: Boolean) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnUseCase.setParams(productId, warehouseId, isTokocabang)
                getAddOnUseCase.executeOnBackground()
            }
            mGetAddOnResult.value = AddOnMapper.mapAddonToUiModel(result)
        }, onError = {
            mErrorThrowable.value = it
        })
    }

    fun setSelectedAddOn(selectedAddonIds: List<String>) {
        this.selectedAddonIds = selectedAddonIds
    }

    fun saveAddOnState() {
        saveAddOnStateUseCase.setParams(SaveAddOnStateRequest())
        saveAddOnStateUseCase.execute(
            onSuccess = {

            },
            onError = {

            }
        )
    }

    fun setSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>) {
        mSelectedAddOn.value = AddOnMapper.getSelectedAddons(addOnGroupUIModels)
    }

    fun getAddOnAggregatedData(addOnIds: List<String>) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                getAddOnDetailUseCase.setParams(addOnIds)
                getAddOnDetailUseCase.executeOnBackground().getAddOnByID
            }
            mAggregatedData.value = result.aggregatedData
            getServerError(result)
        }, onError = {
            mErrorThrowable.value = it
        })
    }

    fun getServerError(result: GetAddOnByID) {
        result.error.let {
            if (it.messages.isNotEmpty()) {
                mErrorThrowable.value = MessageErrorException(it.messages, it.errorCode)
            }
        }
    }
}
