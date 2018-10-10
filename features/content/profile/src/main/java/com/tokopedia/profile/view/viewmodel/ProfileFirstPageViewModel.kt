package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota

/**
 * @author by milhamj on 9/21/18.
 */
data class ProfileFirstPageViewModel(
        val profileHeaderViewModel: ProfileHeaderViewModel,
        val visitableList: List<Visitable<*>>,
        val affiliatePostQuota: AffiliatePostQuota,
        val lastCursor: String = ""
)