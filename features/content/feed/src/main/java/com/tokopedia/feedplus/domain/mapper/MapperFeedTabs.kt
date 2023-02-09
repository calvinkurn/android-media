package com.tokopedia.feedplus.domain.mapper

import com.tokopedia.feedplus.data.FeedTabsEntity
import com.tokopedia.feedplus.presentation.model.DataModel
import com.tokopedia.feedplus.presentation.model.FeedTabsModel
import com.tokopedia.feedplus.presentation.model.MetaModel

/**
 * Created By : Muhammad Furqan on 09/02/23
 */
object MapperFeedTabs {
    fun transform(entity: FeedTabsEntity): FeedTabsModel =
        FeedTabsModel(
            meta = MetaModel(
                selectedIndex = entity.meta.selectedIndex,
                profileApplink = entity.meta.myProfileApplink,
                profileWeblink = entity.meta.myProfileWeblink,
                profilePhotoUrl = entity.meta.myProfilePhotoUrl,
                showMyProfile = entity.meta.showMyProfile,
            ),
            data = entity.getSortedData().map {
                DataModel(
                    title = it.title,
                    key = it.key,
                    type = it.type,
                    position = it.position,
                    isActive = it.isActive
                )
            }.toList()
        )
}
