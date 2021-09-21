package com.tokopedia.discovery2.data.gqlraw

const val GQL_INJECT_DATA: String = """query SetInjectCoupon() {
  SetInjectCouponTimeBased() {
  is_success
  error_message
  }
}
"""