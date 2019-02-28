package com.tokopedia.profile.view.viewmodel

import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel

/**
 * @author by yfsx on 15/02/19.
 */
data class DynamicFeedProfileViewModel(
        val profileHeaderViewModel: ProfileHeaderViewModel = ProfileHeaderViewModel(),
        val dynamicFeedDomainModel: DynamicFeedDomainModel = DynamicFeedDomainModel(),
        val lastCursor: String = ""
) {

}