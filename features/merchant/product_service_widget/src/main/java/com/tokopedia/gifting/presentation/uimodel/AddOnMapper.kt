package com.tokopedia.gifting.presentation.uimodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.gifting.domain.model.GetAddOnByID
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object AddOnMapper {
    private const val NON_TOKOCABANG_ID = 0L

    fun isTokoCabang(getAddOnByID: MutableLiveData<GetAddOnByID>): LiveData<Boolean> {
        return Transformations.map(getAddOnByID) { getAddOnData ->
            getAddOnData.addOnByIDResponse.firstOrNull()?.basic?.ownerWarehouseID?.let { warehouseId ->
                return@map warehouseId.toLongOrZero() != NON_TOKOCABANG_ID
            }
            return@map false
        }
    }
}