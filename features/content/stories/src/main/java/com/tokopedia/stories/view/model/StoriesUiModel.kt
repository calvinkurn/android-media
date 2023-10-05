package com.tokopedia.stories.view.model

import android.os.Parcelable
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.stories.uimodel.StoryAuthor
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesItemContentType.Image
import com.tokopedia.universal_sharing.view.model.LinkProperties
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoriesUiModel(
    val selectedGroupId: String = "",
    val selectedGroupPosition: Int = -1,
    val groupHeader: List<StoriesGroupHeader> = emptyList(),
    val groupItems: List<StoriesGroupItem> = emptyList()
) : Parcelable

@Parcelize
data class StoriesGroupHeader(
    val groupId: String = "",
    val groupName: String = "",
    val image: String = "",
    val isSelected: Boolean = false
) : Parcelable

@Parcelize
data class StoriesGroupItem(
    val groupId: String = "",
    val groupName: String = "",
    val detail: StoriesDetail = StoriesDetail()
) : Parcelable

@Parcelize
data class StoriesDetail(
    val selectedGroupId: String = "",
    val selectedDetailPosition: Int = -1,
    val selectedDetailPositionCached: Int = -1,
    val detailItems: List<StoriesDetailItem> = emptyList()
) : Parcelable

@Parcelize
data class StoriesDetailItem(
    val id: String = "",
    val event: StoriesDetailItemUiEvent = StoriesDetailItemUiEvent.PAUSE,
    val content: StoriesItemContent = StoriesItemContent(),
    val resetValue: Int = -1,
    val isContentLoaded: Boolean = false,
    val meta: Meta = Meta(),
    @IgnoredOnParcel val author: StoryAuthor = StoryAuthor.Unknown,
    @IgnoredOnParcel val menus: List<ContentMenuItem> = emptyList(),
    val productCount: String = "",
    @IgnoredOnParcel val share: Sharing = Sharing.Empty,
    @IgnoredOnParcel val status: StoryStatus = StoryStatus.Unknown
) : Parcelable {

    @Parcelize
    data class Meta(
        val activityTracker: String = "",
        val templateTracker: String = ""
    ) : Parcelable

    @Parcelize
    data class StoriesItemContent(
        val type: StoriesItemContentType = Image,
        val data: String = "",
        val duration: Int = -1
    ) : Parcelable

    enum class StoriesItemContentType(val value: String) {
        Image("image"), Video("video")
    }

    enum class StoriesDetailItemUiEvent {
        PAUSE, RESUME,
    }

    data class Sharing(
        val isShareable: Boolean,
        val shareText: String,
        val metadata: LinkProperties
    ) {
        companion object {
            val Empty get() = Sharing(isShareable = false, shareText = "", metadata = LinkProperties())
        }
    }

    enum class StoryStatus(val value: String) {
        Active("ACTIVE"), Unknown("unknown");

        companion object {
            private val values = StoryStatus.values()

            fun getByValue(value: String): StoryStatus {
                values.forEach {
                    if (it.value.equals(value, true)) return it
                }
                return Unknown
            }
        }
    }

    val isProductAvailable: Boolean = productCount.isNotEmpty() || productCount != "0"
}
