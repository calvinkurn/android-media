package com.tokopedia.addon.presentation.uimodel

import com.tokopedia.addon.domain.model.GetAddOnByProductResponse
import com.tokopedia.product.detail.common.getCurrencyFormatted

object AddOnMapper {

    fun mapAddonToUiModel(response: GetAddOnByProductResponse): List<AddOnGroupUIModel> {
        val addons = response.getAddOnByProduct.addOnByProductResponse.firstOrNull()?.addons
            ?.groupBy {
                it.basic.metadata.infoURL.title
            }

        return addons?.map {
            val infoUrl = it.value.firstOrNull()?.basic?.metadata?.infoURL
            val addonsUi = it.value.map {
                AddOnUIModel(
                    name = it.basic.name,
                    priceFormatted = it.inventory.price.getCurrencyFormatted(),
                    isSelected = it.basic.rules.autoSelect
                )
            }
            val dummy = arrayListOf<AddOnUIModel>()
            dummy.addAll(addonsUi)
            dummy.addAll(addonsUi)
            dummy.addAll(addonsUi)
            AddOnGroupUIModel(
                title = it.key,
                iconUrl = infoUrl?.iconURL.orEmpty(),
                iconDarkmodeUrl = infoUrl?.iconDarkURL.orEmpty(),
                addon = dummy
            )
        }.orEmpty()
    }
}
