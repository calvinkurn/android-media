package com.tokopedia.sellerorder.detail.presentation.model

import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder

data class LogisticInfoAllWrapper(
        val logisticInfoAllList: ArrayList<SomDetailOrder.Data.GetSomDetail.LogisticInfo.All> = arrayListOf()
)