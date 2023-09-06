package com.tokopedia.stories.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoriesGroupUiModel(
    val selectedGroupId: String = "",
    val selectedGroupPosition: Int = -1,
    val groupHeader: List<StoriesGroupHeader> = emptyList(),
    val groupItems: List<StoriesGroupItemUiModel> = emptyList(),
): Parcelable

@Parcelize
data class StoriesGroupHeader(
    val groupId: String = "",
    val image: String = "",
    val title: String = "",
    val isSelected: Boolean = false,
): Parcelable

@Parcelize
data class StoriesGroupItemUiModel(
    val groupId: String = "",
    val groupName: String = "",
    val detail: StoriesDetailUiModel = StoriesDetailUiModel(),
): Parcelable

@Parcelize
data class StoriesDetailUiModel(
    val selectedGroupId: String = "",
    val selectedDetailPosition: Int = -1,
    val selectedDetailPositionCached: Int = -1,
    val detailItems: List<StoriesDetailItemUiModel> = emptyList(),
): Parcelable

@Parcelize
data class StoriesDetailItemUiModel(
    val id: String = "",
    val event: StoriesDetailItemUiEvent = StoriesDetailItemUiEvent.PAUSE,
    val imageContent: String = "",
    val resetValue: Int = -1,
    val isSameContent: Boolean = false,
): Parcelable {

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME,
    }

}
