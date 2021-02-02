package com.tokopedia.pdpsimulation.creditcard.domain.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.pdpsimulation.common.constants.*
import com.tokopedia.pdpsimulation.creditcard.domain.model.CreditCardPdpMetaData
import com.tokopedia.pdpsimulation.creditcard.domain.model.PdpInfoTableItem
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class CreditCardTncMapperUseCase @Inject constructor() : UseCase<CreditCardPdpMetaData>() {

    private val PARAM_PDP_META_DATA = "param_pdp_meta_data"
    private val CREDIT_CARD_TNC_DATA_FAILURE = "NULL DATA"

    fun parseTncData(
            creditCardPdpMetaData: CreditCardPdpMetaData?,
            onSuccess: (CreditCardPdpMetaData) -> Unit, onError: (Throwable) -> Unit,
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_PDP_META_DATA, creditCardPdpMetaData)
        }
        execute({
            onSuccess(it)
        }, {
            onError(it)
        }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): CreditCardPdpMetaData {
        val creditCardPdpMetaData: CreditCardPdpMetaData = (useCaseRequestParams.getObject(PARAM_PDP_META_DATA)
                ?: throw NullPointerException(CREDIT_CARD_TNC_DATA_FAILURE)) as CreditCardPdpMetaData
        extractTncDataType(creditCardPdpMetaData)
        return creditCardPdpMetaData
    }

    private fun extractTncDataType(creditCardPdpMetaData: CreditCardPdpMetaData) {
        val gson = Gson()
        val bulletType = object : TypeToken<ArrayList<String>>() {}.type
        creditCardPdpMetaData.pdpInfoContentList?.let { pdpInfoList ->
            for (pdpInfo in pdpInfoList) {
                if (!pdpInfo.content.isNullOrEmpty()) {
                    when (pdpInfo.contentType) {
                        DATA_TYPE_BULLET -> {
                            pdpInfo.viewType = VIEW_TYPE_BULLET
                            pdpInfo.bulletList = gson.fromJson<ArrayList<String>>(pdpInfo.content, bulletType)
                        }
                        DATA_TYPE_MIN_TRANSACTION -> {
                            val tableData = gson.fromJson(pdpInfo.content, PdpInfoTableItem::class.java)
                            pdpInfo.tableData = tableData
                            pdpInfo.viewType = VIEW_TYPE_TABLE_MIN_TRX
                        }
                        DATA_TYPE_SERVICE_FEE -> {
                            val tableData = gson.fromJson(pdpInfo.content, PdpInfoTableItem::class.java)
                            pdpInfo.tableData = tableData
                            pdpInfo.viewType = VIEW_TYPE_TABLE_SERVICE
                        }
                        else -> pdpInfo.viewType = VIEW_TYPE_BULLET
                    }
                } else pdpInfo.viewType = VIEW_TYPE_BULLET
            }
        } ?: throw NullPointerException(CREDIT_CARD_TNC_DATA_FAILURE)
    }
}