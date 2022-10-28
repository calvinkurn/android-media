package com.tokopedia.gifting.presentation.uimodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.gifting.domain.model.GetAddOnByID

object AddOnMapper {
    private const val NON_TOKOCABANG_LEVEL = "PRODUCT_ADDON"

    fun isTokoCabang(getAddOnByID: MutableLiveData<GetAddOnByID>): LiveData<Boolean> {
        return Transformations.map(getAddOnByID) { getAddOnData ->
            getAddOnData.addOnByIDResponse.firstOrNull()?.basic?.addOnLevel?.let { addOnLevel ->
                return@map addOnLevel != NON_TOKOCABANG_LEVEL
            }
            return@map false
        }
    }
}