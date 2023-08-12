package com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model

import com.tokopedia.kotlin.extensions.view.EMPTY

data class AddOnsProductDataModel(
    var title: String = String.EMPTY,
    var bottomsheet: Bottomsheet = Bottomsheet(),
    var data: List<Data> = listOf(),
    var deselectedData: List<Data> = listOf()
) {
    data class Bottomsheet(
        var title: String = String.EMPTY,
        var applink: String = String.EMPTY,
        var isShown: Boolean = false
    )
    data class Data(
        var id: String = String.EMPTY,
        var uniqueId: String = String.EMPTY,
        var price: Long = 0L,
        var infoLink: String = String.EMPTY,
        var name: String = String.EMPTY,
        var status: Int = 1,
        var type: Int = 1,
        var productQuantity: Int = 0
    )
}
