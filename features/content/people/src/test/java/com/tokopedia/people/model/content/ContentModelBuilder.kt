package com.tokopedia.people.model.content

import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel

class ContentModelBuilder {

    fun buildTabsModel(showTabs: Boolean) = ProfileTabUiModel(
        showTabs = showTabs,
        tabs = if (!showTabs) emptyList() else listOf(
            ProfileTabUiModel.Tab(
                title = "Feed",
                key = "feeds",
                position = 0,
            ),
            ProfileTabUiModel.Tab(
                title = "Video",
                key = "video",
                position = 1,
            )
        )
    )

}
