package com.tokopedia.checkout.domain.datamodel.addtocart

/**
 * Created by Irfan Khoirul on 2019-07-02.
 */

data class AddToCartDataModel(
        var success: Int = 0,
        var message: List<String> = arrayListOf(),
        var data: DataModel = DataModel()
)