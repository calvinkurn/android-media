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



