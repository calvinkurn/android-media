package com.tokopedia.shop.common.data.model

data class DynamicRule(
    val descriptionHeader: String = "",
    val listDynamicRoleData: List<DynamicRoleData> = listOf()
) {
    data class DynamicRoleData(
        val ruleID: String = "",
        val isActive: Boolean = false
    )
}
