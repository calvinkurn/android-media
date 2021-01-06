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

    private const val type = "\$type"
    private const val fullname = "\$fullname"
    private const val email = "\$email"
    private const val password = "\$password"
    private const val osType = "\$os_type"
    private const val validateToken = "\$validate_token"
    private const val goalKey = "\$goal_key"
    private const val authCode = "\$auth_code"
    private const val typeName = "\$accounts_type_name"

    private const val msisdn = "\$msisdn"

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
        query register($type: String!, $fullname: String!, $msisdn: String!, $goalKey: String!, $authCode: String, $typeName: String){
          register(
            goalKey: $goalKey
            authCode: $authCode
            type: $type
            fullname: $fullname
            msisdn: $msisdn
            accounts_type_name: $typeName
            ) {
                user_id
                sid
                access_token
                refresh_token
                token_type
                is_active
                action
                errors {
                    name
                    message
                }
            }
        }
    """.trimIndent()

}