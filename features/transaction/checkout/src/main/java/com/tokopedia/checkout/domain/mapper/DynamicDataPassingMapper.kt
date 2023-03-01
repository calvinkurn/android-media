package com.tokopedia.checkout.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult

object DynamicDataPassingMapper {
    fun getCartString(addOnResult: AddOnResult, shipmentCartItemModel: ShipmentCartItemModel): String {
        var cartString = ""
        if (addOnResult.addOnData.isNotEmpty()) {
            addOnResult.addOnData.forEach { addOnData ->
                if ((shipmentCartItemModel.cartString + "-0").equals(addOnResult.addOnKey, ignoreCase = true)) {
                    cartString = shipmentCartItemModel.cartString ?: ""
                }
            }
        }
        return cartString
    }

    fun getAddOn(
        addOnResult: AddOnResult,
        isOcs: Boolean
    ): DynamicDataPassingParamRequest.AddOn {
        val listAddOnData = arrayListOf<DynamicDataPassingParamRequest.AddOn.AddOnDataParam>()
        addOnResult.addOnData.map { data ->
            val addOnData = DynamicDataPassingParamRequest.AddOn.AddOnDataParam(
                addOnId = data.addOnId.toLongOrZero(),
                addOnQty = data.addOnQty,
                addOnMetadata = DynamicDataPassingParamRequest.AddOn.AddOnDataParam.AddOnMetadataParam(
                    addOnNote = DynamicDataPassingParamRequest.AddOn.AddOnDataParam.AddOnMetadataParam.AddOnNoteParam(
                        from = data.addOnMetadata.addOnNote.from,
                        isCustomNote = data.addOnMetadata.addOnNote.isCustomNote,
                        notes = data.addOnMetadata.addOnNote.notes,
                        to = data.addOnMetadata.addOnNote.to
                    )
                )
            )
            listAddOnData.add(addOnData)
        }

        return DynamicDataPassingParamRequest.AddOn(
            source = if (isOcs) SOURCE_OCS else SOURCE_NORMAL,
            addOnData = listAddOnData
        )
    }

    fun getAddOnFromSAF(
        addOnResult: AddOnsDataModel,
        isOcs: Boolean
    ): DynamicDataPassingParamRequest.AddOn {
        val listAddOnData = arrayListOf<DynamicDataPassingParamRequest.AddOn.AddOnDataParam>()
        addOnResult.addOnsDataItemModelList.map { dataItem ->
            val addOnData = DynamicDataPassingParamRequest.AddOn.AddOnDataParam(
                addOnId = dataItem.addOnId.toLongOrZero(),
                addOnQty = dataItem.addOnQty.toInt(),
                addOnMetadata = DynamicDataPassingParamRequest.AddOn.AddOnDataParam.AddOnMetadataParam(
                    addOnNote = DynamicDataPassingParamRequest.AddOn.AddOnDataParam.AddOnMetadataParam.AddOnNoteParam(
                        from = dataItem.addOnMetadata.addOnNoteItemModel.from,
                        isCustomNote = dataItem.addOnMetadata.addOnNoteItemModel.isCustomNote,
                        notes = dataItem.addOnMetadata.addOnNoteItemModel.notes,
                        to = dataItem.addOnMetadata.addOnNoteItemModel.to
                    )
                )
            )
            listAddOnData.add(addOnData)
        }

        return DynamicDataPassingParamRequest.AddOn(
            source = if (isOcs) SOURCE_OCS else SOURCE_NORMAL,
            addOnData = listAddOnData
        )
    }

    const val ORDER_LEVEL = "order_level"
    const val PRODUCT_LEVEL = "product_level"
    const val PAYMENT_LEVEL = "payment_level"
    const val ATTRIBUTE_ADDON_DETAILS = "addon_details"
    const val ATTRIBUTE_DONATION = "donation"
    const val SOURCE_OCS = "OCS"
    const val SOURCE_NORMAL = "normal"
}
