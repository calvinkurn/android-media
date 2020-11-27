package com.tokopedia.loginregister.external_register.ovo.domain.query

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object QueryCheckHasOvoAcc {

    private const val phoneNo = "\$phone_number"
    private const val clientId = "\$client_id"
    private const val name = "\$name"
    private const val date = "\$date"

    val checkHasOvoQuery: String = """
        query check_ovo_phone($phoneNo: String!){
            goalCheckPhoneRegistration(phone_number: $phoneNo) {
                is_registered
                is_allow
                errors{
                  code
                  title
                  message
                }
            }
        }
    """.trimIndent()

    val activateOvoQuery: String = """
        query activate_ovo($phoneNo: String!, $clientId: String!, $name: String!, $date: String!){
          activateInitRegis(
            client_id: $clientId
            phone: $phoneNo
            name: $name
            date: $date) {
            activationUrl
            goalKey
            errors{
              code
              title
              message
            }
          }
        }
    """.trimIndent()

    val registerOvoQuery: String = """
        query activate_ovo($phoneNo: String!, $clientId: String!, $name: String!, $date: String!){
          activateInitRegis(
            client_id: $clientId
            phone: $phoneNo
            name: $name
            date: $date) {
            activationUrl
            goalKey
            errors{
              code
              title
              message
            }
          }
        }
    """.trimIndent()

}