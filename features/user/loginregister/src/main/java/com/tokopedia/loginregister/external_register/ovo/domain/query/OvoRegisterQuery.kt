package com.tokopedia.loginregister.external_register.ovo.domain.query

/**
 * Created by Yoris Prayogo on 23/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

object OvoRegisterQuery {

    private const val phoneNo = "\$phone_number"
    private const val clientId = "\$client_id"
    private const val name = "\$name"
    private const val date = "\$date"
    private const val regType = "\$reg_type"

    private const val fullname = "\$fullname"
    private const val goalKey = "\$goal_key"
    private const val authCode = "\$auth_code"

    private const val msisdn = "\$phone"

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
        mutation register($regType: String!, $fullname: String!, $msisdn: String!, $goalKey: String!, $authCode: String){
            register(
                input: {
                    goal_key: $goalKey
                    auth_code: $authCode
                    reg_type: $regType
                    fullname: $fullname
                    phone: $msisdn
                }
            ) {
                user_id
                sid
                access_token
                refresh_token
                token_type
                is_active
                action
                enable_2fa
                enable_skip_2fa
                errors {
                    name
                    message
                }
            }
        }
    """.trimIndent()
}