package com.tokopedia.addon.presentation.uimodel

import com.tokopedia.addon.domain.model.GetAddOnByProductResponse
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
                addOnLevel = addonResponse.addOnLevel
            )
        }.orEmpty()
    }

    fun mapAddOnWithSelectedIds(
        addonGroupList: List<AddOnGroupUIModel>,
        selectedAddonIds: List<String>
    ): List<AddOnGroupUIModel> {
        return addonGroupList.map {
            it.copy(
                addon = it.addon.map { addon ->
                    val isPreselected = addon.id in selectedAddonIds || addon.isMandatory
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

    fun getSelectedAddonsIds(addOnGroupUIModels: List<AddOnGroupUIModel>): List<String> {
        return getSelectedAddons(addOnGroupUIModels).map { it.id }
    }

    fun mapToSaveAddOnStateRequest(
        cartId: Long,
        source: String,
        addonGroups: List<AddOnGroupUIModel>?
    ): SaveAddOnStateRequest {
        val addons = flatmapToChangedAddonSelection(addonGroups)
        val request = AddOnRequest(
            addOnKey = cartId.toString(),
            addOnLevel = addonGroups?.firstOrNull()?.addOnLevel.orEmpty(),
            cartProducts = listOf(
                CartProduct(
                    cartId = cartId,
                    productId = addonGroups?.firstOrNull()?.productId.orZero()
                )
            ),
            addOnData = addons.map {
                AddOnDataRequest(
                    addOnId = it.id.toLongOrZero(),
                    addOnQty = ATC_ADDON_DEFAULT_QTY,
                    addOnUniqueId = it.uniqueId,
                    addOnType = it.addOnType,
                    addOnStatus = it.getSelectedStatus().value
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
            .filter { it.getSelectedStatus() != AddOnSelectedStatus.DEFAULT }
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
                group.copy(
                    addon = group.addon.firstOrNull()?.let {
                        listOf(it)
                    } ?: emptyList()
                )
            }
        } else {
            addonGroups
        }
    }
}
