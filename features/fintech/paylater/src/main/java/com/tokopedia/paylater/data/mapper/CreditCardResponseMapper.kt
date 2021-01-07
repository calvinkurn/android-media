package com.tokopedia.paylater.data.mapper

import com.tokopedia.paylater.domain.model.CreditCardBank
import com.tokopedia.paylater.domain.model.SimulationTableResponse

object CreditCardResponseMapper {

    fun populateDummyCreditCardData(): ArrayList<SimulationTableResponse> {
        val bankList = ArrayList<CreditCardBank>()
        bankList.add(CreditCardBank(
                "https://ecs7.tokopedia.net/img/microfinance/credit-card-new/bca/bca-logo.png",
                "Diskon 10%; Kupon 20rb",
                "3, 6"
        ))
        bankList.add(CreditCardBank(
                "https://ecs7.tokopedia.net/assets-fintech-frontend/pdp/banks/bri.png",
                "Diskon 20%; Kupon 5rb",
                "6, 9"
        ))
        for (i in 0..10) {
            bankList.add(CreditCardBank(
                    "https://ecs7.tokopedia.net/img/microfinance/credit-card-new/bca/bca-logo.png",
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
                bankList
        ))
        for (i in 1..2)
            list.add(SimulationTableResponse(
                    "Cicilan 3x",
                    6300000.0,
                    false,
                    false,
                    bankList
            ))
        list.add(SimulationTableResponse(
                "Cicilan 6x",
                3100000.0,
                true,
                false,
                bankList
        ))
        for (i in 0..2)
            list.add(SimulationTableResponse(
                    "Cicilan 9x",
                    250000.0,
                    false,
                    false,
                    bankList
            ))
        return list
    }
}