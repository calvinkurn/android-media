package com.tokopedia.feedplus.presentation.model

/**
 * Created by meyta.taliti on 04/08/23.
 */
data class ActiveTabSource(
    val tabName: String?,
    val index: Int
) {
    companion object {
        val Empty = ActiveTabSource(null, 0)
    }
}
