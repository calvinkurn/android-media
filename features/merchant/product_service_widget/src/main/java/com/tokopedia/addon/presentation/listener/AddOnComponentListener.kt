package com.tokopedia.addon.presentation.listener

import com.tokopedia.addon.presentation.uimodel.AddOnGroupUIModel

interface AddOnComponentListener {

    fun onAddonComponentError(throwable: Throwable)

    fun onAddonComponentClick(
        index: Int,
        indexChild: Int,
        addOnGroupUIModels: List<AddOnGroupUIModel>
    )

    fun onDataEmpty()
}
