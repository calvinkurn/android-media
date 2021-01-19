package com.tokopedia.paylater.data.mapper

import com.google.gson.Gson
import com.tokopedia.paylater.*
import com.tokopedia.paylater.domain.model.CreditCardPdpMetaData
import com.tokopedia.paylater.domain.model.PdpInfoTableItem

object CreditCardResponseMapper {

    fun populatePdpInfoMetaDataResponse(data: CreditCardPdpMetaData?) {
        val gson = Gson()
        data?.let {
            it.pdpInfoContentList?.let { pdpInfoList ->
                for (pdpInfo in pdpInfoList) {
                    if (pdpInfo.contentType == DATA_TYPE_BULLET) {
                        pdpInfo.viewType = VIEW_TYPE_BULLET
                        pdpInfo.bulletList = gson.fromJson(pdpInfo.content, ArrayList::class.java) as ArrayList<String>?
                    } else if (pdpInfo.contentType == DATA_TYPE_MIN_TRANSACTION) {
                        val tableData = gson.fromJson(pdpInfo.content, PdpInfoTableItem::class.java)
                        pdpInfo.tableData = tableData
                        pdpInfo.viewType = VIEW_TYPE_TABLE_MIN_TRX
                    } else if (pdpInfo.contentType == DATA_TYPE_SERVICE_FEE) {
                        val tableData = gson.fromJson(pdpInfo.content, PdpInfoTableItem::class.java)
                        pdpInfo.tableData = tableData
                        pdpInfo.viewType = VIEW_TYPE_TABLE_SERVICE
                    } else pdpInfo.viewType = VIEW_TYPE_BULLET
                }
            }
        }
    }
}