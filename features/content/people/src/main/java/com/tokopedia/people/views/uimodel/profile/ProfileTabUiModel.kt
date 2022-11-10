package com.tokopedia.people.views.uimodel.profile

data class ProfileTabUiModel(
    val showTabs: Boolean = false,
    val tabs: List<Tab> = listOf()
) {
    data class Tab(
        val title: String = "",
        val key: String = "",
        val position: Int = 0,
        val isActive: Boolean = false,
    )
}
