package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel

/**
 * Created by fachrizalmrsln at 21/11/2022
 */
class TabModelBuilder {

    fun mockProfileTab(showTabs: Boolean = true): ProfileTabUiModel {
        return ProfileTabUiModel(
            showTabs = showTabs,
            tabs = if (showTabs) tabs() else emptyList()
        )
    }

    private fun tabs(): List<ProfileTabUiModel.Tab> {
        return listOf(
            ProfileTabUiModel.Tab(
                title = "Feed",
                "feed",
                position = 0
            ),
            ProfileTabUiModel.Tab(
                title = "Video",
                "video",
                position = 1
            ),
        )
    }

}
