package com.tokopedia.feedplus.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
data class FeedTabsModel(
    val data: List<FeedDataModel>,
    val meta: MetaModel
) {
    companion object {
        val Empty: FeedTabsModel
            get() = FeedTabsModel(
                data = emptyList(),
                meta = MetaModel.Empty
            )
    }
}

@Parcelize
data class FeedDataModel(
    val title: String,
    val key: String,
    val type: String,
    val position: Int,
    val isActive: Boolean
) : Parcelable

data class MetaModel(
    val selectedIndex: Int,
    val profileApplink: String,
    val profilePhotoUrl: String,
    val showMyProfile: Boolean,
    val isCreationActive: Boolean,
    val showLive: Boolean,
    val liveApplink: String,
    val entryPoints: List<ContentCreationTypeItem>
) {
    companion object {
        val Empty: MetaModel
            get() = MetaModel(
                selectedIndex = -1,
                profileApplink = "",
                profilePhotoUrl = "",
                showMyProfile = false,
                isCreationActive = false,
                showLive = false,
                liveApplink = "",
                entryPoints = emptyList()
            )
    }
}
