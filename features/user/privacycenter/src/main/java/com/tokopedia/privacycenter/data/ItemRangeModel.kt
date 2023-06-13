package com.tokopedia.privacycenter.data

import com.tokopedia.privacycenter.ui.dsar.uimodel.CustomDateModel

data class TransactionHistoryModel(
    var showBottomSheet: Boolean = false,
    var isChecked: Boolean = false,
    var selectedDate: CustomDateModel = CustomDateModel()
)

data class ItemRangeModel(
    var id: Int,
    var title: String,
    var selected: Boolean = false,
    var transactionDate: CustomDateModel = CustomDateModel()
)
