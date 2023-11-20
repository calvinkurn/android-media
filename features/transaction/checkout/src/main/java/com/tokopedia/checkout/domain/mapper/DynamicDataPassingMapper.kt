package com.tokopedia.checkout.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult

object DynamicDataPassingMapper {

    fun getAddOn(
        addOnResult: AddOnResult,
        isOcs: Boolean
    ): DynamicDataPassingParamRequest.AddOn {
        val listAddOnData: List<DynamicDataPassingParamRequest.AddOn.AddOnDataParam> = addOnResult.addOnData.map { data ->
            DynamicDataPassingParamRequest.AddOn.AddOnDataParam(
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
        }

        return DynamicDataPassingParamRequest.AddOn(
            source = if (isOcs) SOURCE_OCS else SOURCE_NORMAL,
            addOnData = listAddOnData
        )
    }

    fun getAddOnFromSAF(
        addOnResult: AddOnGiftingDataModel,
        isOcs: Boolean
    ): DynamicDataPassingParamRequest.AddOn {
        val listAddOnData: List<DynamicDataPassingParamRequest.AddOn.AddOnDataParam> = addOnResult.addOnsDataItemModelList.map { dataItem ->
            DynamicDataPassingParamRequest.AddOn.AddOnDataParam(
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
