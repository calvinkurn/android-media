package com.tokopedia.tokomember_seller_dashboard.domain

class TokomemberAuthenticatedUsecase {


}

const val IS_AUTHENTICATED  = """
    query isAuthenticatedQuery {
  userShopInfo {
    info {
      shop_id
    }
  }
}
"""