package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnGiftingDataModel(
    var status: Int = 0,
    var addOnsDataItemModelList: List<AddOnGiftingDataItemModel> = emptyList(),
    var addOnsButtonModel: AddOnGiftingButtonModel = AddOnGiftingButtonModel(),
    var addOnsBottomSheetModel: AddOnGiftingBottomSheetModel = AddOnGiftingBottomSheetModel()
) : Parcelable
