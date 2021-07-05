package com.tokopedia.brizzi.util

object DigitalBrizziGqlQuery {

    val tokenBrizzi = """
        query tokenEmoney(${'$'}refresh: Boolean) {
          status
          rechargeEmoneyBRIToken(refresh:${'$'}refresh) {
            token
          }
        }
    """.trimIndent()
}