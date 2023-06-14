package com.tokopedia.addon.presentation.listener

import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel

interface AddOnComponentListener {

    fun onAddonComponentError(throwable: Throwable)

    fun onAddonComponentClick(
        index: Int,
        indexChild: Int,
        addOnGroupUIModels: List<AddOnGroupUIModel>
    )

    fun onAddonHelpClick(position: Int, addOnUIModel: AddOnUIModel) {}

    fun onDataEmpty() {}

    fun onTotalPriceCalculated(price: Long) {}

    fun onAggregatedDataObtained(aggregatedData: AddOnPageResult.AggregatedData) {}

    fun onSaveAddonFailed(throwable: Throwable) {}

    fun onSaveAddonSuccess(selectedAddonIds: List<String>) {}

    fun onSaveAddonLoading() {}
}
