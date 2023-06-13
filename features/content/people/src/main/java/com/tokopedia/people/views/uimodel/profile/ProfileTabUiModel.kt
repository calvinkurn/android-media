package com.tokopedia.people.views.uimodel.profile

/**
 * created by fachrizalmrsln on 10/11/2022
 */
data class ProfileTabUiModel(
    val showTabs: Boolean = false,
    val tabs: List<Tab> = emptyList(),
) {
    data class Tab(
        val title: String = "",
        val key: String = "",
        val position: Int = 0,
    )
}
