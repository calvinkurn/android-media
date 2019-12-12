package com.tokopedia.instantloan.helper

import com.google.gson.Gson
import com.tokopedia.instantloan.data.model.response.GqlFilterDataResponse

object InstantLoanDummyProvider {


    fun getLoanDataSuccessResponse(): GqlFilterDataResponse =
        Gson().fromJson(filterDataDummyResponse, GqlFilterDataResponse::class.java)

    val filterDataDummyResponse = """
        
    {
      "le_getfilter": {
        "JumlahPinjamanMobile": [
          {
            "Label": "Rp 1 Juta",
            "Value": "1000000"
          },
          {
            "Label": "Rp 2 juta",
            "Value": "2000000"
          },
          {
            "Label": "Rp 5 juta",
            "Value": "5000000"
          },
          {
            "Label": "Rp 10 juta",
            "Value": "10000000"
          },
          {
            "Label": "Rp 25 juta",
            "Value": "25000000"
          },
          {
            "Label": "Rp 50 juta",
            "Value": "50000000"
          },
          {
            "Label": "Rp 100 juta",
            "Value": "100000000"
          },
          {
            "Label": "Rp 500 juta",
            "Value": "500000000"
          },
          {
            "Label": "Rp 1 Miliar",
            "Value": "1000000000"
          },
          {
            "Label": "Rp 3 Miliar",
            "Value": "3000000000"
          }
        ],
        "PeriodePinjamanMobile": {
          "Year": {
            "Max": 6,
            "Min": 1
          },
          "Month": {
            "Max": 6,
            "Min": 1
          }
        }
      }
    }
""".trimIndent()

}