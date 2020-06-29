package com.tokopedia.home.beranda.data.model

data class TokopointHomeDrawerData (
        val offFlag: Int = -1,
        val hasNotification: Int = -1,
        val userTier: UserTier = UserTier(),
        val rewardPointsStr: String = "",
        val mainPageUrl: String = "",
        val mainPageTitle: String = "",
        val sumCoupon: Int = -1,
        val sumCouponStr: String = ""
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TokopointHomeDrawerData) return false

        if (offFlag != other.offFlag) return false
        if (hasNotification != other.hasNotification) return false
        if (userTier != other.userTier) return false
        if (rewardPointsStr != other.rewardPointsStr) return false
        if (mainPageUrl != other.mainPageUrl) return false
        if (mainPageTitle != other.mainPageTitle) return false
        if (sumCoupon != other.sumCoupon) return false
        if (sumCouponStr != other.sumCouponStr) return false

        return true
    }

    override fun hashCode(): Int {
        var result = offFlag
        result = HASH_CODE * result + hasNotification
        result = HASH_CODE * result + userTier.hashCode()
        result = HASH_CODE * result + rewardPointsStr.hashCode()
        result = HASH_CODE * result + mainPageUrl.hashCode()
        result = HASH_CODE * result + mainPageTitle.hashCode()
        result = HASH_CODE * result + sumCoupon
        result = HASH_CODE * result + sumCouponStr.hashCode()
        return result
    }

    companion object {
        private const val HASH_CODE = 31
    }
}
