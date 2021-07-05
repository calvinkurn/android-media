package com.tokopedia.gamification.giftbox.domain

const val GAMI_UNSET_REMINDER = """
    mutation GameRemindMe(${'$'}about:String!){
    gameRemindMe(input:{about: ${'$'}about}) {
      resultStatus {
         code
         message
         reason
      }
   }
}
"""