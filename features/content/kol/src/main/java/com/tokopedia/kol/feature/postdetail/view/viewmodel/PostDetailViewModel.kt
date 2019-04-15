package com.tokopedia.kol.feature.postdetail.view.viewmodel

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.kol.feature.post.view.viewmodel.PostDetailFooterModel

/**
 * @author by nisie on 26/03/19.
 */
data class PostDetailViewModel(
        var dynamicPostViewModel: DynamicFeedDomainModel = DynamicFeedDomainModel(),
        var footerModel: PostDetailFooterModel = PostDetailFooterModel()
)