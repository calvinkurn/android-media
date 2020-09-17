package com.tokopedia.tradein.raw

const val GQL_FETCH_TNC: String = """query fetchBlackMarketAndTnC(${'$'}type: Int) {
  fetchTickerAndTnC(params: {
    ValidatePrice: false,
    TncType:1,
    TradeInType: ${'$'}type
  }) {
    TnC
  }
}"""