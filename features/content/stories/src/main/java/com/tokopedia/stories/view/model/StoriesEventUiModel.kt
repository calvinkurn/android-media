package com.tokopedia.stories.view.model

import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.stories.uimodel.StoryAuthor

data class StoriesUiModel(
    val selectedGroup: Int,
    val groups: List<StoriesGroupUiModel>,
)

data class StoriesGroupUiModel(
    val id: String,
    val image: String,
    val title: String,
    val selectedDetail: Int,
    val selected: Boolean,
    val details: List<StoriesDetailUiModel>,
)

data class StoriesDetailUiModel(
    val id: String,
    val selected: Int,
    val event: StoriesDetailUiEvent,
    val imageContent: String,
    val author: StoryAuthor,
    //TODO() rename, temp list
    val menus: List<ContentMenuItem> = defaultMenu,
) {

    enum class StoriesDetailUiEvent {
        PAUSE, START,
    }
    companion object {
        val Empty = StoriesDetailUiModel(
            id = "0",
            selected = 1,
            event = StoriesDetailUiEvent.START,
            imageContent = "",
            author = StoryAuthor.Unknown,
        )
    }
}

//TODO() please remove
val defaultMenu = buildList<ContentMenuItem> {
    add(
        ContentMenuItem(
            iconUnify = IconUnify.WARNING,
            name = R.string.content_common_menu_report,
            type = ContentMenuIdentifier.Report
        )
    )
    add(
        ContentMenuItem(
            iconUnify = IconUnify.DELETE,
            name = com.tokopedia.stories.R.string.stories_delete_story_title,
            type = ContentMenuIdentifier.Delete,
        )
    )
}
