package com.tokopedia.privacycenter.dsar.model

import com.tokopedia.privacycenter.dsar.model.uimodel.CustomDateModel

data class TransactionHistoryModel(
    var showBottomSheet: Boolean = false,
    var isChecked: Boolean = false,
    var itemRange: ArrayList<ItemRangeModel> = arrayListOf()
)

data class ItemRangeModel(
    var id: Int,
    var title: String,
    var selected: Boolean = false,
    var transactionDate: CustomDateModel = CustomDateModel()
)
