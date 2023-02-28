package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnsDataModel(
    var status: Int = 0,
    var addOnsDataItemModelList: List<AddOnDataItemModel> = emptyList(),
    var addOnsButtonModel: AddOnButtonModel = AddOnButtonModel(),
    var addOnsBottomSheetModel: AddOnBottomSheetModel = AddOnBottomSheetModel()
) : Parcelable
