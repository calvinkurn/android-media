package com.tokopedia.shareexperience.stub.domain

import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import com.tokopedia.shareexperience.domain.util.ShareExResult
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.channels.ProducerScope
import javax.inject.Inject

class ShareExBranchRepositoryImplStub @Inject constructor() : ShareExBranchRepository {
    override fun generateBranchUniversalObject(bouRequest: ShareExBranchUniversalObjectRequest): BranchUniversalObject {
        TODO("Not yet implemented")
    }

    override fun generateLinkProperties(params: ShareExBranchLinkPropertiesRequest): LinkProperties {
        TODO("Not yet implemented")
    }

    override fun getBranchListener(scope: ProducerScope<ShareExResult<String>>): Branch.BranchLinkCreateListener {
        TODO("Not yet implemented")
    }
}
