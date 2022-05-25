package com.tokopedia.homenav.mainnav.domain.model

data class NavOrderListModel (
        val orderList: List<NavProductOrder> = listOf(),
        val paymentList: List<NavPaymentOrder> = listOf(),
        val reviewList: List<NavReviewOrder> = listOf()
)

data class NavProductOrder(
        val id: String = "",
        val statusText: String = "",
        val statusTextColor: String = "",
        val productNameText: String = "",
        val descriptionText: String = "",
        val descriptionTextColor: String = "",
        val imageUrl: String = "",
        val additionalProductCount: Int = 0,
        val applink: String = "",
        val estimatedArrival: String = "",
        var fullWidth: Boolean = false
)

data class NavPaymentOrder(
        val id: String = "",
        val statusText: String = "",
        val statusTextColor: String = "",
        val paymentAmountText: String = "",
        val descriptionText: String = "",
        val imageUrl: String = "",
        val applink: String = "",
        var fullWidth: Boolean = false
)

data class NavReviewOrder(
        val productId: String = "",
        val productName: String = "",
        val imageUrl: String = "",
        val reputationId: String = "",
        var fullWidth: Boolean = false
)