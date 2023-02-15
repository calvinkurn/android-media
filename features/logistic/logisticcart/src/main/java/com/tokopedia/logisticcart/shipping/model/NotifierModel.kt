package com.tokopedia.logisticcart.shipping.model

data class NotifierModel(var type: Int) : RatesViewModelType {
    companion object {
        const val TYPE_DEFAULT = 1
        const val TYPE_INSTAN = 2
        const val TYPE_SAMEDAY = 3
    }
}