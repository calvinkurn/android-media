package com.tokopedia.vouchercreation.product.create.domain.entity

data class CouponWithMetadata(val coupon: Coupon, val maxProduct : Int, val selectedWarehouseId: String)
