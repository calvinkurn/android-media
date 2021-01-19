package com.tokopedia.paylater.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.paylater.*
import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.domain.model.CreditCardPdpMetaData
import com.tokopedia.paylater.domain.model.PdpInfoTableItem
import com.tokopedia.paylater.domain.model.SimulationTableResponse

object CreditCardResponseMapper {

    fun populateDummyCreditCardData(): ArrayList<SimulationTableResponse> {
        val bankList = ArrayList<CreditCardBank>()
        bankList.add(CreditCardBank(
                "https://ecs7.tokopedia.net/img/microfinance/credit-card-new/bca/bca-logo.png",
                "Bank BCA",
                "Diskon 10%; Kupon 20rb",
                "3, 6"
        ))
        bankList.add(CreditCardBank(
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/banks/bri.png",
                "Bank BCA",
                "Diskon 20%; Kupon 5rb",
                "6, 9"
        ))
        for (i in 0..10) {
            bankList.add(CreditCardBank(
                    "https://ecs7.tokopedia.net/img/microfinance/credit-card-new/bca/bca-logo.png",
                    "Bank BCA",
                    "Diskon 10%; Kupon 20rb",
                    "3, 6"
            ))
        }
        val list = ArrayList<SimulationTableResponse>()
        list.add(SimulationTableResponse(
                "Cicilan 3x",
                6300000.0,
                false,
                true,
                false,
                bankList
        ))
        for (i in 1..2)
            list.add(SimulationTableResponse(
                    "Cicilan 3x",
                    6300000.0,
                    false,
                    false,
                    false,
                    bankList
            ))
        list.add(SimulationTableResponse(
                "Cicilan 6x",
                3100000.0,
                true,
                false,
                false,
                bankList
        ))
        for (i in 0..2)
            list.add(SimulationTableResponse(
                    "Cicilan 9x",
                    250000.0,
                    false,
                    false,
                    true,
                    bankList
            ))
        return list
    }

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