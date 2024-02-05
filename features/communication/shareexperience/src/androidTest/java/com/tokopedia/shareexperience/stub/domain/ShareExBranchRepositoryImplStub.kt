package com.tokopedia.shareexperience.stub.domain

import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchUniversalObjectRequest
import com.tokopedia.shareexperience.domain.repository.ShareExBranchRepository
import com.tokopedia.shareexperience.domain.util.ShareExResult
import com.tokopedia.shareexperience.stub.ShareExBranchUniversalObjectStub
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch
import io.branch.referral.util.LinkProperties
import kotlinx.coroutines.channels.ProducerScope
import javax.inject.Inject

class ShareExBranchRepositoryImplStub @Inject constructor() : ShareExBranchRepository {

    override fun generateBranchUniversalObject(bouRequest: ShareExBranchUniversalObjectRequest): BranchUniversalObject {
        return ShareExBranchUniversalObjectStub()
    }

    override fun generateLinkProperties(params: ShareExBranchLinkPropertiesRequest): LinkProperties {
        return LinkProperties()
    }

    override fun getBranchListener(scope: ProducerScope<ShareExResult<String>>): Branch.BranchLinkCreateListener {
        return Branch.BranchLinkCreateListener { url, error ->
            if (error == null) {
                scope.trySend(ShareExResult.Success(url))
            } else {
                scope.trySend(ShareExResult.Error(Throwable(error.message)))
            }
            scope.close()
        }
    }
}
