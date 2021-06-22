package com.tokopedia.saldodetails.response.model

import java.util.*

class SummaryDepositParam {

    var endDate: String? = null
    var page: Int = 0
    var perPage = 25
    var startDate: String? = null
    private var saldoType: Int = 0

    val paramSummaryDeposit: Map<String, Any>
        get() {
            val param = HashMap<String, Any>()
            param[PARAM_START_DATE] = startDate!!
            param[PARAM_END_DATE] = endDate!!
            param[PARAM_PER_PAGE] = perPage
            param[PARAM_PAGE] = page
            param[SALDO_TYPE] = saldoType
            return param
        }

    fun setSaldoType(type: Int) {
        saldoType = type
    }

    companion object {

        private val PARAM_START_DATE = "dateFrom"
        private val PARAM_END_DATE = "dateTo"
        private val PARAM_PER_PAGE = "maxRows"
        private val PARAM_PAGE = "page"
        private val SALDO_TYPE = "saldoType"
        val PARAM_IS_SELLER = "isSeller"
    }

}
