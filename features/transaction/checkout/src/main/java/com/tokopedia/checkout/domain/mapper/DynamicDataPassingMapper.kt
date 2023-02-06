package com.tokopedia.checkout.domain.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.model.UpdateDynamicDataPassingUiModel
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.data.request.DynamicDataPassingParamRequest
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult

object DynamicDataPassingMapper {

    private val dynamicDataParamList: List<DynamicDataPassingParamRequest>? = null

    fun mapResponseToUiModel(dynamicData: String): UpdateDynamicDataPassingUiModel {
        return UpdateDynamicDataPassingUiModel(dynamicData = dynamicData)
    }

    fun getCartStringCartId(addOnResult: AddOnResult, listCartItemModel: List<CartItemModel>): String {
        var cartStringCartId = ""
        if (addOnResult.addOnData.isNotEmpty()) {
            addOnResult.addOnData.forEach { addOnData ->
                run loop@{
                    listCartItemModel.forEach { cartItemModel ->
                        val keyProductLevel = cartItemModel.cartString + "-" + cartItemModel.cartId
                        if (addOnResult.addOnKey.equals(keyProductLevel, ignoreCase = true)) {
                            cartStringCartId = keyProductLevel
                            return@loop
                        }
                    }
                }
            }
        }
        return cartStringCartId
    }

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
        if (addOnResult.addOnData.isNotEmpty()) {
            addOnResult.addOnData.forEach { data ->
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
        addOnResult.addOnsDataItemModelList.forEach { dataItem ->
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
    const val ADD_ON_DETAILS = "addon_details"
    const val IS_DONATION = "is_donation"
    const val KEY_IS_ADD_ON = "is_addon"
    const val KEY_ADD_ON_NOTES_TO = "to"
    const val KEY_ADD_ON_NOTES_FROM = "from"
    const val KEY_ADD_ON_NOTES = "notes"
    const val SOURCE_OCS = "OCS"
    const val SOURCE_NORMAL = "normal"
    const val KEY_LEVEL = "level"
    const val KEY_PARENT_UNIQUE_ID = "parent_unique_id"
    const val KEY_UNIQUE_ID = "unique_id"
    const val KEY_ATTRIBUTE = "attribute"
}
