package com.tokopedia.purchase_platform.common.feature.addons.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductDataModel(
    var iconUrl: String = "",
    var title: String = "",
    var bottomsheet: AddOnProductBottomSheetModel = AddOnProductBottomSheetModel(),
    var listAddOnProductData: List<AddOnProductDataItemModel> = emptyList(),
    var listDeselectAddOnProductData: ArrayList<AddOnProductDataItemModel> = arrayListOf()
) : Parcelable
