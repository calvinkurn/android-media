package com.tokopedia.paylater.creditcard.mapper

import com.google.gson.Gson
import com.tokopedia.paylater.common.constants.*
import com.tokopedia.paylater.creditcard.domain.model.CreditCardPdpMetaData
import com.tokopedia.paylater.creditcard.domain.model.PdpCreditCardSimulation
import com.tokopedia.paylater.creditcard.domain.model.PdpInfoTableItem


sealed class CCSimulationDataStatus()
object StatusApiSuccess : CCSimulationDataStatus()
object StatusApiFail : CCSimulationDataStatus()
object StatusCCNotAvailable : CCSimulationDataStatus()


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

    fun handleSimulationResponse(pdpCreditCardSimulationData: PdpCreditCardSimulation?): CCSimulationDataStatus {
        pdpCreditCardSimulationData?.let {
            it.creditCardGetSimulationResult?.let { simulationResult ->
                if (simulationResult.creditCardInstallmentList.isNullOrEmpty()) {
                    return StatusApiFail
                } else {
                    val isCreditCardSimulationAvailable = simulationResult.creditCardInstallmentList.any { installment ->
                        installment.isDisabled == false
                    }
                    simulationResult.creditCardInstallmentList[0].isSelected = true
                    return if (isCreditCardSimulationAvailable) return StatusApiSuccess
                    else StatusCCNotAvailable
                }
            } ?: return StatusApiFail
        } ?: return StatusApiFail
    }
}