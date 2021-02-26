package com.tokopedia.brizzi.util

object GqlQuery {

    val tokenBrizzi = """
        query tokenEmoney(${'$'}refresh: Boolean) {
          status
          rechargeEmoneyBRIToken(refresh:${'$'}refresh) {
            token
          }
        }
    """.trimIndent()
}