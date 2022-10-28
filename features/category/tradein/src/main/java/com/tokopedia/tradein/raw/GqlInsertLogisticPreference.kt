package com.tokopedia.tradein.raw

const val GQL_INSERT_LOGISTIC_PREFERENCE: String = """mutation insertTradeInLogisticPreference(${'$'}params: InsertTradeInLogisticPreferenceInput!) {
  insertTradeInLogisticPreference(params: ${'$'}params) {
    IsSuccess
    ErrCode
    ErrMessage
  }
}
"""