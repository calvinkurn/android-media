package com.tokopedia.tradein.raw

const val GQL_RATES_V3: String = """query getRatesV3API(${'$'}spids: String!, ${'$'}origin: String!, ${'$'}destination: String!, ${'$'}weight: String!, ${'$'}tradeIn: Int) {
  ratesV3Api(input: {spids: ${'$'}spids, origin: ${'$'}origin, destination: ${'$'}destination, weight: ${'$'}weight, trade_in: ${'$'}tradeIn}) {
    ratesv3 {
      id
      rates_id
      type
      services {
        status
        products {
          price {
            price
            formatted_price
          }
        }
      }
    }
  }
}
"""