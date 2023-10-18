package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.profile.ProfileTabUiModel

/**
 * Created by fachrizalmrsln at 21/11/2022
 */
class TabModelBuilder {

    fun mockProfileTab(
        showTabs: Boolean = true,
        newestTab: ProfileTabUiModel.Key = ProfileTabUiModel.Key.Unknown,
    ): ProfileTabUiModel {
        return ProfileTabUiModel(
            showTabs = showTabs,
            tabs = if (showTabs) tabs(newestTab) else emptyList()
        )
    }

    private fun tabs(newestTab: ProfileTabUiModel.Key): List<ProfileTabUiModel.Tab> {
        return listOf(
            ProfileTabUiModel.Tab(
                title = "Feed",
                ProfileTabUiModel.Key.Feeds,
                position = 0,
                isNew = newestTab == ProfileTabUiModel.Key.Feeds,
            ),
            ProfileTabUiModel.Tab(
                title = "Video",
                ProfileTabUiModel.Key.Video,
                position = 1,
                isNew = newestTab == ProfileTabUiModel.Key.Video,
            ),
            ProfileTabUiModel.Tab(
                title = "Review",
                ProfileTabUiModel.Key.Review,
                position = 2,
                isNew = newestTab == ProfileTabUiModel.Key.Review,
            ),
        )
    }

}
