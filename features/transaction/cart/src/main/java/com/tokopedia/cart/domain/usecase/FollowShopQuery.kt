package com.tokopedia.cart.domain.usecase

const val FOLLOW_SHOP_QUERY = """
    mutation followShop(${'$'}input: ParamFollowShop!) {
      followShop(input:${'$'}input){
        success
        message
      }
    }
"""
