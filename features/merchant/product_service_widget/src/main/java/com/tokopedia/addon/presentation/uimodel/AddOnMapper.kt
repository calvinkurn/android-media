package com.tokopedia.addon.presentation.uimodel

import com.tokopedia.addon.domain.model.GetAddOnByProductResponse
import com.tokopedia.gifting.presentation.uimodel.AddOnType
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnDataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.CartProduct
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest

object AddOnMapper {

    private const val ATC_ADDON_DEFAULT_QTY = 1
    private const val ATC_ADDON_SERVICE_FEATURE_TYPE = 1

    fun mapAddonToUiModel(response: GetAddOnByProductResponse): List<AddOnGroupUIModel> {
        val addonResponse = response.getAddOnByProduct.addOnByProductResponse.firstOrNull()
        val addons = addonResponse?.addons?.groupBy {
            it.basic.metadata.infoURL.title
        }

        return addons?.map { addon ->
            val infoUrl = addon.value.firstOrNull()?.basic?.metadata?.infoURL
            var warehouseId = 0L
            val addonsUi = addon.value.map {
                warehouseId = it.basic.ownerWarehouseID.toLongOrZero()
                AddOnUIModel(
                    id = it.basic.basicId,
                    name = it.basic.name,
                    price = it.inventory.price.toLong(),
                    discountedPrice = it.inventory.price.toLong(),
                    isSelected = it.basic.rules.autoSelect || it.basic.rules.mandatory,
                    isMandatory = it.basic.rules.mandatory,
                    isAutoselect = it.basic.rules.autoSelect,
                    addOnType = it.basic.addOnType.toIntSafely(),
                    eduLink = it.basic.metadata.infoURL.eduPageURL,
                    uniqueId = it.basic.addOnKey,
                    description = it.basic.metadata.description,
                    shopId = it.basic.shopID
                )
            }
            AddOnGroupUIModel(
                title = addon.key,
                iconUrl = infoUrl?.iconURL.orEmpty(),
                iconDarkmodeUrl = infoUrl?.iconDarkURL.orEmpty(),
                addon = addonsUi,
                productId = addonResponse.productID.toLongOrZero(),
                warehouseId = warehouseId,
                addOnLevel = addonResponse.addOnLevel,
                addonCount = addonsUi.size
            )
        }.orEmpty()
    }

    fun mapAddOnWithSelectedIds(
        addonGroupList: List<AddOnGroupUIModel>,
        selectedAddonIds: List<String>,
        predeselectedAddonIds: List<String>
    ): List<AddOnGroupUIModel> {
        return addonGroupList.map {
            val activateAutoselect = !it.addon.any { addon ->
                addon.id in predeselectedAddonIds
            }
            it.copy(
                addon = it.addon.map { addon ->
                    val isPreselected = if (selectedAddonIds.isEmpty()) {
                        addon.isMandatory || (addon.isAutoselect && activateAutoselect)
                    } else {
                        addon.id in selectedAddonIds || addon.isMandatory
                    }
                    addon.copy(
                        isSelected = isPreselected,
                        isPreselected = isPreselected
                    )
                }
            )
        }
    }

    fun getSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>?): List<AddOnUIModel> {
        val resultValue: MutableList<AddOnUIModel> = mutableListOf()
        addOnGroupUIModels.orEmpty().forEach { addOnGroupUIModel ->
            resultValue.addAll(
                addOnGroupUIModel.addon.filter { it.isSelected }
            )
        }
        return resultValue
    }

    fun getGroupSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>): List<AddOnUIModel> {
        return addOnGroupUIModels.map { addonGroups ->
            addonGroups.addon.firstOrNull {
                it.isSelected
            } ?: AddOnUIModel()
        }
    }

    fun getPPSelectedAddons(addOnGroupUIModels: List<AddOnGroupUIModel>?): List<AddOnUIModel> {
        val resultValue: MutableList<AddOnUIModel> = mutableListOf()
        addOnGroupUIModels.orEmpty().forEach { addOnGroupUIModel ->
            resultValue.addAll(
                addOnGroupUIModel.addon.filter { it.getSelectedStatus().value > 0 }
            )
        }
        return resultValue
    }

    fun getSelectedAddonsIds(addOnGroupUIModels: List<AddOnGroupUIModel>): List<String> {
        return getSelectedAddons(addOnGroupUIModels).map { it.id }
    }

    fun getSelectedAddonsTypes(addOnGroupUIModels: List<AddOnGroupUIModel>): List<String> {
        return getSelectedAddons(addOnGroupUIModels).map {
            AddOnType.values().getOrNull(it.addOnType.dec())?.name.orEmpty()
        }
    }

    fun mapToSaveAddOnStateRequest(
        cartId: Long,
        source: String,
        addonGroups: List<AddOnGroupUIModel>?,
        addonsSelected: List<AddOnUIModel>
    ): SaveAddOnStateRequest {
        val request = AddOnRequest(
            addOnKey = cartId.toString(),
            addOnLevel = addonGroups?.firstOrNull()?.addOnLevel.orEmpty(),
            cartProducts = listOf(
                CartProduct(
                    cartId = cartId,
                    productId = addonGroups?.firstOrNull()?.productId.orZero()
                )
            ),
            addOnData = addonsSelected.filter { it.id.toLongOrZero().isMoreThanZero() }.map {
                AddOnDataRequest(
                    addOnId = it.id.toLongOrZero(),
                    addOnQty = ATC_ADDON_DEFAULT_QTY,
                    addOnUniqueId = it.uniqueId,
                    addOnType = it.addOnType,
                    addOnStatus = it.getSaveAddonSelectedStatus().value
                )
            }
        )

        return SaveAddOnStateRequest(
            addOns = listOf(request),
            source = source,
            featureType = ATC_ADDON_SERVICE_FEATURE_TYPE
        )
    }

    fun flatmapToChangedAddonSelection(
        addonGroups: List<AddOnGroupUIModel>?
    ): List<AddOnUIModel> {
        return addonGroups.orEmpty()
            .flatMap { it.addon }
            .filter { it.isSelected || it.getSelectedStatus() != AddOnSelectedStatus.DEFAULT }
    }

    fun deepCopyAddonGroup(addonGroups: List<AddOnGroupUIModel>): List<AddOnGroupUIModel> {
        return addonGroups.map { group ->
            group.copy(
                addon = group.addon.map { addon ->
                    addon.copy()
                }
            )
        }
    }

    fun simplifyAddonGroup(addonGroups: List<AddOnGroupUIModel>, isSimplified: Boolean): List<AddOnGroupUIModel> {
        return if (isSimplified) {
            addonGroups.map { group ->
                val selectedAddon = group.addon.firstOrNull { it.isSelected }
                val simplifiedAddon = selectedAddon ?: group.addon.firstOrNull()
                val simplifiedAddonList = listOfNotNull(simplifiedAddon)

                group.copy(addon = simplifiedAddonList)
            }
        } else {
            addonGroups
        }
    }
}
