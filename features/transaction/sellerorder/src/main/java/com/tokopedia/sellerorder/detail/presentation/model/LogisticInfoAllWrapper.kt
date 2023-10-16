package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder

data class LogisticInfoAllWrapper(
    val logisticInfoAllList: ArrayList<SomDetailOrder.GetSomDetail.LogisticInfo.All> = arrayListOf()
)
