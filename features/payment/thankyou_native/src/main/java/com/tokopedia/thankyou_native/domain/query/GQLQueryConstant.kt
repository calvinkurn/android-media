package com.tokopedia.thankyou_native.domain.query

const val GQL_GYRO_RECOMMENDATION = """query validateEngineRequest(${'$'}request: String!){
                        validateEngineRequest(request:${'$'}request){
                                success
                                error_code
                                message
                                data{
                                  title
                                  description
                                  items {
                                    id
                                    detail
                                  }
                                }
                              }
                            }"""

const val GQL_GET_WALLET_BALANCE = """
     query walletappGetBalance(${'$'}partnerCode: String!) {
 	 walletappGetBalance(partnerCode: ${'$'}partnerCode) {
     code
     balance {
       wallet_code
       amount
       active
       whitelisted
     }
   }
 }
 """



