package com.tokopedia.people.views.uimodel.profile

/**
 * created by fachrizalmrsln on 10/11/2022
 */

sealed interface ProfileTabState {

    object Unknown : ProfileTabState

    data class Success(
        val profileTab: ProfileTabUiModel,
    ) : ProfileTabState

    data class Error(
        val error: Throwable
    ) : ProfileTabState

}

data class ProfileTabUiModel(
    val showTabs: Boolean = false,
    val tabs: List<Tab> = emptyList(),
) {
    data class Tab(
        val title: String = "",
        val key: Key = Key.Unknown,
        val position: Int = 0,
        val isNew: Boolean = false,
    )

    enum class Key(val value: String) {
        Feeds("feeds"),
        Video("video"),
        Review("review"),
        Unknown(""),
    }

    companion object {
        fun mapToKey(value: String): Key {
            return Key.values().find { key -> key.value == value } ?: Key.Unknown
        }
    }
}
