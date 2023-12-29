package com.tokopedia.people.model.content

import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel

class ContentModelBuilder {

    fun buildTabsModel(showTabs: Boolean) = ProfileTabUiModel(
        showTabs = showTabs,
        tabs = if (!showTabs) emptyList() else listOf(
            ProfileTabUiModel.Tab(
                title = "Feed",
                ProfileTabUiModel.Key.Feeds,
                position = 0,
                isNew = false,
            ),
            ProfileTabUiModel.Tab(
                title = "Video",
                ProfileTabUiModel.Key.Video,
                position = 1,
                isNew = false,
            ),
            ProfileTabUiModel.Tab(
                title = "Review",
                ProfileTabUiModel.Key.Review,
                position = 2,
                isNew = false,
            ),
        )
    )

}
