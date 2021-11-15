package com.tokopedia.checkout.bundle.view.uimodel

import android.os.Parcelable
import com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform.CrossSellBottomSheet
import com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform.CrossSellInfoData
import com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform.CrossSellOrderSummary
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CrossSellModel(
        var id: String = "",
        var checkboxDisabled: Boolean = false,
        var isChecked: Boolean = false,
        var price: Long = 0,
        var info: CrossSellInfoModel = CrossSellInfoModel(),
        var orderSummary: CrossSellOrderSummaryModel = CrossSellOrderSummaryModel(),
        var bottomSheet: CrossSellBottomSheetModel = CrossSellBottomSheetModel(),
        var additionalVerticalId: String = "",
        var transactionType: String = ""
) : Parcelable
