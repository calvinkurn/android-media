package com.tokopedia.shareexperience.domain.repository

import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.channels.ProducerScope

interface ShareExBranchRepository {
    fun generateBranchUniversalObject(
        bouRequest: ShareExBranchUniversalObjectRequest
    ): BranchUniversalObject

    fun generateLinkProperties(
        params: ShareExBranchLinkPropertiesRequest
    ): LinkProperties

    fun getBranchListener(
        scope: ProducerScope<ShareExResult<String>>
    ): Branch.BranchLinkCreateListener
}
