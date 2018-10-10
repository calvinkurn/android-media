package com.tokopedia.profile.view.viewmodel

import com.tokopedia.kol.feature.post.view.viewmodel.KolPostViewModel
import com.tokopedia.profile.data.pojo.affiliatequota.AffiliatePostQuota

/**
 * @author by milhamj on 9/21/18.
 */
data class ProfileFirstPageViewModel(
        val profileHeaderViewModel: ProfileHeaderViewModel,
        val profilePostViewModel: List<KolPostViewModel>,
        val affiliatePostQuota: AffiliatePostQuota
)