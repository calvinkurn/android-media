package com.tokopedia.addon.presentation.listener

import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel
import com.tokopedia.addon.presentation.uimodel.AddOnPageResult
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel

interface AddOnComponentListener {

    fun onAddonComponentError(errorMessage: String)

    fun onAddonComponentClick(
        index: Int,
        indexChild: Int,
        addOnGroupUIModels: List<AddOnGroupUIModel>
    )

    fun onAddonHelpClick(position: Int, addOnUIModel: AddOnUIModel) {}

    fun onDataEmpty() {}

    fun onTotalPriceCalculated(price: Long) {}

    fun onAggregatedDataObtained(aggregatedData: AddOnPageResult.AggregatedData) {}

    fun onSaveAddonFailed(errorMessage: String) {}

    fun onSaveAddonSuccess(
        selectedAddonIds: List<String>,
        selectedAddons: List<AddOnUIModel>,
        selectedAddonGroup: List<AddOnGroupUIModel>) {}

    fun onSaveAddonLoading() {}
}
