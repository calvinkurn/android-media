package com.tokopedia.kol.feature.postdetail.view.viewmodel

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel

/**
 * @author by nisie on 26/03/19.
 */
data class PostDetailViewModel(
        var dynamicPostViewModel: DynamicFeedDomainModel = DynamicFeedDomainModel(),
        var kolPostViewModel: KolPostViewModel = KolPostViewModel(
                0, "", "", "", "", "", "",
                "", false, "", false, 0, 0,
                0, 0, "", false, false, false,
                false, false
        )
)