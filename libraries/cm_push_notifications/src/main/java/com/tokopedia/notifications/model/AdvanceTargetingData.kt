package com.tokopedia.notifications.model

data class AdvanceTargetingData(
    val mainAppPriority: String?,
    val sellerAppPriority: String?,
    val isAdvanceTargeting: Boolean = false
) {
    fun getMainAppPriority(): Int {
        try {
            if (!mainAppPriority.isNullOrBlank()) {
                return mainAppPriority.toInt()
            }
        } catch (e: Exception) {
        }
        return 1
    }

    fun getSellerAppPriority(): Int {
        try {
            if (!sellerAppPriority.isNullOrBlank()) {
                return sellerAppPriority.toInt()
            }
        } catch (e: Exception) {
        }
        return 1
    }
}
