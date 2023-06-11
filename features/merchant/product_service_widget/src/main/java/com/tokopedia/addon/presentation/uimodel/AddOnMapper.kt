package com.tokopedia.addon.presentation.uimodel

import com.tokopedia.addon.domain.model.GetAddOnByProductResponse
import com.tokopedia.product.detail.common.getCurrencyFormatted

object AddOnMapper {

    private fun String.convertToAddonEnum(): AddOnType {
        return AddOnType.values().find { it.value == this }
            ?: AddOnType.PRODUCT_PROTECTION_INSURANCE_TYPE
    }

    fun mapAddonToUiModel(response: GetAddOnByProductResponse): List<AddOnGroupUIModel> {
        val addons = response.getAddOnByProduct.addOnByProductResponse.firstOrNull()?.addons
            ?.groupBy {
                it.basic.metadata.infoURL.title
            }

        return addons?.map { addon ->
            val infoUrl = addon.value.firstOrNull()?.basic?.metadata?.infoURL
            val addonsUi = addon.value.map {
                AddOnUIModel(
                    id = it.basic.basicId,
                    name = it.basic.name,
                    priceFormatted = it.inventory.price.getCurrencyFormatted(),
                    price = it.inventory.price.toLong(),
                    isSelected = it.basic.rules.autoSelect,
                    addOnType = it.basic.addOnType.convertToAddonEnum()
                )
            }
            AddOnGroupUIModel(
                title = addon.key,
                iconUrl = infoUrl?.iconURL.orEmpty(),
                iconDarkmodeUrl = infoUrl?.iconDarkURL.orEmpty(),
                addon = addonsUi
            )
        }.orEmpty()
    }

    fun mapAddOnWithSelectedIds(
        addonGroupList: List<AddOnGroupUIModel>,
        selectedAddonIds: List<String>
    ): List<AddOnGroupUIModel> {
        addonGroupList.forEach {
            it.addon.forEach { addon ->
                if (addon.id in selectedAddonIds) addon.isSelected = true
            }
        }
        return addonGroupList
    }

    fun mapAddonUiToType(addOnUIModel: AddOnUIModel): String {
        return when(addOnUIModel.addOnType) {
            AddOnType.INSTALLATION_TYPE -> "" // webpage not ready yet
            AddOnType.PRODUCT_PROTECTION_INSURANCE_TYPE -> "" // webpage not ready yet
            else -> ""
        }

    }

    fun getSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>): List<AddOnUIModel> {
        val resultValue: MutableList<AddOnUIModel> = mutableListOf()
        addOnGroupUIModels.forEach { addOnGroupUIModel ->
            resultValue.addAll(
                addOnGroupUIModel.addon.filter { it.isSelected }
            )
        }
        return resultValue
    }
}
