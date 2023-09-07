package com.tokopedia.stories.view.model

import android.os.Parcelable
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesItemContentType.IMAGE
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
    val content: StoriesItemContent = StoriesItemContent(),
    val resetValue: Int = -1,
    val isSameContent: Boolean = false,
    val timerDuration: Int = -1,
): Parcelable {

    @Parcelize
    data class StoriesItemContent(
        val type: StoriesItemContentType = IMAGE,
        val data: String = "",
    ): Parcelable

    enum class StoriesItemContentType(val value: String) {
        IMAGE("image"), VIDEO("video")
    }

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME,
    }

}
