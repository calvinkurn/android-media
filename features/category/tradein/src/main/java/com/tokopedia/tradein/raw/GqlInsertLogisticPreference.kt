package com.tokopedia.tradein.raw

const val GQL_INSERT_LOGISTIC_PREFERENCE: String = """mutation {
    insertTradeInLogisticPreference(params: {
        UserId: "123"
        DeviceId: "DeviceId",
        Is3PL: false,
        FinalPrice: 10000000
        TradeInPrice: 20000
    }) {
        IsSuccess
        ErrCode
        ErrMessage
    }
}
"""