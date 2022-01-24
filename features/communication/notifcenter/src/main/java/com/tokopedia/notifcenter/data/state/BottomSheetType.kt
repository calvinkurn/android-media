package com.tokopedia.notifcenter.data.state

import java.lang.Exception

sealed class BottomSheetType {
    object LongerContent: BottomSheetType()
    object ProductCheckout: BottomSheetType()
    object StockHandler: BottomSheetType()
    object Information: BottomSheetType()

    companion object {
        /*
           * these bottom sheet type sorted based on type from backend:
           * 0: default
           * 1: product card
           * 2: reminder and product list (stock handler)
           * 3: without CTA button (similar longer content)
           * */
        private val bottomSheet = listOf(
                LongerContent,
                ProductCheckout,
                StockHandler,
                Information
        )

        fun map(bottomSheetType: Int): BottomSheetType {
            if (bottomSheetType > bottomSheet.size) {
                throw Exception("no bottomsheet type found")
            }
            return bottomSheet[bottomSheetType]
        }
    }
}