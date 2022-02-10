package com.tokopedia.addongifting.view.mapper

import com.tokopedia.addongifting.data.saveaddonstate.*
import com.tokopedia.purchase_platform.common.feature.addongifting.data.*

object AddOnResultMapper {

    fun mapResult(saveAddOnStateResponse: SaveAddOnStateResponse): SaveAddOnStateResult {
        return SaveAddOnStateResult().apply {
            addOns = saveAddOnStateResponse.addOns.map {
                mapAddOnResult(it)
            }
        }
    }

    private fun mapAddOnResult(addOnResponse: AddOnResponse): AddOnResult {
        return AddOnResult().apply {
            addOnBottomSheet = mapAddOnBottomSheetResult(addOnResponse.addOnBottomSheet)
            addOnButton = mapAddOnButtonResult(addOnResponse.addOnButton)
            addOnData = addOnResponse.addOnData.map {
                mapAddOnDataResult(it)
            }
            addOnKey = addOnResponse.addOnKey
            addOnLevel = addOnResponse.addOnLevel
            status = addOnResponse.status
        }
    }

    private fun mapAddOnBottomSheetResult(addOnBottomSheetResponse: AddOnBottomSheetResponse): AddOnBottomSheetResult {
        return AddOnBottomSheetResult().apply {
            description = addOnBottomSheetResponse.description
            headerTitle = addOnBottomSheetResponse.headerTitle
            products = addOnBottomSheetResponse.products.map {
                mapProductResult(it)
            }
            ticker = mapTickerResult(addOnBottomSheetResponse.ticker)
        }
    }

    private fun mapAddOnButtonResult(addOnButtonResponse: AddOnButtonResponse): AddOnButtonResult {
        return AddOnButtonResult().apply {
            action = addOnButtonResponse.action
            description = addOnButtonResponse.description
            leftIconUrl = addOnButtonResponse.leftIconUrl
            rightIconUrl = addOnButtonResponse.rightIconUrl
            title = addOnButtonResponse.title
        }
    }

    private fun mapAddOnDataResult(addOnDataResponse: AddOnDataResponse): AddOnData {
        return AddOnData().apply {
            addOnId = addOnDataResponse.addOnId
            addOnMetadata = mapAddOnMetadataResult(addOnDataResponse.addOnMetadata)
            addOnPrice = addOnDataResponse.addOnPrice
            addOnQty = addOnDataResponse.addOnQty
        }
    }

    private fun mapAddOnMetadataResult(addOnMetadataResponse: AddOnMetadataResponse): AddOnMetadata {
        return AddOnMetadata().apply {
            addOnNote = mapAddOnNoteResult(addOnMetadataResponse.addOnNote)
        }
    }

    private fun mapAddOnNoteResult(addOnNoteResponse: AddOnNoteResponse): AddOnNote {
        return AddOnNote().apply {
            from = addOnNoteResponse.from
            isCustomNote = addOnNoteResponse.isCustomNote
            notes = addOnNoteResponse.notes
            to = addOnNoteResponse.to
        }
    }

    private fun mapProductResult(productResponse: ProductResponse): ProductResult {
        return ProductResult().apply {
            productImageUrl = productResponse.productImageUrl
            productName = productResponse.productName
        }
    }

    private fun mapTickerResult(tickerResponse: TickerResponse): TickerResult {
        return TickerResult().apply {
            text = tickerResponse.text
        }
    }
}