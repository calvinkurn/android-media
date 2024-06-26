package com.tokopedia.feedplus.presentation.model

import android.os.Parcelable
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment.Companion.TAB_TYPE_FOLLOWING
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
data class FeedTabsModel(
    val tab: FeedTabModel,
    val meta: MetaModel
) {
    companion object {
        val Empty: FeedTabsModel
            get() = FeedTabsModel(
                tab = FeedTabModel(
                    emptyList(),
                    ActiveTabSource(null, 0)
                ),
                meta = MetaModel.Empty
            )
    }
}

data class FeedTabModel(
    val data: List<FeedDataModel>,
    val activeTabSource: ActiveTabSource,
)

@Parcelize
data class FeedDataModel(
    val title: String,
    val key: String,
    val type: String,
    val position: Int,
    val isActive: Boolean,
    val isSelected: Boolean,
    val hasNewContent: Boolean,
) : Parcelable {

    val isFollowingTab = this.type == TAB_TYPE_FOLLOWING
}

data class MetaModel(
    val profileApplink: String,
    val profilePhotoUrl: String,
    val showMyProfile: Boolean,
    val isCreationActive: Boolean,
    val showLive: Boolean,
    val liveApplink: String,
    val entryPoints: List<ContentCreationTypeItem>,
    val showBrowse: Boolean,
    val browseApplink: String
) {
    companion object {
        val Empty: MetaModel
            get() = MetaModel(
                profileApplink = "",
                profilePhotoUrl = "",
                showMyProfile = false,
                isCreationActive = false,
                showLive = false,
                liveApplink = "",
                entryPoints = emptyList(),
                showBrowse = false,
                browseApplink = ""
            )
    }
}
