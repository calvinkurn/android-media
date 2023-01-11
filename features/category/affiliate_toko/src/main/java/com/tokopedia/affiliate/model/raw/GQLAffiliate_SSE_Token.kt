package com.tokopedia.affiliate.model.raw

val GQL_Affiliate_SSE_Token: String = """query GetAffiliateToken(){
  getAffiliateToken(){
    Token
  }
}
""".trimIndent()
