package com.tokopedia.kol.feature.postdetail.view.datamodel

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel

/**
 * @author by nisie on 26/03/19.
 */
data class PostDetailUiModel(
    val dynamicPostViewModel: DynamicFeedDomainModel,
    val footerModel: PostDetailFooterModel,
) {
        companion object {
                val Empty = PostDetailUiModel(
                        dynamicPostViewModel = DynamicFeedDomainModel(),
                        footerModel = PostDetailFooterModel()
                )
        }
}
